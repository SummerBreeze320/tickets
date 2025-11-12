package com.longlong.program.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @description: 节目类型 实体
 * @author: longlong
 **/
@Data
@TableName("d_program_category")
public class ProgramCategoryEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 区域id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父区域id
     */
    private Long parentId;

    /**
     * 区域名字
     */
    private String name;

    /**
     * 1:一级种类 2:二级种类
     */
    private Integer type;
}
