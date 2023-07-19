package com.example.emqdemo.service;

import java.util.Map;

public interface EmqService {

    void onChange(Map<String,Object> mapJson);

    void interval(Map<String,Object> mapJson);

    void set(Map<String,Object> mapJson);

    void resp(Map<String, Object> mapJson);

    void upload(Map<String,Object> mapJson);

    void upoverload(Map<String, Object> mapJson);
}
