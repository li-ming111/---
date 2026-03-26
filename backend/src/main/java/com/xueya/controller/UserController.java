package com.xueya.controller;

import com.xueya.entity.User;
import com.xueya.entity.UserProfile;
import com.xueya.service.UserProfileService;
import com.xueya.service.UserService;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/profile")
    public User getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        return userService.getUserByUsername(username);
    }

    @PutMapping("/profile")
    public Map<String, Object> updateProfile(@RequestHeader("Authorization") String authorizationHeader, @RequestBody User updatedUser) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (user != null) {
            user.setName(updatedUser.getName());
            user.setGender(updatedUser.getGender());
            user.setPhone(updatedUser.getPhone());
            user.setEmail(updatedUser.getEmail());
            user.setSchoolId(updatedUser.getSchoolId());
            user.setMajorId(updatedUser.getMajorId());
            user.setGrade(updatedUser.getGrade());
            userService.updateById(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "个人资料更新成功");
            return response;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "用户不存在");
        return response;
    }

    @GetMapping("/profile/portrait")
    public UserProfile getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        return userProfileService.getUserProfileByUserId(user.getId());
    }

    @PutMapping("/profile/portrait")
    public Map<String, Object> updateUserProfile(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserProfile updatedProfile) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);
        
        if (user != null) {
            updatedProfile.setUserId(user.getId());
            userProfileService.updateUserProfile(updatedProfile);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户画像更新成功");
            return response;
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "用户不存在");
        return response;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        // 模拟统计数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("coursesCompleted", 15);
        stats.put("plansCreated", 5);
        stats.put("resourcesAccessed", 20);
        stats.put("goalsSet", 8);
        stats.put("activePlans", 3);
        stats.put("completedTasks", 12);
        stats.put("learningDays", 7);
        stats.put("achievements", 3);

        return stats;
    }

    // 根据用户名查询用户信息（用于测试）
    @GetMapping("/by-username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/avatar")
    public Map<String, Object> uploadAvatar(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("avatar") MultipartFile file) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (user != null && file != null && !file.isEmpty()) {
            try {
                // 生成唯一文件名
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                // 保存文件到本地（实际项目中应该保存到云存储或文件服务器）
                String filePath = "D:/BS/backend/src/main/resources/static/avatars/" + fileName;
                File dest = new File(filePath);
                dest.getParentFile().mkdirs();
                file.transferTo(dest);

                // 更新用户头像路径
                String avatarUrl = "/static/avatars/" + fileName;
                user.setAvatar(avatarUrl);
                userService.updateById(user);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "头像上传成功");
                response.put("avatarUrl", avatarUrl);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "头像上传失败");
                return response;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "请选择要上传的文件");
        return response;
    }

    // 学校认证API
    @PostMapping("/verify-school")
    public Map<String, Object> verifySchool(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, Object> verificationData) {
        String token = authorizationHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userService.getUserByUsername(username);

        if (user != null) {
            String studentId = (String) verificationData.get("studentId");
            Long schoolId = (Long) verificationData.get("schoolId");

            // 更新用户的学号和学校信息
            user.setStudentId(studentId);
            user.setSchoolId(schoolId);
            userService.updateById(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "学校认证成功");
            return response;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "用户不存在");
        return response;
    }

    // 获取学校列表（用于学校认证）
    @GetMapping("/schools")
    public Map<String, Object> getSchools() {
        Map<String, Object> response = new HashMap<>();
        // 这里应该从数据库中获取学校列表
        // 暂时返回硬编码的学校列表
        List<Map<String, Object>> schools = new ArrayList<>();
        
        // 添加哈尔滨信息工程学院
        Map<String, Object> school1 = new HashMap<>();
        school1.put("id", 1);
        school1.put("name", "哈尔滨信息工程学院");
        school1.put("code", "HIIE");
        schools.add(school1);
        
        // 添加其他学校
        Map<String, Object> school2 = new HashMap<>();
        school2.put("id", 2);
        school2.put("name", "北京大学");
        school2.put("code", "PKU");
        schools.add(school2);
        
        Map<String, Object> school3 = new HashMap<>();
        school3.put("id", 3);
        school3.put("name", "清华大学");
        school3.put("code", "THU");
        schools.add(school3);
        
        Map<String, Object> school4 = new HashMap<>();
        school4.put("id", 4);
        school4.put("name", "哈尔滨工业大学");
        school4.put("code", "HIT");
        schools.add(school4);
        
        Map<String, Object> school5 = new HashMap<>();
        school5.put("id", 5);
        school5.put("name", "哈尔滨工程大学");
        school5.put("code", "HEU");
        schools.add(school5);
        
        response.put("success", true);
        response.put("schools", schools);
        return response;
    }
}