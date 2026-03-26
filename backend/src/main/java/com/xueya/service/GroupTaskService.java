package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.GroupTask;
import java.util.List;

public interface GroupTaskService extends IService<GroupTask> {
    List<GroupTask> getTasksByGroupId(Long groupId);
    List<GroupTask> getTasksByStatus(String status);
    GroupTask getTaskById(Long id);
    boolean createTask(GroupTask task);
    boolean updateTask(GroupTask task);
    boolean deleteTask(Long id);
    boolean updateTaskStatus(Long id, String status);
}