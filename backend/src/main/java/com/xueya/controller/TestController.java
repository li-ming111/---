package com.xueya.controller;

import com.xueya.entity.User;
import com.xueya.entity.UserSkill;
import com.xueya.service.SkillService;
import com.xueya.service.UserService;
import com.xueya.service.LearningResourceService;
import com.xueya.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private UserService userService;

    @Autowired
    private LearningResourceService learningResourceService;

    @Autowired
    private InterestService interestService;

    @Autowired
    private DataSource dataSource;

    // 检查用户技能数据
    @GetMapping("/check-user-skills")
    public String checkUserSkills() {
        User user = userService.getUserByIdCard("232321200501064619");
        if (user == null) {
            return "用户不存在";
        }

        Long userId = user.getId();
        List<UserSkill> userSkills = skillService.getUserSkills(userId);
        if (userSkills.isEmpty()) {
            return "用户没有技能数据";
        } else {
            return "用户技能数据：共" + userSkills.size() + "个技能";
        }
    }

    // 检查学习资源数据
    @GetMapping("/check-learning-resources")
    public String checkLearningResources() {
        List<com.xueya.entity.LearningResource> resources = learningResourceService.list();
        if (resources.isEmpty()) {
            return "学习资源数据为空";
        } else {
            return "学习资源数据：共" + resources.size() + "个资源";
        }
    }

    // 检查兴趣爱好数据
    @GetMapping("/check-interests")
    public String checkInterests() {
        List<com.xueya.entity.Interest> interests = interestService.getPopularInterests();
        List<com.xueya.entity.StudyGroup> groups = interestService.getInterestGroups();
        if (interests.isEmpty() && groups.isEmpty()) {
            return "兴趣爱好数据为空";
        } else {
            return "兴趣爱好数据：共" + interests.size() + "个兴趣，" + groups.size() + "个兴趣小组";
        }
    }

    // 检查管理员账户数据
    @GetMapping("/check-admin-users")
    public String checkAdminUsers() {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            result.append("=== 管理员账户数据检查 ===\n\n");

            // 检查超级管理员
            var rs1 = statement.executeQuery(
                "SELECT COUNT(*) as count FROM user WHERE username = 'superadmin' AND role = '0'"
            );
            rs1.next();
            int superAdminCount = rs1.getInt("count");
            result.append("超级管理员: ").append(superAdminCount == 1 ? "✓ 存在" : "✗ 不存在").append("\n");

            // 检查学校管理员
            var rs2 = statement.executeQuery(
                "SELECT COUNT(*) as count FROM user WHERE role = 'admin'"
            );
            rs2.next();
            int schoolAdminCount = rs2.getInt("count");
            result.append("学校管理员: ").append(schoolAdminCount >= 1 ? "✓ 存在 (" + schoolAdminCount + "个)" : "✗ 不存在").append("\n");

            // 显示所有管理员详情
            result.append("\n=== 管理员账户详情 ===\n");
            var rs3 = statement.executeQuery(
                "SELECT id, username, name, role, status, email, phone, school_id, create_time " +
                "FROM user WHERE role IN ('0', 'admin') ORDER BY role, id"
            );

            result.append(String.format("%-5s %-15s %-12s %-10s %-10s %-25s %-15s %-10s\n",
                "ID", "用户名", "姓名", "角色", "状态", "邮箱", "电话", "学校ID"));
            result.append("------------------------------------------------------------------------------------------------\n");

            while (rs3.next()) {
                result.append(String.format("%-5d %-15s %-12s %-10s %-10s %-25s %-15s %-10s\n",
                    rs3.getLong("id"),
                    rs3.getString("username"),
                    rs3.getString("name"),
                    rs3.getString("role"),
                    rs3.getString("status"),
                    rs3.getString("email"),
                    rs3.getString("phone"),
                    rs3.getObject("school_id") != null ? rs3.getString("school_id") : "NULL"
                ));
            }

            return result.toString();

        } catch (Exception e) {
            return "检查管理员账户时发生错误: " + e.getMessage();
        }
    }

    // 检查所有功能数据
    @GetMapping("/check-all-data")
    public String checkAllData() {
        StringBuilder result = new StringBuilder();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            result.append("=== 系统数据完整性检查 ===\n\n");

            // 检查各项数据
            String[] tables = {
                "user", "role", "user_role", "skill", "user_skill",
                "learning_resource", "interest", "study_plan", "plan_task",
                "goal", "career_planning", "checkin", "community_post", "community_comment"
            };

            for (String table : tables) {
                try {
                    var rs = statement.executeQuery("SELECT COUNT(*) as count FROM " + table);
                    rs.next();
                    int count = rs.getInt("count");
                    result.append(String.format("%-20s: %3d 条记录\n", table, count));
                } catch (Exception e) {
                    result.append(String.format("%-20s: 表不存在或无法访问\n", table));
                }
            }

            result.append("\n=== 检查完成 ===");
            return result.toString();

        } catch (Exception e) {
            return "检查数据时发生错误: " + e.getMessage();
        }
    }
}
