package com.example.emqdemo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
/**
 *  -- 实体类
 */
@Data
@TableName("t_gas_raw_data")
public class TGasRawData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 原始数据
     */
    @TableField("raw_data")
    private String rawData;

    /**
     * 气体数据上报时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 识别码
     */
    @TableField("serial_number")
    private String serialNumber;


}
