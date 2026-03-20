package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.StudyPlanDetail;

import java.util.List;

public interface StudyPlanDetailService extends IService<StudyPlanDetail> {
    List<StudyPlanDetail> getDetailsByPlanId(Long planId);
    StudyPlanDetail getDetailById(Long id);
    boolean createDetail(StudyPlanDetail detail);
    boolean updateDetail(StudyPlanDetail detail);
    boolean deleteDetail(Long id);
    boolean updateProgress(Long id, Double progress);
    List<StudyPlanDetail> getDetailsByStatus(String status);
}
