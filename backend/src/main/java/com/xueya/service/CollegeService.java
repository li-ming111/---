package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.College;

import java.util.List;

public interface CollegeService extends IService<College> {
    List<College> getAllColleges();
    College getCollegeById(Long id);
}