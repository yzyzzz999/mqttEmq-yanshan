package com.example.emqdemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.emqdemo.domain.TGasRawData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
public interface TGasRawDataService extends IService<TGasRawData> {

    void saveGasRawData(TGasRawData gasRawData);

}
