package com.xueya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.Community;
import com.xueya.mapper.CommunityMapper;
import com.xueya.service.CommunityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {
    @Override
    public List<Community> getCommunitiesByType(String type) {
        return baseMapper.selectList(null);
    }

    @Override
    public List<Community> getCommunitiesByCategory(String category) {
        return baseMapper.selectList(null);
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