package com.xueya.controller;

import com.xueya.entity.LearningStats;
import com.xueya.service.LearningStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/learning-stats")
public class LearningStatsController {

    @Autowired
    private LearningStatsService learningStatsService;

    @GetMapping("/range/{userId}")
    public List<LearningStats> getStatsByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return learningStatsService.getStatsByDateRange(userId, startDate, endDate);
    }

    @GetMapping("/current/{userId}")
    public LearningStats getCurrentStats(@PathVariable Long userId) {
        return learningStatsService.getCurrentStats(userId);
    }

    @GetMapping("/summary/{userId}")
    public List<LearningStats> getStatsSummary(@PathVariable Long userId) {
        return learningStatsService.getStatsSummary(userId);
    }
}
