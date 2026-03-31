package com.xueya.controller;

import com.xueya.entity.SystemBackup;
import com.xueya.service.SystemBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/backup", produces = "application/json; charset=utf-8")
public class BackupRestoreController {

    @Autowired
    private SystemBackupService systemBackupService;

    // 获取备份设置
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/settings")
    public Map<String, Object> getBackupSettings() {
        return systemBackupService.getBackupSettings();
    }

    // 保存备份设置
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/settings")
    public boolean saveBackupSettings(@RequestBody Map<String, Object> settings) {
        return systemBackupService.saveBackupSettings(settings);
    }

    // 创建手动备份
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/create")
    public SystemBackup createBackup() {
        return systemBackupService.createManualBackup();
    }

    // 获取备份列表
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/list")
    public List<SystemBackup> getBackups() {
        return systemBackupService.list();
    }

    // 恢复备份
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/restore/{id}")
    public boolean restoreBackup(@PathVariable Long id) {
        return systemBackupService.restoreBackup(id);
    }

    // 删除备份
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public boolean deleteBackup(@PathVariable Long id) {
        return systemBackupService.deleteBackup(id);
    }
}
