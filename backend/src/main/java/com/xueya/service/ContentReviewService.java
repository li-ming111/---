package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.ContentReview;

import java.util.List;
import java.util.Map;

public interface ContentReviewService extends IService<ContentReview> {
    
    /**
     * 获取待审核内容列表
     */
    List<ContentReview> getPendingContents(String type);
    
    /**
     * 审核通过
     */
    boolean approveContent(Long id, Long reviewerId, String reviewerName, String comment);
    
    /**
     * 审核拒绝
     */
    boolean rejectContent(Long id, Long reviewerId, String reviewerName, String comment);
    
    /**
     * 获取审核历史
     */
    List<ContentReview> getReviewHistory(String startDate, String endDate);
    
    /**
     * 获取审核统计
     */
    Map<String, Object> getReviewStats();
    
    /**
     * 提交内容审核
     */
    boolean submitContent(ContentReview contentReview);
}
