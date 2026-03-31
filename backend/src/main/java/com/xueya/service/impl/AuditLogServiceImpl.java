package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.AuditLog;
import com.xueya.mapper.AuditLogMapper;
import com.xueya.service.AuditLogService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {

    @Override
    public void logOperation(String operationType, String operationContent) {
        AuditLog auditLog = new AuditLog();
        
        auditLog.setOperationType(operationType);
        auditLog.setOperationContent(operationContent);
        auditLog.setIpAddress("unknown");
        auditLog.setUserAgent("unknown");
        auditLog.setRequestUrl("unknown");
        auditLog.setRequestMethod("unknown");
        auditLog.setCreateTime(LocalDateTime.now());
        
        save(auditLog);
    }

    @Override
    public List<AuditLog> getAuditLogs(String startDate, String endDate, String operationType, String adminName, int page, int size) {
        QueryWrapper<AuditLog> queryWrapper = buildQueryWrapper(startDate, endDate, operationType, adminName);
        
        Page<AuditLog> pageParam = new Page<>(page, size);
        Page<AuditLog> resultPage = baseMapper.selectPage(pageParam, queryWrapper);
        
        return resultPage.getRecords();
    }

    @Override
    public long getAuditLogCount(String startDate, String endDate, String operationType, String adminName) {
        QueryWrapper<AuditLog> queryWrapper = buildQueryWrapper(startDate, endDate, operationType, adminName);
        return count(queryWrapper);
    }

    @Override
    public String exportAuditLogs(String startDate, String endDate, String operationType, String adminName) {
        QueryWrapper<AuditLog> queryWrapper = buildQueryWrapper(startDate, endDate, operationType, adminName);
        List<AuditLog> logs = baseMapper.selectList(queryWrapper);
        
        // 生成CSV内容
        StringBuilder csv = new StringBuilder();
        csv.append("ID,管理员,操作类型,操作内容,IP地址,请求URL,请求方法,操作时间\n");
        
        for (AuditLog log : logs) {
            csv.append(log.getId()).append(",");
            csv.append(escapeCsv(log.getAdminName())).append(",");
            csv.append(escapeCsv(log.getOperationType())).append(",");
            csv.append(escapeCsv(log.getOperationContent())).append(",");
            csv.append(escapeCsv(log.getIpAddress())).append(",");
            csv.append(escapeCsv(log.getRequestUrl())).append(",");
            csv.append(escapeCsv(log.getRequestMethod())).append(",");
            csv.append(log.getCreateTime()).append("\n");
        }
        
        return csv.toString();
    }

    private QueryWrapper<AuditLog> buildQueryWrapper(String startDate, String endDate, String operationType, String adminName) {
        QueryWrapper<AuditLog> queryWrapper = new QueryWrapper<>();
        
        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge("create_time", startDate);
        }
        
        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le("create_time", endDate);
        }
        
        if (operationType != null && !operationType.isEmpty()) {
            queryWrapper.eq("operation_type", operationType);
        }
        
        if (adminName != null && !adminName.isEmpty()) {
            queryWrapper.like("admin_name", adminName);
        }
        
        queryWrapper.orderByDesc("create_time");
        
        return queryWrapper;
    }



    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
