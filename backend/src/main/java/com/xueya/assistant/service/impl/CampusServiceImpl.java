package com.xueya.assistant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.assistant.entity.CampusCourse;
import com.xueya.assistant.entity.CampusActivity;
import com.xueya.mapper.CampusCourseMapper;
import com.xueya.mapper.CampusActivityMapper;
import com.xueya.assistant.service.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusServiceImpl extends ServiceImpl<CampusCourseMapper, CampusCourse> implements CampusService {

    @Autowired
    private CampusActivityMapper campusActivityMapper;

    @Override
    public List<CampusCourse> getCampusCourses() {
        return list();
    }

    @Override
    public List<CampusActivity> getCampusActivities() {
        return campusActivityMapper.selectList(null);
    }
}
