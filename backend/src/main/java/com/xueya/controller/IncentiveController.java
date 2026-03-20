package com.xueya.controller;

import com.xueya.entity.Incentive;
import com.xueya.entity.UserIncentive;
import com.xueya.service.IncentiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incentives")
public class IncentiveController {

    @Autowired
    private IncentiveService incentiveService;

    // 创建激励
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createIncentive(@RequestBody Incentive incentive) {
        boolean success = incentiveService.createIncentive(incentive);
        if (success) {
            return ResponseEntity.ok("激励创建成功");
        } else {
            return ResponseEntity.badRequest().body("激励创建失败");
        }
    }

    // 更新激励
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateIncentive(@RequestBody Incentive incentive) {
        boolean success = incentiveService.updateIncentive(incentive);
        if (success) {
            return ResponseEntity.ok("激励更新成功");
        } else {
            return ResponseEntity.badRequest().body("激励更新失败");
        }
    }

    // 删除激励
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIncentive(@PathVariable Long id) {
        boolean success = incentiveService.deleteIncentive(id);
        if (success) {
            return ResponseEntity.ok("激励删除成功");
        } else {
            return ResponseEntity.badRequest().body("激励删除失败");
        }
    }

    // 获取所有激励
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<Incentive> getAllIncentives() {
        return incentiveService.list();
    }

    // 获取活跃激励
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/active")
    public List<Incentive> getActiveIncentives() {
        return incentiveService.getActiveIncentives();
    }

    // 根据类型获取激励
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/type/{type}")
    public List<Incentive> getIncentivesByType(@PathVariable String type) {
        return incentiveService.getIncentivesByType(type);
    }

    // 获取激励详情
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Incentive> getIncentiveById(@PathVariable Long id) {
        Incentive incentive = incentiveService.getIncentiveById(id);
        if (incentive != null) {
            return ResponseEntity.ok(incentive);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 授予用户激励
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/grant")
    public ResponseEntity<?> grantIncentiveToUser(@RequestParam Long userId, @RequestParam Long incentiveId) {
        boolean success = incentiveService.grantIncentiveToUser(userId, incentiveId);
        if (success) {
            return ResponseEntity.ok("激励授予成功");
        } else {
            return ResponseEntity.badRequest().body("激励授予失败");
        }
    }

    // 获取用户的激励列表
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<UserIncentive> getUserIncentives(@PathVariable Long userId) {
        return incentiveService.getUserIncentives(userId);
    }

    // 获取用户的总积分
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}/points")
    public Integer getUserTotalPoints(@PathVariable Long userId) {
        return incentiveService.getUserTotalPoints(userId);
    }

    // 获取用户的特定类型激励
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}/type/{type}")
    public List<UserIncentive> getUserIncentivesByType(@PathVariable Long userId, @PathVariable String type) {
        return incentiveService.getUserIncentivesByType(userId, type);
    }

    // 获取激励列表（前端使用）
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("")
    public List<Incentive> getIncentives() {
        return incentiveService.getActiveIncentives();
    }

    // 获取用户激励记录（前端使用）
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @RequestMapping("/../user-incentives")
    public List<UserIncentive> getUserIncentives() {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        return incentiveService.getUserIncentives(userId);
    }

    // 获取用户积分（前端使用）
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @RequestMapping("/../user-points")
    public Integer getUserPoints() {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        return incentiveService.getUserTotalPoints(userId);
    }

    // 领取激励（前端使用）
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{id}/claim")
    public ResponseEntity<?> claimIncentive(@PathVariable Long id) {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        boolean success = incentiveService.claimIncentive(userId, id);
        if (success) {
            return ResponseEntity.ok("激励领取成功");
        } else {
            return ResponseEntity.badRequest().body("激励领取失败");
        }
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId() {
        // 这里需要根据实际的用户认证机制实现
        // 暂时返回一个默认值，实际项目中应该从SecurityContext获取
        return 1L;
    }
}
