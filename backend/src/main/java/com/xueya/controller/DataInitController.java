package com.xueya.controller;

import com.xueya.entity.StudyPlan;
import com.xueya.entity.StudyPlanDetail;
import com.xueya.entity.UserGoal;
import com.xueya.entity.CareerPlan;
import com.xueya.entity.Incentive;
import com.xueya.entity.Checkin;
import com.xueya.entity.Community;
import com.xueya.entity.StudyGroup;
import com.xueya.entity.GroupMember;
import com.xueya.entity.StudyNote;
import com.xueya.entity.Discussion;
import com.xueya.entity.Reply;
import com.xueya.entity.LearningStats;
import com.xueya.entity.Skill;
import com.xueya.entity.CampusActivity;
import com.xueya.service.StudyPlanService;
import com.xueya.service.StudyPlanDetailService;
import com.xueya.service.UserGoalService;
import com.xueya.service.CareerPlanService;
import com.xueya.service.IncentiveService;
import com.xueya.service.CheckinService;
import com.xueya.service.CommunityService;
import com.xueya.service.StudyGroupService;
import com.xueya.service.GroupMemberService;
import com.xueya.service.StudyNoteService;
import com.xueya.service.DiscussionService;
import com.xueya.service.ReplyService;
import com.xueya.service.LearningStatsService;
import com.xueya.service.SkillService;
import com.xueya.service.CampusActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataInitController {
    
    @Autowired
    private StudyPlanService studyPlanService;
    
    @Autowired
    private StudyPlanDetailService studyPlanDetailService;
    
    @Autowired
    private UserGoalService userGoalService;
    
    @Autowired
    private CareerPlanService careerPlanService;
    
    @Autowired
    private IncentiveService incentiveService;
    
    @Autowired
    private CheckinService checkinService;
    
    @Autowired
    private CommunityService communityService;
    
    @Autowired
    private StudyGroupService studyGroupService;
    
    @Autowired
    private GroupMemberService groupMemberService;
    
    @Autowired
    private StudyNoteService studyNoteService;
    
    @Autowired
    private DiscussionService discussionService;
    
    @Autowired
    private ReplyService replyService;
    
    @Autowired
    private LearningStatsService learningStatsService;
    
    @Autowired
    private SkillService skillService;
    
    @Autowired
    private CampusActivityService campusActivityService;
    
    // 学生用户ID
    private static final Long STUDENT_USER_ID = 4L;
    
    @GetMapping("/init-student")
    public Map<String, Object> initStudentData() {
        Map<String, Object> response = new HashMap<>();
        try {
            System.out.println("=== 开始为学生账号2023020616创建数据 ===");
            
            // 创建学习计划
            System.out.println("创建学习计划...");
            StudyPlan semesterPlan = new StudyPlan();
            semesterPlan.setUserId(STUDENT_USER_ID);
            semesterPlan.setTitle("2024春季学期学习计划");
            semesterPlan.setDescription("2024年春季学期的主要学习任务和目标");
            semesterPlan.setStartDate("2024-02-20");
            semesterPlan.setEndDate("2024-06-30");
            
            boolean planCreated = studyPlanService.createPlan(semesterPlan);
            if (planCreated) {
                System.out.println("学习计划创建成功，ID: " + semesterPlan.getId());
                
                // 创建学习计划任务
                System.out.println("创建学习计划任务...");
                
                // 高等数学学习任务
                StudyPlanDetail mathTask = new StudyPlanDetail();
                mathTask.setPlanId(semesterPlan.getId());
                mathTask.setTaskName("高等数学课程学习");
                mathTask.setDescription("完成高等数学下册的学习，重点掌握微积分和线性代数");
                mathTask.setStartDate("2024-02-20");
                mathTask.setEndDate("2024-06-15");
                mathTask.setStatus("active");
                mathTask.setProgress(0.0);
                mathTask.setPriority("high");
                studyPlanDetailService.createDetail(mathTask);
                System.out.println("高等数学学习任务创建成功");
                
                // 英语学习任务
                StudyPlanDetail englishTask = new StudyPlanDetail();
                englishTask.setPlanId(semesterPlan.getId());
                englishTask.setTaskName("英语四级备考");
                englishTask.setDescription("准备英语四级考试，提高听力和阅读能力");
                englishTask.setStartDate("2024-02-20");
                englishTask.setEndDate("2024-06-10");
                englishTask.setStatus("active");
                englishTask.setProgress(0.0);
                englishTask.setPriority("high");
                studyPlanDetailService.createDetail(englishTask);
                System.out.println("英语四级备考任务创建成功");
                
                // 专业课程学习任务
                StudyPlanDetail majorTask = new StudyPlanDetail();
                majorTask.setPlanId(semesterPlan.getId());
                majorTask.setTaskName("专业课程学习");
                majorTask.setDescription("完成数据结构、计算机网络等专业课程的学习");
                majorTask.setStartDate("2024-02-20");
                majorTask.setEndDate("2024-06-20");
                majorTask.setStatus("active");
                majorTask.setProgress(0.0);
                majorTask.setPriority("high");
                studyPlanDetailService.createDetail(majorTask);
                System.out.println("专业课程学习任务创建成功");
                
                // 编程实践任务
                StudyPlanDetail codingTask = new StudyPlanDetail();
                codingTask.setPlanId(semesterPlan.getId());
                codingTask.setTaskName("编程实践");
                codingTask.setDescription("完成至少3个编程项目，提高代码能力");
                codingTask.setStartDate("2024-03-01");
                codingTask.setEndDate("2024-06-25");
                codingTask.setStatus("active");
                codingTask.setProgress(0.0);
                codingTask.setPriority("medium");
                studyPlanDetailService.createDetail(codingTask);
                System.out.println("编程实践任务创建成功");
            } else {
                System.out.println("学习计划创建失败");
            }
            
            // 创建目标管理
            System.out.println("创建目标管理...");
            
            // 短期目标 - 学期目标
            UserGoal shortTermGoal = new UserGoal();
            shortTermGoal.setUserId(STUDENT_USER_ID);
            shortTermGoal.setTitle("学期目标");
            shortTermGoal.setDescription("学期末所有课程成绩达到85分以上");
            shortTermGoal.setStartDate("2024-02-20");
            shortTermGoal.setEndDate("2024-06-30");
            shortTermGoal.setPriority("high");
            shortTermGoal.setStatus("active");
            shortTermGoal.setCreateTime(LocalDateTime.now().toString());
            userGoalService.save(shortTermGoal);
            System.out.println("学期目标创建成功");
            
            // 中期目标 - 英语四级
            UserGoal midTermGoal = new UserGoal();
            midTermGoal.setUserId(STUDENT_USER_ID);
            midTermGoal.setTitle("英语四级考试");
            midTermGoal.setDescription("通过英语四级考试，分数达到550分以上");
            midTermGoal.setStartDate("2024-02-20");
            midTermGoal.setEndDate("2024-06-15");
            midTermGoal.setPriority("high");
            midTermGoal.setStatus("active");
            midTermGoal.setCreateTime(LocalDateTime.now().toString());
            userGoalService.save(midTermGoal);
            System.out.println("英语四级目标创建成功");
            
            // 长期目标 - 职业准备
            UserGoal longTermGoal = new UserGoal();
            longTermGoal.setUserId(STUDENT_USER_ID);
            longTermGoal.setTitle("职业准备");
            longTermGoal.setDescription("掌握Java、Python等编程语言，为未来就业做准备");
            longTermGoal.setStartDate("2024-02-20");
            longTermGoal.setEndDate("2026-06-30");
            longTermGoal.setPriority("medium");
            longTermGoal.setStatus("active");
            longTermGoal.setCreateTime(LocalDateTime.now().toString());
            userGoalService.save(longTermGoal);
            System.out.println("职业准备目标创建成功");
            
            // 创建职业规划
            System.out.println("创建职业规划...");
            CareerPlan careerPlan = new CareerPlan();
            careerPlan.setUserId(STUDENT_USER_ID);
            careerPlan.setCareerGoal("成为一名优秀的软件工程师，专注于后端开发");
            careerPlan.setIndustry("互联网");
            careerPlan.setPosition("后端开发工程师");
            careerPlan.setSkillRequirements("Java、Spring Boot、MySQL、Redis、微服务架构");
            careerPlan.setEducationRequirements("本科计算机科学与技术专业，计划攻读硕士学位");
            careerPlan.setExperienceRequirements("在校期间完成至少5个项目，争取在知名企业实习");
            careerPlan.setShortTermGoals("2024-2026：学习基础技术栈，完成项目实践");
            careerPlan.setMediumTermGoals("2026-2029：进入企业工作，积累经验");
            careerPlan.setLongTermGoals("2029+：成为技术专家");
            careerPlan.setActionPlan("3年内成为中级软件工程师，5年内成为高级软件工程师");
            careerPlan.setStatus("active");
            careerPlan.setCreateTime(LocalDateTime.now().toString());
            careerPlanService.createPlan(careerPlan);
            System.out.println("软件工程师职业规划创建成功");
            
            // 创建激励系统数据
            System.out.println("创建激励系统数据...");
            Incentive incentive1 = new Incentive();
            incentive1.setName("连续打卡7天");
            incentive1.setDescription("连续打卡7天获得的奖励");
            incentive1.setType("badge");
            incentive1.setPoints(100);
            incentive1.setIcon("badge_7days");
            incentive1.setCriteria("连续打卡7天");
            incentive1.setStatus("active");
            incentiveService.save(incentive1);
            System.out.println("激励1创建成功");
            
            Incentive incentive2 = new Incentive();
            incentive2.setName("学习计划完成");
            incentive2.setDescription("完成学习计划获得的奖励");
            incentive2.setType("points");
            incentive2.setPoints(200);
            incentive2.setIcon("points_plan");
            incentive2.setCriteria("完成一个学习计划");
            incentive2.setStatus("active");
            incentiveService.save(incentive2);
            System.out.println("激励2创建成功");
            
            // 创建打卡数据
            System.out.println("创建打卡数据...");
            for (int i = 0; i < 5; i++) {
                Checkin checkin = new Checkin();
                checkin.setUserId(STUDENT_USER_ID);
                checkin.setStatus("已打卡");
                checkinService.save(checkin);
            }
            System.out.println("打卡数据创建成功");
            
            // 创建社区数据
            System.out.println("创建社区数据...");
            Community community = new Community();
            community.setName("计算机科学与技术专业社区");
            community.setDescription("计算机科学与技术专业的学习交流社区");
            community.setType("专业");
            community.setCategory("计算机");
            community.setCreatorId(STUDENT_USER_ID);
            community.setMemberCount(1);
            community.setStatus("活跃");
            communityService.save(community);
            System.out.println("社区创建成功");
            
            // 创建学习小组
            StudyGroup studyGroup = new StudyGroup();
            studyGroup.setCommunityId(community.getId());
            studyGroup.setName("Java学习小组");
            studyGroup.setDescription("Java编程学习小组");
            studyGroup.setLeaderId(STUDENT_USER_ID);
            studyGroup.setMemberCount(1);
            studyGroup.setStatus("活跃");
            studyGroupService.save(studyGroup);
            System.out.println("学习小组创建成功");
            
            // 添加小组成员
            GroupMember groupMember = new GroupMember();
            groupMember.setGroupId(studyGroup.getId());
            groupMember.setUserId(STUDENT_USER_ID);
            groupMember.setRole("组长");
            groupMember.setStatus("正常");
            groupMemberService.save(groupMember);
            System.out.println("小组成员添加成功");
            
            // 创建学习笔记
            System.out.println("创建学习笔记...");
            StudyNote studyNote = new StudyNote();
            studyNote.setUserId(STUDENT_USER_ID);
            studyNote.setGroupId(studyGroup.getId());
            studyNote.setTitle("Java基础学习笔记");
            studyNote.setContent("Java基础包括：变量、数据类型、运算符、控制语句、数组、面向对象编程等。");
            studyNote.setType("学习");
            studyNoteService.save(studyNote);
            System.out.println("学习笔记创建成功");
            
            // 创建讨论
            System.out.println("创建讨论...");
            Discussion discussion = new Discussion();
            discussion.setGroupId(studyGroup.getId());
            discussion.setUserId(STUDENT_USER_ID);
            discussion.setTitle("如何学习Java编程？");
            discussion.setContent("大家好，我是一名初学者，想请教一下如何有效地学习Java编程？");
            discussionService.save(discussion);
            System.out.println("讨论创建成功");
            
            // 创建回复
            Reply reply = new Reply();
            reply.setDiscussionId(discussion.getId());
            reply.setUserId(STUDENT_USER_ID);
            reply.setContent("建议从基础开始，多做练习，参加项目实践。");
            replyService.save(reply);
            System.out.println("回复创建成功");
            
            // 创建学习统计数据
            System.out.println("创建学习统计数据...");
            LearningStats learningStats = new LearningStats();
            learningStats.setUserId(STUDENT_USER_ID);
            learningStats.setStatsDate(java.time.LocalDate.now().toString());
            learningStats.setStudyDuration(120); // 分钟
            learningStats.setTaskCompletionRate(80); // 百分比
            learningStats.setCheckinCount(5);
            learningStats.setResourceUsageCount(10);
            learningStats.setNoteCount(1);
            learningStats.setDiscussionCount(1);
            learningStatsService.save(learningStats);
            System.out.println("学习统计数据创建成功");
            
            // 创建技能评估数据
            System.out.println("创建技能评估数据...");
            Skill skill1 = new Skill();
            skill1.setName("Java编程");
            skill1.setType("技术");
            skill1.setCategory("编程语言");
            skill1.setDescription("Java编程语言技能");
            skill1.setLevel(1);
            skillService.save(skill1);
            System.out.println("技能1创建成功");
            
            Skill skill2 = new Skill();
            skill2.setName("英语");
            skill2.setType("语言");
            skill2.setCategory("外语");
            skill2.setDescription("英语语言技能");
            skill2.setLevel(1);
            skillService.save(skill2);
            System.out.println("技能2创建成功");
            
            // 创建校园活动数据
            System.out.println("创建校园活动数据...");
            CampusActivity activity1 = new CampusActivity();
            activity1.setActivityName("编程大赛");
            activity1.setActivityType("竞赛");
            activity1.setDescription("校园编程大赛，展示学生编程技能");
            activity1.setLocation("学校计算机中心");
            activity1.setStartTime(LocalDateTime.now().plusDays(7).toString());
            activity1.setEndTime(LocalDateTime.now().plusDays(7).plusHours(4).toString());
            activity1.setCapacity(100);
            activity1.setRegisteredCount(0);
            activity1.setStatus("报名中");
            campusActivityService.save(activity1);
            System.out.println("校园活动1创建成功");
            
            CampusActivity activity2 = new CampusActivity();
            activity2.setActivityName("英语角");
            activity2.setActivityType("活动");
            activity2.setDescription("每周英语角活动，提高学生英语口语能力");
            activity2.setLocation("学校图书馆");
            activity2.setStartTime(LocalDateTime.now().plusDays(1).toString());
            activity2.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2).toString());
            activity2.setCapacity(50);
            activity2.setRegisteredCount(0);
            activity2.setStatus("报名中");
            campusActivityService.save(activity2);
            System.out.println("校园活动2创建成功");
            
            System.out.println("=== 学生账号2023020616数据创建完成 ===");
            
            response.put("success", true);
            response.put("message", "学生账号2023020616数据初始化成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "数据初始化失败: " + e.getMessage());
        }
        return response;
    }
}