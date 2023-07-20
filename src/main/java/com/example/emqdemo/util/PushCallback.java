package com.example.emqdemo.util;

import com.alibaba.fastjson.JSONObject;
import com.example.emqdemo.domain.MqttConfiguration;
import com.example.emqdemo.domain.Topic;
import com.example.emqdemo.service.impl.EmqServiceImpl;
import com.example.emqdemo.util.mqttUtil.MqttPushClient;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

import java.util.Date;
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
        JSONObject json  =  JSONObject.parseObject(payload);
        Map<String,Object> mapJson = json.getInnerMap();
        for (String key: mapJson.keySet()){
            mapJson.replace(key,String.valueOf(mapJson.get(key)));
            if ("SendTime".equals(key)){
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                mapJson.replace(key,date);
            }else if ("AlarmStart".equals(key)){
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                mapJson.replace(key,date);
            }
        }
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
            log.error("======》》未识别的topic - {}",payload);
        }
        log.info("消息处理结束");
    }
}
