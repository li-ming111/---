package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.LearningStats;
import com.xueya.assistant.mapper.LearningStatsMapper;
import com.xueya.assistant.service.LearningStatsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class LearningStatsServiceImpl extends ServiceImpl<LearningStatsMapper, LearningStats> implements LearningStatsService {

    @Override
    public LearningStats getStatsByUserIdAndDate(Long userId, String date) {
        QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("stats_date", date);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<LearningStats> getStatsByUserIdAndDateRange(Long userId, String startDate, String endDate) {
        QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.between("stats_date", startDate, endDate);
        queryWrapper.orderByAsc("stats_date");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> getStatsSummary(Long userId) {
        // 获取最近7天的统计数据
        String endDate = LocalDateTime.now().toLocalDate().toString();
        String startDate = LocalDateTime.now().minusDays(6).toLocalDate().toString();
        List<LearningStats> statsList = getStatsByUserIdAndDateRange(userId, startDate, endDate);

        // 计算汇总数据
        int totalStudyDuration = 0;
        int totalTaskCompletionRate = 0;
        int totalCheckinCount = 0;
        int totalResourceUsageCount = 0;
        int totalNoteCount = 0;
        int totalDiscussionCount = 0;

        for (LearningStats stats : statsList) {
            totalStudyDuration += stats.getStudyDuration() != null ? stats.getStudyDuration() : 0;
            totalTaskCompletionRate += stats.getTaskCompletionRate() != null ? stats.getTaskCompletionRate() : 0;
            totalCheckinCount += stats.getCheckinCount() != null ? stats.getCheckinCount() : 0;
            totalResourceUsageCount += stats.getResourceUsageCount() != null ? stats.getResourceUsageCount() : 0;
            totalNoteCount += stats.getNoteCount() != null ? stats.getNoteCount() : 0;
            totalDiscussionCount += stats.getDiscussionCount() != null ? stats.getDiscussionCount() : 0;
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalStudyDuration", totalStudyDuration);
        summary.put("averageTaskCompletionRate", statsList.size() > 0 ? totalTaskCompletionRate / statsList.size() : 0);
        summary.put("totalCheckinCount", totalCheckinCount);
        summary.put("totalResourceUsageCount", totalResourceUsageCount);
        summary.put("totalNoteCount", totalNoteCount);
        summary.put("totalDiscussionCount", totalDiscussionCount);
        summary.put("statsList", statsList);

        return summary;
    }

    @Override
    public boolean updateStats(LearningStats stats) {
        stats.setUpdateTime(LocalDateTime.now());
        return updateById(stats);
    }

    @Override
    public boolean createStats(LearningStats stats) {
        stats.setCreateTime(LocalDateTime.now());
        stats.setUpdateTime(LocalDateTime.now());
        return save(stats);
    }
}
