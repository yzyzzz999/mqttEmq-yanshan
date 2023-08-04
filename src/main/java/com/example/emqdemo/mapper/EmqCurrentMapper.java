package com.example.emqdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.emqdemo.domain.EmqCurrent;
import com.example.emqdemo.domain.EmqInterval;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface EmqCurrentMapper extends BaseMapper<EmqCurrent> {

}