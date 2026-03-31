package com.xueya.init;

import com.xueya.entity.Permission;
import com.xueya.entity.Role;
import com.xueya.entity.RolePermission;
import com.xueya.entity.School;
import com.xueya.entity.Major;
import com.xueya.entity.College;
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
import com.xueya.service.PermissionService;
import com.xueya.service.RoleService;
import com.xueya.service.SchoolService;
import com.xueya.service.MajorService;
import com.xueya.service.CollegeService;
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
import com.xueya.service.UserService;
import com.xueya.entity.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private MajorService majorService;
    @Autowired
    private CollegeService collegeService;
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
    @Autowired
    private UserService userService;

    @Autowired
    private javax.sql.DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        // 异步执行数据库初始化，避免阻塞服务启动
        new Thread(() -> {
            try {
                // 创建必要的表结构
                createUserTable();
                createSchoolTable();
                createCollegeTable();
                createMajorTable();
                createStudentProfileTable();
                createLearningResourceTable();
                createOperationLogTable();
                createStudyPlanTable();
                createStudyPlanDetailTable();
                createUserGoalTable();
                createCareerPlanTable();
                createIncentiveTable();
                createUserIncentiveTable();
                createCheckinTable();
                createCommunityTable();
                createStudyGroupTable();
                createGroupMemberTable();
                createGroupTaskTable();
                createStudyNoteTable();
                createDiscussionTable();
                createReplyTable();
                createLearningStatsTable();
                createLearningReviewTable();
                createSkillTable();
                createUserSkillTable();
                createSkillQuestionTable();
                createOfflineDataTable();
                createPrivacySettingTable();
                createCampusCourseTable();
                createCampusActivityTable();
                createExamTable();

                // 启用数据初始化
                // 检查是否已有学校数据
                if (schoolService.count() == 0) {
                    System.out.println("开始初始化学校数据...");
                    initSchools();
                    System.out.println("学校数据初始化完成！");
                }

                // 检查是否已有学院数据
                if (collegeService.count() == 0) {
                    System.out.println("开始初始化学院数据...");
                    initColleges();
                    System.out.println("学院数据初始化完成！");
                }

                // 检查是否已有专业数据
                if (majorService.count() == 0) {
                    System.out.println("开始初始化专业数据...");
                    initMajors();
                    System.out.println("专业数据初始化完成！");
                }
                
                System.out.println("数据库表结构检查完成");
            } catch (Exception e) {
                System.err.println("数据库初始化失败: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void createSchoolTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'school'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE school " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "code VARCHAR(50) NOT NULL UNIQUE, " +
                            "logo VARCHAR(255), " +
                            "address VARCHAR(255), " +
                            "contact VARCHAR(100), " +
                            "email VARCHAR(100), " +
                            "website VARCHAR(255), " +
                            "description TEXT, " +
                            "status VARCHAR(20) DEFAULT 'active', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("School table created successfully");
                } else {
                    // 表存在，更新表结构
                    updateSchoolTable(conn, stmt);
                    // 修改表字符集
                    stmt.executeUpdate("ALTER TABLE school CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                    System.out.println("School table charset updated to utf8mb4");
                    // 检查表结构
                    String checkSql = "DESCRIBE school";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("School table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void updateSchoolTable(java.sql.Connection conn, java.sql.Statement stmt) throws Exception {
        // 检查并添加缺少的列
        String[] columns = {
            "logo",
            "contact",
            "email",
            "website",
            "description",
            "status"
        };
        
        String[] types = {
            "VARCHAR(255)",
            "VARCHAR(100)",
            "VARCHAR(100)",
            "VARCHAR(255)",
            "TEXT",
            "VARCHAR(20) DEFAULT 'active'"
        };
        
        for (int i = 0; i < columns.length; i++) {
            if (!columnExists(conn, "school", columns[i])) {
                try {
                    String sql = "ALTER TABLE school ADD COLUMN " + columns[i] + " " + types[i];
                    stmt.executeUpdate(sql);
                    System.out.println("Added column: " + columns[i]);
                } catch (Exception e) {
                    System.out.println("Error adding column " + columns[i] + ": " + e.getMessage());
                }
            } else {
                System.out.println("Column " + columns[i] + " already exists");
            }
        }
    }

    private void updateLearningResourceTable(java.sql.Connection conn, java.sql.Statement stmt) throws Exception {
        // 检查并添加缺少的列
        String[] columns = {
            "status",
            "student_stage"
        };
        
        String[] types = {
            "VARCHAR(20) DEFAULT 'published'",
            "VARCHAR(50)"
        };
        
        for (int i = 0; i < columns.length; i++) {
            if (!columnExists(conn, "learning_resource", columns[i])) {
                try {
                    String sql = "ALTER TABLE learning_resource ADD COLUMN " + columns[i] + " " + types[i];
                    stmt.executeUpdate(sql);
                    System.out.println("Added column: " + columns[i]);
                } catch (Exception e) {
                    System.out.println("Error adding column " + columns[i] + ": " + e.getMessage());
                }
            } else {
                System.out.println("Column " + columns[i] + " already exists");
            }
        }
    }

    private boolean columnExists(java.sql.Connection conn, String tableName, String columnName) throws Exception {
        String sql = "DESCRIBE " + tableName;
        try (java.sql.Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                if (rs.getString(1).equals(columnName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void initSchools() {
        List<School> schools = new ArrayList<>();

        // 添加全国主要高校（985、211、双一流）
        // 北京
        schools.add(createSchool("北京大学", "PKU", "北京市海淀区颐和园路5号", "010-62751234", "info@pku.edu.cn", "https://www.pku.edu.cn"));
        schools.add(createSchool("清华大学", "THU", "北京市海淀区清华园1号", "010-62782114", "info@tsinghua.edu.cn", "https://www.tsinghua.edu.cn"));
        schools.add(createSchool("中国人民大学", "RUC", "北京市海淀区中关村大街59号", "010-62511601", "info@ruc.edu.cn", "https://www.ruc.edu.cn"));
        schools.add(createSchool("北京航空航天大学", "BUAA", "北京市海淀区学院路37号", "010-82317114", "info@buaa.edu.cn", "https://www.buaa.edu.cn"));
        schools.add(createSchool("北京理工大学", "BIT", "北京市海淀区中关村南大街5号", "010-68913345", "info@bit.edu.cn", "https://www.bit.edu.cn"));
        schools.add(createSchool("中国农业大学", "CAU", "北京市海淀区圆明园西路2号", "010-62736969", "info@cau.edu.cn", "https://www.cau.edu.cn"));
        schools.add(createSchool("中央民族大学", "MUC", "北京市海淀区中关村南大街27号", "010-68932996", "info@muc.edu.cn", "https://www.muc.edu.cn"));
        schools.add(createSchool("北京师范大学", "BNU", "北京市海淀区新街口外大街19号", "010-58807102", "info@bnu.edu.cn", "https://www.bnu.edu.cn"));
        
        // 上海
        schools.add(createSchool("上海交通大学", "SJTU", "上海市闵行区东川路800号", "021-54740000", "info@sjtu.edu.cn", "https://www.sjtu.edu.cn"));
        schools.add(createSchool("复旦大学", "FUDAN", "上海市杨浦区邯郸路220号", "021-65642222", "info@fudan.edu.cn", "https://www.fudan.edu.cn"));
        schools.add(createSchool("同济大学", "TONGJI", "上海市杨浦区四平路1239号", "021-65982200", "info@tongji.edu.cn", "https://www.tongji.edu.cn"));
        schools.add(createSchool("华东师范大学", "ECNU", "上海市闵行区东川路500号", "021-62233333", "info@ecnu.edu.cn", "https://www.ecnu.edu.cn"));
        
        // 天津
        schools.add(createSchool("南开大学", "NKU", "天津市南开区卫津路94号", "022-23508219", "info@nankai.edu.cn", "https://www.nankai.edu.cn"));
        schools.add(createSchool("天津大学", "TJU", "天津市南开区卫津路92号", "022-27403536", "info@tju.edu.cn", "https://www.tju.edu.cn"));
        
        // 重庆
        schools.add(createSchool("重庆大学", "CQU", "重庆市沙坪坝区沙正街174号", "023-65102371", "info@cqu.edu.cn", "https://www.cqu.edu.cn"));
        
        // 四川
        schools.add(createSchool("四川大学", "SCU", "四川省成都市武侯区一环路南一段24号", "028-85406666", "info@scu.edu.cn", "https://www.scu.edu.cn"));
        schools.add(createSchool("电子科技大学", "UESTC", "四川省成都市郫都区西源大道2006号", "028-61830114", "info@uestc.edu.cn", "https://www.uestc.edu.cn"));
        
        // 湖南
        schools.add(createSchool("中南大学", "CSU", "湖南省长沙市岳麓区麓山南路932号", "0731-88876114", "info@csu.edu.cn", "https://www.csu.edu.cn"));
        schools.add(createSchool("湖南大学", "HNU", "湖南省长沙市岳麓区麓山南路2号", "0731-88823560", "info@hnu.edu.cn", "https://www.hnu.edu.cn"));
        
        // 浙江
        schools.add(createSchool("浙江大学", "ZJU", "浙江省杭州市西湖区余杭塘路866号", "0571-88273114", "info@zju.edu.cn", "https://www.zju.edu.cn"));
        
        // 江苏
        schools.add(createSchool("南京大学", "NJU", "江苏省南京市栖霞区仙林大道163号", "025-83593186", "info@nju.edu.cn", "https://www.nju.edu.cn"));
        schools.add(createSchool("东南大学", "SEU", "江苏省南京市玄武区四牌楼2号", "025-83792452", "info@seu.edu.cn", "https://www.seu.edu.cn"));
        
        // 湖北
        schools.add(createSchool("武汉大学", "WHU", "湖北省武汉市武昌区八一路299号", "027-68755114", "info@whu.edu.cn", "https://www.whu.edu.cn"));
        schools.add(createSchool("华中科技大学", "HUST", "湖北省武汉市洪山区珞喻路1037号", "027-87541114", "info@hust.edu.cn", "https://www.hust.edu.cn"));
        
        // 黑龙江
        schools.add(createSchool("哈尔滨工业大学", "HIT", "黑龙江省哈尔滨市南岗区西大直街92号", "0451-86414060", "info@hit.edu.cn", "https://www.hit.edu.cn"));
        schools.add(createSchool("哈尔滨工程大学", "HEU", "黑龙江省哈尔滨市南岗区南通大街145号", "0451-82519849", "info@heu.edu.cn", "https://www.heu.edu.cn"));
        schools.add(createSchool("哈尔滨信息工程学院", "HIIE", "黑龙江省哈尔滨市宾西经济技术开发区大学城", "0451-57359666", "info@hie.edu.cn", "https://www.hie.edu.cn"));
        
        // 陕西
        schools.add(createSchool("西安交通大学", "XJTU", "陕西省西安市咸宁西路28号", "029-82668888", "info@xjtu.edu.cn", "https://www.xjtu.edu.cn"));
        schools.add(createSchool("西北工业大学", "NPU", "陕西省西安市碑林区友谊西路127号", "029-88492114", "info@nwpu.edu.cn", "https://www.nwpu.edu.cn"));
        
        // 广东
        schools.add(createSchool("中山大学", "SYSU", "广东省广州市海珠区新港西路135号", "020-84113388", "info@sysu.edu.cn", "https://www.sysu.edu.cn"));
        schools.add(createSchool("华南理工大学", "SCUT", "广东省广州市天河区五山路381号", "020-87110000", "info@scut.edu.cn", "https://www.scut.edu.cn"));
        
        // 山东
        schools.add(createSchool("山东大学", "SDU", "山东省济南市历城区山大南路27号", "0531-88364737", "info@sdu.edu.cn", "https://www.sdu.edu.cn"));
        
        // 辽宁
        schools.add(createSchool("大连理工大学", "DUT", "辽宁省大连市甘井子区凌工路2号", "0411-84708322", "info@dlut.edu.cn", "https://www.dlut.edu.cn"));
        
        // 吉林
        schools.add(createSchool("吉林大学", "JLU", "吉林省长春市朝阳区前进大街2699号", "0431-85168888", "info@jlu.edu.cn", "https://www.jlu.edu.cn"));
        
        // 福建
        schools.add(createSchool("厦门大学", "XMU", "福建省厦门市思明区思明南路422号", "0592-2186110", "info@xmu.edu.cn", "https://www.xmu.edu.cn"));
        
        // 安徽
        schools.add(createSchool("中国科学技术大学", "USTC", "安徽省合肥市蜀山区金寨路96号", "0551-63602453", "info@ustc.edu.cn", "https://www.ustc.edu.cn"));
        
        // 甘肃
        schools.add(createSchool("兰州大学", "LZU", "甘肃省兰州市城关区天水南路222号", "0931-8912114", "info@lzu.edu.cn", "https://www.lzu.edu.cn"));

        // 批量保存学校数据
        schoolService.saveBatch(schools);
    }

    private void initPermissions() {
        List<Permission> permissions = new ArrayList<>();

        // 系统管理权限
        permissions.add(createPermission("用户管理", "user:manage", "/api/users/**", "*", "用户管理权限"));
        permissions.add(createPermission("角色管理", "role:manage", "/api/roles/**", "*", "角色管理权限"));
        permissions.add(createPermission("学校管理", "school:manage", "/api/schools/**", "*", "学校管理权限"));
        permissions.add(createPermission("日志管理", "log:manage", "/api/logs/**", "*", "日志管理权限"));

        // 学生功能权限
        permissions.add(createPermission("学习计划", "study:plan", "/api/study-plans/**", "*", "学习计划权限"));
        permissions.add(createPermission("职业规划", "career:plan", "/api/career-plans/**", "*", "职业规划权限"));
        permissions.add(createPermission("学习资源", "resource:access", "/api/resources/**", "*", "学习资源权限"));
        permissions.add(createPermission("用户目标", "goal:manage", "/api/goals/**", "*", "用户目标权限"));
        permissions.add(createPermission("激励系统", "incentive:access", "/api/incentives/**", "*", "激励系统权限"));
        permissions.add(createPermission("专业管理", "major:manage", "/api/majors/**", "*", "专业管理权限"));

        // 批量保存权限数据
        permissionService.saveBatch(permissions);
    }

    private void initRoles() {
        List<Role> roles = new ArrayList<>();

        // 系统管理员角色
        Role adminRole = new Role();
        adminRole.setName("系统管理员");
        adminRole.setCode("ADMIN");
        adminRole.setDescription("系统管理员，拥有所有权限");
        adminRole.setSchoolId(0L); // 0表示系统级角色
        adminRole.setCreateTime(java.time.LocalDateTime.now().toString());
        adminRole.setUpdateTime(java.time.LocalDateTime.now().toString());
        roles.add(adminRole);

        // 学校管理员角色
        Role schoolAdminRole = new Role();
        schoolAdminRole.setName("学校管理员");
        schoolAdminRole.setCode("SCHOOL_ADMIN");
        schoolAdminRole.setDescription("学校管理员，拥有学校级管理权限");
        schoolAdminRole.setSchoolId(0L); // 后续会为每个学校创建对应的角色
        schoolAdminRole.setCreateTime(java.time.LocalDateTime.now().toString());
        schoolAdminRole.setUpdateTime(java.time.LocalDateTime.now().toString());
        roles.add(schoolAdminRole);

        // 学生角色
        Role studentRole = new Role();
        studentRole.setName("学生");
        studentRole.setCode("STUDENT");
        studentRole.setDescription("学生用户，拥有基本功能权限");
        studentRole.setSchoolId(0L);
        studentRole.setCreateTime(java.time.LocalDateTime.now().toString());
        studentRole.setUpdateTime(java.time.LocalDateTime.now().toString());
        roles.add(studentRole);

        // 批量保存角色数据
        roleService.saveBatch(roles);

        // 为系统管理员分配所有权限
        List<Permission> allPermissions = permissionService.getAllPermissions();
        List<Long> permissionIds = allPermissions.stream().map(Permission::getId).collect(java.util.stream.Collectors.toList());
        roleService.assignPermissions(1L, permissionIds); // 系统管理员角色ID为1
    }

    private School createSchool(String name, String code, String address, String contact, String email, String website) {
        School school = new School();
        school.setName(name);
        school.setCode(code);
        school.setAddress(address);
        school.setContact(contact);
        school.setEmail(email);
        school.setWebsite(website);
        school.setDescription(name + "是一所具有悠久历史和优良传统的综合性研究型大学，是国家\"211工程\"和\"985工程\"重点建设高校。");
        school.setCreateTime(java.time.LocalDateTime.now().toString());
        school.setUpdateTime(java.time.LocalDateTime.now().toString());
        return school;
    }

    private Permission createPermission(String name, String code, String url, String method, String description) {
        Permission permission = new Permission();
        permission.setName(name);
        permission.setCode(code);
        permission.setUrl(url);
        permission.setMethod(method);
        permission.setDescription(description);
        permission.setParentId(0L);
        permission.setSort(0);
        permission.setCreateTime(java.time.LocalDateTime.now().toString());
        permission.setUpdateTime(java.time.LocalDateTime.now().toString());
        return permission;
    }

    private void initColleges() {
        List<College> colleges = new ArrayList<>();
        colleges.add(createCollege("信息工程学院", "培养信息技术相关专业人才"));
        colleges.add(createCollege("经济管理学院", "培养经济管理相关专业人才"));
        colleges.add(createCollege("文学院", "培养文学相关专业人才"));
        colleges.add(createCollege("理学院", "培养理学相关专业人才"));
        colleges.add(createCollege("工学院", "培养工学相关专业人才"));
        colleges.add(createCollege("医学院", "培养医学相关专业人才"));
        colleges.add(createCollege("法学院", "培养法学相关专业人才"));
        colleges.add(createCollege("教育学院", "培养教育相关专业人才"));
        colleges.add(createCollege("艺术学院", "培养艺术相关专业人才"));
        // 批量保存学院数据
        collegeService.saveBatch(colleges);
    }

    private void initMajors() {
        List<Major> majors = new ArrayList<>();

        // 工学
        majors.add(createMajor("计算机科学与技术", "34", 1L, "培养具备计算机系统设计、软件开发、人工智能等能力的高级技术人才"));
        majors.add(createMajor("软件工程", "35", 1L, "培养具备软件系统分析、设计、开发和测试能力的专业人才"));
        majors.add(createMajor("电子信息工程", "36", 1L, "培养具备电子电路设计、信号处理、通信系统开发能力的专业人才"));
        majors.add(createMajor("自动化", "37", 1L, "培养具备自动控制系统设计、智能系统开发能力的专业人才"));
        majors.add(createMajor("机械工程", "38", 5L, "培养具备机械设计、制造、自动化能力的专业人才"));
        majors.add(createMajor("土木工程", "39", 5L, "培养具备土木工程设计、施工、管理能力的专业人才"));
        majors.add(createMajor("建筑学", "40", 5L, "培养具备建筑设计、城市规划、景观设计能力的专业人才"));
        majors.add(createMajor("电气工程及其自动化", "41", 5L, "培养具备电力系统设计、电气设备研发能力的专业人才"));

        // 理学
        majors.add(createMajor("数学与应用数学", "21", 4L, "培养具备数学理论研究、应用数学建模能力的专业人才"));
        majors.add(createMajor("物理学", "22", 4L, "培养具备物理学理论研究、实验技能的专业人才"));
        majors.add(createMajor("化学", "23", 4L, "培养具备化学理论研究、实验技能的专业人才"));
        majors.add(createMajor("生物科学", "24", 4L, "培养具备生物学理论研究、实验技能的专业人才"));
        majors.add(createMajor("统计学", "25", 4L, "培养具备统计分析、数据挖掘能力的专业人才"));

        // 经济学
        majors.add(createMajor("金融学", "11", 2L, "培养具备金融市场分析、投资管理能力的专业人才"));
        majors.add(createMajor("经济学", "12", 2L, "培养具备经济理论研究、经济分析能力的专业人才"));
        majors.add(createMajor("国际经济与贸易", "13", 2L, "培养具备国际贸易、国际金融能力的专业人才"));
        majors.add(createMajor("会计学", "14", 2L, "培养具备会计核算、财务管理能力的专业人才"));
        majors.add(createMajor("工商管理", "15", 2L, "培养具备企业管理、市场营销能力的专业人才"));

        // 文学
        majors.add(createMajor("汉语言文学", "01", 3L, "培养具备文学创作、语言研究能力的专业人才"));
        majors.add(createMajor("英语", "02", 3L, "培养具备英语语言应用、翻译能力的专业人才"));
        majors.add(createMajor("新闻学", "03", 3L, "培养具备新闻采编、媒体运营能力的专业人才"));
        majors.add(createMajor("广播电视学", "04", 3L, "培养具备广播电视节目制作、媒体运营能力的专业人才"));

        // 医学
        majors.add(createMajor("临床医学", "51", 6L, "培养具备临床医疗、疾病诊断能力的专业人才"));
        majors.add(createMajor("护理学", "52", 6L, "培养具备护理技能、健康管理能力的专业人才"));
        majors.add(createMajor("药学", "53", 6L, "培养具备药物研发、药品管理能力的专业人才"));
        majors.add(createMajor("口腔医学", "54", 6L, "培养具备口腔疾病诊断、治疗能力的专业人才"));

        // 法学
        majors.add(createMajor("法学", "61", 7L, "培养具备法律理论研究、法律实务能力的专业人才"));
        majors.add(createMajor("政治学与行政学", "62", 7L, "培养具备政治理论研究、行政管理能力的专业人才"));
        majors.add(createMajor("社会学", "63", 7L, "培养具备社会研究、社会服务能力的专业人才"));

        // 教育学
        majors.add(createMajor("教育学", "71", 8L, "培养具备教育理论研究、教育教学能力的专业人才"));
        majors.add(createMajor("学前教育", "72", 8L, "培养具备学前教育教学、儿童发展指导能力的专业人才"));
        majors.add(createMajor("体育教育", "73", 8L, "培养具备体育教学、运动训练能力的专业人才"));

        // 艺术学
        majors.add(createMajor("音乐学", "81", 9L, "培养具备音乐表演、音乐教育能力的专业人才"));
        majors.add(createMajor("美术学", "82", 9L, "培养具备美术创作、美术教育能力的专业人才"));
        majors.add(createMajor("设计学", "83", 9L, "培养具备艺术设计、创意设计能力的专业人才"));

        // 批量保存专业数据
        majorService.saveBatch(majors);
    }

    private College createCollege(String name, String description) {
        College college = new College();
        college.setName(name);
        college.setDescription(description);
        college.setCreateTime(java.time.LocalDateTime.now().toString());
        college.setUpdateTime(java.time.LocalDateTime.now().toString());
        return college;
    }

    private Major createMajor(String name, String code, Long collegeId, String description) {
        Major major = new Major();
        major.setName(name);
        major.setCode(code);
        major.setCollegeId(collegeId);
        major.setDescription(description);
        return major;
    }
    
    private void createUserTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'user'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE user " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "username VARCHAR(50) NOT NULL UNIQUE, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "name VARCHAR(50), " +
                            "gender VARCHAR(10), " +
                            "age INT, " +
                            "major_id BIGINT, " +
                            "grade VARCHAR(20), " +
                            "student_id VARCHAR(50), " +
                            "id_card VARCHAR(50), " +
                            "role VARCHAR(20), " +
                            "status VARCHAR(20), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "email VARCHAR(100), " +
                            "phone VARCHAR(20), " +
                            "school_id BIGINT, " +
                            "student_stage VARCHAR(50)) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("User table created successfully");
                }
            }
        }
    }
    
    private void createStudentProfileTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'student_profile'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE student_profile " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "student_id VARCHAR(50), " +
                            "name VARCHAR(50), " +
                            "college_id BIGINT, " +
                            "major_id BIGINT, " +
                            "class_id BIGINT, " +
                            "privacy_level INT DEFAULT 0, " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Student profile table created successfully");
                }
            }
        }
    }
    
    private void createLearningResourceTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'learning_resource'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE learning_resource " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "title VARCHAR(200) NOT NULL, " +
                            "description TEXT, " +
                            "type VARCHAR(50), " +
                            "url VARCHAR(500), " +
                            "school_id BIGINT, " +
                            "major_id BIGINT, " +
                            "status VARCHAR(20) DEFAULT 'published', " +
                            "student_stage VARCHAR(50), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Learning resource table created successfully");
                } else {
                    updateLearningResourceTable(conn, stmt);
                }
            }
        }
    }
    
    private void createOperationLogTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'operation_log'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE operation_log " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT, " +
                            "operation VARCHAR(100), " +
                            "description TEXT, " +
                            "ip_address VARCHAR(50), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Operation log table created successfully");
                }
            }
        }
    }
    
    private void createStudyPlanTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'study_plan'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE study_plan " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "title VARCHAR(200) NOT NULL, " +
                            "description TEXT, " +
                            "start_date VARCHAR(20), " +
                            "end_date VARCHAR(20), " +
                            "status VARCHAR(20), " +
                            "progress DOUBLE DEFAULT 0, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Study plan table created successfully");
                }
            }
        }
    }
    
    private void createStudyPlanDetailTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'study_plan_detail'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE study_plan_detail " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "plan_id BIGINT NOT NULL, " +
                            "task_name VARCHAR(200), " +
                            "description TEXT, " +
                            "start_date VARCHAR(20), " +
                            "end_date VARCHAR(20), " +
                            "status VARCHAR(20), " +
                            "progress DOUBLE DEFAULT 0, " +
                            "priority VARCHAR(20), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "FOREIGN KEY (plan_id) REFERENCES study_plan(id) ON DELETE CASCADE) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Study plan detail table created successfully");
                }
            }
        }
    }
    
    private void createUserGoalTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'user_goal'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE user_goal " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "title VARCHAR(200), " +
                            "description TEXT, " +
                            "end_date VARCHAR(20), " +
                            "priority VARCHAR(20), " +
                            "status VARCHAR(20), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("User goal table created successfully");
                }
            }
        }
    }
    
    private void createCareerPlanTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'career_plan'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE career_plan " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "user_name VARCHAR(50), " +
                            "school_id BIGINT, " +
                            "career_goal TEXT, " +
                            "industry VARCHAR(100), " +
                            "position VARCHAR(100), " +
                            "skill_requirements TEXT, " +
                            "education_requirements TEXT, " +
                            "experience_requirements TEXT, " +
                            "short_term_goals TEXT, " +
                            "medium_term_goals TEXT, " +
                            "long_term_goals TEXT, " +
                            "action_plan TEXT, " +
                            "status VARCHAR(20), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Career plan table created successfully");
                }
            }
        }
    }
    
    private void createIncentiveTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'incentive'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE incentive " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100), " +
                            "description TEXT, " +
                            "type VARCHAR(50), " +
                            "points INT, " +
                            "icon VARCHAR(255), " +
                            "criteria TEXT, " +
                            "status VARCHAR(20)) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Incentive table created successfully");
                }
            }
        }
    }
    
    private void createUserIncentiveTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'user_incentive'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE user_incentive " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "incentive_id BIGINT NOT NULL, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("User incentive table created successfully");
                }
            }
        }
    }
    
    private void createCheckinTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'checkin'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE checkin " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "checkin_time TIMESTAMP, " +
                            "status VARCHAR(20)) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Checkin table created successfully");
                }
            }
        }
    }
    
    private void createCommunityTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'community'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE community " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100), " +
                            "description TEXT, " +
                            "type VARCHAR(50), " +
                            "category VARCHAR(50), " +
                            "creator_id BIGINT, " +
                            "status VARCHAR(20)) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Community table created successfully");
                }
            }
        }
    }
    
    private void createStudyGroupTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'study_group'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE study_group " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100), " +
                            "description TEXT, " +
                            "creator_id BIGINT, " +
                            "max_members INT, " +
                            "status VARCHAR(20)) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Study group table created successfully");
                }
            }
        }
    }
    
    private void createGroupMemberTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'group_member'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE group_member " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "group_id BIGINT NOT NULL, " +
                            "user_id BIGINT NOT NULL, " +
                            "role VARCHAR(20), " +
                            "join_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Group member table created successfully");
                }
            }
        }
    }
    
    private void createGroupTaskTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'group_task'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE group_task " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "group_id BIGINT NOT NULL, " +
                            "title VARCHAR(200), " +
                            "description TEXT, " +
                            "deadline TIMESTAMP, " +
                            "status VARCHAR(20)) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Group task table created successfully");
                }
            }
        }
    }
    
    private void createStudyNoteTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'study_note'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE study_note " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "title VARCHAR(200), " +
                            "content TEXT, " +
                            "subject VARCHAR(100), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Study note table created successfully");
                }
            }
        }
    }
    
    private void createDiscussionTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'discussion'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE discussion " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "title VARCHAR(200), " +
                            "content TEXT, " +
                            "community_id BIGINT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Discussion table created successfully");
                }
            }
        }
    }
    
    private void createReplyTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'reply'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE reply " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "discussion_id BIGINT NOT NULL, " +
                            "user_id BIGINT NOT NULL, " +
                            "content TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Reply table created successfully");
                }
            }
        }
    }
    
    private void createLearningStatsTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'learning_stats'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE learning_stats " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "stats_date DATE, " +
                            "study_duration INT, " +
                            "task_completion_rate INT, " +
                            "checkin_count INT, " +
                            "resource_usage_count INT, " +
                            "note_count INT, " +
                            "discussion_count INT) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Learning stats table created successfully");
                }
            }
        }
    }
    
    private void createLearningReviewTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'learning_review'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE learning_review " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "review_date DATE, " +
                            "content TEXT, " +
                            "rating INT) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Learning review table created successfully");
                }
            }
        }
    }
    
    private void createSkillTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'skill'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE skill " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100), " +
                            "category VARCHAR(50), " +
                            "description TEXT) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Skill table created successfully");
                }
            }
        }
    }
    
    private void createUserSkillTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'user_skill'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE user_skill " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "skill_id BIGINT NOT NULL, " +
                            "proficiency INT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("User skill table created successfully");
                }
            }
        }
    }
    
    private void createSkillQuestionTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'skill_question'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE skill_question " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "skill_id BIGINT NOT NULL, " +
                            "question TEXT, " +
                            "options TEXT, " +
                            "correct_answer INT) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Skill question table created successfully");
                }
            }
        }
    }
    
    private void createOfflineDataTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'offline_data'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE offline_data " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "data_type VARCHAR(50), " +
                            "data_content TEXT, " +
                            "sync_status VARCHAR(20), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Offline data table created successfully");
                }
            }
        }
    }
    
    private void createPrivacySettingTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'privacy_setting'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE privacy_setting " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "setting_name VARCHAR(100), " +
                            "setting_value VARCHAR(255), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Privacy setting table created successfully");
                }
            }
        }
    }
    
    private void createCampusCourseTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'campus_course'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE campus_course " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(200), " +
                            "description TEXT, " +
                            "school_id BIGINT, " +
                            "credits INT, " +
                            "hours INT) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Campus course table created successfully");
                }
            }
        }
    }
    
    private void createCampusActivityTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'campus_activity'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE campus_activity " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "title VARCHAR(200), " +
                            "description TEXT, " +
                            "location VARCHAR(200), " +
                            "start_time TIMESTAMP, " +
                            "end_time TIMESTAMP, " +
                            "organizer_id BIGINT, " +
                            "status VARCHAR(20)) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Campus activity table created successfully");
                }
            }
        }
    }
    
    private void createExamTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'exam'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE exam " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "exam_name VARCHAR(200), " +
                            "exam_type VARCHAR(50), " +
                            "description TEXT, " +
                            "exam_date TIMESTAMP, " +
                            "location VARCHAR(200), " +
                            "duration INT, " +
                            "status VARCHAR(20), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Exam table created successfully");
                }
            }
        }
    }
    
    private void createCollegeTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'college'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE college " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "description TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("College table created successfully");
                }
            }
        }
    }
    
    private void createMajorTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            String checkTableSql = "SHOW TABLES LIKE 'major'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    String createSql = "CREATE TABLE major " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "code VARCHAR(10) NOT NULL UNIQUE, " +
                            "college_id BIGINT, " +
                            "description TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci";
                    stmt.executeUpdate(createSql);
                    System.out.println("Major table created successfully");
                }
            }
        }
    }
}
