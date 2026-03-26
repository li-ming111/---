package com.xueya.controller;

import com.xueya.entity.UserGoal;
import com.xueya.service.UserGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/goals")
public class GoalsController {

    @Autowired
    private UserGoalService userGoalService;

    // 获取用户的所有目标
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    public List<UserGoal> getGoals() {
        // 从JWT token中获取用户ID
        return userGoalService.list();
    }

    // 获取用户的目标列表
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<UserGoal> getGoalsByUserId(@PathVariable Long userId) {
        return userGoalService.getGoalsByUserId(userId);
    }

    // 获取目标详情
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserGoal> getGoalById(@PathVariable Long id) {
        UserGoal goal = userGoalService.getById(id);
        if (goal != null) {
            return ResponseEntity.ok(goal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 添加目标
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping
    public ResponseEntity<?> addGoal(@RequestBody UserGoal goal) {
        boolean success = userGoalService.save(goal);
        if (success) {
            return ResponseEntity.ok("目标添加成功");
        } else {
            return ResponseEntity.badRequest().body("目标添加失败");
        }
    }

    // 更新目标
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGoal(@PathVariable Long id, @RequestBody UserGoal goal) {
        goal.setId(id);
        boolean success = userGoalService.updateById(goal);
        if (success) {
            return ResponseEntity.ok("目标更新成功");
        } else {
            return ResponseEntity.badRequest().body("目标更新失败");
        }
    }

    // 更新目标状态
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateGoalStatus(@PathVariable Long id, @RequestBody Map<String, String> statusRequest) {
        String status = statusRequest.get("status");
        UserGoal goal = userGoalService.getById(id);
        if (goal != null) {
            goal.setStatus(status);
            boolean success = userGoalService.updateById(goal);
            if (success) {
                return ResponseEntity.ok("目标状态更新成功");
            } else {
                return ResponseEntity.badRequest().body("目标状态更新失败");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 删除目标
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(@PathVariable Long id) {
        boolean success = userGoalService.removeById(id);
        if (success) {
            return ResponseEntity.ok("目标删除成功");
        } else {
            return ResponseEntity.badRequest().body("目标删除失败");
        }
    }

    // 根据状态获取目标
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public List<UserGoal> getGoalsByStatus(@PathVariable String status) {
        return userGoalService.getGoalsByStatus(status);
    }
}
