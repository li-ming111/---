package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.ContentReview;
import com.xueya.mapper.ContentReviewMapper;
import com.xueya.service.ContentReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContentReviewServiceImpl extends ServiceImpl<ContentReviewMapper, ContentReview> implements ContentReviewService {

    @Override
    public List<ContentReview> getPendingContents(String type) {
        QueryWrapper<ContentReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "pending");
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq("type", type);
        }
        queryWrapper.orderByDesc("create_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean approveContent(Long id, Long reviewerId, String reviewerName, String comment) {
        ContentReview contentReview = baseMapper.selectById(id);
        if (contentReview == null) {
            return false;
        }
        
        contentReview.setStatus("approved");
        contentReview.setReviewerId(reviewerId);
        contentReview.setReviewerName(reviewerName);
        contentReview.setReviewComment(comment);
        contentReview.setReviewTime(LocalDateTime.now());
        contentReview.setUpdateTime(LocalDateTime.now());
        
        return updateById(contentReview);
    }

    @Override
    public boolean rejectContent(Long id, Long reviewerId, String reviewerName, String comment) {
        ContentReview contentReview = baseMapper.selectById(id);
        if (contentReview == null) {
            return false;
        }
        
        contentReview.setStatus("rejected");
        contentReview.setReviewerId(reviewerId);
        contentReview.setReviewerName(reviewerName);
        contentReview.setReviewComment(comment);
        contentReview.setReviewTime(LocalDateTime.now());
        contentReview.setUpdateTime(LocalDateTime.now());
        
        return updateById(contentReview);
    }

    @Override
    public List<ContentReview> getReviewHistory(String startDate, String endDate) {
        QueryWrapper<ContentReview> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("status", "pending");
        
        if (startDate != null && !startDate.isEmpty()) {
            queryWrapper.ge("review_time", startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            queryWrapper.le("review_time", endDate);
        }
        
        queryWrapper.orderByDesc("review_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> getReviewStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 待审核数量
        QueryWrapper<ContentReview> pendingWrapper = new QueryWrapper<>();
        pendingWrapper.eq("status", "pending");
        long totalPending = count(pendingWrapper);
        
        // 已通过数量
        QueryWrapper<ContentReview> approvedWrapper = new QueryWrapper<>();
        approvedWrapper.eq("status", "approved");
        long totalApproved = count(approvedWrapper);
        
        // 已拒绝数量
        QueryWrapper<ContentReview> rejectedWrapper = new QueryWrapper<>();
        rejectedWrapper.eq("status", "rejected");
        long totalRejected = count(rejectedWrapper);
        
        // 计算通过率
        long total = totalApproved + totalRejected;
        int approvalRate = total > 0 ? (int) ((double) totalApproved / total * 100) : 0;
        
        stats.put("totalPending", totalPending);
        stats.put("totalApproved", totalApproved);
        stats.put("totalRejected", totalRejected);
        stats.put("approvalRate", approvalRate);
        
        return stats;
    }

    @Override
    public boolean submitContent(ContentReview contentReview) {
        contentReview.setStatus("pending");
        contentReview.setCreateTime(LocalDateTime.now());
        contentReview.setUpdateTime(LocalDateTime.now());
        return save(contentReview);
    }
}
