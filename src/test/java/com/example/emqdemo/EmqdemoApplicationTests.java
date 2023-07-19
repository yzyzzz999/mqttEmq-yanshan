package com.example.emqdemo;

import com.alibaba.fastjson.JSONObject;
import com.example.emqdemo.mapper.EmqMapper;
import com.example.emqdemo.service.impl.EmqServiceImpl;
import com.example.emqdemo.util.SpringUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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
        String Payload = "{\"DevCode\":\"Four in one sensor\",\"OnlineStatus\":1,\"SendTime\":1689735249529,\"SeqId\":4,\"values\":{\"485 address\":1,\"Baud code\":3,\"CH4 concentration\":0,\"CO concentration\":0,\"Communication Status\":1,\"ETH working mode\":1,\"Embedded version\":258,\"H2S concentration\":0,\"IP Address-1\":192,\"IP Address-2\":168,\"IP Address-3\":1,\"IP Address-4\":101,\"MODBUS port\":502,\"O2 concentration\":20.9,\"Parity check\":0,\"Pump speed feedback\":0,\"Pump speed setting\":0,\"Pump working mode setting\":0,\"Type\":1537,\"WIFI switch\":1,\"gateway-1\":192,\"gateway-2\":168,\"gateway-3\":1,\"gateway-4\":1,\"mask-1\":255,\"mask-2\":255,\"mask-3\":255,\"mask-4\":0}}";
        JSONObject json  =  JSONObject.parseObject(Payload);
        Map<String,Object> mapJson = json.getInnerMap();
        for (String key: mapJson.keySet()){
            mapJson.replace(key,mapJson.get(key).toString());
            if (key.equals("SendTime")){
                Date date = new Date(Long.parseLong((String) mapJson.get(key)));
                System.out.println(date);
                mapJson.replace(key,date);
            }
        }
        EmqServiceImpl emqService= SpringUtil.getBean(EmqServiceImpl.class);
        emqService.interval(mapJson);
        System.out.println(mapJson);
    }

}
