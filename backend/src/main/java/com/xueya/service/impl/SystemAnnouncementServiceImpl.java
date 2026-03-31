package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.SystemAnnouncement;
import com.xueya.mapper.SystemAnnouncementMapper;
import com.xueya.service.SystemAnnouncementService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemAnnouncementServiceImpl extends ServiceImpl<SystemAnnouncementMapper, SystemAnnouncement> implements SystemAnnouncementService {

    @Override
    public boolean publishAnnouncement(SystemAnnouncement announcement) {
        announcement.setStatus("active");
        announcement.setReadCount(0);
        announcement.setPublishTime(LocalDateTime.now());
        announcement.setCreateTime(LocalDateTime.now());
        announcement.setUpdateTime(LocalDateTime.now());
        return save(announcement);
    }

    @Override
    public List<SystemAnnouncement> getAnnouncements(String status, String search) {
        QueryWrapper<SystemAnnouncement> queryWrapper = new QueryWrapper<>();
        
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        
        if (search != null && !search.isEmpty()) {
            queryWrapper.and(wrapper -> wrapper
                .like("title", search)
                .or()
                .like("content", search)
            );
        }
        
        queryWrapper.orderByDesc("publish_time");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> getAnnouncementStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 总公告数
        long totalAnnouncements = count();
        
        // 生效中
        QueryWrapper<SystemAnnouncement> activeWrapper = new QueryWrapper<>();
        activeWrapper.eq("status", "active");
        long activeAnnouncements = count(activeWrapper);
        
        // 已过期
        QueryWrapper<SystemAnnouncement> expiredWrapper = new QueryWrapper<>();
        expiredWrapper.eq("status", "expired");
        long expiredAnnouncements = count(expiredWrapper);
        
        // 总阅读数
        List<SystemAnnouncement> announcements = baseMapper.selectList(new QueryWrapper<>());
        long totalReads = announcements.stream().mapToLong(a -> a.getReadCount() != null ? a.getReadCount() : 0).sum();
        
        stats.put("totalAnnouncements", totalAnnouncements);
        stats.put("activeAnnouncements", activeAnnouncements);
        stats.put("expiredAnnouncements", expiredAnnouncements);
        stats.put("totalReads", totalReads);
        
        return stats;
    }

    @Override
    public boolean incrementReadCount(Long id) {
        SystemAnnouncement announcement = baseMapper.selectById(id);
        if (announcement != null) {
            announcement.setReadCount(announcement.getReadCount() != null ? announcement.getReadCount() + 1 : 1);
            return updateById(announcement);
        }
        return false;
    }

    @Override
    public void checkAndUpdateExpiredAnnouncements() {
        QueryWrapper<SystemAnnouncement> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "active");
        queryWrapper.lt("expiry_date", LocalDateTime.now());
        
        List<SystemAnnouncement> expiredAnnouncements = baseMapper.selectList(queryWrapper);
        for (SystemAnnouncement announcement : expiredAnnouncements) {
            announcement.setStatus("expired");
            announcement.setUpdateTime(LocalDateTime.now());
            updateById(announcement);
        }
    }
}
