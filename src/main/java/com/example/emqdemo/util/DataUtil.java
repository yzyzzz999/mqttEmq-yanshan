package com.example.emqdemo.util;

import com.example.emqdemo.domain.TGasRawData;

import java.util.Date;

/**
 * 数据处理工具
 */
public class DataUtil {

    public static TGasRawData rawDataInit(String payLoad, String seqId){
        TGasRawData tGasRawData = new TGasRawData();
        tGasRawData.setId(IdGen.genId());
        tGasRawData.setRawData(payLoad);
        tGasRawData.setCreateTime(new Date());
        tGasRawData.setSerialNumber(seqId);
        return tGasRawData;
    }
}
