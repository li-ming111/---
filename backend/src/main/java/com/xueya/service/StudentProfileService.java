package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.StudentProfile;

public interface StudentProfileService extends IService<StudentProfile> {
    StudentProfile getStudentProfileByUserId(Long userId);
}