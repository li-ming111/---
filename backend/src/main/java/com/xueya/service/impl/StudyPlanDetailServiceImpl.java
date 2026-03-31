package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.StudyPlanDetail;
import com.xueya.mapper.StudyPlanDetailMapper;
import com.xueya.service.StudyPlanDetailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudyPlanDetailServiceImpl extends ServiceImpl<StudyPlanDetailMapper, StudyPlanDetail> implements StudyPlanDetailService {

    @Override
    public List<StudyPlanDetail> getDetailsByPlanId(Long planId) {
        QueryWrapper<StudyPlanDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", planId);
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public StudyPlanDetail getDetailById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createDetail(StudyPlanDetail detail) {
        // 检查是否已存在相同的学习计划详情
        QueryWrapper<StudyPlanDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("plan_id", detail.getPlanId());
        queryWrapper.eq("task_name", detail.getTaskName());
        List<StudyPlanDetail> existingDetails = baseMapper.selectList(queryWrapper);
        if (!existingDetails.isEmpty()) {
            // 已存在相同的学习计划详情，返回false
            return false;
        }
        
        detail.setCreateTime(LocalDateTime.now().toString());
        detail.setUpdateTime(LocalDateTime.now().toString());
        detail.setStatus("active");
        detail.setProgress(0.0);
        return save(detail);
    }

    @Override
    public boolean updateDetail(StudyPlanDetail detail) {
        detail.setUpdateTime(LocalDateTime.now().toString());
        return updateById(detail);
    }

    @Override
    public boolean deleteDetail(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateProgress(Long id, Double progress) {
        StudyPlanDetail detail = baseMapper.selectById(id);
        if (detail != null) {
            detail.setProgress(progress);
            detail.setUpdateTime(LocalDateTime.now().toString());
            // 根据进度更新状态
            if (progress >= 100) {
                detail.setStatus("completed");
            } else if (progress > 0) {
                detail.setStatus("in_progress");
            }
            return updateById(detail);
        }
        return false;
    }

    @Override
    public List<StudyPlanDetail> getDetailsByStatus(String status) {
        QueryWrapper<StudyPlanDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }
}
