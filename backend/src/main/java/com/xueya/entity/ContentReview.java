package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("content_review")
public class ContentReview {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 内容标题
     */
    private String title;
    
    /**
     * 内容类型：document-文档, video-视频, audio-音频, image-图片, other-其他
     */
    private String type;
    
    /**
     * 内容描述
     */
    private String description;
    
    /**
     * 上传者ID
     */
    private Long uploaderId;
    
    /**
     * 上传者名称
     */
    private String uploaderName;
    
    /**
     * 学校ID
     */
    private Long schoolId;
    
    /**
     * 学校名称
     */
    private String schoolName;
    
    /**
     * 文件URL
     */
    private String fileUrl;
    
    /**
     * 文件大小（字节）
     */
    private Long fileSize;
    
    /**
     * 审核状态：pending-待审核, approved-已通过, rejected-已拒绝
     */
    private String status;
    
    /**
     * 审核人ID
     */
    private Long reviewerId;
    
    /**
     * 审核人名称
     */
    private String reviewerName;
    
    /**
     * 审核意见
     */
    private String reviewComment;
    
    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
