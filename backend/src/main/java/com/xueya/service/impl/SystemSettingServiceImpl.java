package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.SystemSetting;
import com.xueya.mapper.SystemSettingMapper;
import com.xueya.service.SystemSettingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class SystemSettingServiceImpl extends ServiceImpl<SystemSettingMapper, SystemSetting> implements SystemSettingService {

    @Override
    public String getSettingValue(String key, String defaultValue) {
        QueryWrapper<SystemSetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setting_key", key);
        SystemSetting setting = baseMapper.selectOne(queryWrapper);
        return setting != null ? setting.getSettingValue() : defaultValue;
    }

    @Override
    public boolean saveSetting(String key, String value, String description) {
        QueryWrapper<SystemSetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setting_key", key);
        SystemSetting existingSetting = baseMapper.selectOne(queryWrapper);
        
        if (existingSetting != null) {
            existingSetting.setSettingValue(value);
            existingSetting.setDescription(description);
            existingSetting.setUpdateTime(LocalDateTime.now());
            return updateById(existingSetting);
        } else {
            SystemSetting newSetting = new SystemSetting();
            newSetting.setSettingKey(key);
            newSetting.setSettingValue(value);
            newSetting.setDescription(description);
            newSetting.setCreateTime(LocalDateTime.now());
            newSetting.setUpdateTime(LocalDateTime.now());
            return save(newSetting);
        }
    }

    @Override
    public SystemSetting getSettingByKey(String key) {
        QueryWrapper<SystemSetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("setting_key", key);
        return baseMapper.selectOne(queryWrapper);
    }
}
