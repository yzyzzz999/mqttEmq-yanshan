package com.example.emqdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.emqdemo.util.mqttUtil.MqttPushClient;
import com.example.emqdemo.util.mqttUtil.MqttSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * ZDR
 * 说明:
 * 作用:
 * 目的:
 *
 * @DATE: 2021/3/3
 * @TIME: 12:26
 */
@RestController
@Slf4j

@ResponseBody
@RequestMapping("/mqtt")
public class MqttController {

    //发送逻辑
    @Autowired
    private MqttSender mqttSender;

    //订阅逻辑
    @Autowired
    private MqttPushClient mqttPushClient;





    /**
     * 向指定主题发布数据
     * @param typelist
     * @return
     */
    @PostMapping("/sendmqtt")
    public String sendmqttTexts(@RequestBody Map<String,String> typelist){
        if (whatJson(typelist.get("json"))){
            log.info("    本机主题:"+typelist.get("topic")+" 发送数据为:"+JSONObject.toJSONString(typelist.get("json")));
            mqttSender.send(typelist.get("topic"), typelist.get("json"));
        } else {
            log.info("发送的数据非JSON格式");
            return "非JSON格式发送失败";
        }
        return "发送结束";
    }



    /**
     * 订阅主题
     * @param
     * @param session
     * @return
     */
    @RequestMapping("/getsubscribetopic")
    public Object getsubscribetopic( @RequestBody Map<String,String> typelist, HttpSession session ){
        int Qos=1;
        String key = "";
        if(typelist!=null && typelist.size() > 0){
            key = typelist.get("topic");
            String[] topics={key};
            log.info("订阅主题为:"+key);
            int[] qos={2};
            mqttPushClient.subscribe(topics,qos);
            return key;
        }else{
            return "主题不能为空";
        }
    }

    /**
     * 取消订阅
     * @param
     * @param session
     * @return
     */
    @RequestMapping("/getcallsubscribe")
    public Object getcallsubscribe( @RequestBody Map<String,String> typelist, HttpSession session ){
        String key = "";
        if(typelist!=null && typelist.size() > 0){
            key = typelist.get("topic");
            mqttPushClient.cleanTopic(key);
        }
        return "取消订阅成功";
    }


    /**
     * 验证字符串是否严格按照JSON格式编写
     * @param StrJson 待验证的JSON串
     * @return
     */
    public boolean whatJson(String StrJson) {
        if(StringUtils.isEmpty(StrJson)){
            return false;
        }
        boolean isJsonObject = true;
        boolean isJsonArray = true;
        try {
            com.alibaba.fastjson.JSONObject.parseObject(StrJson);
        } catch (Exception e) {
            isJsonObject = false;
        }
        try {
            com.alibaba.fastjson.JSONObject.parseArray(StrJson);
        } catch (Exception e) {
            isJsonArray = false;
        }
        if(!isJsonObject && !isJsonArray){ //不是json格式
            return false;
        }
        return true;
    }


}
