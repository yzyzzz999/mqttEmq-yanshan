package com.example.emqdemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author yzy
 * @since 2023-07-31
 */
@Data
@TableName("emq_onchange")
public class EmqOnchange implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("dev_code")
    private String devCode;

    @TableField("online_status")
    private Boolean onlineStatus;

    @TableField("send_time")
    private LocalDateTime sendTime;

    /**
     * 设备id
     */
    @TableField("seq_id")
    private Long seqId;

    @TableField("values")
    private String values;

    /**
     * 在线点
     */
    @TableField("communication_status")
    private Boolean communicationStatus;

    /**
     * 模块型号
     */
    @TableField("type")
    private Integer type;

    /**
     * 模块软件版本
     */
    @TableField("embedded_version")
    private Integer embeddedVersion;

    /**
     * 模块485地址
     */
    @TableField("address_485")
    private Boolean address485;

    /**
     * 485波特率代码
     */
    @TableField("baud_code")
    private BigDecimal baudCode;

    /**
     * 485奇偶校验
     */
    @TableField("parity_check")
    private Boolean parityCheck;

    /**
     * IP地址/WAN口IP-1
     */
    @TableField("ip_address_1")
    private Integer ipAddress1;

    /**
     * IP地址/WAN口IP-2
     */
    @TableField("ip_address_2")
    private Integer ipAddress2;

    /**
     * IP地址/WAN口IP-3
     */
    @TableField("ip_address_3")
    private Integer ipAddress3;

    /**
     * IP地址/WAN口IP-4
     */
    @TableField("ip_address_4")
    private Integer ipAddress4;

    /**
     * IP地址/WAN口掩码-1
     */
    @TableField("mask_1")
    private Integer mask1;

    /**
     * IP地址/WAN口掩码-2
     */
    @TableField("mask_2")
    private Integer mask2;

    /**
     * IP地址/WAN口掩码-3
     */
    @TableField("mask_3")
    private Integer mask3;

    /**
     * IP地址/WAN口掩码-4
     */
    @TableField("mask_4")
    private String mask4;

    /**
     * IP地址/WAN口网关-1
     */
    @TableField("gateway_1")
    private Integer gateway1;

    /**
     * IP地址/WAN口网关-2
     */
    @TableField("gateway_2")
    private Integer gateway2;

    /**
     * IP地址/WAN口网关-3
     */
    @TableField("gateway_3")
    private Integer gateway3;

    /**
     * IP地址/WAN口网关-4
     */
    @TableField("gateway_4")
    private String gateway4;

    /**
     * MODBUS TCP端口号
     */
    @TableField("modbus_port")
    private Integer modbusPort;

    /**
     * WIFI开关
     */
    @TableField("wifi_switch")
    private Boolean wifiSwitch;

    /**
     * ETH工作模式
     */
    @TableField("eth_working_mode")
    private Boolean ethWorkingMode;

    /**
     * 一氧化碳(CO)浓度
     */
    @TableField("co_concentration")
    private BigDecimal coConcentration;

    /**
     * 硫化氧(H2S)浓度
     */
    @TableField("h2s_concentration")
    private BigDecimal h2sConcentration;

    /**
     * 氧(O2)浓度
     */
    @TableField("o2_concentration")
    private BigDecimal o2Concentration;

    /**
     * 甲烷(CH4)浓度
     */
    @TableField("ch4_concentration")
    private BigDecimal ch4Concentration;

    /**
     * 泵工作方式设置
     */
    @TableField("pump_working_mode_setting")
    private Integer pumpWorkingModeSetting;

    /**
     * 泵转速反馈
     */
    @TableField("pump_speed_feedback")
    private Boolean pumpSpeedFeedback;

    /**
     * 泵转速设置
     */
    @TableField("pump_speed_setting")
    private Boolean pumpSpeedSetting;


}
