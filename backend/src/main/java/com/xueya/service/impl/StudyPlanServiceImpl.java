package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.StudyPlan;
import com.xueya.mapper.StudyPlanMapper;
import com.xueya.service.StudyPlanService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudyPlanServiceImpl extends ServiceImpl<StudyPlanMapper, StudyPlan> implements StudyPlanService {

    @Override
    public List<StudyPlan> getPlansByUserId(Long userId) {
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<StudyPlan> getPlansBySchoolId(Long schoolId) {
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("school_id", schoolId);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public StudyPlan getPlanById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createPlan(StudyPlan studyPlan) {
        // 检查是否已存在相同标题的学习计划
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", studyPlan.getUserId());
        queryWrapper.eq("title", studyPlan.getTitle());
        List<StudyPlan> existingPlans = baseMapper.selectList(queryWrapper);
        if (!existingPlans.isEmpty()) {
            // 已存在相同标题的学习计划，返回false
            return false;
        }
        
        studyPlan.setCreateTime(LocalDateTime.now().toString());
        studyPlan.setUpdateTime(LocalDateTime.now().toString());
        studyPlan.setStatus("active");
        studyPlan.setProgress(0.0);
        return save(studyPlan);
    }

    @Override
    public boolean updatePlan(StudyPlan studyPlan) {
        studyPlan.setUpdateTime(LocalDateTime.now().toString());
        return updateById(studyPlan);
    }

    @Override
    public boolean deletePlan(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateProgress(Long id, Double progress) {
        StudyPlan plan = baseMapper.selectById(id);
        if (plan != null) {
            plan.setProgress(progress);
            plan.setUpdateTime(LocalDateTime.now().toString());
            // 根据进度更新状态
            if (progress >= 100) {
                plan.setStatus("completed");
            } else if (progress > 0) {
                plan.setStatus("in_progress");
            }
            return updateById(plan);
        }
        return false;
    }

    @Override
    public List<StudyPlan> getPlansByStatus(String status) {
        QueryWrapper<StudyPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }
}
