package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.GroupMemberMapper;
import com.xueya.service.GroupMemberService;
import com.xueya.assistant.entity.GroupMember;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements GroupMemberService {
    @Override
    public List<GroupMember> getMembersByGroupId(Long groupId) {
        QueryWrapper<GroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<GroupMember> getGroupsByUserId(Long userId) {
        QueryWrapper<GroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public GroupMember getMemberByGroupIdAndUserId(Long groupId, Long userId) {
        QueryWrapper<GroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId)
               .eq("user_id", userId);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public boolean joinGroup(GroupMember member) {
        return save(member);
    }

    @Override
    public boolean leaveGroup(Long groupId, Long userId) {
        QueryWrapper<GroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId)
               .eq("user_id", userId);
        return remove(wrapper);
    }

    @Override
    public boolean updateMemberRole(Long groupId, Long userId, String role) {
        QueryWrapper<GroupMember> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId)
               .eq("user_id", userId);
        GroupMember member = new GroupMember();
        member.setRole(role);
        return update(member, wrapper);
    }
}