package com.example.emqdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emqdemo.domain.EmqCurrent;
import com.example.emqdemo.mapper.EmqCurrentMapper;
import com.example.emqdemo.service.EmqCurrentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmqCurrentServiceImpl extends ServiceImpl<EmqCurrentMapper, EmqCurrent> implements EmqCurrentService {

    @Autowired
    private EmqCurrentMapper emqCurrentMapper;
    @Autowired
    public List<EmqCurrent> list(){
        List<EmqCurrent> list = new ArrayList<>();

        return list;

    }
}
