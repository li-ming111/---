package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.ReplyMapper;
import com.xueya.service.ReplyService;
import com.xueya.entity.Reply;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {
    @Override
    public List<Reply> getRepliesByDiscussionId(Long discussionId) {
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("discussion_id", discussionId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<Reply> getRepliesByUserId(Long userId) {
        QueryWrapper<Reply> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Reply getReplyById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createReply(Reply reply) {
        return save(reply);
    }

    @Override
    public boolean updateReply(Reply reply) {
        return updateById(reply);
    }

    @Override
    public boolean deleteReply(Long id) {
        return removeById(id);
    }
}