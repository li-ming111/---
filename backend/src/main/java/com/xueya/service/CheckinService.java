package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Checkin;

import java.util.List;

public interface CheckinService extends IService<Checkin> {
    boolean checkin(Long userId);
    boolean hasCheckedInToday(Long userId);
    List<Checkin> getUserCheckinRecords(Long userId);
    Integer getUserCheckinCount(Long userId);
}