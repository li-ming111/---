package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.UserSkill;
import com.xueya.mapper.UserSkillMapper;
import com.xueya.assistant.service.UserSkillService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserSkillServiceImpl extends ServiceImpl<UserSkillMapper, UserSkill> implements UserSkillService {

    @Override
    public List<UserSkill> getUserSkillsByUserId(Long userId) {
        QueryWrapper<UserSkill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("assessment_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getUserSkillRatings(Long userId) {
        List<UserSkill> userSkills = getUserSkillsByUserId(userId);
        List<Map<String, Object>> ratings = new ArrayList<>();

        for (UserSkill userSkill : userSkills) {
            Map<String, Object> rating = new HashMap<>();
            rating.put("skillId", userSkill.getSkillId());
            rating.put("score", userSkill.getScore());
            rating.put("assessmentTime", userSkill.getAssessmentDate());
            rating.put("level", getSkillLevel(userSkill.getScore()));
            ratings.add(rating);
        }

        return ratings;
    }

    private String getSkillLevel(int score) {
        if (score >= 90) {
            return "优秀";
        } else if (score >= 80) {
            return "良好";
        } else if (score >= 70) {
            return "中等";
        } else if (score >= 60) {
            return "及格";
        } else {
            return "待提高";
        }
    }
}
