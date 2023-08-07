package com.example.emqdemo.util.mqttUtil;

import com.example.emqdemo.domain.MqttConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Mqttbeanbak {
    @Autowired
    private MqttConfiguration mqttConfiguration;
//    @Bean("mqttPushClient")
//    public MqttPushClient getMqttPushClient() {
//        MqttPushClient mqttPushClient = new MqttPushClient();
//        return mqttPushClient;
//    }
}
