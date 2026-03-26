package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.UserGoal;

import java.util.List;

public interface UserGoalService extends IService<UserGoal> {
    List<UserGoal> getUserGoalsByUserId(Long userId);
    List<UserGoal> getUserGoalsByUserIdAndType(Long userId, String type);
    List<UserGoal> getGoalsByUserId(Long userId);
    List<UserGoal> getGoalsByStatus(String status);
}