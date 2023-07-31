package com.example.emqdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.emqdemo.domain.EmqResp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yzy
 * @since 2023-07-31
 */
@Mapper
public interface EmqRespMapper extends BaseMapper<EmqResp> {

}
