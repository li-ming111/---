package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("privacy_setting")
public class PrivacySetting {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    @TableField("learning_record_visible")
    private String learningVisible;
    @TableField("checkin_data_visible")
    private String checkinVisible;
    @TableField("personal_info_visible")
    private String profileVisible;
    @TableField("data_export_enabled")
    private Boolean exportEnabled;
    private Boolean loginAlert;
    private String deviceList;
    private String notificationSettings;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}