package com.longlong.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.longlong.user.enums.AreaType;
import com.longlong.user.enums.BusinessStatus;
import com.longlong.user.mapper.*;
import com.longlong.user.pojo.dto.AreaGetDto;
import com.longlong.user.pojo.dto.AreaSelectDto;
import com.longlong.user.pojo.entity.AreaEntity;
import com.longlong.user.pojo.vo.AreaVo;
import com.longlong.user.service.AreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.Objects;

/**
 * @description: 用户服务实现类
 * @author: longlong
 */
@Slf4j
@RequiredArgsConstructor
public class AreaServiceImpl extends ServiceImpl<AreaMapper, AreaEntity> implements AreaService {

    private final AreaMapper areaMapper;

    public List<AreaVo> selectCityData() {
        final LambdaQueryWrapper<AreaEntity> lambdaQueryWrapper = Wrappers.lambdaQuery(AreaEntity.class)
                .eq(AreaEntity::getType, AreaType.MUNICIPALITIES.getCode())
                .or(wrapper -> wrapper
                        .eq(AreaEntity::getType, AreaType.PROVINCE.getCode())
                        .eq(AreaEntity::getMunicipality, BusinessStatus.YES.getCode()));
        List<AreaEntity> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas, AreaVo.class);
    }

    public List<AreaVo> selectByIdList(AreaSelectDto areaSelectDto) {
        final LambdaQueryWrapper<AreaEntity> lambdaQueryWrapper = Wrappers.lambdaQuery(AreaEntity.class)
                .in(AreaEntity::getId, areaSelectDto.getIdList());
        List<AreaEntity> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas,AreaVo.class);
    }

    public AreaVo getById(AreaGetDto areaGetDto) {
        log.info("基础服务调用 getById:{}", JSON.toJSONString(areaGetDto));
        final LambdaQueryWrapper<AreaEntity> lambdaQueryWrapper = Wrappers.lambdaQuery(AreaEntity.class)
                .eq(AreaEntity::getId, areaGetDto.getId());
        AreaEntity area = areaMapper.selectOne(lambdaQueryWrapper);
        AreaVo areaVo = new AreaVo();
        if (Objects.nonNull(area)) {
            BeanUtil.copyProperties(area,areaVo);
        }
        return areaVo;
    }

    public AreaVo current() {
        final LambdaQueryWrapper<AreaEntity> lambdaQueryWrapper = Wrappers.lambdaQuery(AreaEntity.class)
                .eq(AreaEntity::getId, 2);
        AreaEntity area = areaMapper.selectOne(lambdaQueryWrapper);
        AreaVo areaVo = new AreaVo();
        if (Objects.nonNull(area)) {
            BeanUtil.copyProperties(area,areaVo);
        }
        return areaVo;
    }

    public List<AreaVo> hot() {
        final LambdaQueryWrapper<AreaEntity> lambdaQueryWrapper = Wrappers.lambdaQuery(AreaEntity.class)
                .in(AreaEntity::getName, "全国","北京","上海","深圳","广州","杭州","天津","重庆","成都","中国香港");
        List<AreaEntity> areas = areaMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(areas,AreaVo.class);
    }

}
