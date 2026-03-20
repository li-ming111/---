package com.xueya.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.College;
import com.xueya.mapper.CollegeMapper;
import com.xueya.service.CollegeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {

    @Override
    public List<College> getAllColleges() {
        return baseMapper.selectList(null);
    }

    @Override
    public College getCollegeById(Long id) {
        return baseMapper.selectById(id);
    }
}