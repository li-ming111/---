package com.xueya.assistant.controller;

import com.xueya.assistant.entity.CampusCourse;
import com.xueya.assistant.entity.CampusActivity;
import com.xueya.assistant.service.CampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campus")
public class CampusController {

    @Autowired
    private CampusService campusService;

    @GetMapping("/courses")
    public List<CampusCourse> getCampusCourses() {
        return campusService.getCampusCourses();
    }

    @GetMapping("/activities")
    public List<CampusActivity> getCampusActivities() {
        return campusService.getCampusActivities();
    }
}
