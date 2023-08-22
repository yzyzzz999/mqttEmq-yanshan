package com.example.emqdemo;




import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.emqdemo.compoent.RedisUtil;
import com.example.emqdemo.constants.Constants;
import com.example.emqdemo.domain.*;
import com.example.emqdemo.mapper.EmqCurrentMapper;
import com.example.emqdemo.mapper.EmqIntervalMapper;
import com.example.emqdemo.mapper.EmqMapper;
import com.example.emqdemo.mapper.EmqOnchangeMapper;
import com.example.emqdemo.service.EmqIntervalService;
import com.example.emqdemo.service.TGasDataCurrentService;
import com.example.emqdemo.service.TGasDataService;
import com.example.emqdemo.service.TGasRawDataService;
import com.example.emqdemo.service.impl.EmqServiceImpl;
import com.example.emqdemo.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

@Slf4j
@SpringBootTest
class EmqdemoApplicationTests {

    @Autowired
    private EmqMapper emqMapper;

    @Autowired
    public EmqIntervalMapper emqIntervalMapper;

    @Autowired
    public EmqIntervalService emqIntervalService;

    @Autowired
    private YmlAnalysis ymlAnalysis;

    @Autowired
    private DataUtil dataUtil;

    @Autowired
    private TGasRawDataService tGasRawDataService;

    @Autowired
    private TGasDataCurrentService tGasDataCurrentService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TGasDataService tGasDataService;


    @Test
    void contextLoads() {
        Map<String, Object> resp = new HashMap<>();

        resp.put("guid", "126");
        resp.put("code", "aaa");
        resp.put("msg","aaa");
        emqMapper.resp(resp);
    }

    @Test
    void test() {
        String payload = "{\"DevCode\":\"testttttttttt1Four in one sensor\",\"OnlineStatus\":1,\"SendTime\":1691114944429,\"SeqId\":4,\"values\":{\"485 address\":1,\"Baud code\":3,\"CH4 concentration\":0,\"CO concentration\":0,\"Communication Status\":1,\"ETH working mode\":1,\"Embedded version\":258,\"H2S concentration\":0,\"IP Address-1\":192,\"IP Address-2\":168,\"IP Address-3\":1,\"IP Address-4\":101,\"MODBUS port\":502,\"Parity check\":0,\"Pump speed feedback\":0,\"Pump speed setting\":0,\"Pump working mode setting\":0,\"Type\":1537,\"WIFI switch\":1,\"gateway-1\":192,\"gateway-2\":168,\"gateway-3\":1,\"gateway-4\":1,\"mask-1\":255,\"mask-2\":255,\"mask-3\":255,\"mask-4\":0}}";
//        String payload = "{\"DevCode\":\"测试1\",\"OnlineStatus\":1,\"SendTime\":1690726906447,\"SeqId\":4," + "\"values\":{\"485 address\":1,\"Baud code\":3,\"CH4 concentration\":0,\"CO concentration\":0,\"Communication Status\":1,\"ETH working mode\":1,\"Embedded version\":258,\"H2S concentration\":0,\"IP Address-1\":192,\"IP Address-2\":168,\"IP Address-3\":1,\"IP Address-4\":101,\"MODBUS port\":502,\"Parity check\":0,\"Pump speed feedback\":0,\"Pump working mode setting\":0,\"Type\":1537,\"WIFI switch\":1,\"gateway-1\":192,\"gateway-2\":168,\"gateway-3\":1,\"gateway-4\":1,\"mask-1\":255,\"mask-2\":255,\"mask-3\":255,\"mask-4\":0,\"O2 concentration\":20.89,\"Pump speed setting\":1}}";
//        String payload = "{\"DevCode\":\"测试1\",\"OnlineStatus\":1,\"SendTime\":1690726909446,\"SeqId\":4,\"values\":{\"O2 concentration\":19.9}}";
        String topic = "/values/interval";
        //将json转map,方便读取数据
        Map<String,Object> mapJson = messageResolve(JSONObject.parseObject(payload),topic);
        System.out.println("mapJson="+mapJson);
        if (!"4".equals(mapJson.get("SeqId").toString())){
            //数据入库
            if (saveMessage(mapJson,topic)){
                log.error("======》》未识别的topic - {}",payload);
            }
        }else {
            switch (topic){
                case Topic.VALUES_INTERVAL:
                    EmqInterval emqInterval = EmqInterval.init(mapJson);
                    EmqIntervalMapper intervalMapper = SpringUtil.getBean(EmqIntervalMapper.class);
                    intervalMapper.insert(emqInterval);
                    break;
                case Topic.VALUES_ONCHANGE:
                    EmqOnchange emqOnchange = EmqOnchange.init(mapJson);
                    EmqOnchangeMapper onchangeMapper = SpringUtil.getBean(EmqOnchangeMapper.class);
                    onchangeMapper.insert(emqOnchange);
                    break;
                default:
            }
        }
        //emqService.upload(mapJson);
    }
    @Test
    void test1() {
        String payload = "{\"DevCode\":\"Four in one sensor\",\"OnlineStatus\":1,\"SendTime\":1691545922974,\"SeqId\":4,\"values\":{\"485 address\":1,\"Baud code\":3,\"CH4 concentration\":0,\"CO concentration\":0,\"Communication Status\":1,\"ETH working mode\":1,\"Embedded version\":258,\"H2S concentration\":0,\"IP Address-1\":192,\"IP Address-2\":168,\"IP Address-3\":1,\"IP Address-4\":101,\"MODBUS port\":502,\"Parity check\":0,\"Pump speed feedback\":0,\"Pump speed setting\":0,\"Pump working mode setting\":0,\"Type\":1537,\"WIFI switch\":1,\"gateway-1\":192,\"gateway-2\":168,\"gateway-3\":1,\"gateway-4\":1,\"mask-1\":255,\"mask-2\":255,\"mask-3\":255,\"mask-4\":0}}";
        String topic = "/values/onchange";
        Map<String,Object> mapJson = messageResolve(JSONObject.parseObject(payload),topic);
//        JSONObject json = JSONObject.parseObject(payload);
//        Map<String,Object> mapJson = json.getInnerMap();
        if (mapJson.get("SeqId") == null){
            log.info("======》》设备ID不存在!!");
            return;
        }

        //将json转TGasRawData备份
        TGasRawData tGasRawData = dataUtil.rawDataInit(payload,mapJson.get("SeqId").toString());
        tGasRawDataService.saveGasRawData(tGasRawData);
        List<TGasData> gasDataList = dataUtil.gasDatasInit(mapJson,tGasRawData.getId());
        tGasDataService.batchSaveGasData(gasDataList);

    }

