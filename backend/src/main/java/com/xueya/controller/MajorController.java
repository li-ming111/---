package com.xueya.controller;

import com.xueya.entity.Major;
import com.xueya.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/majors")
public class MajorController {
    @Autowired
    private MajorService majorService;

    // 获取所有专业
    @GetMapping("/list")
    public List<Major> getMajors() {
        return majorService.list();
    }

    // 根据ID获取专业
    @GetMapping("/{id}")
    public Major getMajorById(@PathVariable Long id) {
        return majorService.getById(id);
    }

    // 新增专业
    @PostMapping("/add")
    public boolean addMajor(@RequestBody Major major) {
        return majorService.save(major);
    }

    // 更新专业
    @PutMapping("/update")
    public boolean updateMajor(@RequestBody Major major) {
        return majorService.updateById(major);
    }

    // 删除专业
    @DeleteMapping("/{id}")
    public boolean deleteMajor(@PathVariable Long id) {
        return majorService.removeById(id);
    }
}