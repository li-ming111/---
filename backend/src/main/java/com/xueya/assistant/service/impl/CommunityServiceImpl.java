package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.Community;
import com.xueya.assistant.mapper.CommunityMapper;
import com.xueya.assistant.service.CommunityService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

    @Override
    public List<Community> getCommunitiesByType(String type) {
        QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.orderByDesc("member_count");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Community> getCommunitiesByCategory(String category) {
        QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        queryWrapper.orderByDesc("member_count");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Community> getCommunitiesByCreatorId(Long creatorId) {
        QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator_id", creatorId);
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean createCommunity(Community community) {
        community.setCreateTime(LocalDateTime.now());
        community.setUpdateTime(LocalDateTime.now());
        community.setMemberCount(1); // 创建者自动成为成员
        community.setStatus("活跃");
        return save(community);
    }

    @Override
    public boolean updateCommunity(Community community) {
        community.setUpdateTime(LocalDateTime.now());
        return updateById(community);
    }

    @Override
    public boolean deleteCommunity(Long id) {
        return removeById(id);
    }
}
