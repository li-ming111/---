package com.xueya.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("privacy_setting")
public class PrivacySetting {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String learningRecordVisible;
    private String checkinDataVisible;
    private String personalInfoVisible;
    private Boolean dataExportEnabled;
    private Boolean loginAlert;
    private String deviceList;
    private String notificationSettings;
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

    public String getLearningRecordVisible() {
        return learningRecordVisible;
    }

    public void setLearningRecordVisible(String learningRecordVisible) {
        this.learningRecordVisible = learningRecordVisible;
    }

    public String getCheckinDataVisible() {
        return checkinDataVisible;
    }

    public void setCheckinDataVisible(String checkinDataVisible) {
        this.checkinDataVisible = checkinDataVisible;
    }

    public String getPersonalInfoVisible() {
        return personalInfoVisible;
    }

    public void setPersonalInfoVisible(String personalInfoVisible) {
        this.personalInfoVisible = personalInfoVisible;
    }

    public Boolean getDataExportEnabled() {
        return dataExportEnabled;
    }

    public void setDataExportEnabled(Boolean dataExportEnabled) {
        this.dataExportEnabled = dataExportEnabled;
    }

    public Boolean getLoginAlert() {
        return loginAlert;
    }

    public void setLoginAlert(Boolean loginAlert) {
        this.loginAlert = loginAlert;
    }

    public String getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(String deviceList) {
        this.deviceList = deviceList;
    }

    public String getNotificationSettings() {
        return notificationSettings;
    }

    public void setNotificationSettings(String notificationSettings) {
        this.notificationSettings = notificationSettings;
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
