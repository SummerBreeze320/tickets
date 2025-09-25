package com.longlong.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.mapper.TicketUserMapper;
import com.longlong.user.pojo.entity.TicketUserEntity;
import com.longlong.user.service.TicketUserService;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Server
public class TicketUserServiceImpl extends ServiceImpl<TicketUserMapper, TicketUserEntity> implements TicketUserService {
}