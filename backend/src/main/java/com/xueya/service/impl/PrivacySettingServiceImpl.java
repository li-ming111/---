package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.PrivacySetting;
import com.xueya.mapper.PrivacySettingMapper;
import com.xueya.service.PrivacySettingService;
import org.springframework.stereotype.Service;

@Service
public class PrivacySettingServiceImpl extends ServiceImpl<PrivacySettingMapper, PrivacySetting> implements PrivacySettingService {
    @Override
    public PrivacySetting getByUserId(Long userId) {
        QueryWrapper<PrivacySetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.getOne(queryWrapper);
    }
}
