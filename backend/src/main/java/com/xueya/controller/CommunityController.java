package com.xueya.controller;

import com.xueya.entity.Community;
import com.xueya.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/communities")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @GetMapping("/all")
    public List<Community> getAllCommunities() {
        // 获取所有社群
        return communityService.list();
    }

    @GetMapping("/type/{type}")
    public List<Community> getCommunitiesByType(@PathVariable String type) {
        // 按类型获取社群
        return communityService.getCommunitiesByType(type);
    }

    @PostMapping("/create")
    public boolean createCommunity(@RequestBody Community community) {
        // 创建新社群
        return communityService.createCommunity(community);
    }

    @GetMapping("/detail/{id}")
    public Community getCommunityById(@PathVariable Long id) {
        // 获取社群详情
        return communityService.getCommunityById(id);
    }
}
