package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Community;
import java.util.List;

public interface CommunityService extends IService<Community> {
    List<Community> getCommunitiesByType(String type);
    List<Community> getCommunitiesByCategory(String category);
    Community getCommunityById(Long id);
    boolean createCommunity(Community community);
    boolean updateCommunity(Community community);
    boolean deleteCommunity(Long id);
}