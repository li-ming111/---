package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.SystemAnnouncement;

import java.util.List;
import java.util.Map;

public interface SystemAnnouncementService extends IService<SystemAnnouncement> {
    
    /**
     * 发布公告
     */
    boolean publishAnnouncement(SystemAnnouncement announcement);
    
    /**
     * 获取公告列表
     */
    List<SystemAnnouncement> getAnnouncements(String status, String search);
    
    /**
     * 获取公告统计
     */
    Map<String, Object> getAnnouncementStats();
    
    /**
     * 增加阅读数
     */
    boolean incrementReadCount(Long id);
    
    /**
     * 检查并更新过期公告
     */
    void checkAndUpdateExpiredAnnouncements();
}
