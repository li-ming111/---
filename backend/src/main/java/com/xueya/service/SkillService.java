package com.xueya.service;

import com.xueya.entity.Skill;
import com.xueya.entity.UserSkill;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

public interface SkillService extends IService<Skill> {
    // 获取技能分类和等级数据
    List<Skill> getSkillsWithCategories();
    
    // 获取用户技能评估结果
    List<UserSkill> getUserSkills(Long userId);
    
    // 保存用户技能评估结果
    boolean saveUserSkills(Long userId, List<UserSkill> userSkills);
    
    // 获取技能详情
    Skill getSkillById(Long id);
    
    // 获取技能推荐
    List<?> getSkillRecommendations(Long userId);
}
