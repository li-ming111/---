package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.LearningStats;
import java.util.List;
import java.util.Map;

public interface LearningStatsService extends IService<LearningStats> {
    LearningStats getStatsByUserIdAndDate(Long userId, String date);
    List<LearningStats> getStatsByUserIdAndDateRange(Long userId, String startDate, String endDate);
    Map<String, Object> getStatsSummary(Long userId);
    boolean updateStats(LearningStats stats);
    boolean createStats(LearningStats stats);
}
