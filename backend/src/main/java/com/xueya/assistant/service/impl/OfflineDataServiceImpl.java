package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.OfflineData;
import com.xueya.mapper.OfflineDataMapper;
import com.xueya.assistant.service.OfflineDataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfflineDataServiceImpl extends ServiceImpl<OfflineDataMapper, OfflineData> implements OfflineDataService {

    @Override
    public List<OfflineData> getOfflineDataByUserId(Long userId) {
        QueryWrapper<OfflineData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<OfflineData> getUnsyncedOfflineData(Long userId) {
        QueryWrapper<OfflineData> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("sync_status", "未同步");
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }
}
