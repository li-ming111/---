package com.xueya.controller;

import com.xueya.entity.StudyPlan;
import com.xueya.entity.StudyPlanDetail;
import com.xueya.service.StudyPlanService;
import com.xueya.service.StudyPlanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-plans")
public class StudyPlanController {

    @Autowired
    private StudyPlanService studyPlanService;

    @Autowired
    private StudyPlanDetailService studyPlanDetailService;

    // 创建学习计划
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody StudyPlan studyPlan) {
        boolean success = studyPlanService.createPlan(studyPlan);
        if (success) {
            return ResponseEntity.ok("学习计划创建成功");
        } else {
            return ResponseEntity.badRequest().body("学习计划创建失败");
        }
    }

    // 更新学习计划
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updatePlan(@RequestBody StudyPlan studyPlan) {
        boolean success = studyPlanService.updatePlan(studyPlan);
        if (success) {
            return ResponseEntity.ok("学习计划更新成功");
        } else {
            return ResponseEntity.badRequest().body("学习计划更新失败");
        }
    }

    // 删除学习计划
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlan(@PathVariable Long id) {
        boolean success = studyPlanService.deletePlan(id);
        if (success) {
            return ResponseEntity.ok("学习计划删除成功");
        } else {
            return ResponseEntity.badRequest().body("学习计划删除失败");
        }
    }

    // 更新学习计划进度
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/progress/{id}")
    public ResponseEntity<?> updateProgress(@PathVariable Long id, @RequestParam Double progress) {
        boolean success = studyPlanService.updateProgress(id, progress);
        if (success) {
            return ResponseEntity.ok("学习计划进度更新成功");
        } else {
            return ResponseEntity.badRequest().body("学习计划进度更新失败");
        }
    }

    // 获取用户的学习计划列表
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<StudyPlan> getPlansByUserId(@PathVariable Long userId) {
        return studyPlanService.getPlansByUserId(userId);
    }

    // 获取学校的学习计划列表
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/school/{schoolId}")
    public List<StudyPlan> getPlansBySchoolId(@PathVariable Long schoolId) {
        return studyPlanService.getPlansBySchoolId(schoolId);
    }

    // 获取学习计划详情
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<StudyPlan> getPlanById(@PathVariable Long id) {
        StudyPlan plan = studyPlanService.getPlanById(id);
        if (plan != null) {
            return ResponseEntity.ok(plan);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取所有学习计划（管理员）
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<StudyPlan> getAllPlans() {
        return studyPlanService.list();
    }

    // 根据状态获取学习计划
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public List<StudyPlan> getPlansByStatus(@PathVariable String status) {
        return studyPlanService.getPlansByStatus(status);
    }

    // 创建学习计划详情（任务）
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/detail/create")
    public ResponseEntity<?> createDetail(@RequestBody StudyPlanDetail detail) {
        boolean success = studyPlanDetailService.createDetail(detail);
        if (success) {
            return ResponseEntity.ok("学习计划任务创建成功");
        } else {
            return ResponseEntity.badRequest().body("学习计划任务创建失败");
        }
    }

    // 更新学习计划详情（任务）
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PutMapping("/detail/update")
    public ResponseEntity<?> updateDetail(@RequestBody StudyPlanDetail detail) {
        boolean success = studyPlanDetailService.updateDetail(detail);
        if (success) {
            return ResponseEntity.ok("学习计划任务更新成功");
        } else {
            return ResponseEntity.badRequest().body("学习计划任务更新失败");
        }
    }

    // 删除学习计划详情（任务）
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @DeleteMapping("/detail/{id}")
    public ResponseEntity<?> deleteDetail(@PathVariable Long id) {
        boolean success = studyPlanDetailService.deleteDetail(id);
        if (success) {
            return ResponseEntity.ok("学习计划任务删除成功");
        } else {
            return ResponseEntity.badRequest().body("学习计划任务删除失败");
        }
    }

    // 更新学习计划详情（任务）进度
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/detail/progress/{id}")
    public ResponseEntity<?> updateDetailProgress(@PathVariable Long id, @RequestParam Double progress) {
        boolean success = studyPlanDetailService.updateProgress(id, progress);
        if (success) {
            return ResponseEntity.ok("学习计划任务进度更新成功");
        } else {
            return ResponseEntity.badRequest().body("学习计划任务进度更新失败");
        }
    }

    // 获取学习计划的所有任务
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/detail/plan/{planId}")
    public List<StudyPlanDetail> getDetailsByPlanId(@PathVariable Long planId) {
        return studyPlanDetailService.getDetailsByPlanId(planId);
    }

    // 获取任务详情
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/detail/{id}")
    public ResponseEntity<StudyPlanDetail> getDetailById(@PathVariable Long id) {
        StudyPlanDetail detail = studyPlanDetailService.getDetailById(id);
        if (detail != null) {
            return ResponseEntity.ok(detail);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 根据状态获取任务
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/detail/status/{status}")
    public List<StudyPlanDetail> getDetailsByStatus(@PathVariable String status) {
        return studyPlanDetailService.getDetailsByStatus(status);
    }
}
