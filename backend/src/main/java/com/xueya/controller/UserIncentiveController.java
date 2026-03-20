package com.xueya.controller;

import com.xueya.entity.UserIncentive;
import com.xueya.service.IncentiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserIncentiveController {

    @Autowired
    private IncentiveService incentiveService;

    // 获取用户激励记录
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user-incentives")
    public java.util.List<UserIncentive> getUserIncentives() {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        return incentiveService.getUserIncentives(userId);
    }

    // 获取用户积分
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user-points")
    public Integer getUserPoints() {
        // 从当前登录用户获取userId
        Long userId = getCurrentUserId();
        return incentiveService.getUserTotalPoints(userId);
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId() {
        // 这里需要根据实际的用户认证机制实现
        // 暂时返回一个默认值，实际项目中应该从SecurityContext获取
        return 1L;
    }
}
