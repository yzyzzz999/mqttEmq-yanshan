package com.example.emqdemo.service.impl;

import com.example.emqdemo.mapper.EmqMapper;
import com.example.emqdemo.service.EmqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class EmqServiceImpl implements EmqService {

    @Autowired
    private EmqMapper emqMapper;

    @Override
    public void onChange(Map<String,Object> mapJson) {
        emqMapper.onChange(mapJson);
    }

    @Override
    public void interval(Map<String, Object> mapJson) {
        emqMapper.interval(mapJson);
    }

    /**
     * 该内容由控制端发布，guid为任意值，⽤于回复的时候对应指令，后续3个字段含义为将device1的pointA点点值设置为1
     * @param mapJson
     */
    @Override
    public void set(Map<String, Object> mapJson) {
        emqMapper.set(mapJson);
    }

    @Override
    public void resp(Map<String, Object> mapJson) {
        emqMapper.resp(mapJson);
    }

    @Override
    public void upload(Map<String, Object> mapJson) {
        emqMapper.upload(mapJson);
    }

    @Override
    public void upoverload(Map<String, Object> mapJson) {
        emqMapper.upoverload(mapJson);
    }
}
