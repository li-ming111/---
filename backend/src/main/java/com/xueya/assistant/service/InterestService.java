package com.xueya.assistant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.assistant.entity.Interest;

import java.util.List;

public interface InterestService extends IService<Interest> {
    List<Interest> getInterestsByCategory(String category);
    List<Interest> getPopularInterests();
    List<Interest> searchInterests(String keyword);
}
