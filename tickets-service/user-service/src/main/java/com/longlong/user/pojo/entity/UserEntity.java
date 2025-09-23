package com.longlong.user.pojo.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

/**
 * @description: 用户实体类
 * @author: Mr.Longlong
 * @create: 2020-08-07 14:05
 **/
@Data
@TableName("user")
public class UserEntity extends BaseEntity implements  Serializable {

    // 序列化ID
    @Serial
    private static final long serialVersionUID = 1L;

    // 用户ID
    private Long id;

    // 用户名
    private String name;

    // 真实姓名
    private String relName;

    // 手机号
    private String mobile;

    // 性别
    private Integer gender;

    // 密码
    private String password;

    // 邮箱状态
    private Integer emailStatus;

    // 邮箱
    private String email;

    // 实名认证状态
    private Integer relAuthenticationStatus;

    // 身份证号码
    private String idNumber;

    // 地址
    private String address;

}
