package com.example.emqdemo;

import com.alibaba.fastjson.JSONObject;
import com.example.emqdemo.domain.Topic;
import com.example.emqdemo.mapper.EmqMapper;
import com.example.emqdemo.service.EmqService;
import com.example.emqdemo.service.impl.EmqServiceImpl;
import com.example.emqdemo.util.SpringUtil;
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

    @Test
    void test(){
        String Payload = "{\"DevCode\":\"123124\",\"OnlineStatus\":1,\"SendTime\":1689754853894,\"SeqId\":4,\"values\":{\"Pump speed feedback\":0,\"Pump working mode setting\":0}}";
        JSONObject json  =  JSONObject.parseObject(Payload);
        Map<String,Object> mapJson = json.getInnerMap();
        for (String key: mapJson.keySet()){
            mapJson.replace(key,mapJson.get(key).toString());
            if ("SendTime".equals(key)){
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                mapJson.replace(key,date);
            }else if ("AlarmStart".equals(key)){
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                mapJson.replace(key,date);
            }
        }
        EmqServiceImpl emqService= SpringUtil.getBean(EmqServiceImpl.class);
        emqService.interval(mapJson);
    }

}
