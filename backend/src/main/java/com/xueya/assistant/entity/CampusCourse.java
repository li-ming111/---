package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("campus_course")
public class CampusCourse {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String courseName;
    private String courseCode;
    private String teacher;
    private String classroom;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String semester;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}