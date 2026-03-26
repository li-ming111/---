package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Reply;
import java.util.List;

public interface ReplyService extends IService<Reply> {
    List<Reply> getRepliesByDiscussionId(Long discussionId);
    List<Reply> getRepliesByUserId(Long userId);
    Reply getReplyById(Long id);
    boolean createReply(Reply reply);
    boolean updateReply(Reply reply);
    boolean deleteReply(Long id);
}