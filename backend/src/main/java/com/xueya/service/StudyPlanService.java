package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.StudyPlan;

import java.util.List;

public interface StudyPlanService extends IService<StudyPlan> {
    List<StudyPlan> getPlansByUserId(Long userId);
    List<StudyPlan> getPlansBySchoolId(Long schoolId);
    StudyPlan getPlanById(Long id);
    boolean createPlan(StudyPlan studyPlan);
    boolean updatePlan(StudyPlan studyPlan);
    boolean deletePlan(Long id);
    boolean updateProgress(Long id, Double progress);
    List<StudyPlan> getPlansByStatus(String status);
}
