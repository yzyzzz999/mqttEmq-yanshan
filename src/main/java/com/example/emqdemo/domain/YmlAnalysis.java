package com.example.emqdemo.domain;

import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "custom.map")
@Setter
public class YmlAnalysis {
    Map<String,Object> value;
}



