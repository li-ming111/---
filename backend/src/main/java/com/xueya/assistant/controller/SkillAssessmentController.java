package com.xueya.assistant.controller;

import com.xueya.assistant.entity.Skill;
import com.xueya.assistant.entity.SkillQuestion;
import com.xueya.assistant.entity.UserSkill;
import com.xueya.assistant.service.SkillService;
import com.xueya.assistant.service.SkillQuestionService;
import com.xueya.assistant.service.UserSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/skill-assessment")
public class SkillAssessmentController {

    @Autowired
    private SkillService skillService;

    @Autowired
    private SkillQuestionService skillQuestionService;

    @Autowired
    private UserSkillService userSkillService;

    // 获取所有技能类型
    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getSkills() {
        List<Skill> skills = skillService.list();
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    // 获取技能详情
    @GetMapping("/skills/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) {
        Skill skill = skillService.getById(id);
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    // 获取技能相关问题
    @GetMapping("/skills/{skillId}/questions")
    public ResponseEntity<List<SkillQuestion>> getSkillQuestions(@PathVariable Long skillId) {
        List<SkillQuestion> questions = skillQuestionService.getQuestionsBySkillId(skillId);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    // 提交技能测评
    @PostMapping("/submit")
    public ResponseEntity<UserSkill> submitAssessment(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        Long skillId = Long.valueOf(request.get("skillId").toString());
        Integer score = Integer.valueOf(request.get("score").toString());

        UserSkill userSkill = new UserSkill();
        userSkill.setUserId(userId);
        userSkill.setSkillId(skillId);
        userSkill.setScore(score);
        userSkill.setAssessmentDate(java.time.LocalDateTime.now().toString());

        userSkillService.save(userSkill);
        return new ResponseEntity<>(userSkill, HttpStatus.CREATED);
    }

    // 获取用户技能测评历史
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserSkill>> getUserSkillAssessments(@PathVariable Long userId) {
        List<UserSkill> userSkills = userSkillService.getUserSkillsByUserId(userId);
        return new ResponseEntity<>(userSkills, HttpStatus.OK);
    }

    // 获取用户技能评级
    @GetMapping("/user/{userId}/ratings")
    public ResponseEntity<List<Map<String, Object>>> getUserSkillRatings(@PathVariable Long userId) {
        List<Map<String, Object>> ratings = userSkillService.getUserSkillRatings(userId);
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }
}
