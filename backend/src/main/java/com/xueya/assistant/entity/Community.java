package com.xueya.assistant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("community")
public class Community {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String type; // 专业、目标(考研/考公/竞赛)、兴趣
    private String category; // 具体分类
    private Long creatorId;
    private Integer memberCount;
    private String status; // 活跃、非活跃
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}