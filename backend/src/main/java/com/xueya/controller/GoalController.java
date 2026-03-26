package com.xueya.controller;

import com.xueya.entity.UserGoal;
import com.xueya.service.UserGoalService;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goals")
public class GoalController {
    @Autowired
    private UserGoalService userGoalService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public List<UserGoal> getGoals(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            String token = authorizationHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            return userGoalService.getUserGoalsByUserId(userId);
        } catch (Exception e) {
            // 处理token解析异常
            return java.util.Collections.emptyList();
        }
    }

    @PostMapping
    public UserGoal addGoal(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserGoal goal) {
        try {
            String token = authorizationHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            goal.setUserId(userId);
            goal.setStatus("active");
            userGoalService.save(goal);
            return goal;
        } catch (Exception e) {
            // 处理token解析异常
            return null;
        }
    }

    @PutMapping("/{goalId}/status")
    public void updateGoalStatus(@PathVariable Long goalId, @RequestBody Map<String, String> statusData) {
        String status = statusData.get("status");
        UserGoal goal = userGoalService.getById(goalId);
        if (goal != null) {
            goal.setStatus(status);
            userGoalService.updateById(goal);
        }
    }
}