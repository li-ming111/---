package com.xueya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueya.entity.Community;
import com.xueya.mapper.CommunityMapper;
import com.xueya.service.CommunityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {
    @Override
    public List<Community> getCommunitiesByType(String type) {
        QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Community> getCommunitiesByCategory(String category) {
        QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Community getCommunityById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createCommunity(Community community) {
        return save(community);
    }

    @Override
    public boolean updateCommunity(Community community) {
        return updateById(community);
    }

    @Override
    public boolean deleteCommunity(Long id) {
        return removeById(id);
    }
}