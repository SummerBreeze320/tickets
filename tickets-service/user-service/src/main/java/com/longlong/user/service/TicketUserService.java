package com.longlong.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longlong.user.pojo.dto.*;
import com.longlong.user.pojo.entity.TicketUserEntity;
import com.longlong.user.pojo.vo.TicketUserVo;
import com.longlong.user.pojo.vo.UserGetAndTicketUserListVo;
import com.longlong.user.pojo.vo.UserVo;

import java.util.List;

/**
 * @description: 购票人服务接口
 * @author: longlong
 */
public interface TicketUserService extends IService<TicketUserEntity> {
    
    /**
     * 查询用户的购票人列表
     * @param ticketUserListDto 查询条件，包含用户ID
     * @return 购票人列表
     */
    List<TicketUserVo> list(TicketUserListDto ticketUserListDto);

    /**
     * 添加购票人信息
     * @param ticketUserDto 购票人信息
     */
    void add(TicketUserDto ticketUserDto);

    /**
     * 获取用户信息及其购票人列表
     * @param userGetAndTicketUserListDto 查询条件，包含用户ID
     * @return 用户信息和购票人列表
     */
    UserGetAndTicketUserListVo getUserAndTicketUserList(UserGetAndTicketUserListDto userGetAndTicketUserListDto);

    /**
     * 删除购票人信息
     * @param ticketUserIdDto 购票人
     */
    void delete(TicketUserIdDto ticketUserIdDto);

    /**
     * 根据用户ID获取用户信息
     * @param userIdDto 用户ID
     * @return 用户信息
     */
    UserVo getById(UserIdDto userIdDto);
}