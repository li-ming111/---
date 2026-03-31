package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.User;
import com.xueya.mapper.UserMapper;
import com.xueya.service.UserService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public User getUserByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("username", username));
    }

    @Override
    public User getUserByStudentId(String studentId) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("student_id", studentId));
    }

    @Override
    public User getUserByIdCard(String idCard) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("id_card", idCard));
    }

    @Override
    public User getUserByPhone(String phone) {
        return baseMapper.selectOne(new QueryWrapper<User>().eq("phone", phone));
    }

    @Override
    public List<User> getUsersBySchoolId(Long schoolId) {
        return baseMapper.selectList(new QueryWrapper<User>().eq("school_id", schoolId));
    }

    @Override
    public Map<String, Long> getUserCountByRole() {
        Map<String, Long> roleCounts = new HashMap<>();
        
        // 查询所有用户并按角色统计
        List<User> users = baseMapper.selectList(new QueryWrapper<>());
        
        for (User user : users) {
            String role = user.getRole();
            if (role != null) {
                String roleName = getRoleName(role);
                roleCounts.put(roleName, roleCounts.getOrDefault(roleName, 0L) + 1);
            }
        }
        
        return roleCounts;
    }

    private String getRoleName(String role) {
        switch (role) {
            case "ROLE_SUPER_ADMIN":
                return "超级管理员";
            case "ROLE_SCHOOL_ADMIN":
                return "学校管理员";
            case "ROLE_TEACHER":
                return "教师";
            case "ROLE_STUDENT":
                return "学生";
            default:
                return "其他";
        }
    }
}