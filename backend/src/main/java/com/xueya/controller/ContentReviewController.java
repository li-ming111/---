package com.xueya.controller;

import com.xueya.entity.ContentReview;
import com.xueya.entity.User;
import com.xueya.service.ContentReviewService;
import com.xueya.service.UserService;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/content-review", produces = "application/json; charset=utf-8")
public class ContentReviewController {

    @Autowired
    private ContentReviewService contentReviewService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 获取审核规则
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/rules")
    public Map<String, Object> getReviewRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("autoReview", true);
        rules.put("sensitiveWordFilter", true);
        rules.put("fileTypeLimit", true);
        rules.put("fileSizeLimit", 50);
        rules.put("sensitiveWords", "敏感词1,敏感词2,敏感词3");
        return rules;
    }

    // 保存审核规则
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PostMapping("/rules")
    public boolean saveReviewRules(@RequestBody Map<String, Object> rules) {
        // 实际项目中应该保存到数据库或配置文件
        return true;
    }

    // 获取待审核内容
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/pending")
    public List<ContentReview> getPendingContents(@RequestParam(required = false) String type) {
        return contentReviewService.getPendingContents(type);
    }

    // 查看内容详情
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/content/{id}")
    public ContentReview viewContent(@PathVariable Long id) {
        return contentReviewService.getById(id);
    }

    // 审核通过
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PostMapping("/approve/{id}")
    public boolean approveContent(@PathVariable Long id, 
                                   @RequestHeader("Authorization") String authorizationHeader,
                                   @RequestParam(required = false) String comment) {
        Long reviewerId = getCurrentUserId(authorizationHeader);
        String reviewerName = getCurrentUserName(authorizationHeader);
        return contentReviewService.approveContent(id, reviewerId, reviewerName, comment);
    }

    // 审核拒绝
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PostMapping("/reject/{id}")
    public boolean rejectContent(@PathVariable Long id,
                                  @RequestHeader("Authorization") String authorizationHeader,
                                  @RequestParam String reason) {
        Long reviewerId = getCurrentUserId(authorizationHeader);
        String reviewerName = getCurrentUserName(authorizationHeader);
        return contentReviewService.rejectContent(id, reviewerId, reviewerName, reason);
    }

    // 获取审核历史
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/history")
    public List<ContentReview> getReviewHistory(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return contentReviewService.getReviewHistory(startDate, endDate);
    }

    // 获取审核统计
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/stats")
    public Map<String, Object> getReviewStats() {
        return contentReviewService.getReviewStats();
    }

    // 提交内容审核
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    @PostMapping("/submit")
    public boolean submitContent(@RequestBody ContentReview contentReview) {
        return contentReviewService.submitContent(contentReview);
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        return user != null ? user.getId() : null;
    }

    // 获取当前登录用户名称的辅助方法
    private String getCurrentUserName(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        return user != null ? user.getName() : username;
    }
}
