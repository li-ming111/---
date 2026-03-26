package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.GroupMember;
import java.util.List;

public interface GroupMemberService extends IService<GroupMember> {
    List<GroupMember> getMembersByGroupId(Long groupId);
    List<GroupMember> getGroupsByUserId(Long userId);
    GroupMember getMemberByGroupIdAndUserId(Long groupId, Long userId);
    boolean joinGroup(GroupMember member);
    boolean leaveGroup(Long groupId, Long userId);
    boolean updateMemberRole(Long groupId, Long userId, String role);
}