package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.UserProfile;

public interface UserProfileService extends IService<UserProfile> {
    UserProfile getUserProfileByUserId(Long userId);
    void updateUserProfile(UserProfile userProfile);
}