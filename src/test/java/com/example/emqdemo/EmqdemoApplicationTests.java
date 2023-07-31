package com.example.emqdemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.emqdemo.domain.EmqInterval;
import com.example.emqdemo.domain.EmqOnchange;
import com.example.emqdemo.domain.Topic;
import com.example.emqdemo.mapper.EmqIntervalMapper;
import com.example.emqdemo.mapper.EmqMapper;
import com.example.emqdemo.mapper.EmqOnchangeMapper;
import com.example.emqdemo.service.EmqService;
import com.example.emqdemo.service.impl.EmqServiceImpl;
import com.example.emqdemo.util.SpringUtil;
import com.example.emqdemo.util.splitMessage;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest
class EmqdemoApplicationTests {

    @Autowired
    private EmqMapper emqMapper;

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
        String payload = "{\"DevCode\":\"测试\",\"OnlineStatus\":1,\"SendTime\":1690726906447,\"SeqId\":4,\"values\":{\"485 address\":1,\"Baud code\":3,\"CH4 concentration\":0,\"CO concentration\":0,\"Communication Status\":1,\"ETH working mode\":1,\"Embedded version\":258,\"H2S concentration\":0,\"IP Address-1\":192,\"IP Address-2\":168,\"IP Address-3\":1,\"IP Address-4\":101,\"MODBUS port\":502,\"Parity check\":0,\"Pump speed feedback\":0,\"Pump working mode setting\":0,\"Type\":1537,\"WIFI switch\":1,\"gateway-1\":192,\"gateway-2\":168,\"gateway-3\":1,\"gateway-4\":1,\"mask-1\":255,\"mask-2\":255,\"mask-3\":255,\"mask-4\":0}}";
//        payload = "{\"DevCode\":\"测试\",\"OnlineStatus\":1,\"SendTime\":1690726909446,\"SeqId\":4,\"values\":{\"O2 concentration\":19.9}}";
        String topic = "/values/onchange";
        //将json转map,方便读取数据
        Map<String,Object> mapJson = messageResolve(JSONObject.parseObject(payload),topic);

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
                    //非点位数据直接入库
                    if (saveMessage(mapJson,topic)){
                        log.error("======》》未识别的topic - {}",payload);
                    }
                    break;
            }
        }
        //emqService.upload(mapJson);
    }

    @Test
    void test1() {
        String Payload = "{\"guid\":\"131\",\"code\":\"abc\",\"msg\":\"{\\\"IP Address-1\\\":1}\"}";
        String topic = "/control/resp";
        JSONObject json = JSONObject.parseObject(Payload);
        Map<String,Object> map = json.getInnerMap();

        JSONObject mapJson = JSONObject.parseObject((String)map.get("msg"));
        Map<String,Object> newmap = mapJson.getInnerMap();
        System.out.println(newmap.keySet());

        for(String key : newmap.keySet()){
            String val = String.valueOf(newmap.get(key));
            if (val.equals("null")) {
                newmap.replace(key, null);
            }else {
                newmap.replace(key, String.valueOf(newmap.get(key)));
            }
        }
        map.putAll(newmap);
        System.out.println(map.keySet());
        if (!new splitMessage().compare(map,topic)){
//            log.error("存在字段为空");
        }

        EmqServiceImpl emqService = SpringUtil.getBean(EmqServiceImpl.class);
        emqService.resp(map);
    }



    /**
     * mqtt数据-转map
     * @param json mqtt数据
     * @param topic mqtt主题
     * @return mapJson
     */
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
        System.out.println(newmap.keySet());
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
        if (new splitMessage().compare(mapJson,topic)){
            log.error("字段为空");
        }
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


}
