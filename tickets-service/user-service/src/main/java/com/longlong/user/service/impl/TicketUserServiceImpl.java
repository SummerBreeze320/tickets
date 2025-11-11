package com.longlong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.mapper.TicketUserMapper;
import com.longlong.user.mapper.UserEmailMapper;
import com.longlong.user.mapper.UserMapper;
import com.longlong.user.mapper.UserMobileMapper;
import com.longlong.user.pojo.dto.*;
import com.longlong.user.pojo.entity.TicketUserEntity;
import com.longlong.user.pojo.entity.UserEntity;
import com.longlong.user.pojo.vo.TicketUserVo;
import com.longlong.user.pojo.vo.UserGetAndTicketUserListVo;
import com.longlong.user.pojo.vo.UserVo;
import com.longlong.user.service.TicketUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @description: 购票人服务实现类
 * @author: longlong
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TicketUserServiceImpl extends ServiceImpl<TicketUserMapper, TicketUserEntity> implements TicketUserService {

    private final UserMapper userMapper;

    private final TicketUserMapper ticketUserMapper;

    /**
     * 查询用户的购票人列表
     * 实现逻辑：根据用户ID查询对应的所有购票人记录
     * @param ticketUserListDto 查询条件，包含用户ID
     * @return 购票人列表
     */
    @Override
    public List<TicketUserVo> list(TicketUserListDto ticketUserListDto) {
        LambdaQueryWrapper<TicketUserEntity> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUserEntity.class)
                .eq(TicketUserEntity::getUserId, ticketUserListDto.getUserId());
        List<TicketUserEntity> ticketUsers = ticketUserMapper.selectList(ticketUserLambdaQueryWrapper);
        return BeanUtil.copyToList(ticketUsers, TicketUserVo.class);
    }

    /**
     * 添加购票人信息
     * 实现逻辑：1.验证用户是否存在 2.检查购票人是否已存在 3.添加新购票人信息
     * @param ticketUserDto 购票人信息
     */
    @Override
    public void add(TicketUserDto ticketUserDto) {
        UserEntity user = userMapper.selectById(ticketUserDto.getUserId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("无该用户");
        }
        LambdaQueryWrapper<TicketUserEntity> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUserEntity.class)
                .eq(TicketUserEntity::getUserId, ticketUserDto.getUserId())
                .eq(TicketUserEntity::getIdType, ticketUserDto.getIdType())
                .eq(TicketUserEntity::getIdNumber, ticketUserDto.getIdNumber());
        TicketUserEntity ticketUser = ticketUserMapper.selectOne(ticketUserLambdaQueryWrapper);
        if (Objects.nonNull(ticketUser)) {
            throw new RuntimeException("此购票人已存在");
        }
        TicketUserEntity addTicketUser = new TicketUserEntity();
        BeanUtil.copyProperties(ticketUserDto, addTicketUser);

        ticketUserMapper.insert(addTicketUser);
    }

    public void delete(TicketUserIdDto ticketUserIdDto) {
        TicketUserEntity ticketUser = ticketUserMapper.selectById(ticketUserIdDto.getId());
        if (Objects.isNull(ticketUser)) {
            throw new RuntimeException("购票人不存在");
        }
        ticketUserMapper.deleteById(ticketUserIdDto.getId());
    }


    /**
     * 获取用户信息及其购票人列表
     * 实现逻辑：1.获取用户信息 2.获取该用户的所有购票人信息 3.组合返回结果
     * @param userGetAndTicketUserListDto 查询条件，包含用户ID
     * @return 用户信息和购票人列表
     */
    @Override
    public UserGetAndTicketUserListVo getUserAndTicketUserList(final UserGetAndTicketUserListDto userGetAndTicketUserListDto) {
        UserIdDto userIdDto = new UserIdDto();
        userIdDto.setId(userGetAndTicketUserListDto.getUserId());
        UserVo userVo = getById(userIdDto);

        LambdaQueryWrapper<TicketUserEntity> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUserEntity.class)
                .eq(TicketUserEntity::getUserId, userGetAndTicketUserListDto.getUserId());
        List<TicketUserEntity> ticketUserList = ticketUserMapper.selectList(ticketUserLambdaQueryWrapper);
        List<TicketUserVo> ticketUserVoList = BeanUtil.copyToList(ticketUserList, TicketUserVo.class);

        UserGetAndTicketUserListVo userGetAndTicketUserListVo = new UserGetAndTicketUserListVo();
        userGetAndTicketUserListVo.setUserVo(userVo);
        userGetAndTicketUserListVo.setTicketUserVoList(ticketUserVoList);
        return userGetAndTicketUserListVo;
    }

    /**
     * 根据用户ID获取用户信息
     * 实现逻辑：通过用户ID查询用户实体，转换为VO对象返回
     * @param userIdDto 用户ID
     * @return 用户信息
     */
    @Override
    public UserVo getById(UserIdDto userIdDto) {
        UserEntity user = userMapper.selectById(userIdDto.getId());
        if (Objects.isNull(user)) {
            throw new RuntimeException("该用户为空");
        }
        UserVo userVo = new UserVo();
        BeanUtil.copyProperties(user, userVo);
        return userVo;
    }
}