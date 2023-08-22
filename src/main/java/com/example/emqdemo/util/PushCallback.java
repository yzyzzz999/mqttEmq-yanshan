package com.example.emqdemo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.emqdemo.domain.*;
import com.example.emqdemo.service.TGasDataCurrentService;
import com.example.emqdemo.service.TGasDataService;
import com.example.emqdemo.service.TGasRawDataService;
import com.example.emqdemo.service.impl.EmqServiceImpl;
import com.example.emqdemo.util.mqttUtil.MqttPushClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class PushCallback implements MqttCallback {

    private MqttPushClient client;
    private MqttConfiguration mqttConfiguration;

    @Autowired
    private SplitMessage splitMessage;

    @Autowired
    private YmlAnalysis ymlAnalysis;

    @Autowired
    private DataUtil dataUtil;

    @Autowired
    private TGasRawDataService tGasRawDataService;

    @Autowired
    private TGasDataService tGasDataService;

    @Autowired
    private TGasDataCurrentService tGasDataCurrentService;
    public void init(MqttPushClient client ,MqttConfiguration mqttConfiguration) {
        this.client = client;
        this.mqttConfiguration = mqttConfiguration;
    }

    @Override
    public void connectionLost(Throwable cause) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        final Future<?> []futures = {null};
        futures[0] = executorService.scheduleWithFixedDelay(new TimerTask() {
            @Override
            public void run() {
                /* 连接丢失后，一般在这里面进行重连 **/
                if(client != null) {
                    try {
                        log.info("==============》》》[MQTT] 连接断开，2S之后尝试重连...");
//                    MqttPushClient mqttPushClient = new MqttPushClient();
                        client.connect(mqttConfiguration);
                        if(MqttPushClient.getClient().isConnected()){
                            log.info("=============>>重连成功");
                            Future<?> future;
                            while (null == (future = futures[0])) {
                                Thread.yield();
                            }
                            future.cancel(false);
                            return;
                        }
//                        break;
                    } catch (Exception e) {
                        log.error("=============>>>[MQTT] 连接断开，重连失败！<<=============");
                        e.printStackTrace();
                        run();
//                            continue;
                    }

                }
                log.info(cause.getMessage());

            }
        },0,2,TimeUnit.SECONDS);
    }
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //publish后会执行到这里
        log.info("publish后会执行到这里");
        log.info("pushComplete==============>>>" + token.isComplete());
    }


    /**
     * 监听对应的主题消息
     * @param topic 主题
     * @param message mqtt消息
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        // subscribe后得到的消息会执行到这里面
        String payload = new String(message.getPayload());
        log.info("============》》接收消息主题 : " + topic);
        log.info("============》》接收消息Qos : " + message.getQos());
        log.info("============》》接收消息内容 : " + payload);
        log.info("============》》接收ID : " + message.getId());
        log.info("接收数据结束 下面可以执行数据处理操作");

        //将json转map,方便读取数据
        Map<String,Object> mapJson = messageResolve(JSONObject.parseObject(payload),topic);
        if (mapJson.get("SeqId") == null && mapJson.get("AlarmDevice") == null){
            log.info("======》》设备ID不存在!!");
            return;
        }
        //将json转TGasRawData备份
        String serialNumber = (mapJson.get("SeqId") != null ? mapJson.get("SeqId"):mapJson.get("AlarmDevice")).toString();
        TGasRawData tGasRawData = dataUtil.rawDataInit(payload,serialNumber);
        tGasRawDataService.saveGasRawData(tGasRawData);
        log.info("原始数据备份到t_gas_raw_data！");
        if ("5".equals(mapJson.get("SeqId").toString())){
            log.info("锂电池数据暂不做其他处理！");
            return;
        }
        //转存
        switch (topic){
            case Topic.VALUES_INTERVAL:
            case Topic.VALUES_ONCHANGE:
                //mqtt数据转t_gas_data
                log.info("数据转存全量表t_gas_data");
                try{
                    List<TGasData> gasDataList = dataUtil.gasDatasInit(mapJson,tGasRawData.getId());
                    log.info("gasDataList - {}",gasDataList);
                    tGasDataService.batchSaveGasData(gasDataList);
                }catch (Exception e){
                    e.printStackTrace();
                }
                //mqtt数据转t_gas_data_current
                log.info("数据转存当前表t_gas_data_current");
                try{
                    List<TGasDataCurrent> tGasDataCurrentList = dataUtil.gasDataCurrentsInit(tGasRawData,mapJson);
                    log.info("tGasDataCurrentList - {}",tGasDataCurrentList);
                    tGasDataCurrentService.batchSaveGasData(tGasDataCurrentList);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case Topic.ALARM_UPLOAD:
                log.info("save alarmStart to cache");
                try {
                    dataUtil.saveAlarmStart(mapJson);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case Topic.ALARM_UPOVERLOAD:
                log.info("save slarmDelete to cache");
                try {
                    dataUtil.saveAlarmEnd(mapJson);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                log.info("123456789");
                log.info("123456789");
                log.info("123456789");
                log.info("======》》接收ID : " + message.getId() + "==》》未识别的topic - {}",payload);
        }
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
        try {
//            YmlAnalysis ymlAnalysis = SpringUtil.getBean(YmlAnalysis.class);
            splitMessage.compare(ymlAnalysis,mapJson,topic);
        } catch (RuntimeException e){
            throw e;
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
