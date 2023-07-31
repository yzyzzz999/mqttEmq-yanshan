package com.example.emqdemo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.emqdemo.domain.EmqInterval;
import com.example.emqdemo.domain.EmqOnchange;
import com.example.emqdemo.domain.MqttConfiguration;
import com.example.emqdemo.domain.Topic;
import com.example.emqdemo.mapper.EmqIntervalMapper;
import com.example.emqdemo.mapper.EmqOnchangeMapper;
import com.example.emqdemo.service.impl.EmqServiceImpl;
import com.example.emqdemo.util.mqttUtil.MqttPushClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PushCallback implements MqttCallback {

    private MqttPushClient client;
    private MqttConfiguration mqttConfiguration;

    public PushCallback(MqttPushClient client ,MqttConfiguration mqttConfiguration) {
        this.client = client;
        this.mqttConfiguration = mqttConfiguration;
    }

    @Override
    public void connectionLost(Throwable cause) {
        /* 连接丢失后，一般在这里面进行重连 **/
        if(client != null) {
            while (true) {
                try {
                    log.info("==============》》》[MQTT] 连接断开，2S之后尝试重连...");
                    Thread.sleep(2000);
                    MqttPushClient mqttPushClient = new MqttPushClient();
                    mqttPushClient.connect(mqttConfiguration);
                    if(MqttPushClient.getClient().isConnected()){
                        log.info("=============>>重连成功");
                    }
                    break;
                } catch (Exception e) {
                    log.error("=============>>>[MQTT] 连接断开，重连失败！<<=============");
                    continue;
                }
            }
        }
        log.info(cause.getMessage());
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
        if ((new splitMessage().compare(mapJson,topic))){
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
