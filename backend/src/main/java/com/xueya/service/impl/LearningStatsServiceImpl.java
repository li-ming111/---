package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.LearningStatsMapper;
import com.xueya.service.LearningStatsService;
import com.xueya.entity.LearningStats;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LearningStatsServiceImpl extends ServiceImpl<LearningStatsMapper, LearningStats> implements LearningStatsService {
    @Override
    public List<LearningStats> getStatsByUserId(Long userId) {
        QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return list(queryWrapper);
    }

    @Override
    public List<LearningStats> getStatsByDateRange(Long userId, String startDate, String endDate) {
        QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .between("stats_date", startDate, endDate);
        return list(queryWrapper);
    }

    @Override
    public LearningStats getStatsByDate(Long userId, String date) {
        QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("stats_date", date);
        return getOne(queryWrapper);
    }

    @Override
    public boolean saveOrUpdateStats(LearningStats stats) {
        return saveOrUpdate(stats);
    }

    @Override
    public List<LearningStats> getWeeklyStats(Long userId, String weekStartDate) {
        QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .ge("stats_date", weekStartDate);
        return list(queryWrapper);
    }

    @Override
    public List<LearningStats> getMonthlyStats(Long userId, String month) {
        QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .like("stats_date", month);
        return list(queryWrapper);
    }

    @Override
    public LearningStats getCurrentStats(Long userId) {
        String currentDate = java.time.LocalDate.now().toString();
        return getStatsByDate(userId, currentDate);
    }

    @Override
    public List<LearningStats> getStatsSummary(Long userId) {
        QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("stats_date")
                .last("LIMIT 7");
        return list(queryWrapper);
    }
}