package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.Incentive;
import com.xueya.entity.UserIncentive;

import java.util.List;

public interface IncentiveService extends IService<Incentive> {
    List<Incentive> getIncentivesByType(String type);
    List<Incentive> getActiveIncentives();
    Incentive getIncentiveById(Long id);
    boolean createIncentive(Incentive incentive);
    boolean updateIncentive(Incentive incentive);
    boolean deleteIncentive(Long id);
    boolean grantIncentiveToUser(Long userId, Long incentiveId);
    List<UserIncentive> getUserIncentives(Long userId);
    Integer getUserTotalPoints(Long userId);
    List<UserIncentive> getUserIncentivesByType(Long userId, String type);
    boolean claimIncentive(Long userId, Long incentiveId);
}
