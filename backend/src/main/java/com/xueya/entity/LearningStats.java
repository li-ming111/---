package com.xueya.entity;

public class LearningStats {
    private Long id;
    private Long userId;
    private String statsDate;
    private Integer studyDuration;
    private Integer taskCompletionRate;
    private Integer checkinCount;
    private Integer resourceUsageCount;
    private Integer noteCount;
    private Integer discussionCount;
    private String createTime;
    private String updateTime;

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

    public String getStatsDate() {
        return statsDate;
    }

    public void setStatsDate(String statsDate) {
        this.statsDate = statsDate;
    }

    public Integer getStudyDuration() {
        return studyDuration;
    }

    public void setStudyDuration(Integer studyDuration) {
        this.studyDuration = studyDuration;
    }

    public Integer getTaskCompletionRate() {
        return taskCompletionRate;
    }

    public void setTaskCompletionRate(Integer taskCompletionRate) {
        this.taskCompletionRate = taskCompletionRate;
    }

    public Integer getCheckinCount() {
        return checkinCount;
    }

    public void setCheckinCount(Integer checkinCount) {
        this.checkinCount = checkinCount;
    }

    public Integer getResourceUsageCount() {
        return resourceUsageCount;
    }

    public void setResourceUsageCount(Integer resourceUsageCount) {
        this.resourceUsageCount = resourceUsageCount;
    }

    public Integer getNoteCount() {
        return noteCount;
    }

    public void setNoteCount(Integer noteCount) {
        this.noteCount = noteCount;
    }

    public Integer getDiscussionCount() {
        return discussionCount;
    }

    public void setDiscussionCount(Integer discussionCount) {
        this.discussionCount = discussionCount;
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