package com.longlong.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longlong.user.pojo.entity.UserEmailEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 用户邮箱 mapper
 * @author: longlong
 **/
@Mapper
public interface UserEmailMapper extends BaseMapper<UserEmailEntity> {

}