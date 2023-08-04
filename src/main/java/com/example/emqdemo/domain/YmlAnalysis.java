package com.example.emqdemo.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "custom.map")
public class YmlAnalysis {
    Map<String,Object> value;
}



