package com.longlong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.mapper.TicketUserMapper;
import com.longlong.user.mapper.UserEmailMapper;
import com.longlong.user.mapper.UserMapper;
import com.longlong.user.mapper.UserMobileMapper;
import com.longlong.user.pojo.dto.TicketUserDto;
import com.longlong.user.pojo.dto.TicketUserListDto;
import com.longlong.user.pojo.dto.UserGetAndTicketUserListDto;
import com.longlong.user.pojo.dto.UserIdDto;
import com.longlong.user.pojo.entity.TicketUserEntity;
import com.longlong.user.pojo.entity.UserEntity;
import com.longlong.user.pojo.vo.TicketUserVo;
import com.longlong.user.pojo.vo.UserGetAndTicketUserListVo;
import com.longlong.user.pojo.vo.UserVo;
import com.longlong.user.service.TicketUserService;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Server
public class TicketUserServiceImpl extends ServiceImpl<TicketUserMapper, TicketUserEntity> implements TicketUserService {
    private final UserMapper userMapper;

    private final TicketUserMapper ticketUserMapper;

    public List<TicketUserVo> list(TicketUserListDto ticketUserListDto) {
        LambdaQueryWrapper<TicketUserEntity> ticketUserLambdaQueryWrapper = Wrappers.lambdaQuery(TicketUserEntity.class)
                .eq(TicketUserEntity::getUserId, ticketUserListDto.getUserId());
        List<TicketUserEntity> ticketUsers = ticketUserMapper.selectList(ticketUserLambdaQueryWrapper);
        return BeanUtil.copyToList(ticketUsers, TicketUserVo.class);
    }

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
        BeanUtil.copyProperties(ticketUserDto,addTicketUser);

        ticketUserMapper.insert(addTicketUser);
    }

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