package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("user_incentive")
public class UserIncentive implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long incentiveId;
    private String incentiveName;
    private Integer points;
    private String obtainedTime;
    private String status;

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

    public Long getIncentiveId() {
        return incentiveId;
    }

    public void setIncentiveId(Long incentiveId) {
        this.incentiveId = incentiveId;
    }

    public String getIncentiveName() {
        return incentiveName;
    }

    public void setIncentiveName(String incentiveName) {
        this.incentiveName = incentiveName;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getObtainedTime() {
        return obtainedTime;
    }

    public void setObtainedTime(String obtainedTime) {
        this.obtainedTime = obtainedTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
