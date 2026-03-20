package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("group_task")
public class GroupTask {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long groupId;
    private String name;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status; // 未开始、进行中、已完成
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}