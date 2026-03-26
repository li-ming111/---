package com.xueya.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/system/monitoring")
public class SystemMonitoringController {

    // 获取系统状态
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status")
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // 模拟系统状态数据
        status.put("cpuUsage", Math.floor(Math.random() * 30) + 30);
        status.put("memoryUsage", Math.floor(Math.random() * 20) + 60);
        status.put("diskUsage", Math.floor(Math.random() * 10) + 30);
        status.put("apiResponseTime", Math.floor(Math.random() * 100) + 100);
        
        return status;
    }

    // 获取服务器信息
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/server-info")
    public List<Map<String, String>> getServerInfo() {
        List<Map<String, String>> serverInfo = new ArrayList<>();
        
        serverInfo.add(createServerInfoItem("服务器名称", "server-001"));
        serverInfo.add(createServerInfoItem("IP地址", "192.168.1.100"));
        serverInfo.add(createServerInfoItem("操作系统", "Linux Ubuntu 20.04 LTS"));
        serverInfo.add(createServerInfoItem("CPU型号", "Intel Core i7-10700"));
        serverInfo.add(createServerInfoItem("内存大小", "32GB"));
        serverInfo.add(createServerInfoItem("磁盘容量", "1TB SSD"));
        serverInfo.add(createServerInfoItem("系统启动时间", new java.util.Date().toLocaleString()));
        serverInfo.add(createServerInfoItem("运行时间", "0天 0小时 0分钟"));
        
        return serverInfo;
    }

    // 获取异常预警
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/alerts")
    public List<Map<String, Object>> getAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        
        // 模拟异常预警数据
        // 实际项目中，这里应该从数据库或监控系统获取真实的异常预警
        
        return alerts;
    }

    // 辅助方法：创建服务器信息项
    private Map<String, String> createServerInfoItem(String key, String value) {
        Map<String, String> item = new HashMap<>();
        item.put("key", key);
        item.put("value", value);
        return item;
    }
}
