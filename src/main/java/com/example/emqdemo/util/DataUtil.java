package com.example.emqdemo.util;

import cn.hutool.core.util.ObjectUtil;
import com.example.emqdemo.compoent.RedisUtil;
import com.example.emqdemo.constants.Constants;
import com.example.emqdemo.domain.TGasData;
import com.example.emqdemo.domain.TGasRawData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * 数据处理工具
 */
@Component
public class DataUtil {

    @Autowired
    private RedisUtil redisUtil;

    public TGasRawData rawDataInit(String payLoad, String seqId){
        TGasRawData tGasRawData = new TGasRawData();
        tGasRawData.setId(IdGen.genId());
        tGasRawData.setRawData(payLoad);
        tGasRawData.setCreateTime(new Date());
        tGasRawData.setSerialNumber(seqId);
        return tGasRawData;
    }

    public List<TGasData> gasDatasInit(Map<String, Object> mapJson, Long rawDataId) {
        List<TGasData> gasDataList = new ArrayList<>();
        Map<String,Object> gasDict = redisUtil.getCacheMap(Constants.GAS_DICT);
        for (String gasType:gasDict.keySet()){
            String key = gasType+ Constants.MQTT_SUFFIX;
            if (mapJson.containsKey(key)){
                TGasData tGasData = new TGasData();
                tGasData.setId(IdGen.genId());
                tGasData.setRawDataId(rawDataId);
                tGasData.setSerialNumber((String) mapJson.get("SeqId"));
                if (ObjectUtil.isNotNull(mapJson.get(key))){
                    tGasData.setGasValue(new BigDecimal((String)mapJson.get(key)));
                }
                tGasData.setGasType(gasType);
                tGasData.setGasUnit((String) gasDict.get(gasType));
                tGasData.setCreateTime(new Date());
                gasDataList.add(tGasData);
            }
        }

        return null;
    }
}
