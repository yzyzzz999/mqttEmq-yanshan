package com.example.emqdemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.emqdemo.domain.TGasData;
import com.example.emqdemo.mapper.TGasDataMapper;
import com.example.emqdemo.service.TGasDataService;
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
public class TGasDataServiceImpl extends ServiceImpl<TGasDataMapper, TGasData> implements TGasDataService {



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveGasData(List<TGasData> gasDataList) {
        this.saveBatch(gasDataList);
    }

}
