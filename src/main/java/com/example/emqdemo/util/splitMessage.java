package com.example.emqdemo.util;


import com.example.emqdemo.domain.EmqInterval;
import com.example.emqdemo.domain.EmqOnchange;
import com.example.emqdemo.domain.EmqResp;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;



@Slf4j
public class splitMessage {

    EmqResp Emqresp = new EmqResp();

    EmqInterval Emqinterval = new EmqInterval();


    EmqOnchange EmqOnchange = new EmqOnchange();

    public boolean compare(Map<String, Object> mapJson, String topic) {
        Field[] fields = new Field[0];
        switch (topic) {
            case "/control/resp":
                fields = Emqresp.getClass().getDeclaredFields();
                break;
            case "/control/onChange":
                fields = EmqOnchange.getClass().getDeclaredFields();
                break;
            case "/values/interval":
                fields = Emqinterval.getClass().getDeclaredFields();
                break;
            default:
        }

        Set<String> keySet = mapJson.keySet();
        boolean flag = true;
        StringBuffer str = new StringBuffer();
        for (Field field : fields) {
            if (keySet.stream().noneMatch(ket -> ket.equals(field.getName()))) {
                flag = false;
                str.append(field.getName()+",");
            }
        }
        if(str.length() > 0){
            str.deleteCharAt(str.length() - 1);
        }
        log.info(str+"丢失");
        return flag;
    }

}



