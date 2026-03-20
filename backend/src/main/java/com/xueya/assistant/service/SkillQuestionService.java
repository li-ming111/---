package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.SkillQuestion;

import java.util.List;

public interface SkillQuestionService extends IService<SkillQuestion> {
    List<SkillQuestion> getQuestionsBySkillId(Long skillId);
}
