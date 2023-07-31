package com.example.emqdemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yzy
 * @since 2023-07-31
 */
@Data
@TableName("emq_resp")
/**
 *  -- 实体类
 */
public class EmqResp implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("guid")
    private String guid;

    @TableField("code")
    private String code;

    @TableField("msg")
    private String msg;

    @TableField("ip_address_2")
    private String ipAddress2;

    @TableField("ip_address_3")
    private String ipAddress3;

    @TableField("ip_address_4")
    private String ipAddress4;


}
