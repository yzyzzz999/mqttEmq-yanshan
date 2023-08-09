package com.example.emqdemo.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 气体报警记录表
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
/**
 * 气体报警记录表 -- 实体类
 */
@Data
@TableName("t_gas_data_alarm")
public class TGasDataAlarm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId("id")
    private Long id;

    /**
     * 原始数据ID
     */
    @TableField("raw_data_id")
    private Long rawDataId;

    /**
     * 识别码
     */
    @TableField("serial_number")
    private String serialNumber;

    /**
     * 告警状态1（00：正常工作 01：超过低段报警值报警  02：超过高段报警值报警）
     */
    @TableField("alarm")
    private String alarm;

    /**
     * 传感器1气体浓度值
     */
    @TableField("gas_value")
    private BigDecimal gasValue;

    /**
     * 传感器1气体代码（t_gas_type的hexadecimal_code）
     */
    @TableField("gas_type")
    private String gasType;

    /**
     * 传感器1气体单位（t_pub_code的CODE）
     */
    @TableField("gas_unit")
    private String gasUnit;

    /**
     * 高报值
     */
    @TableField("upper_alarm")
    private BigDecimal upperAlarm;

    /**
     * 低报值
     */
    @TableField("lower_alarm")
    private BigDecimal lowerAlarm;

    /**
     * 高不报值
     */
    @TableField("upper_no_alarm")
    private BigDecimal upperNoAlarm;

    /**
     * 低不报值
     */
    @TableField("lower_no_alarm")
    private BigDecimal lowerNoAlarm;

    /**
     * 气体数据上报时间
     */
    @TableField("create_time")
    private Date createTime;


}
