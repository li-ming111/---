package com.xueya.service;

import java.util.Map;

public interface SystemMonitoringService {
    
    /**
     * 获取CPU使用率
     */
    double getCpuUsage();
    
    /**
     * 获取内存使用率
     */
    double getMemoryUsage();
    
    /**
     * 获取磁盘使用率
     */
    double getDiskUsage();
    
    /**
     * 获取系统运行时间
     */
    long getUptime();
    
    /**
     * 获取系统负载
     */
    Map<String, Object> getSystemLoad();
    
    /**
     * 获取JVM内存信息
     */
    Map<String, Object> getJvmMemoryInfo();
    
    /**
     * 获取线程信息
     */
    Map<String, Object> getThreadInfo();
}
