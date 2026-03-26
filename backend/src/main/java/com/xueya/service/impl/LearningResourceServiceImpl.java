package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.LearningResource;
import com.xueya.entity.User;
import com.xueya.mapper.LearningResourceMapper;
import com.xueya.service.LearningResourceService;
import com.xueya.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LearningResourceServiceImpl extends ServiceImpl<LearningResourceMapper, LearningResource> implements LearningResourceService {

    @Autowired
    private UserService userService;

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

    @Override
    public List<LearningResource> getResourcesByStudentStage(Long userId) {
        // 获取用户信息
        User user = userService.getById(userId);
        if (user == null) {
            return new ArrayList<>();
        }
        
        // 获取用户的学生阶段
        String studentStage = user.getStudentStage();
        
        // 根据学生阶段过滤资源
        QueryWrapper<LearningResource> queryWrapper = new QueryWrapper<>();
        
        // 处理学生阶段为null的情况
        if (studentStage == null) {
            // 默认返回所有资源
            queryWrapper.orderByDesc("create_time");
            return baseMapper.selectList(queryWrapper);
        }
        
        // 所有大学生可以查看大学生的资源
        if (studentStage.equals("大学专科") || studentStage.equals("大学本科") || studentStage.equals("硕士研究生") || studentStage.equals("博士研究生")) {
            queryWrapper.in("student_stage", "大学专科", "大学本科", "硕士研究生", "博士研究生");
        } else {
            // 其他阶段的学生只能查看自己阶段的资源
            queryWrapper.eq("student_stage", studentStage);
        }
        
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }
}
