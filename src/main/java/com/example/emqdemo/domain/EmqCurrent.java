package com.example.emqdemo.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Data
@TableName("emq_current")
public class EmqCurrent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("dev_code")
    private String devCode;

    @TableField("online_status")
    private Boolean onlineStatus;

    @TableField("send_time")
    private Date sendTime;

    /**
     * 设备id
     */
    @TableField("seq_id")
    private Long seqId;

    @TableField("value")
    private String value;

    /**
     * 在线点
     */
    @TableField("communication_status")
    private Integer communicationStatus;

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
    private Integer address485;

    /**
     * 485波特率代码
     */
    @TableField("baud_code")
    private Integer baudCode;

    /**
     * 485奇偶校验
     */
    @TableField("parity_check")
    private Integer parityCheck;

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
    private Integer mask4;

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
    private Integer gateway4;

    /**
     * MODBUS TCP端口号
     */
    @TableField("modbus_port")
    private Integer modbusPort;

    /**
     * WIFI开关
     */
    @TableField("wifi_switch")
    private Integer wifiSwitch;

    /**
     * ETH工作模式
     */
    @TableField("eth_working_mode")
    private Integer ethWorkingMode;

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
    private Integer pumpSpeedFeedback;

    /**
     * 泵转速设置
     */
    @TableField("pump_speed_setting")
    private Integer pumpSpeedSetting;

    public static EmqCurrent init(Map<String, Object> mapJson){
        EmqCurrent emqCurrent= new EmqCurrent();
        emqCurrent.setSendTime((Date) mapJson.get("SendTime"));
        emqCurrent.setDevCode(String.valueOf(String.valueOf(mapJson.get("DevCode"))));
        emqCurrent.setOnlineStatus("1".equalsIgnoreCase(String.valueOf(mapJson.get("OnlineStatus"))));
        emqCurrent.setSeqId( Long.valueOf((String) mapJson.get("SeqId")));
        emqCurrent.setValue( String.valueOf(mapJson.get("values")));
        emqCurrent.setCommunicationStatus(mapJson.get("Communication Status") != null? (Integer.parseInt(String.valueOf(mapJson.get("Communication Status")))):-1);
        emqCurrent.setWifiSwitch(mapJson.get("WIFI switch") != null? (Integer.parseInt(String.valueOf(mapJson.get("WIFI switch")))):-1);
        emqCurrent.setPumpSpeedFeedback(mapJson.get("Pump speed feedback") != null? (Integer.parseInt(String.valueOf(mapJson.get("Pump speed feedback")))):-1);
        emqCurrent.setPumpSpeedSetting(mapJson.get("Pump speed setting") != null? (Integer.parseInt(String.valueOf(mapJson.get("Pump speed setting")))):-1);
        emqCurrent.setPumpWorkingModeSetting(mapJson.get("Pump working mode setting") != null? (Integer.parseInt((String) mapJson.get("Pump working mode setting"))):-1);
        emqCurrent.setO2Concentration(new BigDecimal((String) Optional.ofNullable(mapJson.get("O2 concentration")).orElse("-1"))) ;
        emqCurrent.setH2sConcentration(new BigDecimal((String) Optional.ofNullable(mapJson.get("H2S concentration")).orElse("-1")));
        emqCurrent.setCh4Concentration(new BigDecimal((String) Optional.ofNullable(mapJson.get("CH4 concentration")).orElse("-1")));
        emqCurrent.setCoConcentration(new BigDecimal((String) Optional.ofNullable(mapJson.get("CO concentration")).orElse("-1")));
        emqCurrent.setMask1(mapJson.get("mask-1") != null? (Integer.parseInt(String.valueOf(mapJson.get("mask-1")))):-1);
        emqCurrent.setMask2(mapJson.get("mask-2") != null? (Integer.parseInt(String.valueOf(mapJson.get("mask-2")))):-1);
        emqCurrent.setMask3(mapJson.get("mask-3") != null? (Integer.parseInt(String.valueOf(mapJson.get("mask-3")))):-1);
        emqCurrent.setMask4(mapJson.get("mask-4") != null? (Integer.parseInt(String.valueOf(mapJson.get("mask-4")))):-1);
        emqCurrent.setIpAddress1(mapJson.get("IP Address-1") != null? (Integer.parseInt(String.valueOf(mapJson.get("IP Address-1")))):-1);
        emqCurrent.setIpAddress2(mapJson.get("IP Address-2") != null? (Integer.parseInt(String.valueOf(mapJson.get("IP Address-2")))):-1);
        emqCurrent.setIpAddress3(mapJson.get("IP Address-3") != null? (Integer.parseInt(String.valueOf(mapJson.get("IP Address-3")))):-1);
        emqCurrent.setIpAddress4(mapJson.get("IP Address-4") != null? (Integer.parseInt(String.valueOf(mapJson.get("IP Address-4")))):-1);
        emqCurrent.setEthWorkingMode(mapJson.get("ETH working mode") != null? (Integer.parseInt(String.valueOf(mapJson.get("ETH working mode")))):-1);
        emqCurrent.setAddress485(mapJson.get("485 address") != null? (Integer.parseInt(String.valueOf(mapJson.get("485 address")))):-1);
        emqCurrent.setGateway1(mapJson.get("gateway-1") != null? (Integer.parseInt(String.valueOf(mapJson.get("gateway-1")))):-1);
        emqCurrent.setGateway2(mapJson.get("gateway-2") != null? (Integer.parseInt(String.valueOf(mapJson.get("gateway-2")))):-1);
        emqCurrent.setGateway3(mapJson.get("gateway-3") != null? (Integer.parseInt(String.valueOf(mapJson.get("gateway-3")))):-1);
        emqCurrent.setGateway4(mapJson.get("gateway-4") != null? (Integer.parseInt(String.valueOf(mapJson.get("gateway-4")))):-1);
        emqCurrent.setModbusPort(mapJson.get("MODBUS port") != null? (Integer.parseInt(String.valueOf(mapJson.get("MODBUS port")))):-1);
        emqCurrent.setBaudCode(mapJson.get("Baud code") != null? (Integer.parseInt(String.valueOf(mapJson.get("Baud code")))):-1);
        emqCurrent.setType(mapJson.get("Type") != null? (Integer.parseInt(String.valueOf(mapJson.get("Type")))):-1);
        emqCurrent.setEmbeddedVersion(mapJson.get("Embedded version") != null? (Integer.parseInt(String.valueOf(mapJson.get("Embedded version")))):-1);
        emqCurrent.setParityCheck(mapJson.get("Parity check") != null? (Integer.parseInt(String.valueOf(mapJson.get("Parity check")))):-1);
        return emqCurrent;
    }
}

