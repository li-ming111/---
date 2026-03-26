package com.xueya.controller;

import com.xueya.entity.Checkin;
import com.xueya.entity.User;
import com.xueya.service.CheckinService;
import com.xueya.service.UserService;
import com.xueya.utils.JwtUtil;
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

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 打卡
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/do-checkin")
    public ResponseEntity<?> doCheckin(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        boolean success = checkinService.checkin(userId);
        if (success) {
            return ResponseEntity.ok("打卡成功");
        } else {
            return ResponseEntity.badRequest().body("今天已经打卡过了");
        }
    }

    // 检查今天是否已打卡
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/has-checked-in-today")
    public ResponseEntity<Boolean> hasCheckedInToday(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        boolean hasCheckedIn = checkinService.hasCheckedInToday(userId);
        return ResponseEntity.ok(hasCheckedIn);
    }

    // 获取用户打卡记录
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/records")
    public List<Checkin> getUserCheckinRecords(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        return checkinService.getUserCheckinRecords(userId);
    }

    // 获取用户打卡次数
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/count")
    public ResponseEntity<Integer> getUserCheckinCount(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        Integer count = checkinService.getUserCheckinCount(userId);
        return ResponseEntity.ok(count);
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        return user != null ? user.getId() : null;
    }
}