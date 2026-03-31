package com.xueya.controller;

import com.xueya.entity.Interest;
import com.xueya.entity.StudyGroup;
import com.xueya.service.InterestService;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interest")
public class InterestController {
    
    @Autowired
    private InterestService interestService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // 获取热门兴趣列表
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/popular")
    public List<Interest> getPopularInterests() {
        return interestService.getPopularInterests();
    }

    // 根据分类获取兴趣
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/category/{category}")
    public List<Interest> getInterestsByCategory(@PathVariable String category) {
        return interestService.getInterestsByCategory(category);
    }

    // 搜索兴趣
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/search")
    public List<Interest> searchInterests(@RequestParam String keyword) {
        return interestService.searchInterests(keyword);
    }

    // 获取兴趣小组列表
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/groups")
    public List<StudyGroup> getInterestGroups() {
        return interestService.getInterestGroups();
    }

    // 根据兴趣获取小组
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/groups/interest")
    public List<StudyGroup> getGroupsByInterest(@RequestParam String interestName) {
        return interestService.getGroupsByInterest(interestName);
    }

    // 创建兴趣小组
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/groups/create")
    public ResponseEntity<?> createGroup(@RequestBody StudyGroup group, @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        group.setLeaderId(userId);
        boolean success = interestService.createGroup(group);
        if (success) {
            return ResponseEntity.ok("兴趣小组创建成功");
        } else {
            return ResponseEntity.badRequest().body("兴趣小组创建失败");
        }
    }

    // 加入小组
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/groups/{groupId}/join")
    public ResponseEntity<?> joinGroup(@PathVariable Long groupId, @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        boolean success = interestService.joinGroup(groupId, userId);
        if (success) {
            return ResponseEntity.ok("加入小组成功");
        } else {
            return ResponseEntity.badRequest().body("加入小组失败");
        }
    }

    // 查看小组详情
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/groups/{id}")
    public ResponseEntity<StudyGroup> getGroupById(@PathVariable Long id) {
        StudyGroup group = interestService.getGroupById(id);
        if (group != null) {
            return ResponseEntity.ok(group);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
