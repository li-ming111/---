package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.StudyGroup;
import java.util.List;

public interface StudyGroupService extends IService<StudyGroup> {
    List<StudyGroup> getGroupsByCommunityId(Long communityId);
    List<StudyGroup> getGroupsByLeaderId(Long leaderId);
    StudyGroup getGroupById(Long id);
    boolean createGroup(StudyGroup group);
    boolean updateGroup(StudyGroup group);
    boolean deleteGroup(Long id);
}