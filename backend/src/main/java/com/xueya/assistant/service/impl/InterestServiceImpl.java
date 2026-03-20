package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.Interest;
import com.xueya.assistant.mapper.InterestMapper;
import com.xueya.assistant.service.InterestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterestServiceImpl extends ServiceImpl<InterestMapper, Interest> implements InterestService {

    @Override
    public List<Interest> getInterestsByCategory(String category) {
        QueryWrapper<Interest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        queryWrapper.orderByDesc("popularity");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Interest> getPopularInterests() {
        QueryWrapper<Interest> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("popularity");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Interest> searchInterests(String keyword) {
        QueryWrapper<Interest> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyword).or().like("description", keyword);
        queryWrapper.orderByDesc("popularity");
        return baseMapper.selectList(queryWrapper);
    }
}
