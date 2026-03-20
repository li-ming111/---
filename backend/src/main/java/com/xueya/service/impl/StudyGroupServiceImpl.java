package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.StudyGroupMapper;
import com.xueya.service.StudyGroupService;
import com.xueya.assistant.entity.StudyGroup;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudyGroupServiceImpl extends ServiceImpl<StudyGroupMapper, StudyGroup> implements StudyGroupService {
    @Override
    public List<StudyGroup> getGroupsByCommunityId(Long communityId) {
        QueryWrapper<StudyGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("community_id", communityId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<StudyGroup> getGroupsByLeaderId(Long leaderId) {
        QueryWrapper<StudyGroup> wrapper = new QueryWrapper<>();
        wrapper.eq("leader_id", leaderId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public StudyGroup getGroupById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createGroup(StudyGroup group) {
        return save(group);
    }

    @Override
    public boolean updateGroup(StudyGroup group) {
        return updateById(group);
    }

    @Override
    public boolean deleteGroup(Long id) {
        return removeById(id);
    }
}