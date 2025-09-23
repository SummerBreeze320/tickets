package com.longlong.user.pojo.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import java.io.Serializable;

/**
 * @description: 用户邮箱实体类
 * @author: Mr.Longlong
 * @create: 2020-08-07 14:05
 **/
@Data
@TableName("user_email")
public class UserEmailEntity extends BaseEntity implements Serializable {

	// 序列化ID
	@Serial
	private static final long serialVersionUID = 1L;

	// 邮箱ID
	private Long id;

	// 用户ID
	private Long userId;

	// 邮箱
	private String email;

	// 邮箱状态
	private Integer status;

}
