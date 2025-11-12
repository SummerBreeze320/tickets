package com.longlong.program.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * @description: 基础实体类
 * @author: longlong
 **/
@Data
public class BaseEntity {

    // 创建时间
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    // 修改时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date editTime;

    // 删除状态 0-正常 1-删除
    private Integer status;
}
