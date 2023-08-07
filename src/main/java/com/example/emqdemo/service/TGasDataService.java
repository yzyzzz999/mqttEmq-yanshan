package com.example.emqdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.emqdemo.domain.TGasData;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
public interface TGasDataService extends IService<TGasData> {


    void batchSaveGasData(List<TGasData> gasDataList);

}
