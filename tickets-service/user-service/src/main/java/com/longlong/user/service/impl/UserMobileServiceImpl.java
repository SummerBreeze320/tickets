package com.longlong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.mapper.TicketUserMapper;
import com.longlong.user.mapper.UserEmailMapper;
import com.longlong.user.mapper.UserMapper;
import com.longlong.user.mapper.UserMobileMapper;
import com.longlong.user.pojo.dto.UserMobileDto;
import com.longlong.user.pojo.dto.UserUpdateMobileDto;
import com.longlong.user.pojo.entity.UserEntity;
import com.longlong.user.pojo.entity.UserMobileEntity;
import com.longlong.user.pojo.vo.UserVo;
import com.longlong.user.service.UserMobileService;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Server
public class UserMobileServiceImpl extends ServiceImpl<UserMobileMapper, UserMobileEntity> implements UserMobileService {

    private final UserMapper userMapper;

    private final UserMobileMapper userMobileMapper;

    public void updateMobile(UserUpdateMobileDto userUpdateMobileDto){
        UserEntity user = userMapper.selectById(userUpdateMobileDto.getId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("无该用户");
        }
        String oldMobile = user.getMobile();
        UserEntity updateUser = new UserEntity();
        BeanUtil.copyProperties(userUpdateMobileDto,updateUser);
        userMapper.updateById(updateUser);
        LambdaQueryWrapper<UserMobileEntity> userMobileLambdaQueryWrapper = Wrappers.lambdaQuery(UserMobileEntity.class)
                .eq(UserMobileEntity::getMobile, userUpdateMobileDto.getMobile());
        UserMobileEntity userMobile = userMobileMapper.selectOne(userMobileLambdaQueryWrapper);
        if (Objects.isNull(userMobile)) {
            userMobile = new UserMobileEntity();
            userMobile.setUserId(user.getId());
            userMobile.setMobile(userUpdateMobileDto.getMobile());
            userMobileMapper.insert(userMobile);
        }else {
            LambdaUpdateWrapper<UserMobileEntity> userMobileLambdaUpdateWrapper = Wrappers.lambdaUpdate(UserMobileEntity.class)
                    .eq(UserMobileEntity::getMobile, oldMobile);
            UserMobileEntity updateUserMobile = new UserMobileEntity();
            updateUserMobile.setMobile(userUpdateMobileDto.getMobile());
            userMobileMapper.update(updateUserMobile,userMobileLambdaUpdateWrapper);
        }
    }

    public UserVo getByMobile(UserMobileDto userMobileDto) {
        LambdaQueryWrapper<UserMobileEntity> queryWrapper = Wrappers.lambdaQuery(UserMobileEntity.class)
                .eq(UserMobileEntity::getMobile, userMobileDto.getMobile());
        UserMobileEntity userMobile = userMobileMapper.selectOne(queryWrapper);
        if (Objects.isNull(userMobile)) {
            throw new RuntimeException("该用户手机为空");
        }
        UserEntity user = userMapper.selectById(userMobile.getUserId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("该用户为空");
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user,userVo);
        userVo.setMobile(userMobile.getMobile());
        return userVo;
    }

    public List<String> getAllMobile(){
        QueryWrapper<UserEntity> lambdaQueryWrapper = Wrappers.emptyWrapper();
        List<UserEntity> users = userMapper.selectList(lambdaQueryWrapper);
        return users.stream().map(UserEntity::getMobile).collect(Collectors.toList());
    }
}