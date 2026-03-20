package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("offline_data")
public class OfflineData {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String dataType;
    private String dataContent;
    private String syncStatus;
    private LocalDateTime offlineTime;
    private LocalDateTime syncTime;
    private String deviceId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}