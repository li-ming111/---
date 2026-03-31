package com.xueya.controller;

import com.xueya.entity.Skill;
import com.xueya.entity.UserSkill;
import com.xueya.service.SkillService;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/skill-assessment")
public class SkillAssessmentController {
    
    @Autowired
    private SkillService skillService;
    
    @Autowired
    private JwtUtil jwtUtil;

    // 获取技能分类和等级数据
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/skills")
    public List<Skill> getSkills() {
        return skillService.getSkillsWithCategories();
    }

    // 获取用户技能评估结果
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/user-skills")
    public List<UserSkill> getUserSkills(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        return skillService.getUserSkills(userId);
    }

    // 开始技能测评
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/start")
    public ResponseEntity<?> startAssessment(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        // 这里简化处理，实际应该创建测评记录
        return ResponseEntity.ok("技能测评开始");
    }

    // 提交技能测评结果
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/submit")
    public ResponseEntity<?> submitAssessment(@RequestBody List<UserSkill> userSkills, @RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        boolean success = skillService.saveUserSkills(userId, userSkills);
        if (success) {
            return ResponseEntity.ok("技能测评提交成功");
        } else {
            return ResponseEntity.badRequest().body("技能测评提交失败");
        }
    }

    // 获取技能详情
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) {
        Skill skill = skillService.getSkillById(id);
        if (skill != null) {
            return ResponseEntity.ok(skill);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 获取推荐学习资源
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestHeader("Authorization") String authorizationHeader) {
        Long userId = getCurrentUserId(authorizationHeader);
        List<?> recommendations = skillService.getSkillRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    // 获取当前登录用户ID的辅助方法
    private Long getCurrentUserId(String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        return jwtUtil.getUserIdFromToken(token);
    }
}
