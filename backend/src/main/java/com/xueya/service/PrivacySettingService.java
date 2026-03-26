package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.PrivacySetting;

public interface PrivacySettingService extends IService<PrivacySetting> {
    PrivacySetting getByUserId(Long userId);
}
