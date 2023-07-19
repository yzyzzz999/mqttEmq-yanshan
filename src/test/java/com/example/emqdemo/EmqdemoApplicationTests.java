package com.example.emqdemo;

import com.alibaba.fastjson.JSONObject;
import com.example.emqdemo.domain.Topic;
import com.example.emqdemo.mapper.EmqMapper;
import com.example.emqdemo.service.EmqService;
import com.example.emqdemo.service.impl.EmqServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@SpringBootTest
class EmqdemoApplicationTests {

    @Autowired
    private EmqMapper emqMapper;

    @Test
    void contextLoads() {
        Map<String,Object> resp=new HashMap<>();
        resp.put("guid","123");
        resp.put("code","aaa");
        resp.put("msg","aaa");
        emqMapper.resp(resp);
    }

}
