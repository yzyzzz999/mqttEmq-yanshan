package com.example.emqdemo.util;


import com.example.emqdemo.domain.emqInterval;
import com.example.emqdemo.domain.emqOnchange;
import com.example.emqdemo.domain.emqResp;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;



@Slf4j
public class splitMessage {

    emqResp emqresp = new emqResp();
    emqInterval emqinterval = new emqInterval();

    emqOnchange emqOnchange = new emqOnchange();

    public boolean compare(Map<String, Object> mapJson, String topic) {
        Field[] fields = new Field[0];
        switch (topic) {
            case "/control/resp":
                fields = emqresp.getClass().getDeclaredFields();
                break;
            case "/control/onChange":
                fields = emqOnchange.getClass().getDeclaredFields();
                break;
            case "/values/interval":
                fields = emqinterval.getClass().getDeclaredFields();
                break;
            default:
        }

        Set<String> keySet = mapJson.keySet();
        boolean flag = true;
        StringBuffer str = new StringBuffer();
        for (Field field : fields) {
            if (!keySet.stream().anyMatch(ket -> ket.equals(field.getName()))) {
                flag = false;
                str.append(field.getName()+",");
//                log.info(field.getName() + "丢失");
            }
        }
        if(str.length() > 0){
            str.deleteCharAt(str.length() - 1);
        }
        log.info(str+"丢失");
        return flag;
    }

}



