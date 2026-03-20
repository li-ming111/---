package com.xueya.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xueya.entity.Incentive;
import com.xueya.entity.UserIncentive;
import com.xueya.mapper.IncentiveMapper;
import com.xueya.mapper.UserIncentiveMapper;
import com.xueya.service.IncentiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncentiveServiceImpl extends ServiceImpl<IncentiveMapper, Incentive> implements IncentiveService {

    @Autowired
    private UserIncentiveMapper userIncentiveMapper;

    @Override
    public List<Incentive> getIncentivesByType(String type) {
        QueryWrapper<Incentive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);
        queryWrapper.orderByDesc("points");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public List<Incentive> getActiveIncentives() {
        QueryWrapper<Incentive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "active");
        queryWrapper.orderByDesc("points");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Incentive getIncentiveById(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public boolean createIncentive(Incentive incentive) {
        incentive.setCreateTime(LocalDateTime.now().toString());
        incentive.setUpdateTime(LocalDateTime.now().toString());
        incentive.setStatus("active");
        return save(incentive);
    }

    @Override
    public boolean updateIncentive(Incentive incentive) {
        incentive.setUpdateTime(LocalDateTime.now().toString());
        return updateById(incentive);
    }

    @Override
    public boolean deleteIncentive(Long id) {
        return removeById(id);
    }

    @Override
    public boolean grantIncentiveToUser(Long userId, Long incentiveId) {
        Incentive incentive = baseMapper.selectById(incentiveId);
        if (incentive != null) {
            UserIncentive userIncentive = new UserIncentive();
            userIncentive.setUserId(userId);
            userIncentive.setIncentiveId(incentiveId);
            userIncentive.setIncentiveName(incentive.getName());
            userIncentive.setPoints(incentive.getPoints());
            userIncentive.setObtainedTime(LocalDateTime.now().toString());
            userIncentive.setStatus("active");
            return userIncentiveMapper.insert(userIncentive) > 0;
        }
        return false;
    }

    @Override
    public List<UserIncentive> getUserIncentives(Long userId) {
        QueryWrapper<UserIncentive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("obtained_time");
        return userIncentiveMapper.selectList(queryWrapper);
    }

    @Override
    public Integer getUserTotalPoints(Long userId) {
        List<UserIncentive> userIncentives = getUserIncentives(userId);
        return userIncentives.stream()
                .mapToInt(UserIncentive::getPoints)
                .sum();
    }

    @Override
    public List<UserIncentive> getUserIncentivesByType(Long userId, String type) {
        // 先获取该类型的所有激励ID
        List<Incentive> incentives = getIncentivesByType(type);
        List<Long> incentiveIds = incentives.stream()
                .map(Incentive::getId)
                .collect(java.util.stream.Collectors.toList());

        if (incentiveIds.isEmpty()) {
            return java.util.Collections.emptyList();
        }

        // 然后获取用户的该类型激励
        QueryWrapper<UserIncentive> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.in("incentive_id", incentiveIds);
        queryWrapper.orderByDesc("obtained_time");
        return userIncentiveMapper.selectList(queryWrapper);
    }

    @Override
    public boolean claimIncentive(Long userId, Long incentiveId) {
        // 1. 检查激励是否存在且状态为活跃
        Incentive incentive = baseMapper.selectById(incentiveId);
        if (incentive == null || !"active".equals(incentive.getStatus())) {
            return false;
        }

        // 2. 检查用户积分是否足够
        Integer userPoints = getUserTotalPoints(userId);
        if (userPoints < incentive.getPoints()) {
            return false;
        }

        // 3. 检查激励数量是否足够
        if (incentive.getQuantity() <= 0) {
            return false;
        }

        // 4. 创建用户激励记录
        UserIncentive userIncentive = new UserIncentive();
        userIncentive.setUserId(userId);
        userIncentive.setIncentiveId(incentiveId);
        userIncentive.setIncentiveName(incentive.getName());
        userIncentive.setPoints(incentive.getPoints());
        userIncentive.setObtainedTime(LocalDateTime.now().toString());
        userIncentive.setStatus("active");
        if (userIncentiveMapper.insert(userIncentive) <= 0) {
            return false;
        }

        // 5. 更新激励数量
        incentive.setQuantity(incentive.getQuantity() - 1);
        if (incentive.getQuantity() <= 0) {
            incentive.setStatus("expired");
        }
        incentive.setUpdateTime(LocalDateTime.now().toString());
        return updateById(incentive);
    }
}
