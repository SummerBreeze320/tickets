package com.longlong.user.controller;


import com.longlong.user.pojo.dto.TicketUserDto;
import com.longlong.user.pojo.dto.TicketUserIdDto;
import com.longlong.user.pojo.dto.TicketUserListDto;
import com.longlong.user.pojo.vo.TicketUserVo;
import com.longlong.user.service.TicketUserService;
import com.longlong.user.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 购票人控制器
 * @description: 处理购票人相关的HTTP请求
 * @author: longlong
 */
@RestController
@RequestMapping("/ticket/user")
@RequiredArgsConstructor
@Tag(name = "ticket-user", description = "购票人")
public class TicketUserController {
    

    private final TicketUserService ticketUserService;

    @Operation(summary  = "查询购票人列表")
    @PostMapping(value = "/list")
    public ApiResponse<List<TicketUserVo>> list(@Valid @RequestBody TicketUserListDto ticketUserListDto){
        return ApiResponse.ok(ticketUserService.list(ticketUserListDto));
    }
    
    @Operation(summary  = "添加购票人")
    @PostMapping(value = "/add")
    public ApiResponse<Void> add(@Valid @RequestBody TicketUserDto ticketUserDto){
        ticketUserService.add(ticketUserDto);
        return ApiResponse.ok();
    }
    
    @Operation(summary  = "删除购票人")
    @PostMapping(value = "/delete")
    public ApiResponse<Void> delete(@Valid @RequestBody TicketUserIdDto ticketUserIdDto){
        ticketUserService.delete(ticketUserIdDto);
        return ApiResponse.ok();
    }
}
