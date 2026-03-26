package com.xueya.controller;

import com.xueya.entity.Exam;
import com.xueya.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    // 获取用户的考试列表
    @GetMapping("/user/{userId}")
    public List<Exam> getExamsByUserId(@PathVariable Long userId) {
        return examService.getExamsByUserId(userId);
    }

    // 获取考试详情
    @GetMapping("/{id}")
    public ResponseEntity<Exam> getExamById(@PathVariable Long id) {
        Exam exam = examService.getExamById(id);
        if (exam != null) {
            return ResponseEntity.ok(exam);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 创建考试
    @PostMapping("/create")
    public ResponseEntity<?> createExam(@RequestBody Exam exam) {
        boolean success = examService.createExam(exam);
        if (success) {
            return ResponseEntity.ok("考试创建成功");
        } else {
            return ResponseEntity.badRequest().body("考试创建失败");
        }
    }

    // 更新考试
    @PutMapping("/update")
    public ResponseEntity<?> updateExam(@RequestBody Exam exam) {
        boolean success = examService.updateExam(exam);
        if (success) {
            return ResponseEntity.ok("考试更新成功");
        } else {
            return ResponseEntity.badRequest().body("考试更新失败");
        }
    }

    // 删除考试
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExam(@PathVariable Long id) {
        boolean success = examService.deleteExam(id);
        if (success) {
            return ResponseEntity.ok("考试删除成功");
        } else {
            return ResponseEntity.badRequest().body("考试删除失败");
        }
    }

    // 获取即将到来的考试
    @GetMapping("/upcoming/{userId}")
    public List<Exam> getUpcomingExams(@PathVariable Long userId) {
        return examService.getUpcomingExams(userId);
    }
}
