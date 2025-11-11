package com.longlong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.enums.BusinessStatus;
import com.longlong.user.mapper.TicketUserMapper;
import com.longlong.user.mapper.UserEmailMapper;
import com.longlong.user.mapper.UserMapper;
import com.longlong.user.mapper.UserMobileMapper;
import com.longlong.user.pojo.dto.UserUpdateEmailDto;
import com.longlong.user.pojo.entity.UserEmailEntity;
import com.longlong.user.pojo.entity.UserEntity;
import com.longlong.user.service.UserEmailService;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Server
public class UserEmailServiceImpl extends ServiceImpl<UserEmailMapper, UserEmailEntity> implements UserEmailService {

    private final UserMapper userMapper;

    private final UserEmailMapper userEmailMapper;

    public void updateEmail(UserUpdateEmailDto userUpdateEmailDto){
        UserEntity user = userMapper.selectById(userUpdateEmailDto.getId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("无该用户");
        }
        UserEntity updateUser = new UserEntity();
        BeanUtil.copyProperties(userUpdateEmailDto,updateUser);
        updateUser.setEmailStatus(BusinessStatus.YES.getCode());
        userMapper.updateById(updateUser);

        String oldEmail = user.getEmail();
        LambdaQueryWrapper<UserEmailEntity> userEmailLambdaQueryWrapper = Wrappers.lambdaQuery(UserEmailEntity.class)
                .eq(UserEmailEntity::getEmail, userUpdateEmailDto.getEmail());
        UserEmailEntity userEmail = userEmailMapper.selectOne(userEmailLambdaQueryWrapper);
        if (Objects.isNull(userEmail)) {
            userEmail = new UserEmailEntity();
            userEmail.setUserId(user.getId());
            userEmail.setEmail(userUpdateEmailDto.getEmail());
            userEmailMapper.insert(userEmail);
        }else {
            LambdaUpdateWrapper<UserEmailEntity> userEmailLambdaUpdateWrapper = Wrappers.lambdaUpdate(UserEmailEntity.class)
                    .eq(UserEmailEntity::getEmail, oldEmail);
            UserEmailEntity updateUserEmail = new UserEmailEntity();
            updateUserEmail.setEmail(userUpdateEmailDto.getEmail());
            userEmailMapper.update(updateUserEmail,userEmailLambdaUpdateWrapper);
        }
    }
}