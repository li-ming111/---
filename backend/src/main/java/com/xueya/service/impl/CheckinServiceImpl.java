package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.Checkin;
import com.xueya.mapper.CheckinMapper;
import com.xueya.service.CheckinService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CheckinServiceImpl extends ServiceImpl<CheckinMapper, Checkin> implements CheckinService {

    @Override
    public boolean checkin(Long userId) {
        // 检查今天是否已经打卡
        if (hasCheckedInToday(userId)) {
            return false;
        }

        // 创建打卡记录
        Checkin checkin = new Checkin();
        checkin.setUserId(userId);
        checkin.setCheckinTime(LocalDateTime.now());
        checkin.setStatus("已打卡");
        checkin.setCreateTime(LocalDateTime.now());
        checkin.setUpdateTime(LocalDateTime.now());

        return save(checkin);
    }

    @Override
    public boolean hasCheckedInToday(Long userId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(23, 59, 59, 999999999);
        
        QueryWrapper<Checkin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.between("checkin_time", startOfDay, endOfDay);
        return count(queryWrapper) > 0;
    }

    @Override
    public List<Checkin> getUserCheckinRecords(Long userId) {
        QueryWrapper<Checkin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("checkin_time");
        return list(queryWrapper);
    }

    @Override
    public Integer getUserCheckinCount(Long userId) {
        QueryWrapper<Checkin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return Math.toIntExact(count(queryWrapper));
    }
}