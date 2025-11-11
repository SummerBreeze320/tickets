package com.longlong.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longlong.user.pojo.dto.UserUpdateEmailDto;
import com.longlong.user.pojo.entity.UserEmailEntity;

/**
 * @description: 用户邮箱服务接口
 * @author: longlong
 */
public interface UserEmailService extends IService<UserEmailEntity> {

    /**
     * 更新用户邮箱信息
     * @param userUpdateEmailDto 用户邮箱更新信息
     * @return 是否更新成功
     */
    void updateEmail(UserUpdateEmailDto userUpdateEmailDto);
}