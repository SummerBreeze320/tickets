package com.longlong.user.pojo.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import java.io.Serializable;

/**
 * @description: 票务用户实体类
 * @author: Mr.Longlong
 * @create: 2020-08-07 14:05
 **/
@Data
@TableName("ticket_user")
public class TicketUserEntity extends BaseEntity implements Serializable {

	// 序列化ID
	@Serial
	private static final long serialVersionUID = 1L;

	// 票务用户ID
	private Long id;

	// 用户ID
	private Long userId;

	// 票务类型
	private String ticketType;

	// 票务状态
	private Integer status;

}
