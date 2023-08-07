package com.example.emqdemo;

import com.example.emqdemo.domain.MqttConfiguration;
import com.example.emqdemo.util.mqttUtil.MqttPushClient;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@Slf4j
@SpringBootApplication
@MapperScan("com.example.emqdemo.mapper")
@ConfigurationPropertiesScan
public class EmqdemoApplication implements ApplicationRunner {
    //读取mqtt配置
    @Autowired
    private MqttConfiguration mqttConfiguration;

    @Autowired
    private MqttPushClient mqttPushClient;


    public static void main(String[] args) {
        SpringApplication.run(EmqdemoApplication.class, args);
    }

    /**
     * mqtt 初始化
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(true){
            if (log.isInfoEnabled()){
                log.info("===============>>>Mqtt is run starting:<<==================");
            }
            mqttPushClient.connect(mqttConfiguration);
            String[] topics= {"/values/onchange","/values/interval","/control/set","/control/resp","/alarm/upload"};
            int[] qos={2,2,2,2,2};
            mqttPushClient.subscribe(topics,qos);
        }
    }

}
