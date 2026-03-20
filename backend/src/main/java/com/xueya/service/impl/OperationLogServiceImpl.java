package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.OperationLog;
import com.xueya.mapper.OperationLogMapper;
import com.xueya.service.OperationLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    @Override
    public List<OperationLog> getLogsBySchoolId(Long schoolId) {
        // 这里简化处理，实际应该根据用户和学校的关系查询
        return baseMapper.selectList(null);
    }
    
    @Override
    public List<OperationLog> getLogsByUserId(Long userId) {
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return baseMapper.selectList(queryWrapper);
    }
    
    @Override
    public List<OperationLog> getLogsByTimeRange(String startTime, String endTime) {
        QueryWrapper<OperationLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("create_time", startTime, endTime);
        return baseMapper.selectList(queryWrapper);
    }
}