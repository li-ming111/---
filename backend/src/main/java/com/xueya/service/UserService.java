package com.xueya.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xueya.entity.User;
import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    User getUserByUsername(String username);
    User getUserByStudentId(String studentId);
    User getUserByIdCard(String idCard);
    User getUserByPhone(String phone);
    List<User> getUsersBySchoolId(Long schoolId);
    
    /**
     * 按角色统计用户数量
     */
    Map<String, Long> getUserCountByRole();
}