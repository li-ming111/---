package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.User;
import com.xueya.mapper.UserMapper;
import com.xueya.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

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
    public List<User> getUsersBySchoolId(Long schoolId) {
        return baseMapper.selectList(new QueryWrapper<User>().eq("school_id", schoolId));
    }
}