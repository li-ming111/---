package com.xueya.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    // 系统配置存储（实际生产环境应使用数据库或配置文件）
    private Map<String, Object> systemConfig = new HashMap<>();
    
    // 备份存储目录
    private static final String BACKUP_DIR = "backups";
    
    // 初始化备份目录
    public SystemController() {
        File backupDir = new File(BACKUP_DIR);
        if (!backupDir.exists()) {
            backupDir.mkdirs();
        }
    }

    // 保存系统配置
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/config")
    public Map<String, Object> saveSystemConfig(@RequestBody Map<String, Object> config) {
        systemConfig.putAll(config);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "系统配置保存成功");
        return response;
    }

    // 获取系统配置
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/config")
    public Map<String, Object> getSystemConfig() {
        return systemConfig;
    }

    // 创建数据备份
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/backup")
    public Map<String, Object> createBackup(@RequestBody Map<String, String> backupInfo) {
        Map<String, Object> response = new HashMap<>();
        try {
            String backupName = backupInfo.get("name");
            String description = backupInfo.get("description");
            
            // 生成备份文件名
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = sdf.format(new Date());
            String fileName = backupName + "_" + timestamp + ".bak";
            Path backupPath = Paths.get(BACKUP_DIR, fileName);
            
            // 模拟数据库备份（实际项目中应实现真实的数据库备份逻辑）
            try (FileWriter writer = new FileWriter(backupPath.toFile())) {
                writer.write("# 数据库备份文件\n");
                writer.write("备份时间: " + new Date().toString() + "\n");
                writer.write("备份名称: " + backupName + "\n");
                writer.write("描述: " + description + "\n");
                writer.write("# 备份数据\n");
                writer.write("-- 这里是模拟的数据库备份数据\n");
            }
            
            // 记录备份信息
            Map<String, Object> backupRecord = new HashMap<>();
            backupRecord.put("id", UUID.randomUUID().toString());
            backupRecord.put("name", backupName);
            backupRecord.put("description", description);
            backupRecord.put("fileName", fileName);
            backupRecord.put("createTime", new Date().toString());
            backupRecord.put("size", backupPath.toFile().length() + " bytes");
            
            response.put("success", true);
            response.put("message", "备份创建成功");
            response.put("backup", backupRecord);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "备份创建失败: " + e.getMessage());
        }
        return response;
    }
    
    // 获取备份列表
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/backups")
    public List<Map<String, Object>> getBackups() {
        List<Map<String, Object>> backups = new ArrayList<>();
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(BACKUP_DIR), "*.bak");
            for (Path path : stream) {
                File file = path.toFile();
                Map<String, Object> backup = new HashMap<>();
                backup.put("id", UUID.randomUUID().toString());
                // 使用简单的字符串处理方法避免正则表达式转义问题
                String fileName = file.getName();
                int lastUnderscoreIndex = fileName.lastIndexOf("_");
                if (lastUnderscoreIndex != -1) {
                    String backupName = fileName.substring(0, lastUnderscoreIndex);
                    backup.put("name", backupName);
                } else {
                    backup.put("name", fileName.replace(".bak", ""));
                }
                backup.put("fileName", fileName);
                backup.put("createTime", new Date(file.lastModified()).toString());
                backup.put("size", file.length() + " bytes");
                backups.add(backup);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backups;
    }
    
    // 恢复备份
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/backup/restore/{fileName}")
    public Map<String, Object> restoreBackup(@PathVariable String fileName) {
        Map<String, Object> response = new HashMap<>();
        try {
            Path backupPath = Paths.get(BACKUP_DIR, fileName);
            if (!Files.exists(backupPath)) {
                response.put("success", false);
                response.put("message", "备份文件不存在");
                return response;
            }
            
            // 模拟备份恢复（实际项目中应实现真实的数据库恢复逻辑）
            response.put("success", true);
            response.put("message", "备份恢复成功");
            response.put("backupName", fileName);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "备份恢复失败: " + e.getMessage());
        }
        return response;
    }
    
    // 删除备份
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/backup/{fileName}")
    public Map<String, Object> deleteBackup(@PathVariable String fileName) {
        Map<String, Object> response = new HashMap<>();
        try {
            Path backupPath = Paths.get(BACKUP_DIR, fileName);
            if (!Files.exists(backupPath)) {
                response.put("success", false);
                response.put("message", "备份文件不存在");
                return response;
            }
            
            Files.delete(backupPath);
            response.put("success", true);
            response.put("message", "备份删除成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "备份删除失败: " + e.getMessage());
        }
        return response;
    }

    // 获取系统状态
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/status")
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // 模拟在线用户数
        status.put("onlineUsers", (int)(Math.random() * 200) + 50);
        
        // 模拟系统负载
        double load = Math.random() * 100;
        status.put("systemLoad", String.format("%.1f%%", load));
        status.put("loadLevel", load < 30 ? "低" : (load < 70 ? "中" : "高"));
        
        // 模拟内存使用情况
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double memoryUsage = (double) usedMemory / maxMemory * 100;
        status.put("memoryUsage", String.format("%.1f%%", memoryUsage));
        status.put("memoryDetails", String.format("已用: %.2fMB / 总: %.2fMB", 
            usedMemory / 1024.0 / 1024.0, maxMemory / 1024.0 / 1024.0));
        
        // 模拟磁盘空间使用情况
        File root = new File("/");
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        double diskUsage = (double) usedSpace / totalSpace * 100;
        status.put("storageUsage", String.format("%.1f%% 已使用", diskUsage));
        status.put("diskDetails", String.format("已用: %.2fGB / 总: %.2fGB", 
            usedSpace / 1024.0 / 1024.0 / 1024.0, totalSpace / 1024.0 / 1024.0 / 1024.0));
        
        // 数据库状态
        status.put("databaseStatus", "正常");
        
        // 系统启动时间
        status.put("serverTime", new Date().toString());
        status.put("uptime", getUptime());
        
        // JVM信息
        status.put("jvmVersion", System.getProperty("java.version"));
        status.put("osName", System.getProperty("os.name"));
        status.put("osVersion", System.getProperty("os.version"));
        
        return status;
    }
    
    // 获取系统运行时间
    private String getUptime() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long days = uptime / (1000 * 60 * 60 * 24);
        long hours = (uptime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        return String.format("%d天 %d小时 %d分钟", days, hours, minutes);
    }
}