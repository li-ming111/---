package com.xueya.controller;

import com.xueya.entity.CampusActivity;
import com.xueya.entity.CampusCourse;
import com.xueya.service.CampusActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/campus")
public class CampusController {

    @Autowired
    private CampusActivityService campusActivityService;

    @GetMapping("/courses")
    public List<CampusCourse> getCampusCourses() {
        // 这里可以根据实际需求添加查询条件
        return campusActivityService.getCampusCourses();
    }

    @GetMapping("/activities")
    public List<CampusActivity> getCampusActivities() {
        // 这里可以根据实际需求添加查询条件
        return campusActivityService.list();
    }
}
