package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.PrivacySetting;
import com.xueya.mapper.PrivacySettingMapper;
import com.xueya.assistant.service.PrivacySettingService;
import org.springframework.stereotype.Service;

@Service
public class PrivacySettingServiceImpl extends ServiceImpl<PrivacySettingMapper, PrivacySetting> implements PrivacySettingService {

    @Override
    public PrivacySetting getPrivacySettingByUserId(Long userId) {
        return baseMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PrivacySetting>()
                .eq(PrivacySetting::getUserId, userId)
        );
    }

    @Override
    public void updatePrivacySetting(PrivacySetting setting) {
        PrivacySetting existing = getPrivacySettingByUserId(setting.getUserId());
        if (existing != null) {
            setting.setId(existing.getId());
            updateById(setting);
        } else {
            save(setting);
        }
    }
}
