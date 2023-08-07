package com.example.emqdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.emqdemo.domain.TGasDataAlarm;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 气体报警记录表 Mapper 接口
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
@Mapper
public interface TGasDataAlarmMapper extends BaseMapper<TGasDataAlarm> {

}
