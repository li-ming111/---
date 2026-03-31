package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.AuditLog;

import java.util.List;
import java.util.Map;

public interface AuditLogService extends IService<AuditLog> {
    
    /**
     * 记录审计日志
     */
    void logOperation(String operationType, String operationContent);
    
    /**
     * 获取审计日志列表
     */
    List<AuditLog> getAuditLogs(String startDate, String endDate, String operationType, String adminName, int page, int size);
    
    /**
     * 获取审计日志总数
     */
    long getAuditLogCount(String startDate, String endDate, String operationType, String adminName);
    
    /**
     * 导出审计日志
     */
    String exportAuditLogs(String startDate, String endDate, String operationType, String adminName);
}
