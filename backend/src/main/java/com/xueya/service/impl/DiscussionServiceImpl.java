package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.DiscussionMapper;
import com.xueya.service.DiscussionService;
import com.xueya.assistant.entity.Discussion;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DiscussionServiceImpl extends ServiceImpl<DiscussionMapper, Discussion> implements DiscussionService {
    @Override
    public List<Discussion> getDiscussionsByGroupId(Long groupId) {
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Discussion> getDiscussionsByUserId(Long userId) {
        QueryWrapper<Discussion> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Discussion getDiscussionById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createDiscussion(Discussion discussion) {
        return save(discussion);
    }

    @Override
    public boolean updateDiscussion(Discussion discussion) {
        return updateById(discussion);
    }

    @Override
    public boolean deleteDiscussion(Long id) {
        return removeById(id);
    }

    @Override
    public boolean incrementViewCount(Long id) {
        Discussion discussion = getById(id);
        if (discussion != null) {
            discussion.setViewCount(discussion.getViewCount() + 1);
            return updateById(discussion);
        }
        return false;
    }

    @Override
    public boolean incrementReplyCount(Long id) {
        Discussion discussion = getById(id);
        if (discussion != null) {
            discussion.setReplyCount(discussion.getReplyCount() + 1);
            return updateById(discussion);
        }
        return false;
    }
}