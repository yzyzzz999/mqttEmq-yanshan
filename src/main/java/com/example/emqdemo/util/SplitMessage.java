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
    public void compare(YmlAnalysis ymlAnalysis,Map<String, Object> mapJson,String topic) {
        Set<String> keySet = mapJson.keySet();
        StringBuffer str = new StringBuffer();
        //创建一个map获取yml中的内容
        Map<String,Object> ymlmap = ymlAnalysis.getValue();
        for(String key:ymlmap.keySet()){
            if (keySet.stream().noneMatch(ket -> ket.equals(ymlmap.get(key)))) {
                str.append(key).append(", ");
            }
        }
        if(str.length() > 0){
            str.deleteCharAt(str.length() - 1);
        }
        if (!StringUtils.isEmpty(str)){
            log.info("点位：" + str + " 未发送!");
        }
    }
}



