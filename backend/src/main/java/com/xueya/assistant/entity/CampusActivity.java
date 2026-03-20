package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("campus_activity")
public class CampusActivity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String activityName;
    private String activityType;
    private String description;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer capacity;
    private Integer registeredCount;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}