package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("study_note")
public class StudyNote {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long groupId;
    private String title;
    private String content;
    private String type; // 笔记类型
    private Integer viewCount;
    private Integer likeCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}