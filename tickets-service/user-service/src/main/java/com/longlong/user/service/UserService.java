package com.longlong.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longlong.user.pojo.dto.*;
import com.longlong.user.pojo.entity.UserEntity;
import com.longlong.user.pojo.vo.UserLoginVo;

/**
 * @description: 用户服务接口
 * @author: longlong
 */
public interface UserService extends IService<UserEntity> {

    /**
     * 注册用户
     * @param userRegisterDto 用户注册信息
     */
    Boolean register(UserRegisterDto userRegisterDto);

    /**
     * 检查用户是否存在
     * @param userExistDto 查询条件，包含用户名或手机号
     * @return 用户是否存在
     */
    void exist(UserExistDto userExistDto);

    /**
     * 用户登录
     * @param userLoginDto 登录信息
     * @return 登录结果，包含用户信息和令牌
     */
    UserLoginVo login(UserLoginDto userLoginDto);

    /**
     * 用户登出
     * @param userLogoutDto 登出信息，包含令牌
     */
    Boolean logout(UserLogoutDto userLogoutDto);

    /**
     * 更新用户信息
     * @param userUpdateDto 用户更新信息
     */
    void update(UserUpdateDto userUpdateDto);

    /**
     * 修改密码
     * @param userUpdatePasswordDto 密码更新信息
     */
    void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto);

    /**
     * 用户身份验证
     * @param userAuthenticationDto 验证信息
     * @return 验证是否通过
     */
    void authentication(UserAuthenticationDto userAuthenticationDto);
}
