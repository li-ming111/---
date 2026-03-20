package com.xueya.controller;

import com.xueya.entity.User;
import com.xueya.entity.School;
import com.xueya.service.UserService;
import com.xueya.service.SchoolService;
import com.xueya.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private SchoolService schoolService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // 从请求中获取当前用户信息
    private Map<String, Object> getCurrentUserInfo(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String token = authorizationHeader.substring(7);
        String role = jwtUtil.getRoleFromToken(token);
        Long schoolId = jwtUtil.getSchoolIdFromToken(token);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("role", role);
        userInfo.put("schoolId", schoolId);
        return userInfo;
    }
    
    // 验证学校管理员权限
    private boolean validateSchoolAdminPermission(HttpServletRequest request, Long targetSchoolId) {
        Map<String, Object> userInfo = getCurrentUserInfo(request);
        String role = (String) userInfo.get("role");
        Long userSchoolId = (Long) userInfo.get("schoolId");
        
        // 超级管理员可以管理所有学校
        if ("super_admin".equals(role)) {
            return true;
        }
        
        // 学校管理员只能管理自己学校
        if ("school_admin".equals(role) && userSchoolId != null && userSchoolId.equals(targetSchoolId)) {
            return true;
        }
        
        return false;
    }

    // 获取学校列表
    @GetMapping("/schools")
    public Map<String, Object> getSchools(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> userInfo = getCurrentUserInfo(request);
        String role = (String) userInfo.get("role");
        Long schoolId = (Long) userInfo.get("schoolId");
        
        if ("super_admin".equals(role)) {
            // 超级管理员可以看到所有学校
            response.put("success", true);
            response.put("schools", schoolService.list());
        } else if ("school_admin".equals(role) && schoolId != null) {
            // 学校管理员只能看到自己学校
            School school = schoolService.getById(schoolId);
            response.put("success", true);
            response.put("schools", school != null ? List.of(school) : List.of());
        } else {
            response.put("success", false);
            response.put("message", "无权限访问");
        }
        return response;
    }

    // 获取学校用户列表
    @GetMapping("/users/{schoolId}")
    public Map<String, Object> getUsersBySchool(@PathVariable Long schoolId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        if (!validateSchoolAdminPermission(request, schoolId)) {
            response.put("success", false);
            response.put("message", "无权限访问");
            return response;
        }
        
        List<User> users = userService.getUsersBySchoolId(schoolId);
        response.put("success", true);
        response.put("users", users);
        return response;
    }

    // 创建学校管理员
    @PostMapping("/create-admin")
    public Map<String, Object> createAdmin(@RequestBody User user, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        if (!validateSchoolAdminPermission(request, user.getSchoolId())) {
            response.put("success", false);
            response.put("message", "无权限创建管理员");
            return response;
        }
        
        if (userService.getUserByUsername(user.getUsername()) != null) {
            response.put("success", false);
            response.put("message", "用户名已存在");
            return response;
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("school_admin");
        userService.save(user);

        response.put("success", true);
        response.put("message", "学校管理员创建成功");
        return response;
    }

    // 更新用户角色
    @PutMapping("/update-role")
    public Map<String, Object> updateRole(@RequestBody Map<String, Object> data, HttpServletRequest request) {
        Long userId = (Long) data.get("userId");
        String role = (String) data.get("role");

        User user = userService.getById(userId);
        if (user == null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户不存在");
            return response;
        }
        
        if (!validateSchoolAdminPermission(request, user.getSchoolId())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "无权限更新角色");
            return response;
        }

        user.setRole(role);
        userService.updateById(user);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "角色更新成功");
        return response;
    }

    // 删除用户
    @DeleteMapping("/delete-user/{userId}")
    public Map<String, Object> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        User user = userService.getById(userId);
        if (user == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return response;
        }
        
        if (!validateSchoolAdminPermission(request, user.getSchoolId())) {
            response.put("success", false);
            response.put("message", "无权限删除用户");
            return response;
        }
        
        userService.removeById(userId);
        response.put("success", true);
        response.put("message", "用户删除成功");
        return response;
    }
    
    // 更新用户
    @PutMapping("/update-user")
    public Map<String, Object> updateUser(@RequestBody User user, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        if (!validateSchoolAdminPermission(request, user.getSchoolId())) {
            response.put("success", false);
            response.put("message", "无权限更新用户");
            return response;
        }
        
        User existingUser = userService.getById(user.getId());
        if (existingUser == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return response;
        }
        
        // 只更新非密码字段，密码需要单独处理
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setRole(user.getRole());
        existingUser.setSchoolId(user.getSchoolId());
        
        userService.updateById(existingUser);
        response.put("success", true);
        response.put("message", "用户更新成功");
        return response;
    }
}