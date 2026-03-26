package com.xueya.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-assistant")
public class AIAssistantController {

    // 获取AI助手回复
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    @PostMapping("/ask")
    public Map<String, Object> getAIResponse(@RequestBody Map<String, String> request) {
        String question = request.get("question");
        String response = getResponse(question);
        
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("response", response);
        
        return result;
    }

    // 简单的回复逻辑
    private String getResponse(String question) {
        Map<String, String> responses = new HashMap<>();
        responses.put("你是谁", "我是学涯助手系统的AI助手，专门为学生提供系统使用指导和帮助，包括学习计划、目标管理、职业规划等功能的使用说明。");
        responses.put("如何使用", "你可以通过我了解学涯助手系统的各项功能，包括学习计划制定、学习资源查找、目标管理、职业规划、激励系统和每日打卡等。如果有具体问题，可以随时向我咨询。");
        responses.put("学习计划", "学习计划功能可以帮助你制定合理的学习目标和时间表，创建详细的学习任务，跟踪学习进度，提高学习效率。你可以在\"学习计划\"页面创建和管理你的学习计划。");
        responses.put("学习资源", "学习资源功能提供了丰富的学习材料，包括课程资料、参考书籍、视频教程等。你可以在\"学习资源\"页面浏览和下载这些资源，支持按类别和关键词搜索。");
        responses.put("目标管理", "目标管理功能可以帮助你设定短期和长期目标，监控目标完成情况，及时调整学习策略。你可以在\"目标管理\"页面创建和跟踪你的学习目标。");
        responses.put("职业规划", "职业规划功能可以根据你的专业和兴趣，提供职业发展建议和相关资源。你可以在\"职业规划\"页面了解不同职业路径，获取职业指导。");
        responses.put("激励系统", "激励系统通过积分和奖励机制，鼓励学生积极参与学习活动，完成学习目标。你可以在\"激励中心\"页面查看你的积分和奖励记录。");
        responses.put("每日打卡", "每日打卡功能可以帮助你养成良好的学习习惯，记录你的学习时间和进度。你可以在\"每日打卡\"页面进行打卡，并查看你的打卡历史。");
        responses.put("校园适应", "校园适应功能提供了校园生活相关的信息和活动，帮助你更好地适应大学生活。你可以在\"校园适应\"页面了解校园活动和课程信息。");
        responses.put("学习统计", "学习统计功能可以分析你的学习数据，提供学习报告和建议。你可以在\"学习统计\"页面查看你的学习情况和进步。");

        // 检查是否有匹配的回复
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            if (question.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // 默认回复
        return "抱歉，我不太理解你的问题。你可以尝试询问关于学习计划、学习资源、目标管理、职业规划、激励系统或每日打卡等方面的问题。";
    }
}
