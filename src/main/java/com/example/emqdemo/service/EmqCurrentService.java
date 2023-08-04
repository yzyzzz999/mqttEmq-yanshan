package com.example.emqdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.emqdemo.domain.EmqCurrent;
import com.example.emqdemo.domain.EmqInterval;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface EmqCurrentService extends IService<EmqCurrent> {
    List<EmqCurrent> list();

}