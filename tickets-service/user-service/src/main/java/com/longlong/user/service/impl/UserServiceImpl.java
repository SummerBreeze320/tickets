package com.longlong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.enums.BusinessStatus;
import com.longlong.user.mapper.TicketUserMapper;
import com.longlong.user.mapper.UserEmailMapper;
import com.longlong.user.mapper.UserMapper;
import com.longlong.user.mapper.UserMobileMapper;
import com.longlong.user.pojo.dto.*;
import com.longlong.user.pojo.entity.TicketUserEntity;
import com.longlong.user.pojo.entity.UserEmailEntity;
import com.longlong.user.pojo.entity.UserEntity;
import com.longlong.user.pojo.entity.UserMobileEntity;
import com.longlong.user.pojo.vo.TicketUserVo;
import com.longlong.user.pojo.vo.UserGetAndTicketUserListVo;
import com.longlong.user.pojo.vo.UserLoginVo;
import com.longlong.user.pojo.vo.UserVo;
import com.longlong.user.service.UserService;
import com.longlong.user.utils.JWTUtils;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @description: 用户服务实现类
 * @author: longlong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private final UserMapper userMapper;

    private final UserMobileMapper userMobileMapper;

    private final UserEmailMapper userEmailMapper;

    private final TicketUserMapper ticketUserMapper;

    /**
     * 用户注册
     * 实现逻辑：1.记录注册手机号日志 2.创建用户实体并保存 3.创建手机号关联记录
     * @param userRegisterDto 用户注册信息
     * @return 注册是否成功
     */
    @Override
    public Boolean register(UserRegisterDto userRegisterDto) {
        // todo 注册信息责任链
        log.info("注册手机号:{}", userRegisterDto.getMobile());
        //用户表添加
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userRegisterDto, userEntity);
        userMapper.insert(userEntity);
        //用户手机表添加
        UserMobileEntity userMobileEntity = new UserMobileEntity();
        userMobileEntity.setUserId(userEntity.getId());
        userMobileEntity.setMobile(userRegisterDto.getMobile());
        userMobileMapper.insert(userMobileEntity);
        return true;
    }

    /**
     * 检查用户是否存在
     * 实现逻辑：根据手机号查询用户，如果存在则抛出异常
     * @param userExistDto 查询条件，包含手机号
     */
    @Override
    public void exist(UserExistDto userExistDto){
        // todo 添加布隆过滤器
        String mobile = userExistDto.getMobile();
        LambdaQueryWrapper<UserMobileEntity> queryWrapper = Wrappers.lambdaQuery(UserMobileEntity.class)
                .eq(UserMobileEntity::getMobile, mobile);
        UserMobileEntity userMobile = userMobileMapper.selectOne(queryWrapper);
        if (Objects.nonNull(userMobile)) {
            // todo 统一错误码
            throw new RuntimeException("用户已存在");
        }
    }

    /**
     * 用户登录
     * 实现逻辑：1.验证登录参数 2.根据手机号或邮箱查询用户ID 3.验证密码 4.生成JWT令牌
     * @param userLoginDto 登录信息
     * @return 登录结果，包含用户信息和令牌
     */
    @Override
    public UserLoginVo login(UserLoginDto userLoginDto) {
        UserLoginVo userLoginVo = new UserLoginVo();
        String code = userLoginDto.getCode();
        String mobile = userLoginDto.getMobile();
        String email = userLoginDto.getEmail();
        String password = userLoginDto.getPassword();
        if (StringUtils.isEmpty(mobile) && StringUtils.isEmpty(email)) {
            throw new RuntimeException("手机号和邮箱不能同时为空");
        }
        Long userId;
        if (!StringUtils.isEmpty(mobile)) {
            LambdaQueryWrapper<UserMobileEntity> queryWrapper = Wrappers.lambdaQuery(UserMobileEntity.class)
                    .eq(UserMobileEntity::getMobile, mobile);
            UserMobileEntity userMobile = userMobileMapper.selectOne(queryWrapper);
            userId = userMobile.getUserId();
        }else {
            LambdaQueryWrapper<UserEmailEntity> queryWrapper = Wrappers.lambdaQuery(UserEmailEntity.class)
                    .eq(UserEmailEntity::getEmail, email);
            UserEmailEntity userEmail = userEmailMapper.selectOne(queryWrapper);
            userId = userEmail.getUserId();
        }
        LambdaQueryWrapper<UserEntity> queryUserWrapper = Wrappers.lambdaQuery(UserEntity.class)
                .eq(UserEntity::getId, userId).eq(UserEntity::getPassword, password);
        UserEntity user = userMapper.selectOne(queryUserWrapper);
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");
        }
        userLoginVo.setUserId(userId);
        // todo 在redis中存储.
        
        Map<String, Object> map = new HashMap<>(4);
        map.put("userId", userId);
        String token = JWTUtils.generateAccessToken(String.valueOf(userId), JSON.toJSONString(map));
        userLoginVo.setToken(token);
        return userLoginVo;
    }

    /**
     * 用户登出
     * 实现逻辑：1.解析JWT令牌 2.验证令牌有效性 3.（TODO）清理Redis中的登录状态
     * @param userLogoutDto 登出信息，包含令牌
     * @return 登出是否成功
     */
    @Override
    public Boolean logout(UserLogoutDto userLogoutDto) {
        String userStr = JWTUtils.parseJwtToken(userLogoutDto.getToken());
        if (StringUtils.isEmpty(userStr)) {
            throw new RuntimeException("token无效");
        }
        String userId = JSONObject.parseObject(userStr).getString("userId");
        // todo redis中删除用户登录状态
        return true;
    }

    /**
     * 更新用户信息
     * 实现逻辑：1.验证用户是否存在 2.更新用户信息
     * @param userUpdateDto 用户更新信息
     */
    @Override
    public void update(UserUpdateDto userUpdateDto){
        UserEntity user = userMapper.selectById(userUpdateDto.getId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("无该用户");
        }
        UserEntity updateUser = new UserEntity();
        BeanUtils.copyProperties(userUpdateDto, updateUser);
        userMapper.updateById(updateUser);
    }

    /**
     * 修改密码
     * 实现逻辑：1.验证用户是否存在 2.更新用户密码
     * @param userUpdatePasswordDto 密码更新信息
     */
    @Override
    public void updatePassword(UserUpdatePasswordDto userUpdatePasswordDto){
        UserEntity user = userMapper.selectById(userUpdatePasswordDto.getId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("无该用户");
        }
        UserEntity updateUser = new UserEntity();
        BeanUtils.copyProperties(userUpdatePasswordDto, updateUser);
        userMapper.updateById(updateUser);
    }

    /**
     * 用户身份验证
     * 实现逻辑：1.验证用户是否存在 2.检查用户是否已认证 3.更新用户认证信息
     * @param userAuthenticationDto 验证信息
     */
    @Override
    public void authentication(UserAuthenticationDto userAuthenticationDto){
        UserEntity user = userMapper.selectById(userAuthenticationDto.getId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("无该用户");
        }
        if (Objects.equals(user.getRelAuthenticationStatus(), BusinessStatus.YES.getCode())) {
            throw new RuntimeException("用户已认证");
        }
        UserEntity updateUser = new UserEntity();
        updateUser.setId(user.getId());
        updateUser.setRelName(userAuthenticationDto.getRelName());
        updateUser.setIdNumber(userAuthenticationDto.getIdNumber());
        updateUser.setRelAuthenticationStatus(BusinessStatus.YES.getCode());
        userMapper.updateById(updateUser);
    }
}
