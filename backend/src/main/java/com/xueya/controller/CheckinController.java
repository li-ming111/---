package com.xueya.controller;

import com.xueya.entity.Checkin;
import com.xueya.service.CheckinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkin")
public class CheckinController {

    @Autowired
    private CheckinService checkinService;

    // 打卡
    // @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/do-checkin")
    public ResponseEntity<?> doCheckin() {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        boolean success = checkinService.checkin(userId);
        if (success) {
            return ResponseEntity.ok("打卡成功");
        } else {
            return ResponseEntity.badRequest().body("今天已经打卡过了");
        }
    }

    // 检查今天是否已打卡
    // @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/has-checked-in-today")
    public ResponseEntity<Boolean> hasCheckedInToday() {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        boolean hasCheckedIn = checkinService.hasCheckedInToday(userId);
        return ResponseEntity.ok(hasCheckedIn);
    }

    // 获取用户打卡记录
    // @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/records")
    public List<Checkin> getUserCheckinRecords() {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        return checkinService.getUserCheckinRecords(userId);
    }

    // 获取用户打卡次数
    // @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/count")
    public ResponseEntity<Integer> getUserCheckinCount() {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        Integer count = checkinService.getUserCheckinCount(userId);
        return ResponseEntity.ok(count);
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId() {
        // 这里需要根据实际的用户认证机制实现
        // 暂时返回一个默认值，实际项目中应该从SecurityContext获取
        return 1L;
    }
}