    //临时表
    @Test
    public void test2(){
        String payload = "{\"DevCode\":\"测试4\",\"OnlineStatus\":1,\"SendTime\":1690726906447,\"SeqId\":4,\"values\":{\"485 address\":1,\"Baud code\":3,\"CH4 concentration\":0,\"CO concentration\":0,\"Communication Status\":1,\"ETH working mode\":1,\"Embedded version\":258,\"H2S concentration\":0,\"IP Address-1\":192,\"IP Address-2\":168,\"IP Address-3\":1,\"IP Address-4\":101,\"MODBUS port\":502,\"Parity check\":0,\"Pump speed feedback\":0,\"Pump working mode setting\":0,\"Type\":1537,\"WIFI switch\":1,\"gateway-1\":192,\"gateway-2\":168,\"gateway-3\":1,\"gateway-4\":1,\"mask-1\":255,\"mask-2\":255,\"mask-3\":255,\"mask-4\":0,\"O2 concentration\":20.89,\"Pump speed setting\":1}}";
//        String payload = "{\"DevCode\":\"测试1\",\"OnlineStatus\":1,\"SendTime\":1690726909446,\"SeqId\":4,\"values\":{\"O2 concentration\":19.9}}";
        String topic = "/values/interval";
        //将json转map,方便读取数据
        Map<String,Object> mapJson = messageResolve(JSONObject.parseObject(payload),topic);

        //拿到mapJson先放入临时表
        EmqCurrent emqCurrent = EmqCurrent.init(mapJson);
        EmqCurrentMapper currentMapper = SpringUtil.getBean(EmqCurrentMapper.class);
        currentMapper.insert(emqCurrent);
        EmqCurrentMapper emqCurrentMapper = SpringUtil.getBean(EmqCurrentMapper.class);

        //LambdaQueryWrapper进行查询操作，使用lambda表达式构建SQL查询语句
        LambdaQueryWrapper<EmqCurrent> emqCurrentLambdaQueryWrapper = new LambdaQueryWrapper<EmqCurrent>();
        emqCurrentLambdaQueryWrapper.select(
                EmqCurrent::getCommunicationStatus, EmqCurrent::getType, EmqCurrent::getEmbeddedVersion,
                EmqCurrent::getAddress485, EmqCurrent::getBaudCode, EmqCurrent::getParityCheck,
                EmqCurrent::getIpAddress1, EmqCurrent::getIpAddress2, EmqCurrent::getIpAddress3,
                EmqCurrent::getIpAddress4, EmqCurrent::getMask1, EmqCurrent::getMask2,
                EmqCurrent::getMask3, EmqCurrent::getMask4, EmqCurrent::getGateway1,
                EmqCurrent::getGateway2, EmqCurrent::getGateway3, EmqCurrent::getGateway4,
                EmqCurrent::getModbusPort, EmqCurrent::getWifiSwitch, EmqCurrent::getEthWorkingMode,
                EmqCurrent::getCoConcentration, EmqCurrent::getH2sConcentration, EmqCurrent::getO2Concentration,
                EmqCurrent::getCh4Concentration, EmqCurrent::getPumpWorkingModeSetting, EmqCurrent::getPumpSpeedFeedback, EmqCurrent::getPumpSpeedSetting,
                EmqCurrent::getDevCode,EmqCurrent::getOnlineStatus,EmqCurrent::getSendTime,EmqCurrent::getId,EmqCurrent::getSeqId,EmqCurrent::getValue);
        List<EmqCurrent> emqCurrentList = emqCurrentMapper.selectList(emqCurrentLambdaQueryWrapper);
        System.out.println("emqCurrentList"+emqCurrentList);
//        List<EmqInterval> emqIntervalList = CglibUtil.copyList(emqCurrentList,EmqInterval::new);

        //根据topic放入不同的数据表中
        List<EmqInterval> emqIntervalList = BeanCopyUtil.copyListProperties(emqCurrentList,EmqInterval::new);
        System.out.println("emqIntervalList"+emqIntervalList);
        emqIntervalService.saveBatch(emqIntervalList);

//        if (!"4".equals(mapJson.get("SeqId").toString())){
//            //数据入库
//            if (!saveMessage(mapJson,topic)){
//                log.error("======》》接收ID : " + message.getId() + "==》》未识别的topic - {}",payload);
//            }
//        }else {
//            switch (topic){
//                case Topic.VALUES_INTERVAL:
//                    EmqInterval emqInterval = EmqInterval.init(mapJson);
//                    EmqIntervalMapper intervalMapper = SpringUtil.getBean(EmqIntervalMapper.class);
//                    intervalMapper.insert(emqInterval);
//                    break;
//                case Topic.VALUES_ONCHANGE:
//                    EmqOnchange emqOnchange = EmqOnchange.init(mapJson);
//                    EmqOnchangeMapper onchangeMapper = SpringUtil.getBean(EmqOnchangeMapper.class);
//                    onchangeMapper.insert(emqOnchange);
//                    break;
//                default:
//            }
//        }
//    }

    }

