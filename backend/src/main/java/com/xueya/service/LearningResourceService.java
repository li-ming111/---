package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.LearningResource;

import java.util.List;
import java.util.Map;

public interface LearningResourceService extends IService<LearningResource> {
    List<LearningResource> getResourcesBySchoolId(Long schoolId);
    List<LearningResource> getResourcesByCategory(String category);
    List<LearningResource> searchResources(String keyword);
    List<LearningResource> getRecommendedResources(Long userId);
    List<LearningResource> getResourcesByStudentStage(Long userId);
    void incrementViewCount(Long resourceId);
    void incrementDownloadCount(Long resourceId);
    
    /**
     * 按类型统计资源数量
     */
    Map<String, Long> getResourceCountByType();
}
