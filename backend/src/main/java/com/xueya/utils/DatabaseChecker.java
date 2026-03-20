package com.xueya.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueya.entity.User;
import com.xueya.entity.School;
import com.xueya.service.UserService;
import com.xueya.service.SchoolService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DatabaseChecker {
    public static void main(String[] args) {
        // 加载Spring配置文件
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        // 获取UserService和SchoolService
        UserService userService = context.getBean(UserService.class);
        SchoolService schoolService = context.getBean(SchoolService.class);
        
        // 检查用户2023020616
        System.out.println("=== 检查用户2023020616 ===");
        User user = userService.getUserByUsername("2023020616");
        if (user != null) {
            System.out.println("用户存在:");
            System.out.println("ID: " + user.getId());
            System.out.println("用户名: " + user.getUsername());
            System.out.println("密码: " + user.getPassword());
            System.out.println("学校ID: " + user.getSchoolId());
            
            // 检查学校信息
            if (user.getSchoolId() != null) {
                School school = schoolService.getById(user.getSchoolId());
                if (school != null) {
                    System.out.println("学校信息:");
                    System.out.println("ID: " + school.getId());
                    System.out.println("名称: " + school.getName());
                    System.out.println("代码: " + school.getCode());
                } else {
                    System.out.println("学校不存在");
                }
            }
        } else {
            System.out.println("用户不存在");
        }
        
        // 检查所有学校
        System.out.println("\n=== 检查所有学校 ===");
        for (School school : schoolService.list()) {
            System.out.println("ID: " + school.getId() + ", 名称: " + school.getName() + ", 代码: " + school.getCode());
        }
        
        // 关闭Spring容器
        ((ClassPathXmlApplicationContext) context).close();
    }
}