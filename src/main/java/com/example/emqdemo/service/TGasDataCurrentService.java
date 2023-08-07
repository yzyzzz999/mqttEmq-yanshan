package com.example.emqdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.emqdemo.domain.TGasDataCurrent;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
public interface TGasDataCurrentService extends IService<TGasDataCurrent> {
    void batchSaveGasData(List<TGasDataCurrent> gasDataList);

}
