package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.LearningResource;
import com.xueya.mapper.LearningResourceMapper;
import com.xueya.service.LearningResourceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningResourceServiceImpl extends ServiceImpl<LearningResourceMapper, LearningResource> implements LearningResourceService {

    @Override
    public List<LearningResource> getResourcesBySchoolId(Long schoolId) {
        QueryWrapper<LearningResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("school_id", schoolId);
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<LearningResource> getResourcesByCategory(String category) {
        QueryWrapper<LearningResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<LearningResource> searchResources(String keyword) {
        QueryWrapper<LearningResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", keyword)
                .or().like("description", keyword)
                .or().like("tags", keyword);
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<LearningResource> getRecommendedResources(Long userId) {
        // 简单的推荐算法：返回下载量和浏览量较高的资源
        QueryWrapper<LearningResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("download_count")
                .orderByDesc("view_count");
        queryWrapper.last("LIMIT 10");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void incrementViewCount(Long resourceId) {
        LearningResource resource = baseMapper.selectById(resourceId);
        if (resource != null) {
            resource.setViewCount(resource.getViewCount() != null ? resource.getViewCount() + 1 : 1);
            baseMapper.updateById(resource);
        }
    }

    @Override
    public void incrementDownloadCount(Long resourceId) {
        LearningResource resource = baseMapper.selectById(resourceId);
        if (resource != null) {
            resource.setDownloadCount(resource.getDownloadCount() != null ? resource.getDownloadCount() + 1 : 1);
            baseMapper.updateById(resource);
        }
    }
}
