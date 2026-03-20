package com.xueya.controller;

import com.xueya.entity.User;
import com.xueya.entity.LearningResource;
import com.xueya.entity.StudyPlan;
import com.xueya.service.UserService;
import com.xueya.service.LearningResourceService;
import com.xueya.service.StudyPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private UserService userService;

    @Autowired
    private LearningResourceService learningResourceService;

    @Autowired
    private StudyPlanService studyPlanService;

    // 获取学校统计数据
    @PreAuthorize("hasRole('SCHOOL_ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/school/{schoolId}")
    public Map<String, Object> getSchoolStatistics(@PathVariable Long schoolId) {
        Map<String, Object> statistics = new HashMap<>();

        // 获取总学生数
        List<User> users = userService.getUsersBySchoolId(schoolId);
        int totalStudents = users.size();

        // 计算活跃学生数（这里简单处理，实际应该根据登录或学习记录判断）
        int activeStudents = (int) users.stream().filter(user -> "1".equals(user.getStatus())).count();

        // 获取总资源数
        List<LearningResource> resources = learningResourceService.getResourcesBySchoolId(schoolId);
        int totalResources = resources.size();

        // 获取完成的学习计划数
        List<StudyPlan> plans = studyPlanService.getPlansBySchoolId(schoolId);
        int completedPlans = (int) plans.stream().filter(plan -> "completed".equals(plan.getStatus())).count();

        statistics.put("totalStudents", totalStudents);
        statistics.put("activeStudents", activeStudents);
        statistics.put("totalResources", totalResources);
        statistics.put("completedPlans", completedPlans);

        return statistics;
    }

    // 获取系统统计数据（超级管理员）
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/system")
    public Map<String, Object> getSystemStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 获取总用户数
        List<User> users = userService.list();
        int totalUsers = users.size();

        // 获取总资源数
        List<LearningResource> resources = learningResourceService.list();
        int totalResources = resources.size();

        // 获取总学习计划数
        List<StudyPlan> plans = studyPlanService.list();
        int totalPlans = plans.size();
        int completedPlans = (int) plans.stream().filter(plan -> "completed".equals(plan.getStatus())).count();

        statistics.put("totalUsers", totalUsers);
        statistics.put("totalResources", totalResources);
        statistics.put("totalPlans", totalPlans);
        statistics.put("completedPlans", completedPlans);

        return statistics;
    }
}
