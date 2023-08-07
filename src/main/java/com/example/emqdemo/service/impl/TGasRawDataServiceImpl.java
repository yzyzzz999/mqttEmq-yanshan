package com.example.emqdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emqdemo.domain.TGasRawData;
import com.example.emqdemo.mapper.TGasRawDataMapper;
import com.example.emqdemo.service.TGasRawDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
@Service
public class TGasRawDataServiceImpl extends ServiceImpl<TGasRawDataMapper, TGasRawData> implements TGasRawDataService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGasRawData(TGasRawData gasRawData) {
        this.save(gasRawData);
    }

}
