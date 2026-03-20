package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.Community;
import java.util.List;

public interface CommunityService extends IService<Community> {
    List<Community> getCommunitiesByType(String type);
    List<Community> getCommunitiesByCategory(String category);
    List<Community> getCommunitiesByCreatorId(Long creatorId);
    boolean createCommunity(Community community);
    boolean updateCommunity(Community community);
    boolean deleteCommunity(Long id);
}
