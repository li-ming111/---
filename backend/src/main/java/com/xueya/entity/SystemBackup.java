package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("system_backup")
public class SystemBackup {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 备份名称
     */
    private String backupName;
    
    /**
     * 备份类型：auto-自动, manual-手动
     */
    private String backupType;
    
    /**
     * 备份大小（字节）
     */
    private Long backupSize;
    
    /**
     * 备份文件路径
     */
    private String filePath;
    
    /**
     * 备份描述
     */
    private String description;
    
    /**
     * 状态：success-成功, failed-失败
     */
    private String status;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
