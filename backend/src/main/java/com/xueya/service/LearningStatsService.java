package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.LearningStats;
import java.util.List;

public interface LearningStatsService extends IService<LearningStats> {
    List<LearningStats> getStatsByUserId(Long userId);
    List<LearningStats> getStatsByDateRange(Long userId, String startDate, String endDate);
    LearningStats getStatsByDate(Long userId, String date);
    boolean saveOrUpdateStats(LearningStats stats);
    List<LearningStats> getWeeklyStats(Long userId, String weekStartDate);
    List<LearningStats> getMonthlyStats(Long userId, String month);
    LearningStats getCurrentStats(Long userId);
    List<LearningStats> getStatsSummary(Long userId);
}