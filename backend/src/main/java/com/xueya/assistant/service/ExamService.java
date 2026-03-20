package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.Exam;

import java.util.List;

public interface ExamService extends IService<Exam> {
    List<Exam> getExamsByType(String type);
    List<Exam> getUpcomingExams();
    List<Exam> getExamsByDateRange(String startDate, String endDate);
}
