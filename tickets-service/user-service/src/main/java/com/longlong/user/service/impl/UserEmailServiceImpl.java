package com.longlong.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.mapper.UserEmailMapper;
import com.longlong.user.pojo.entity.UserEmailEntity;
import com.longlong.user.service.UserEmailService;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Server
public class UserEmailServiceImpl extends ServiceImpl<UserEmailMapper, UserEmailEntity> implements UserEmailService {
}