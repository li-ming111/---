package com.xueya.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueya.entity.School;
import com.xueya.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    // 获取所有学校
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/list")
    public List<School> getSchoolList() {
        return schoolService.list();
    }

    // 根据ID获取学校
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/{id}")
    public School getSchoolById(@PathVariable Long id) {
        return schoolService.getById(id);
    }

    // 新增学校
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/add")
    public boolean addSchool(@RequestBody School school) {
        return schoolService.save(school);
    }

    // 更新学校
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @PutMapping("/{id}")
    public boolean updateSchool(@PathVariable Long id, @RequestBody School school) {
        school.setId(id);
        return schoolService.updateById(school);
    }

    // 删除学校
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @DeleteMapping("/{id}")
    public boolean deleteSchool(@PathVariable Long id) {
        return schoolService.removeById(id);
    }

    // 根据学校代码获取学校
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SCHOOL_ADMIN')")
    @GetMapping("/code/{code}")
    public School getSchoolByCode(@PathVariable String code) {
        QueryWrapper<School> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code", code);
        return schoolService.getOne(queryWrapper);
    }
}