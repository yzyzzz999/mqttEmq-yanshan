package com.example.emqdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.emqdemo.domain.EmqCurrent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmqCurrentService extends IService<EmqCurrent> {
    List<EmqCurrent> list();

}
