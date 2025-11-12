package com.longlong.program.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description: 票档 实体
 * @author: longlong
 **/
@Data
@TableName("d_ticket_category")
public class TicketCategoryEntity extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;
    
    /**
     * 节目表id
     */
    private Long programId;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 总数量
     * */
    private Long totalNumber;
    
    /**
     * 剩余数量
     * */
    private Long remainNumber;
    
    
}
