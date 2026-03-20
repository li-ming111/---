package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exam")
public class Exam {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String examName;
    private String examType;
    private String description;
    private LocalDateTime examDate;
    private String location;
    private Integer duration;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
