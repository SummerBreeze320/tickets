package com.longlong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.mapper.UserMapper;
import com.longlong.user.mapper.UserMobileMapper;
import com.longlong.user.pojo.dto.UserMobileDto;
import com.longlong.user.pojo.dto.UserUpdateMobileDto;
import com.longlong.user.pojo.entity.UserEntity;
import com.longlong.user.pojo.entity.UserMobileEntity;
import com.longlong.user.pojo.vo.UserVo;
import com.longlong.user.service.UserMobileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: 用户手机号服务实现类
 * @author: longlong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserMobileServiceImpl extends ServiceImpl<UserMobileMapper, UserMobileEntity> implements UserMobileService {

    private final UserMapper userMapper;

    private final UserMobileMapper userMobileMapper;

    /**
     * 更新用户手机号信息
     * 实现逻辑：1.验证用户是否存在 2.更新用户表中的手机号 3.更新手机号关联表数据
     * @param userUpdateMobileDto 用户手机号更新信息
     */
    @Override
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
            // 新手机号已存在，更新旧手机号记录
            LambdaUpdateWrapper<UserMobileEntity> userMobileLambdaUpdateWrapper = Wrappers.lambdaUpdate(UserMobileEntity.class)
                    .eq(UserMobileEntity::getMobile, oldMobile);
            UserMobileEntity updateUserMobile = new UserMobileEntity();
            updateUserMobile.setMobile(userUpdateMobileDto.getMobile());
            userMobileMapper.update(updateUserMobile, userMobileLambdaUpdateWrapper);
        }
    }

    /**
     * 根据手机号获取用户信息
     * 实现逻辑：1.通过手机号查询手机号关联表 2.获取对应用户信息 3.组装返回结果
     * @param userMobileDto 手机号信息
     * @return 用户信息
     */
    @Override
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
        BeanUtil.copyProperties(user, userVo);
        userVo.setMobile(userMobile.getMobile());
        return userVo;
    }

    /**
     * 获取所有用户的手机号列表
     * 实现逻辑：查询所有用户信息，提取手机号列表返回
     * @return 手机号列表
     */
    @Override
    public List<String> getAllMobile(){
        QueryWrapper<UserEntity> lambdaQueryWrapper = Wrappers.emptyWrapper();
        List<UserEntity> users = userMapper.selectList(lambdaQueryWrapper);
        return users.stream().map(UserEntity::getMobile).collect(Collectors.toList());
    }
}