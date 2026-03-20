package com.xueya.assistant.controller;

import com.xueya.assistant.entity.Community;
import com.xueya.assistant.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    // 获取所有社群
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/all")
    public List<Community> getAllCommunities() {
        return communityService.list();
    }

    // 根据类型获取社群
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/type/{type}")
    public List<Community> getCommunitiesByType(@PathVariable String type) {
        return communityService.getCommunitiesByType(type);
    }

    // 根据分类获取社群
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/category/{category}")
    public List<Community> getCommunitiesByCategory(@PathVariable String category) {
        return communityService.getCommunitiesByCategory(category);
    }

    // 获取用户创建的社群
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<Community> getCommunitiesByUserId(@PathVariable Long userId) {
        return communityService.getCommunitiesByCreatorId(userId);
    }

    // 创建社群
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createCommunity(@RequestBody Community community) {
        boolean success = communityService.createCommunity(community);
        if (success) {
            return ResponseEntity.ok("社群创建成功");
        } else {
            return ResponseEntity.badRequest().body("社群创建失败");
        }
    }

    // 更新社群
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateCommunity(@RequestBody Community community) {
        boolean success = communityService.updateCommunity(community);
        if (success) {
            return ResponseEntity.ok("社群更新成功");
        } else {
            return ResponseEntity.badRequest().body("社群更新失败");
        }
    }

    // 删除社群
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommunity(@PathVariable Long id) {
        boolean success = communityService.deleteCommunity(id);
        if (success) {
            return ResponseEntity.ok("社群删除成功");
        } else {
            return ResponseEntity.badRequest().body("社群删除失败");
        }
    }

    // 获取社群详情
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Community> getCommunityById(@PathVariable Long id) {
        Community community = communityService.getById(id);
        if (community != null) {
            return ResponseEntity.ok(community);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
