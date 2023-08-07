package com.example.emqdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emqdemo.compoent.RedisUtil;
import com.example.emqdemo.constants.Constants;
import com.example.emqdemo.domain.TPubCode;
import com.example.emqdemo.mapper.TPubCodeMapper;
import com.example.emqdemo.service.TPubCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
@Service
public class TPubCodeServiceImpl extends ServiceImpl<TPubCodeMapper, TPubCode> implements TPubCodeService {

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 气体类型-单位初始化
     */
    @Override
    public void cacheGasDict() {
        Map<String,String> gasDict = new HashMap<>();
        gasDict.put(Constants.GAS_CO,Constants.UNIT_CO);
        gasDict.put(Constants.GAS_H2S,Constants.UNIT_H2S);
        gasDict.put(Constants.GAS_O2,Constants.UNIT_O2);
        gasDict.put(Constants.GAS_CH4,Constants.UNIT_CH4);
        redisUtil.setCacheMap(Constants.GAS_DICT,gasDict);
    }

    /**
     * 气体告警-点位初始化
     */
    @Override
    public void cacheAlarmDict() {
        Map<String,String> alarmDict = new HashMap<>();
        alarmDict.put(Constants.GAS_CO,Constants.ALARM_CO);
        alarmDict.put(Constants.GAS_H2S,Constants.ALARM_H2S);
        alarmDict.put(Constants.GAS_O2,Constants.ALARM_O2);
        alarmDict.put(Constants.GAS_CH4,Constants.ALARM_CH4);
        redisUtil.setCacheMap(Constants.ALARM_DICT,alarmDict);

    }
}
