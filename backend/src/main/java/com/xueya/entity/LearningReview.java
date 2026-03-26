package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("learning_review")
public class LearningReview {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String reviewMonth;
    private Integer totalStudyDuration;
    private Integer averageDailyDuration;
    private Integer taskCompletionRate;
    private Integer checkinRate;
    private String strengths;
    private String weaknesses;
    private String suggestions;
    private String reportContent;
    private String createTime;
    private String updateTime;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getReviewMonth() {
        return reviewMonth;
    }

    public void setReviewMonth(String reviewMonth) {
        this.reviewMonth = reviewMonth;
    }

    public Integer getTotalStudyDuration() {
        return totalStudyDuration;
    }

    public void setTotalStudyDuration(Integer totalStudyDuration) {
        this.totalStudyDuration = totalStudyDuration;
    }

    public Integer getAverageDailyDuration() {
        return averageDailyDuration;
    }

    public void setAverageDailyDuration(Integer averageDailyDuration) {
        this.averageDailyDuration = averageDailyDuration;
    }

    public Integer getTaskCompletionRate() {
        return taskCompletionRate;
    }

    public void setTaskCompletionRate(Integer taskCompletionRate) {
        this.taskCompletionRate = taskCompletionRate;
    }

    public Integer getCheckinRate() {
        return checkinRate;
    }

    public void setCheckinRate(Integer checkinRate) {
        this.checkinRate = checkinRate;
    }

    public String getStrengths() {
        return strengths;
    }

    public void setStrengths(String strengths) {
        this.strengths = strengths;
    }

    public String getWeaknesses() {
        return weaknesses;
    }

    public void setWeaknesses(String weaknesses) {
        this.weaknesses = weaknesses;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
