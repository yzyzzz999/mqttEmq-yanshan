package com.example.emqdemo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.emqdemo.domain.Topic;
import com.example.emqdemo.mapper.EmqMapper;
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
        String Payload = "{\"AlarmDesc\":\"这是一条测试\",\"AlarmDevice\":\"这是一条测试\",\"AlarmGenValue\":0,\"AlarmGuid\":\"baeb1bf-643-489-5a-78fefeded4ha\",\"AlarmIndex\":null,\"AlarmLevel\":2,\"AlarmPoint\":\"Pump speed fehack\",\"AlarmStart\":168975600900,\"AlarmStatus\":1}";
        JSONObject json = JSONObject.parseObject(Payload);
        Map<String, Object> mapJson = json.getInnerMap();
        System.out.println(mapJson.keySet());
        for (String key : mapJson.keySet()) {
            mapJson.replace(key, String.valueOf(mapJson.get(key)));
            String str = String.valueOf(mapJson.get(key));
            if (str.equals("null")) {
                mapJson.replace(key, null);
            }
            if ("SendTime".equals(key)) {
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                mapJson.replace(key, date);
            } else if ("AlarmStart".equals(key)) {
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                mapJson.replace(key, date);
            }
        }
        EmqServiceImpl emqService = SpringUtil.getBean(EmqServiceImpl.class);
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


}
