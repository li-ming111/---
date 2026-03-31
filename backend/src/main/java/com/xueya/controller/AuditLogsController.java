package com.xueya.controller;

import com.xueya.entity.AuditLog;
import com.xueya.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/audit-logs", produces = "application/json; charset=utf-8")
public class AuditLogsController {

    @Autowired
    private AuditLogService auditLogService;

    // 获取审计日志列表
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/list")
    public Map<String, Object> getAuditLogs(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String adminName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        List<AuditLog> logs = auditLogService.getAuditLogs(startDate, endDate, operationType, adminName, page, size);
        long total = auditLogService.getAuditLogCount(startDate, endDate, operationType, adminName);
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", logs);
        result.put("total", total);
        
        return result;
    }

    // 导出审计日志
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PostMapping("/export")
    public ResponseEntity<byte[]> exportLogs(@RequestBody Map<String, Object> params) {
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String operationType = (String) params.get("operationType");
        String adminName = (String) params.get("adminName");
        
        String csvContent = auditLogService.exportAuditLogs(startDate, endDate, operationType, adminName);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv; charset=UTF-8"));
        headers.setContentDispositionFormData("attachment", "audit_logs.csv");
        headers.add("Content-Type", "text/csv; charset=utf-8");
        
        // 添加BOM以支持Excel中文显示
        byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);
        byte[] content = new byte[bom.length + csvBytes.length];
        System.arraycopy(bom, 0, content, 0, bom.length);
        System.arraycopy(csvBytes, 0, content, bom.length, csvBytes.length);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(content);
    }

    // 下载导出的日志文件
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadLogs(@PathVariable String fileName) {
        // 这里可以实现从文件系统读取已导出的日志文件
        // 简化处理，返回空内容
        return ResponseEntity.ok().body(new byte[0]);
    }

    // 记录操作日志（内部使用）
    public void logOperation(String operationType, String operationContent) {
        auditLogService.logOperation(operationType, operationContent);
    }
}
