package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Exam;
import java.util.List;

public interface ExamService extends IService<Exam> {
    List<Exam> getExamsByUserId(Long userId);
    Exam getExamById(Long id);
    boolean createExam(Exam exam);
    boolean updateExam(Exam exam);
    boolean deleteExam(Long id);
    List<Exam> getUpcomingExams(Long userId);
}
