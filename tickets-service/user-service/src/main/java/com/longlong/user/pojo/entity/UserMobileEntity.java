package com.longlong.user.pojo.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serial;
import java.io.Serializable;

/**
 * @description: 用户手机号实体类
 * @author: longlong
 **/
@Data
@TableName("user_mobile")
public class UserMobileEntity extends BaseEntity implements Serializable {

	// 序列化ID
	@Serial
	private static final long serialVersionUID = 1L;

	// 手机号ID
	private Long id;

	// 用户ID
	private Long userId;

	// 手机号
	private String mobile;

}
