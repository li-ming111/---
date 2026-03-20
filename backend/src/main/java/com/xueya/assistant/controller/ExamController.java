package com.xueya.assistant.controller;

import com.xueya.assistant.entity.Exam;
import com.xueya.assistant.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    // 获取所有考试
    @GetMapping("/all")
    public ResponseEntity<List<Exam>> getAllExams() {
        List<Exam> exams = examService.list();
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // 按类型获取考试
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Exam>> getExamsByType(@PathVariable String type) {
        List<Exam> exams = examService.getExamsByType(type);
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // 获取即将到来的考试
    @GetMapping("/upcoming")
    public ResponseEntity<List<Exam>> getUpcomingExams() {
        List<Exam> exams = examService.getUpcomingExams();
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // 按日期范围获取考试
    @GetMapping("/date-range")
    public ResponseEntity<List<Exam>> getExamsByDateRange(@RequestParam String startDate, @RequestParam String endDate) {
        List<Exam> exams = examService.getExamsByDateRange(startDate, endDate);
        return new ResponseEntity<>(exams, HttpStatus.OK);
    }

    // 创建考试
    @PostMapping("/create")
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) {
        examService.save(exam);
        return new ResponseEntity<>(exam, HttpStatus.CREATED);
    }

    // 更新考试
    @PutMapping("/update")
    public ResponseEntity<Exam> updateExam(@RequestBody Exam exam) {
        examService.updateById(exam);
        return new ResponseEntity<>(exam, HttpStatus.OK);
    }

    // 删除考试
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExam(@PathVariable Long id) {
        examService.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
