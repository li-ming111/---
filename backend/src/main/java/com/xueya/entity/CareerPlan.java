package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("career_plan")
public class CareerPlan implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String userName;
    private Long schoolId;
    private String careerGoal;
    private String industry;
    private String position;
    private String skillRequirements;
    private String educationRequirements;
    private String experienceRequirements;
    private String shortTermGoals;
    private String mediumTermGoals;
    private String longTermGoals;
    private String actionPlan;
    private String status;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getCareerGoal() {
        return careerGoal;
    }

    public void setCareerGoal(String careerGoal) {
        this.careerGoal = careerGoal;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSkillRequirements() {
        return skillRequirements;
    }

    public void setSkillRequirements(String skillRequirements) {
        this.skillRequirements = skillRequirements;
    }

    public String getEducationRequirements() {
        return educationRequirements;
    }

    public void setEducationRequirements(String educationRequirements) {
        this.educationRequirements = educationRequirements;
    }

    public String getExperienceRequirements() {
        return experienceRequirements;
    }

    public void setExperienceRequirements(String experienceRequirements) {
        this.experienceRequirements = experienceRequirements;
    }

    public String getShortTermGoals() {
        return shortTermGoals;
    }

    public void setShortTermGoals(String shortTermGoals) {
        this.shortTermGoals = shortTermGoals;
    }

    public String getMediumTermGoals() {
        return mediumTermGoals;
    }

    public void setMediumTermGoals(String mediumTermGoals) {
        this.mediumTermGoals = mediumTermGoals;
    }

    public String getLongTermGoals() {
        return longTermGoals;
    }

    public void setLongTermGoals(String longTermGoals) {
        this.longTermGoals = longTermGoals;
    }

    public String getActionPlan() {
        return actionPlan;
    }

    public void setActionPlan(String actionPlan) {
        this.actionPlan = actionPlan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
