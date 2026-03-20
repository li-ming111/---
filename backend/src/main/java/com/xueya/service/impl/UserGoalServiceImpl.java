package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.UserGoal;
import com.xueya.mapper.UserGoalMapper;
import com.xueya.service.UserGoalService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserGoalServiceImpl extends ServiceImpl<UserGoalMapper, UserGoal> implements UserGoalService {
    @Override
    public List<UserGoal> getUserGoalsByUserId(Long userId) {
        return baseMapper.selectList(new QueryWrapper<UserGoal>().eq("user_id", userId));
    }

    @Override
    public List<UserGoal> getUserGoalsByUserIdAndType(Long userId, String type) {
        return baseMapper.selectList(new QueryWrapper<UserGoal>().eq("user_id", userId).eq("type", type));
    }
}