package com.example.emqdemo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface EmqMapper {

    void onChange (Map<String, Object> mapJson);

    void interval (Map<String, Object> mapJson);

    void set(Map<String, Object> mapJson);

    void resp(Map<String, Object> mapJson);

    void upload(Map<String, Object> mapJson);

    void upoverload(Map<String, Object> mapJson);
}