    @Test
    void test3(){

    }




    public Map<String,Object> messageResolve(JSONObject json,String topic){

        Map<String,Object> mapJson = json.getInnerMap();
        for (String key: mapJson.keySet()){
            mapJson.replace(key,String.valueOf(mapJson.get(key)));
            String str = String.valueOf(mapJson.get(key));
            if ("null".equals(str)) {
                mapJson.replace(key, null);
            }
            if ("SendTime".equals(key)){
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                mapJson.replace(key,date);
            }else if ("AlarmStart".equals(key)){
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                mapJson.replace(key,date);
            }
        }

        //拆分
        JSONObject getJson = JSONObject.parseObject((String)mapJson.get("values"));
        Map<String,Object> newmap = getJson.getInnerMap();
        System.out.println("newmap"+newmap.keySet());
        String msg = String.valueOf(json.get("values"));
        Map<String,Object> returnMap= JSON.parseObject(msg, HashMap.class);
        for(String key : newmap.keySet()){
            String str = String.valueOf(returnMap.get(key));
            if ("null".equals(str)) {
                newmap.replace(key, null);
            }else{
                newmap.replace(key, String.valueOf(returnMap.get(key)));
            }
        }
        mapJson.putAll(newmap);
        new SplitMessage().compare(ymlAnalysis,mapJson,topic);
        return mapJson;
    }

    public boolean saveMessage(Map<String,Object> mapJson, String topic){

        //实例化入库方法
        EmqServiceImpl emqService=SpringUtil.getBean(EmqServiceImpl.class);
        //调用入库方法
        if (Topic.VALUES_ONCHANGE.equals(topic)){
            emqService.onChange(mapJson);
        }else if (Topic.VALUES_INTERVAL.equals(topic)){
            emqService.interval(mapJson);
        }else if (Topic.ALARM_UPLOAD.equals(topic)){
            emqService.upload(mapJson);
        }else if (Topic.ALARM_UPOVERLOAD.equals(topic)){
            emqService.upoverload(mapJson);
        }else if (Topic.CONTROL_SET.equals(topic)){
            emqService.set(mapJson);
        }else if(Topic.CONTROL_RESP.equals(topic)){
            emqService.resp(mapJson);
        }else{
            return false;
        }
        log.info("消息处理结束");
        return true;
    }

    public void compare(YmlAnalysis ymlAnalysis,Map<String, Object> mapJson) {
        Set<String> keySet = mapJson.keySet();
        StringBuffer str = new StringBuffer();
        //创建一个map获取yml中的内容
        Map<String,Object> ymlmap = ymlAnalysis.getValue();
        for(String key:ymlmap.keySet()){
            if (keySet.stream().noneMatch(ket -> ket.equals(ymlmap.get(key)))) {
                str.append(key).append(", ");
            }
        }
        if(str.length() > 0){
            str.deleteCharAt(str.length() - 1);
        }
        if (!StringUtils.isEmpty(str)){
            log.info("点位：" + str + " 未发送!");
        }
    }









}



