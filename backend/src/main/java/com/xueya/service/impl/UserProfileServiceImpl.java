package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.UserProfile;
import com.xueya.mapper.UserProfileMapper;
import com.xueya.service.UserProfileService;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {
    @Override
    public UserProfile getUserProfileByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserProfile>().eq("user_id", userId));
    }

    @Override
    public void updateUserProfile(UserProfile userProfile) {
        baseMapper.updateById(userProfile);
    }
}