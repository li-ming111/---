package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("discussion")
public class Discussion {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long groupId;
    private Long userId;
    private String title;
    private String content;
    private Integer replyCount;
    private Integer viewCount;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}