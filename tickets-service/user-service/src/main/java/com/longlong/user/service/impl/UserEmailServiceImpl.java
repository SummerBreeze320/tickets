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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @description: 用户邮箱服务实现类
 * @author: longlong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserEmailServiceImpl extends ServiceImpl<UserEmailMapper, UserEmailEntity> implements UserEmailService {

    private final UserMapper userMapper;

    private final UserEmailMapper userEmailMapper;

    /**
     * 更新用户邮箱信息
     * 实现逻辑：1.验证用户是否存在 2.更新用户表中的邮箱信息 3.更新邮箱关联表数据
     * @param userUpdateEmailDto 用户邮箱更新信息
     * @return 是否更新成功
     */
    @Override
    public void updateEmail(UserUpdateEmailDto userUpdateEmailDto){
        UserEntity user = userMapper.selectById(userUpdateEmailDto.getId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("无该用户");
        }
        UserEntity updateUser = new UserEntity();
        BeanUtil.copyProperties(userUpdateEmailDto, updateUser);
        updateUser.setEmailStatus(BusinessStatus.YES.getCode());
        userMapper.updateById(updateUser);

        String oldEmail = user.getEmail();
        // 检查新邮箱是否已存在于邮箱表中
        LambdaQueryWrapper<UserEmailEntity> userEmailLambdaQueryWrapper = Wrappers.lambdaQuery(UserEmailEntity.class)
                .eq(UserEmailEntity::getEmail, userUpdateEmailDto.getEmail());
        UserEmailEntity userEmail = userEmailMapper.selectOne(userEmailLambdaQueryWrapper);
        
        if (Objects.isNull(userEmail)) {
            // 新邮箱不存在，插入新记录
            userEmail = new UserEmailEntity();
            userEmail.setUserId(user.getId());
            userEmail.setEmail(userUpdateEmailDto.getEmail());
            userEmailMapper.insert(userEmail);
        } else {
            // 新邮箱已存在，更新旧邮箱记录
            LambdaUpdateWrapper<UserEmailEntity> userEmailLambdaUpdateWrapper = Wrappers.lambdaUpdate(UserEmailEntity.class)
                    .eq(UserEmailEntity::getEmail, oldEmail);
            UserEmailEntity updateUserEmail = new UserEmailEntity();
            updateUserEmail.setEmail(userUpdateEmailDto.getEmail());
            userEmailMapper.update(updateUserEmail, userEmailLambdaUpdateWrapper);
        }
    }
}