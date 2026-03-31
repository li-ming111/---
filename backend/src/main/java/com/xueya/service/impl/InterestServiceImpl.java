package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.Interest;
import com.xueya.entity.StudyGroup;
import com.xueya.mapper.InterestMapper;
import com.xueya.mapper.StudyGroupMapper;
import com.xueya.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InterestServiceImpl extends ServiceImpl<InterestMapper, Interest> implements InterestService {
    
    @Autowired
    private InterestMapper interestMapper;
    
    @Autowired
    private StudyGroupMapper studyGroupMapper;

    @Override
    public List<Interest> getPopularInterests() {
        QueryWrapper<Interest> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("popularity");
        queryWrapper.last("LIMIT 8");
        return interestMapper.selectList(queryWrapper);
    }

    @Override
    public List<Interest> getInterestsByCategory(String category) {
        QueryWrapper<Interest> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category", category);
        queryWrapper.orderByDesc("popularity");
        return interestMapper.selectList(queryWrapper);
    }

    @Override
    public List<Interest> searchInterests(String keyword) {
        QueryWrapper<Interest> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyword);
        queryWrapper.orderByDesc("popularity");
        return interestMapper.selectList(queryWrapper);
    }

    @Override
    public List<StudyGroup> getInterestGroups() {
        QueryWrapper<StudyGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("member_count");
        return studyGroupMapper.selectList(queryWrapper);
    }

    @Override
    public List<StudyGroup> getGroupsByInterest(String interestName) {
        // 这里简化处理，实际应该根据兴趣名称关联查询
        QueryWrapper<StudyGroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", interestName);
        return studyGroupMapper.selectList(queryWrapper);
    }

    @Override
    public boolean createGroup(StudyGroup group) {
        group.setCreateTime(LocalDateTime.now().toString());
        group.setUpdateTime(LocalDateTime.now().toString());
        group.setMemberCount(1); // 创建者自动加入
        group.setStatus("active");
        return studyGroupMapper.insert(group) > 0;
    }

    @Override
    public boolean joinGroup(Long groupId, Long userId) {
        // 这里简化处理，实际应该检查用户是否已在小组中
        StudyGroup group = studyGroupMapper.selectById(groupId);
        if (group != null) {
            group.setMemberCount(group.getMemberCount() + 1);
            group.setUpdateTime(LocalDateTime.now().toString());
            return studyGroupMapper.updateById(group) > 0;
        }
        return false;
    }

    @Override
    public StudyGroup getGroupById(Long id) {
        return studyGroupMapper.selectById(id);
    }
}
