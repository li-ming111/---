package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.Exam;
import com.xueya.assistant.mapper.ExamMapper;
import com.xueya.assistant.service.ExamService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {

    @Override
    public List<Exam> getExamsByType(String type) {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exam_type", type);
        queryWrapper.orderByDesc("exam_date");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Exam> getUpcomingExams() {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt("exam_date", LocalDateTime.now());
        queryWrapper.orderByAsc("exam_date");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Exam> getExamsByDateRange(String startDate, String endDate) {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("exam_date", startDate, endDate);
        queryWrapper.orderByAsc("exam_date");
        return baseMapper.selectList(queryWrapper);
    }
}
