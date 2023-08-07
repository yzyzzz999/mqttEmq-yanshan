package com.example.emqdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emqdemo.domain.TGasDataCurrent;
import com.example.emqdemo.mapper.TGasDataCurrentMapper;
import com.example.emqdemo.service.TGasDataCurrentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
@Service
public class TGasDataCurrentServiceImpl extends ServiceImpl<TGasDataCurrentMapper, TGasDataCurrent> implements TGasDataCurrentService {
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveGasData(List<TGasDataCurrent> tGasDataList) {
        this.saveOrUpdateBatch(tGasDataList);
    }
}
