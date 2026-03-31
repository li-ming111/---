package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.SystemBackup;
import com.xueya.entity.SystemSetting;
import com.xueya.mapper.SystemBackupMapper;
import com.xueya.service.SystemBackupService;
import com.xueya.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemBackupServiceImpl extends ServiceImpl<SystemBackupMapper, SystemBackup> implements SystemBackupService {

    @Autowired
    private SystemSettingService systemSettingService;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    @Value("${spring.datasource.username}")
    private String datasourceUsername;

    @Value("${spring.datasource.password}")
    private String datasourcePassword;

    private static final String BACKUP_DIR = "backups/";

    @Override
    public SystemBackup createManualBackup() {
        return createBackup("manual");
    }

    @Override
    public SystemBackup createAutoBackup() {
        return createBackup("auto");
    }

    private SystemBackup createBackup(String backupType) {
        SystemBackup backup = new SystemBackup();
        String backupName = "backup_" + backupType + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        backup.setBackupName(backupName);
        backup.setBackupType(backupType);
        backup.setCreateTime(LocalDateTime.now());
        backup.setStatus("processing");
        
        // 保存初始记录
        save(backup);
        
        try {
            // 创建备份目录
            File backupDir = new File(BACKUP_DIR);
            if (!backupDir.exists()) {
                backupDir.mkdirs();
            }
            
            // 解析数据库连接信息
            String dbName = parseDatabaseName(datasourceUrl);
            String backupFilePath = BACKUP_DIR + backupName + ".sql";
            
            // 执行mysqldump命令
            ProcessBuilder processBuilder = new ProcessBuilder(
                "mysqldump",
                "-u" + datasourceUsername,
                "-p" + datasourcePassword,
                "--databases",
                dbName,
                "-r",
                backupFilePath
            );
            
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                // 备份成功
                File backupFile = new File(backupFilePath);
                backup.setBackupSize(backupFile.length());
                backup.setFilePath(backupFilePath);
                backup.setStatus("success");
                backup.setDescription("数据库备份成功");
            } else {
                // 备份失败
                backup.setStatus("failed");
                backup.setErrorMessage("备份命令执行失败，退出码：" + exitCode);
            }
            
        } catch (Exception e) {
            backup.setStatus("failed");
            backup.setErrorMessage(e.getMessage());
            e.printStackTrace();
        }
        
        updateById(backup);
        return backup;
    }

    @Override
    public boolean restoreBackup(Long id) {
        SystemBackup backup = getById(id);
        if (backup == null || !"success".equals(backup.getStatus())) {
            return false;
        }
        
        try {
            String backupFilePath = backup.getFilePath();
            File backupFile = new File(backupFilePath);
            
            if (!backupFile.exists()) {
                return false;
            }
            
            // 执行mysql命令恢复
            ProcessBuilder processBuilder = new ProcessBuilder(
                "mysql",
                "-u" + datasourceUsername,
                "-p" + datasourcePassword,
                "-e",
                "source " + backupFilePath
            );
            
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            
            return exitCode == 0;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteBackup(Long id) {
        SystemBackup backup = getById(id);
        if (backup == null) {
            return false;
        }
        
        // 删除备份文件
        if (backup.getFilePath() != null) {
            File backupFile = new File(backup.getFilePath());
            if (backupFile.exists()) {
                backupFile.delete();
            }
        }
        
        // 删除数据库记录
        return removeById(id);
    }

    @Override
    public Map<String, Object> getBackupSettings() {
        Map<String, Object> settings = new HashMap<>();
        
        settings.put("autoBackup", "true".equals(systemSettingService.getSettingValue("backup.auto_backup", "true")));
        settings.put("backupFrequency", systemSettingService.getSettingValue("backup.backup_frequency", "daily"));
        settings.put("backupTime", systemSettingService.getSettingValue("backup.backup_time", "02:00"));
        settings.put("backupRetention", Integer.parseInt(systemSettingService.getSettingValue("backup.backup_retention", "10")));
        
        return settings;
    }

    @Override
    public boolean saveBackupSettings(Map<String, Object> settings) {
        try {
            systemSettingService.saveSetting("backup.auto_backup", String.valueOf(settings.get("autoBackup")), "是否开启自动备份");
            systemSettingService.saveSetting("backup.backup_frequency", (String) settings.get("backupFrequency"), "备份频率");
            systemSettingService.saveSetting("backup.backup_time", (String) settings.get("backupTime"), "备份时间");
            systemSettingService.saveSetting("backup.backup_retention", String.valueOf(settings.get("backupRetention")), "备份保留数量");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void cleanExpiredBackups(int retentionDays) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        
        QueryWrapper<SystemBackup> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("create_time", cutoffDate);
        
        List<SystemBackup> expiredBackups = list(queryWrapper);
        
        for (SystemBackup backup : expiredBackups) {
            deleteBackup(backup.getId());
        }
    }

    private String parseDatabaseName(String url) {
        // 解析jdbc:mysql://localhost:3306/dbname?param=value
        if (url != null && url.contains("/")) {
            String[] parts = url.split("/");
            if (parts.length > 0) {
                String lastPart = parts[parts.length - 1];
                if (lastPart.contains("?")) {
                    return lastPart.substring(0, lastPart.indexOf("?"));
                }
                return lastPart;
            }
        }
        return "xueya";
    }
}
