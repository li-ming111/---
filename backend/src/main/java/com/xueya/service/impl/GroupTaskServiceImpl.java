package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.mapper.GroupTaskMapper;
import com.xueya.service.GroupTaskService;
import com.xueya.entity.GroupTask;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GroupTaskServiceImpl extends ServiceImpl<GroupTaskMapper, GroupTask> implements GroupTaskService {
    @Override
    public List<GroupTask> getTasksByGroupId(Long groupId) {
        QueryWrapper<GroupTask> wrapper = new QueryWrapper<>();
        wrapper.eq("group_id", groupId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<GroupTask> getTasksByStatus(String status) {
        QueryWrapper<GroupTask> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public GroupTask getTaskById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createTask(GroupTask task) {
        return save(task);
    }

    @Override
    public boolean updateTask(GroupTask task) {
        return updateById(task);
    }

    @Override
    public boolean deleteTask(Long id) {
        return removeById(id);
    }

    @Override
    public boolean updateTaskStatus(Long id, String status) {
        GroupTask task = new GroupTask();
        task.setStatus(status);
        return updateById(task);
    }
}