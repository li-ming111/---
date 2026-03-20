package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_skill")
public class UserSkill {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long skillId;
    private Integer score;
    private Integer level;
    private String assessmentDate;
    private String improvementPlan;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}