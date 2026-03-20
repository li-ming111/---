package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.PsychologicalEvaluation;
import com.xueya.mapper.PsychologicalEvaluationMapper;
import com.xueya.service.PsychologicalEvaluationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PsychologicalEvaluationServiceImpl extends ServiceImpl<PsychologicalEvaluationMapper, PsychologicalEvaluation> implements PsychologicalEvaluationService {
    @Override
    public List<PsychologicalEvaluation> getEvaluationsByUserId(Long userId) {
        return baseMapper.selectList(new QueryWrapper<PsychologicalEvaluation>().eq("user_id", userId));
    }

    @Override
    public List<PsychologicalEvaluation> getEvaluationsByUserIdAndType(Long userId, String type) {
        return baseMapper.selectList(new QueryWrapper<PsychologicalEvaluation>().eq("user_id", userId).eq("evaluation_type", type));
    }
}