package com.example.emqdemo.compoent;

import com.example.emqdemo.service.TPubCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author susen
 */
@Component
@Slf4j
public class CustomerComp {

    @Autowired
    private TPubCodeService tPubCodeService;

    @PostConstruct
    public void init() {
        log.info("初始化开始 ...");
        tPubCodeService.cacheGasDict();
        tPubCodeService.cacheAlarmDict();
        log.info("初始化结束 ...");
    }
}
