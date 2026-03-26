package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Discussion;
import java.util.List;

public interface DiscussionService extends IService<Discussion> {
    List<Discussion> getDiscussionsByGroupId(Long groupId);
    List<Discussion> getDiscussionsByUserId(Long userId);
    Discussion getDiscussionById(Long id);
    boolean createDiscussion(Discussion discussion);
    boolean updateDiscussion(Discussion discussion);
    boolean deleteDiscussion(Long id);
    boolean incrementViewCount(Long id);
    boolean incrementReplyCount(Long id);
}