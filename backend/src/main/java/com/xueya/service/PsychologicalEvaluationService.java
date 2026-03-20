package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.PsychologicalEvaluation;

import java.util.List;

public interface PsychologicalEvaluationService extends IService<PsychologicalEvaluation> {
    List<PsychologicalEvaluation> getEvaluationsByUserId(Long userId);
    List<PsychologicalEvaluation> getEvaluationsByUserIdAndType(Long userId, String type);
}