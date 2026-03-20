package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.SkillQuestion;
import com.xueya.mapper.SkillQuestionMapper;
import com.xueya.assistant.service.SkillQuestionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillQuestionServiceImpl extends ServiceImpl<SkillQuestionMapper, SkillQuestion> implements SkillQuestionService {

    @Override
    public List<SkillQuestion> getQuestionsBySkillId(Long skillId) {
        QueryWrapper<SkillQuestion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("skill_id", skillId);
        queryWrapper.orderByAsc("question_order");
        return baseMapper.selectList(queryWrapper);
    }
}
