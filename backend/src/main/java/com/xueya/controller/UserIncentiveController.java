package com.xueya.controller;

import com.xueya.entity.User;
import com.xueya.entity.UserIncentive;
import com.xueya.service.IncentiveService;
import com.xueya.service.UserService;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserIncentiveController {

    @Autowired
    private IncentiveService incentiveService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 获取用户激励记录
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user-incentives")
    public java.util.List<UserIncentive> getUserIncentives(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        return incentiveService.getUserIncentives(userId);
    }

    // 获取用户积分
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user-points")
    public Integer getUserPoints(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        return incentiveService.getUserTotalPoints(userId);
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        return user != null ? user.getId() : null;
    }
}
