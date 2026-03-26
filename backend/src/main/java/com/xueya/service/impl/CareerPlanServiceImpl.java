package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.CareerPlan;
import com.xueya.mapper.CareerPlanMapper;
import com.xueya.service.CareerPlanService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CareerPlanServiceImpl extends ServiceImpl<CareerPlanMapper, CareerPlan> implements CareerPlanService {

    @Override
    public List<CareerPlan> getPlansByUserId(Long userId) {
        QueryWrapper<CareerPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<CareerPlan> getPlansBySchoolId(Long schoolId) {
        QueryWrapper<CareerPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("school_id", schoolId);
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public CareerPlan getPlanById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createPlan(CareerPlan careerPlan) {
        // 检查是否已存在相同职业目标的职业规划
        QueryWrapper<CareerPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", careerPlan.getUserId());
        queryWrapper.eq("career_goal", careerPlan.getCareerGoal());
        List<CareerPlan> existingPlans = baseMapper.selectList(queryWrapper);
        if (!existingPlans.isEmpty()) {
            // 已存在相同职业目标的职业规划，返回false
            return false;
        }
        
        careerPlan.setCreateTime(LocalDateTime.now().toString());
        careerPlan.setUpdateTime(LocalDateTime.now().toString());
        if (careerPlan.getStatus() == null || careerPlan.getStatus().isEmpty()) {
            careerPlan.setStatus("未开始");
        }
        return save(careerPlan);
    }

    @Override
    public boolean updatePlan(CareerPlan careerPlan) {
        careerPlan.setUpdateTime(LocalDateTime.now().toString());
        return updateById(careerPlan);
    }

    @Override
    public boolean deletePlan(Long id) {
        return removeById(id);
    }
}
