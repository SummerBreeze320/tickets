package com.longlong.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longlong.user.pojo.dto.UserMobileDto;
import com.longlong.user.pojo.dto.UserUpdateMobileDto;
import com.longlong.user.pojo.entity.UserMobileEntity;
import com.longlong.user.pojo.vo.UserVo;

import java.util.List;

/**
 * @description: 用户手机号服务接口
 * @author: longlong
 */
public interface UserMobileService extends IService<UserMobileEntity> {

    /**
     * 更新用户手机号信息
     * @param userUpdateMobileDto 用户手机号更新信息
     */
    void updateMobile(UserUpdateMobileDto userUpdateMobileDto);

    /**
     * 根据手机号获取用户信息
     * @param userMobileDto 手机号信息
     * @return 用户信息
     */
    UserVo getByMobile(UserMobileDto userMobileDto);

    /**
     * 获取所有用户的手机号列表
     * @return 手机号列表
     */
    List<String> getAllMobile();
}