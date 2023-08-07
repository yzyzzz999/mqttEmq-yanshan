package com.example.emqdemo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author yzy
 * @since 2023-08-04
 */
/**
 * 字典表 -- 实体类
 */
@Data
@TableName("t_pub_code")
public class TPubCode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 编码
     */
    @TableField("code")
    private String code;

    /**
     * 分类
     */
    @TableField("type")
    private String type;

    /**
     * 排序
     */
    @TableField("orders")
    private String orders;

    /**
     * 备注
     */
    @TableField("remarks")
    private String remarks;


}
