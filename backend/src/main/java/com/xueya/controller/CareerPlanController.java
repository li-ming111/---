package com.xueya.controller;

import com.xueya.entity.CareerPlan;
import com.xueya.service.CareerPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/career-plans")
public class CareerPlanController {

    @Autowired
    private CareerPlanService careerPlanService;

    // 创建职业规划
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody CareerPlan careerPlan) {
        boolean success = careerPlanService.createPlan(careerPlan);
        if (success) {
            return ResponseEntity.ok("职业规划创建成功");
        } else {
            return ResponseEntity.badRequest().body("职业规划创建失败");
        }
    }

    // 更新职业规划
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updatePlan(@RequestBody CareerPlan careerPlan) {
        boolean success = careerPlanService.updatePlan(careerPlan);
        if (success) {
            return ResponseEntity.ok("职业规划更新成功");
        } else {
            return ResponseEntity.badRequest().body("职业规划更新失败");
        }
    }

    // 删除职业规划
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        boolean success = careerPlanService.deletePlan(id);
        if (success) {
            return ResponseEntity.ok("职业规划删除成功");
        } else {
            return ResponseEntity.badRequest().body("职业规划删除失败");
        }
    }

    // 获取用户的职业规划列表
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<CareerPlan> getPlansByUserId(@PathVariable Long userId) {
        try {
            return careerPlanService.getPlansByUserId(userId);
        } catch (Exception e) {
            // 处理数据库查询异常
            return java.util.Collections.emptyList();
        }
    }

    // 获取学校的职业规划列表
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/school/{schoolId}")
    public List<CareerPlan> getPlansBySchoolId(@PathVariable Long schoolId) {
        return careerPlanService.getPlansBySchoolId(schoolId);
    }

    // 获取职业规划详情
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CareerPlan> getPlanById(@PathVariable Long id) {
        CareerPlan plan = careerPlanService.getPlanById(id);
        if (plan != null) {
            return ResponseEntity.ok(plan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取所有职业规划（管理员）
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<CareerPlan> getAllPlans() {
        return careerPlanService.list();
    }
}
