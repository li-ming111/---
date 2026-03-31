package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.SystemBackup;

import java.util.List;
import java.util.Map;

public interface SystemBackupService extends IService<SystemBackup> {
    
    /**
     * 创建手动备份
     */
    SystemBackup createManualBackup();
    
    /**
     * 创建自动备份
     */
    SystemBackup createAutoBackup();
    
    /**
     * 恢复备份
     */
    boolean restoreBackup(Long id);
    
    /**
     * 删除备份
     */
    boolean deleteBackup(Long id);
    
    /**
     * 获取备份设置
     */
    Map<String, Object> getBackupSettings();
    
    /**
     * 保存备份设置
     */
    boolean saveBackupSettings(Map<String, Object> settings);
    
    /**
     * 清理过期备份
     */
    void cleanExpiredBackups(int retentionDays);
}
