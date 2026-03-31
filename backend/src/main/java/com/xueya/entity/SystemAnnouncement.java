package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("system_announcement")
public class SystemAnnouncement {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 公告标题
     */
    private String title;
    
    /**
     * 公告内容
     */
    private String content;
    
    /**
     * 优先级：low-低, medium-中, high-高
     */
    private String priority;
    
    /**
     * 发布范围：all-所有用户, role-按角色, school-按学校
     */
    private String scope;
    
    /**
     * 目标角色
     */
    private String targetRole;
    
    /**
     * 目标学校ID
     */
    private Long targetSchoolId;
    
    /**
     * 发布人ID
     */
    private Long publisherId;
    
    /**
     * 发布人名称
     */
    private String publisherName;
    
    /**
     * 状态：active-生效中, expired-已过期
     */
    private String status;
    
    /**
     * 阅读数
     */
    private Integer readCount;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishTime;
    
    /**
     * 有效期
     */
    private LocalDateTime expiryDate;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
