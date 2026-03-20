package com.xueya.controller;

import com.xueya.entity.OperationLog;
import com.xueya.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {
    @Autowired
    private OperationLogService operationLogService;
    
    // 获取学校操作日志
    @PreAuthorize("hasRole('SCHOOL_ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/school/{schoolId}")
    public List<OperationLog> getSchoolLogs(@PathVariable Long schoolId) {
        return operationLogService.getLogsBySchoolId(schoolId);
    }
    
    // 获取系统所有操作日志（超级管理员）
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/system")
    public List<OperationLog> getSystemLogs() {
        return operationLogService.list();
    }
    
    // 根据用户ID获取操作日志
    @PreAuthorize("hasRole('SCHOOL_ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/user/{userId}")
    public List<OperationLog> getUserLogs(@PathVariable Long userId) {
        return operationLogService.getLogsByUserId(userId);
    }
}