package com.example.emqdemo.util;


import com.example.emqdemo.domain.YmlAnalysis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;



@Slf4j
@Component
public class SplitMessage {
//    EmqResp Emqresp = new EmqResp();
//    EmqInterval Emqinterval = new EmqInterval();
//    EmqOnchange EmqOnchange = new EmqOnchange();

    public boolean compare(YmlAnalysis ymlAnalysis,Map<String, Object> mapJson, String topic) {
//        Field[] fields = new Field[0];
//        switch (topic) {
//            case "/control/resp":
//                fields = Emqresp.getClass().getDeclaredFields();
//                break;
//            case "/values/onchange":
//                fields = EmqOnchange.getClass().getDeclaredFields();
//                break;
//            case "/values/interval":
//                fields = Emqinterval.getClass().getDeclaredFields();
//                break;
//            default:
//        }
        Set<String> keySet = mapJson.keySet();
        boolean flag = true;
        StringBuffer str = new StringBuffer();
        //创建一个map获取yml中的内容
        Map<String,Object> ymlmap = ymlAnalysis.getValue();
        for(String key:ymlmap.keySet()){
            if (keySet.stream().noneMatch(ket -> ket.equals(ymlmap.get(key)))) {
                flag = false;
                str.append(key + ",");
            }
        }
//        for (Field field : fields) {
//            if (keySet.stream().noneMatch(ket -> ket.equals(field.getName()))) {
//                flag = false;
//                str.append(field.getName()+",");
//            }
//        }
        if(str.length() > 0){
            str.deleteCharAt(str.length() - 1);
        }
        if (!StringUtils.isEmpty(str)){
            log.info("点位：" + str + " 未发送!");
        }
        return flag;
    }


}



