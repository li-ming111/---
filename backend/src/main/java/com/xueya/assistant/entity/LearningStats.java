package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("learning_stats")
public class LearningStats {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String statsDate;
    private Integer studyDuration;
    private Integer taskCompletionRate;
    private Integer checkinCount;
    private Integer resourceUsageCount;
    private Integer noteCount;
    private Integer discussionCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}