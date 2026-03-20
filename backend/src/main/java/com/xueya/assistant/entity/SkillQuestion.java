package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("skill_question")
public class SkillQuestion {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long skillId;
    private String questionText;
    private String questionType;
    private String options;
    private String correctAnswer;
    private Integer score;
    private String difficulty;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}