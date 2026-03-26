package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.CampusActivityMapper;
import com.xueya.mapper.CampusCourseMapper;
import com.xueya.service.CampusActivityService;
import com.xueya.entity.CampusActivity;
import com.xueya.entity.CampusCourse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CampusActivityServiceImpl extends ServiceImpl<CampusActivityMapper, CampusActivity> implements CampusActivityService {

    @Autowired
    private CampusCourseMapper campusCourseMapper;
    @Override
    public List<CampusActivity> getActivitiesByType(String type) {
        QueryWrapper<CampusActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_type", type);
        return list(queryWrapper);
    }

    @Override
    public List<CampusActivity> getActivitiesByStatus(String status) {
        QueryWrapper<CampusActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return list(queryWrapper);
    }

    @Override
    public CampusActivity getActivityById(Long id) {
        return getById(id);
    }

    @Override
    public boolean createActivity(CampusActivity activity) {
        // 检查是否已存在相同名称的校园活动
        QueryWrapper<CampusActivity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("activity_name", activity.getActivityName());
        List<CampusActivity> existingActivities = baseMapper.selectList(queryWrapper);
        if (!existingActivities.isEmpty()) {
            // 已存在相同名称的校园活动，返回false
            return false;
        }
        return save(activity);
    }

    @Override
    public boolean updateActivity(CampusActivity activity) {
        return updateById(activity);
    }

    @Override
    public boolean deleteActivity(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateActivityStatus(Long id, String status) {
        CampusActivity activity = new CampusActivity();
        activity.setId(id);
        activity.setStatus(status);
        return updateById(activity);
    }

    @Override
    public List<CampusCourse> getCampusCourses() {
        // 这里可以根据实际需求添加查询条件
        return campusCourseMapper.selectList(null);
    }
}