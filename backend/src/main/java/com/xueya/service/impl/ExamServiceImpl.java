package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.ExamMapper;
import com.xueya.service.ExamService;
import com.xueya.entity.Exam;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {
    @Override
    public List<Exam> getExamsByUserId(Long userId) {
        QueryWrapper<Exam> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.orderByDesc("exam_date");
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Exam getExamById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createExam(Exam exam) {
        exam.setCreateTime(LocalDateTime.now().toString());
        exam.setUpdateTime(LocalDateTime.now().toString());
        return save(exam);
    }

    @Override
    public boolean updateExam(Exam exam) {
        exam.setUpdateTime(LocalDateTime.now().toString());
        return updateById(exam);
    }

    @Override
    public boolean deleteExam(Long id) {
        return removeById(id);
    }

    @Override
    public List<Exam> getUpcomingExams(Long userId) {
        QueryWrapper<Exam> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.gt("exam_date", LocalDateTime.now());
        wrapper.orderByAsc("exam_date");
        return baseMapper.selectList(wrapper);
    }
}
