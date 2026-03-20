package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.UserSkill;

import java.util.List;
import java.util.Map;

public interface UserSkillService extends IService<UserSkill> {
    List<UserSkill> getUserSkillsByUserId(Long userId);
    List<Map<String, Object>> getUserSkillRatings(Long userId);
}
