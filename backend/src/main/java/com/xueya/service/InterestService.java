package com.xueya.service;

import com.xueya.entity.Interest;
import com.xueya.entity.StudyGroup;

import java.util.List;

public interface InterestService {
    // 获取热门兴趣列表
    List<Interest> getPopularInterests();
    
    // 根据分类获取兴趣
    List<Interest> getInterestsByCategory(String category);
    
    // 搜索兴趣
    List<Interest> searchInterests(String keyword);
    
    // 获取兴趣小组列表
    List<StudyGroup> getInterestGroups();
    
    // 根据兴趣获取小组
    List<StudyGroup> getGroupsByInterest(String interestName);
    
    // 创建兴趣小组
    boolean createGroup(StudyGroup group);
    
    // 加入小组
    boolean joinGroup(Long groupId, Long userId);
    
    // 查看小组详情
    StudyGroup getGroupById(Long id);
}
