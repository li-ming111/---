package com.xueya.assistant.controller;

import com.xueya.assistant.entity.LearningStats;
import com.xueya.assistant.service.LearningStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/learning-stats")
public class LearningStatsController {

    @Autowired
    private LearningStatsService learningStatsService;

    // 获取用户的学习统计摘要
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/summary/{userId}")
    public Map<String, Object> getStatsSummary(@PathVariable Long userId) {
        return learningStatsService.getStatsSummary(userId);
    }

    // 获取用户指定日期的学习统计
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/date/{userId}/{date}")
    public ResponseEntity<LearningStats> getStatsByDate(@PathVariable Long userId, @PathVariable String date) {
        LearningStats stats = learningStatsService.getStatsByUserIdAndDate(userId, date);
        if (stats != null) {
            return ResponseEntity.ok(stats);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取用户指定日期范围的学习统计
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @GetMapping("/range/{userId}")
    public List<LearningStats> getStatsByDateRange(
            @PathVariable Long userId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return learningStatsService.getStatsByUserIdAndDateRange(userId, startDate, endDate);
    }

    // 创建学习统计
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createStats(@RequestBody LearningStats stats) {
        boolean success = learningStatsService.createStats(stats);
        if (success) {
            return ResponseEntity.ok("学习统计创建成功");
        } else {
            return ResponseEntity.badRequest().body("学习统计创建失败");
        }
    }

    // 更新学习统计
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<?> updateStats(@RequestBody LearningStats stats) {
        boolean success = learningStatsService.updateStats(stats);
        if (success) {
            return ResponseEntity.ok("学习统计更新成功");
        } else {
            return ResponseEntity.badRequest().body("学习统计更新失败");
        }
    }
}
