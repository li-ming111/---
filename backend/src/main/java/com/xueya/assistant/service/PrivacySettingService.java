package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.PrivacySetting;

public interface PrivacySettingService extends IService<PrivacySetting> {
    PrivacySetting getPrivacySettingByUserId(Long userId);
    void updatePrivacySetting(PrivacySetting setting);
}
