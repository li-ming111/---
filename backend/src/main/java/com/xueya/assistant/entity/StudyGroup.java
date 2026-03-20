package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("study_group")
public class StudyGroup {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long communityId;
    private String name;
    private String description;
    private Long leaderId;
    private Integer memberCount;
    private String status; // 活跃、非活跃
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}