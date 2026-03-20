package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.CampusCourse;
import com.xueya.assistant.entity.CampusActivity;

import java.util.List;

public interface CampusService extends IService<CampusCourse> {
    List<CampusCourse> getCampusCourses();
    List<CampusActivity> getCampusActivities();
}
