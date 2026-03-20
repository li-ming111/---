package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("learning_review")
public class LearningReview {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String reviewMonth;
    private Integer totalStudyDuration;
    private Integer averageDailyDuration;
    private Integer taskCompletionRate;
    private Integer checkinRate;
    private String strengths;
    private String weaknesses;
    private String suggestions;
    private String reportContent;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}