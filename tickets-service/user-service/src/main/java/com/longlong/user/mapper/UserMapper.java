package com.longlong.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longlong.user.pojo.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 用户 mapper
 * @author: longlong
 **/
@Mapper
public interface UserMapper extends BaseMapper<UserEntity> {

}
