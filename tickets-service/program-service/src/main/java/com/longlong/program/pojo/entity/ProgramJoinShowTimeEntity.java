package com.longlong.program.pojo.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @description: 节目 实体 连表使用
 * @author: longlong
 **/
@Data
public class ProgramJoinShowTimeEntity extends ProgramEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    
    /**
     * 演出时间
     */
    private Date showTime;
    
    /**
     * 演出时间(精确到天)
     */
    private Date showDayTime;
    
    /**
     * 演出时间所在的星期
     */
    private String showWeekTime;
}
