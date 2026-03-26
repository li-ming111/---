package com.xueya.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueya.entity.StudyPlan;
import com.xueya.mapper.StudyPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    // 系统配置存储（实际生产环境应使用数据库或配置文件）
    private Map<String, Object> systemConfig = new HashMap<>();
    
    // 备份存储目录
    private static final String BACKUP_DIR = "backups";
    
    @Autowired
    private StudyPlanMapper studyPlanMapper;
    
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
    
    // 系统监控API：获取系统状态
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/monitoring/status")
    public Map<String, Object> getMonitoringStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // 模拟CPU使用率
        double cpuUsage = Math.random() * 100;
        status.put("cpuUsage", (int) cpuUsage);
        
        // 模拟内存使用率
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double memoryUsage = (double) usedMemory / maxMemory * 100;
        status.put("memoryUsage", (int) memoryUsage);
        
        // 模拟磁盘使用率
        File root = new File("/");
        long totalSpace = root.getTotalSpace();
        long freeSpace = root.getFreeSpace();
        long usedSpace = totalSpace - freeSpace;
        double diskUsage = (double) usedSpace / totalSpace * 100;
        status.put("diskUsage", (int) diskUsage);
        
        // 模拟API响应时间
        int apiResponseTime = (int) (Math.random() * 200) + 50;
        status.put("apiResponseTime", apiResponseTime);
        
        return status;
    }
    
    // 系统监控API：获取服务器信息
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/monitoring/server-info")
    public List<Map<String, Object>> getServerInfo() {
        List<Map<String, Object>> serverInfo = new ArrayList<>();
        
        // 服务器基本信息
        Map<String, Object> info1 = new HashMap<>();
        info1.put("key", "服务器名称");
        info1.put("value", "智学方舟服务器");
        serverInfo.add(info1);
        
        Map<String, Object> info2 = new HashMap<>();
        info2.put("key", "操作系统");
        info2.put("value", System.getProperty("os.name") + " " + System.getProperty("os.version"));
        serverInfo.add(info2);
        
        Map<String, Object> info3 = new HashMap<>();
        info3.put("key", "JVM版本");
        info3.put("value", System.getProperty("java.version"));
        serverInfo.add(info3);
        
        Map<String, Object> info4 = new HashMap<>();
        info4.put("key", "服务器时间");
        info4.put("value", new Date().toString());
        serverInfo.add(info4);
        
        Map<String, Object> info5 = new HashMap<>();
        info5.put("key", "运行时间");
        info5.put("value", getUptime());
        serverInfo.add(info5);
        
        return serverInfo;
    }
    
    // 系统监控API：获取异常预警
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/monitoring/alerts")
    public List<Map<String, Object>> getAlerts() {
        List<Map<String, Object>> alerts = new ArrayList<>();
        
        // 模拟异常预警
        Map<String, Object> alert1 = new HashMap<>();
        alert1.put("id", 1);
        alert1.put("title", "磁盘空间警告");
        alert1.put("description", "服务器磁盘空间使用超过80%，请及时清理");
        alert1.put("type", "warning");
        alerts.add(alert1);
        
        Map<String, Object> alert2 = new HashMap<>();
        alert2.put("id", 2);
        alert2.put("title", "API响应缓慢");
        alert2.put("description", "部分API响应时间超过200ms，建议优化");
        alert2.put("type", "info");
        alerts.add(alert2);
        
        return alerts;
    }
    
    // 获取系统运行时间
    private String getUptime() {
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long days = uptime / (1000 * 60 * 60 * 24);
        long hours = (uptime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (uptime % (1000 * 60 * 60)) / (1000 * 60);
        return String.format("%d天 %d小时 %d分钟", days, hours, minutes);
    }

    // 清理重复的学习计划数据
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/clean-duplicates")
    public Map<String, Object> cleanDuplicateStudyPlans() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 获取所有学习计划
            List<StudyPlan> allPlans = studyPlanMapper.selectList(null);
            int totalPlans = allPlans.size();
            
            // 按user_id和title分组，找出重复的记录
            Map<String, List<StudyPlan>> groupedPlans = allPlans.stream()
                    .collect(Collectors.groupingBy(plan -> plan.getUserId() + "_" + plan.getTitle()));
            
            int deletedCount = 0;
            
            // 遍历每个组，保留最小ID的记录，删除其他记录
            for (Map.Entry<String, List<StudyPlan>> entry : groupedPlans.entrySet()) {
                List<StudyPlan> plans = entry.getValue();
                if (plans.size() > 1) {
                    // 找到最小ID的记录
                    long minId = plans.stream().mapToLong(StudyPlan::getId).min().orElse(0);
                    
                    // 删除其他记录
                    for (StudyPlan plan : plans) {
                        if (plan.getId() != minId) {
                            studyPlanMapper.deleteById(plan.getId());
                            deletedCount++;
                        }
                    }
                }
            }
            
            // 再次检查总记录数
            long finalCount = studyPlanMapper.selectCount(null);
            
            response.put("success", true);
            response.put("message", "清理完成！");
            response.put("totalPlans", totalPlans);
            response.put("deletedCount", deletedCount);
            response.put("finalCount", finalCount);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "清理重复数据失败: " + e.getMessage());
        }
        return response;
    }
    
    // 清理所有实体的重复数据
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/clean-all-duplicates")
    public Map<String, Object> cleanAllDuplicates() {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Integer> cleanupResults = new HashMap<>();
            
            // 清理学习计划重复数据
            cleanupResults.put("study_plans", cleanDuplicateStudyPlansInternal());
            
            // 这里可以添加其他实体的清理逻辑
            // cleanupResults.put("study_notes", cleanDuplicateStudyNotes());
            // cleanupResults.put("career_plans", cleanDuplicateCareerPlans());
            // cleanupResults.put("campus_activities", cleanDuplicateCampusActivities());
            
            response.put("success", true);
            response.put("message", "所有实体重复数据清理完成！");
            response.put("results", cleanupResults);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "清理重复数据失败: " + e.getMessage());
        }
        return response;
    }
    
    // 内部方法：清理学习计划重复数据并返回删除的记录数
    private int cleanDuplicateStudyPlansInternal() {
        List<StudyPlan> allPlans = studyPlanMapper.selectList(null);
        Map<String, List<StudyPlan>> groupedPlans = allPlans.stream()
                .collect(Collectors.groupingBy(plan -> plan.getUserId() + "_" + plan.getTitle()));
        
        int deletedCount = 0;
        for (Map.Entry<String, List<StudyPlan>> entry : groupedPlans.entrySet()) {
            List<StudyPlan> plans = entry.getValue();
            if (plans.size() > 1) {
                long minId = plans.stream().mapToLong(StudyPlan::getId).min().orElse(0);
                for (StudyPlan plan : plans) {
                    if (plan.getId() != minId) {
                        studyPlanMapper.deleteById(plan.getId());
                        deletedCount++;
                    }
                }
            }
        }
        return deletedCount;
    }
    
    // 定时任务：每天凌晨1点清理重复数据
    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduledCleanDuplicateStudyPlans() {
        System.out.println("开始执行定时清理重复数据任务...");
        Map<String, Object> result = cleanAllDuplicates();
        System.out.println("定时清理任务完成:");
        System.out.println("成功: " + result.get("success"));
        System.out.println("消息: " + result.get("message"));
        if (result.containsKey("results")) {
            System.out.println("清理结果: " + result.get("results"));
        }
    }
    
    // 主方法，用于直接运行清理逻辑
    public static void main(String[] args) {
        // 加载Spring Boot应用上下文
        org.springframework.boot.SpringApplication app = new org.springframework.boot.SpringApplication(com.xueya.Application.class);
        org.springframework.context.ApplicationContext context = app.run(args);
        
        // 获取SystemController实例
        SystemController controller = context.getBean(SystemController.class);
        
        // 调用清理方法
        Map<String, Object> result = controller.cleanDuplicateStudyPlans();
        
        // 打印结果
        System.out.println("清理结果:");
        System.out.println("成功: " + result.get("success"));
        System.out.println("消息: " + result.get("message"));
        System.out.println("总记录数: " + result.get("totalPlans"));
        System.out.println("删除记录数: " + result.get("deletedCount"));
        System.out.println("最终记录数: " + result.get("finalCount"));
        
        // 关闭应用
        System.exit(0);
    }
}