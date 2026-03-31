package com.xueya.controller;

import com.xueya.entity.SystemAnnouncement;
import com.xueya.entity.User;
import com.xueya.service.SystemAnnouncementService;
import com.xueya.service.UserService;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/announcements", produces = "application/json; charset=utf-8")
public class SystemAnnouncementsController {

    @Autowired
    private SystemAnnouncementService systemAnnouncementService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // 获取公告列表
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/list")
    public List<SystemAnnouncement> getAnnouncements(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search) {
        return systemAnnouncementService.getAnnouncements(status, search);
    }

    // 发布公告
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PostMapping("/publish")
    public SystemAnnouncement publishAnnouncement(@RequestBody SystemAnnouncement announcement,
                                                   @RequestHeader("Authorization") String authorizationHeader) {
        Long publisherId = getCurrentUserId(authorizationHeader);
        String publisherName = getCurrentUserName(authorizationHeader);
        
        announcement.setPublisherId(publisherId);
        announcement.setPublisherName(publisherName);
        
        systemAnnouncementService.publishAnnouncement(announcement);
        return announcement;
    }

    // 查看公告详情
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN') or hasRole('STUDENT') or hasRole('TEACHER')")
    @GetMapping("/{id}")
    public SystemAnnouncement viewAnnouncement(@PathVariable Long id) {
        // 增加阅读数
        systemAnnouncementService.incrementReadCount(id);
        return systemAnnouncementService.getById(id);
    }

    // 删除公告
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @DeleteMapping("/{id}")
    public boolean deleteAnnouncement(@PathVariable Long id) {
        return systemAnnouncementService.removeById(id);
    }

    // 获取公告统计
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/stats")
    public Map<String, Object> getAnnouncementStats() {
        return systemAnnouncementService.getAnnouncementStats();
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
