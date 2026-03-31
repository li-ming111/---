package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.Skill;
import com.xueya.entity.UserSkill;
import com.xueya.mapper.SkillMapper;
import com.xueya.mapper.UserSkillMapper;
import com.xueya.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SkillServiceImpl extends ServiceImpl<SkillMapper, Skill> implements SkillService {
    
    @Autowired
    private SkillMapper skillMapper;
    
    @Autowired
    private UserSkillMapper userSkillMapper;

    @Override
    public List<Skill> getSkillsWithCategories() {
        // 这里简化处理，实际应该按分类分组
        return skillMapper.selectList(null);
    }

    @Override
    public List<UserSkill> getUserSkills(Long userId) {
        QueryWrapper<UserSkill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return userSkillMapper.selectList(queryWrapper);
    }

    @Override
    public boolean saveUserSkills(Long userId, List<UserSkill> userSkills) {
        // 先删除用户现有的技能评估记录
        QueryWrapper<UserSkill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        userSkillMapper.delete(queryWrapper);
        
        // 保存新的技能评估记录
        for (UserSkill userSkill : userSkills) {
            userSkill.setUserId(userId);
            userSkill.setCreateTime(LocalDateTime.now().toString());
            userSkill.setAssessmentDate(null); // 明确设置为null
            userSkill.setImprovementPlan(null); // 明确设置为null
            userSkill.setUpdateTime(null); // 明确设置为null
            userSkillMapper.insert(userSkill);
        }
        return true;
    }

    @Override
    public Skill getSkillById(Long id) {
        return skillMapper.selectById(id);
    }

    @Override
    public List<?> getSkillRecommendations(Long userId) {
        // 这里简化处理，实际应该根据用户技能水平推荐资源
        List<UserSkill> userSkills = getUserSkills(userId);
        List<Object> recommendations = new ArrayList<>();
        
        // 模拟推荐数据
        for (UserSkill userSkill : userSkills) {
            if (userSkill.getLevel() < 3) { // 技能水平较低，推荐学习资源
                java.util.Map<String, Object> recommendation = new java.util.HashMap<>();
                // 通过skillId获取Skill对象
                Skill skill = getSkillById(userSkill.getSkillId());
                String skillName = skill != null ? skill.getName() : "未知技能";
                recommendation.put("title", "技能提升课程: " + skillName);
                recommendation.put("type", "课程");
                recommendation.put("description", "适合初学者的技能提升课程");
                recommendation.put("skills", java.util.List.of(skillName));
                recommendations.add(recommendation);
            }
        }
        
        return recommendations;
    }
}
