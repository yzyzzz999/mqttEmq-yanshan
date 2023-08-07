package com.example.emqdemo.util;

import cn.hutool.core.util.ObjectUtil;
import com.example.emqdemo.compoent.RedisUtil;
import com.example.emqdemo.constants.Constants;
import com.example.emqdemo.domain.TGasData;
import com.example.emqdemo.domain.TGasDataAlarm;
import com.example.emqdemo.domain.TGasDataCurrent;
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
                tGasData.setAlarm(setAlarm((String) mapJson.get("SeqId"),gasType,tGasData.getCreateTime()));
                gasDataList.add(tGasData);
            }
        }

        return gasDataList;
    }

    public List<TGasDataCurrent> gasDataCurrentsInit(TGasRawData tGasRawData, Map<String,Object> gasMapJson){
        List<TGasDataCurrent> tGasDataCurrentList = new ArrayList<>();
        Map<String,Object> gasCurrentDict = redisUtil.getCacheMap(Constants.GAS_DICT);
        for (String gasCurrentType:gasCurrentDict.keySet()){
            String key = gasCurrentType + Constants.MQTT_SUFFIX;
            if (gasMapJson.containsKey(key)){
                TGasDataCurrent tGasDataCurrent = new TGasDataCurrent();
                tGasDataCurrent.setCurrentId(IdGen.genId().toString());
                tGasDataCurrent.setRawDataId(tGasRawData.getId());
                tGasDataCurrent.setSerialNumber(tGasRawData.getSerialNumber());
                tGasDataCurrent.setAlarm(setAlarm((String) gasMapJson.get("SeqId"),gasCurrentType,tGasDataCurrent.getCreateTime()));
                if (ObjectUtil.isNotNull(gasMapJson.get(key))){
                    tGasDataCurrent.setGasValue(new BigDecimal(String.valueOf(gasMapJson.get(key))));
                }
                tGasDataCurrent.setGasType(key);
                tGasDataCurrent.setGasUnit((String) gasCurrentDict.get(gasCurrentType));
                tGasDataCurrent.setCreateTime(new Date());
                tGasDataCurrentList.add(tGasDataCurrent);
            }
        }
        return tGasDataCurrentList;
    }

    public void saveAlarmStart(Map<String, Object> mapJson) {
        String alarmPoint = (String) mapJson.get("AlarmPoint");
        String device = (String) mapJson.get("AlarmDevice");
        Map<String,Object> gasDict = redisUtil.getCacheMap(Constants.GAS_DICT);
        for (String gasType:gasDict.keySet()){
            String key = gasType+ Constants.MQTT_SUFFIX;
            Date alarmStart = new Date(Long.parseLong((String) mapJson.get("AlarmStart")));
            Map<String,Date> alarmSet = new HashMap<>();
            if (alarmPoint.equals(key)){
                switch (gasType){
                    case Constants.GAS_O2:
                        alarmSet.put(Constants.ALARM_O2,alarmStart);
                        redisUtil.setCacheMap(device +" " + alarmPoint,alarmSet);
                        break;
                    case Constants.GAS_CH4:
                        alarmSet.put(Constants.ALARM_CH4,alarmStart);
                        redisUtil.setCacheMap(device +" " + alarmPoint,alarmSet);
                        break;
                    case Constants.GAS_H2S:
                        alarmSet.put(Constants.ALARM_H2S,alarmStart);
                        redisUtil.setCacheMap(device +" " + alarmPoint,alarmSet);
                        break;
                    case Constants.GAS_CO:
                        alarmSet.put(Constants.ALARM_CO,alarmStart);
                        redisUtil.setCacheMap(device +" " + alarmPoint,alarmSet);
                        break;
                    default:
                }
            }
        }
    }

    public void saveAlarmEnd(Map<String, Object> mapJson) {
        String alarmPoint = (String) mapJson.get("AlarmPoint");
        String device = (String) mapJson.get("AlarmDevice");
        Map<String,Object> gasDict = redisUtil.getCacheMap(Constants.GAS_DICT);
        for (String gasType:gasDict.keySet()){
            String key = gasType+ Constants.MQTT_SUFFIX;
            if (alarmPoint.equals(key)){
                redisUtil.deleteObject(device +" " + alarmPoint);
            }
        }
    }

    public String setAlarm(String device, String gasType,Date createTime){
        String key = device +" " + gasType + Constants.MQTT_SUFFIX;
        if (redisUtil.hasKey(key)){
            Map<String,Date> alarmSet = redisUtil.getCacheMap(key);
            if (createTime.compareTo(alarmSet.get(gasType)) < 0){
                switch (gasType){
                    case Constants.GAS_O2:
                    case Constants.GAS_CH4:
                    case Constants.GAS_H2S:
                        return Constants.ALARM_HIGH;
                    case Constants.GAS_CO:
                        return Constants.ALARM_LOW;
                    default:
                        return Constants.ALARM_NORMAL;
                }
            }
        }
        return Constants.ALARM_NORMAL;
    }
}
