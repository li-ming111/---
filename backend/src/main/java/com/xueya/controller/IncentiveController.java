package com.xueya.controller;

import com.xueya.entity.Incentive;
import com.xueya.entity.User;
import com.xueya.entity.UserIncentive;
import com.xueya.service.IncentiveService;
import com.xueya.service.UserService;
import com.xueya.utils.JwtUtil;
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

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 创建激励
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
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
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
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
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
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
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/all")
    public List<Incentive> getAllIncentives() {
        return incentiveService.list();
    }

    // 获取活跃激励
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/active")
    public List<Incentive> getActiveIncentives() {
        return incentiveService.getActiveIncentives();
    }

    // 根据类型获取激励
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/type/{type}")
    public List<Incentive> getIncentivesByType(@PathVariable String type) {
        return incentiveService.getIncentivesByType(type);
    }

    // 获取激励详情
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
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
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
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
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/user/{userId}")
    public List<UserIncentive> getUserIncentives(@PathVariable Long userId) {
        return incentiveService.getUserIncentives(userId);
    }

    // 获取用户的总积分
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/user/{userId}/points")
    public Integer getUserTotalPoints(@PathVariable Long userId) {
        return incentiveService.getUserTotalPoints(userId);
    }

    // 获取用户的特定类型激励
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/user/{userId}/type/{type}")
    public List<UserIncentive> getUserIncentivesByType(@PathVariable Long userId, @PathVariable String type) {
        return incentiveService.getUserIncentivesByType(userId, type);
    }

    // 获取激励列表（前端使用）
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("")
    public List<Incentive> getIncentives() {
        return incentiveService.getActiveIncentives();
    }

    // 获取用户激励记录（前端使用）
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/user-incentives")
    public List<UserIncentive> getUserIncentives(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        return incentiveService.getUserIncentives(userId);
    }

    // 获取用户积分（前端使用）
    @PreAuthorize("hasRole('STUDENT') or hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/user-points")
    public Integer getUserPoints(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        return incentiveService.getUserTotalPoints(userId);
    }

    // 领取激励（前端使用）
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{id}/claim")
    public ResponseEntity<?> claimIncentive(@PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        boolean success = incentiveService.claimIncentive(userId, id);
        if (success) {
            return ResponseEntity.ok("激励领取成功");
        } else {
            return ResponseEntity.badRequest().body("激励领取失败");
        }
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        return user != null ? user.getId() : null;
    }
}
