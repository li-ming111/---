package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.CampusActivity;
import java.util.List;

public interface CampusActivityService extends IService<CampusActivity> {
    List<CampusActivity> getActivitiesByType(String type);
    List<CampusActivity> getActivitiesByStatus(String status);
    CampusActivity getActivityById(Long id);
    boolean createActivity(CampusActivity activity);
    boolean updateActivity(CampusActivity activity);
    boolean deleteActivity(Long id);
    boolean updateActivityStatus(Long id, String status);
    List<com.xueya.entity.CampusCourse> getCampusCourses();
}