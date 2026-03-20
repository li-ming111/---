package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.OperationLog;

import java.util.List;

public interface OperationLogService extends IService<OperationLog> {
    List<OperationLog> getLogsBySchoolId(Long schoolId);
    List<OperationLog> getLogsByUserId(Long userId);
    List<OperationLog> getLogsByTimeRange(String startTime, String endTime);
}