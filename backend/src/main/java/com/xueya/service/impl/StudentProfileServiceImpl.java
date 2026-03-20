package com.xueya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.StudentProfile;
import com.xueya.mapper.StudentProfileMapper;
import com.xueya.service.StudentProfileService;
import org.springframework.stereotype.Service;

@Service
public class StudentProfileServiceImpl extends ServiceImpl<StudentProfileMapper, StudentProfile> implements StudentProfileService {

    @Override
    public StudentProfile getStudentProfileByUserId(Long userId) {
        return baseMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<StudentProfile>()
                .eq(StudentProfile::getUserId, userId)
        );
    }
}