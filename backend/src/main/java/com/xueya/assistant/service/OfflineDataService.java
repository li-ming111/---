package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.OfflineData;

import java.util.List;

public interface OfflineDataService extends IService<OfflineData> {
    List<OfflineData> getOfflineDataByUserId(Long userId);
    List<OfflineData> getUnsyncedOfflineData(Long userId);
}
