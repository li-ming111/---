package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.SystemSetting;

public interface SystemSettingService extends IService<SystemSetting> {
    
    /**
     * 根据key获取设置值
     */
    String getSettingValue(String key, String defaultValue);
    
    /**
     * 保存设置
     */
    boolean saveSetting(String key, String value, String description);
    
    /**
     * 根据key获取设置
     */
    SystemSetting getSettingByKey(String key);
}
