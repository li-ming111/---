package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.CareerPlan;

import java.util.List;

public interface CareerPlanService extends IService<CareerPlan> {
    List<CareerPlan> getPlansByUserId(Long userId);
    List<CareerPlan> getPlansBySchoolId(Long schoolId);
    CareerPlan getPlanById(Long id);
    boolean createPlan(CareerPlan careerPlan);
    boolean updatePlan(CareerPlan careerPlan);
    boolean deletePlan(Long id);
}
