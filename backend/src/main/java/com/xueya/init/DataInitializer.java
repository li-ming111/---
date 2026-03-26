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
                
                // 初始化激励数据
                if (incentiveService.count() == 0) {
                    System.out.println("开始初始化激励数据...");
                    initIncentives();
                    System.out.println("激励数据初始化完成！");
                }
                
                // 初始化社区数据
                if (communityService.count() == 0) {
                    System.out.println("开始初始化社区数据...");
                    initCommunities();
                    System.out.println("社区数据初始化完成！");
                }
                
                // 初始化学生账号2023020616的数据
                System.out.println("开始初始化学生账号2023020616的数据...");
                initStudentDataFor2023020616();
                System.out.println("学生账号2023020616数据初始化完成！");
                
                // 初始化学生账号232321200501064619的数据
                System.out.println("开始初始化学生账号232321200501064619的数据...");
                initUser8Data();
                System.out.println("学生账号232321200501064619数据初始化完成！");
                
                System.out.println("数据库表结构检查完成，数据初始化已完成");
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
    
    private void initIncentives() {
        List<Incentive> incentives = new ArrayList<>();
        
        // 学习类激励
        incentives.add(createIncentive("学习达人", "完成学习任务获得的激励", "学习", 100, "学习达人图标", "连续7天完成学习任务"));
        incentives.add(createIncentive("知识渊博", "学习时长达到一定时间获得的激励", "学习", 200, "知识渊博图标", "累计学习时长达到100小时"));
        incentives.add(createIncentive("学习新星", "首次完成学习任务获得的激励", "学习", 50, "学习新星图标", "首次完成学习任务"));
        
        // 目标类激励
        incentives.add(createIncentive("目标达成", "完成目标获得的激励", "目标", 150, "目标达成图标", "完成一个长期目标"));
        incentives.add(createIncentive("目标先锋", "设定目标获得的激励", "目标", 80, "目标先锋图标", "设定第一个目标"));
        
        // 打卡类激励
        incentives.add(createIncentive("坚持不懈", "连续打卡获得的激励", "打卡", 200, "坚持不懈图标", "连续打卡30天"));
        incentives.add(createIncentive("打卡达人", "累计打卡获得的激励", "打卡", 300, "打卡达人图标", "累计打卡100天"));
        
        // 社区类激励
        incentives.add(createIncentive("社区活跃", "在社区活跃获得的激励", "社区", 100, "社区活跃图标", "在社区发布10个帖子"));
        incentives.add(createIncentive("社区贡献", "为社区做出贡献获得的激励", "社区", 150, "社区贡献图标", "获得50个帖子点赞"));
        
        // 批量保存激励数据
        incentiveService.saveBatch(incentives);
    }
    
    private void initCommunities() {
        List<Community> communities = new ArrayList<>();
        
        // 学习类社区
        communities.add(createCommunity("学习交流", "分享学习经验和资源的社区", "学习", "学习交流"));
        communities.add(createCommunity("考试备考", "讨论考试相关话题的社区", "学习", "考试备考"));
        
        // 兴趣类社区
        communities.add(createCommunity("技术分享", "分享技术知识和经验的社区", "兴趣", "技术"));
        communities.add(createCommunity("文学艺术", "讨论文学和艺术的社区", "兴趣", "文学艺术"));
        
        // 职业类社区
        communities.add(createCommunity("职业规划", "讨论职业规划和发展的社区", "职业", "职业规划"));
        communities.add(createCommunity("就业信息", "分享就业信息和经验的社区", "职业", "就业信息"));
        
        // 批量保存社区数据
        communityService.saveBatch(communities);
    }
    
    private void initStudentData() {
        // 初始化学生的学习统计数据
        initLearningStats();
        
        // 初始化学生的打卡数据
        initCheckinData();
        
        // 初始化学生的目标数据
        initUserGoals();
        
        // 初始化学生的职业规划数据
        initCareerPlan();
    }
    
    private void initLearningStats() {
        List<LearningStats> statsList = new ArrayList<>();
        
        // 生成最近7天的学习统计数据
        java.util.Date now = new java.util.Date();
        for (int i = 6; i >= 0; i--) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(java.util.Calendar.DAY_OF_MONTH, -i);
            java.util.Date date = calendar.getTime();
            String dateStr = new java.sql.Date(date.getTime()).toString();
            
            // 检查是否已经存在该用户在该日期的记录
            QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", 1L).eq("stats_date", dateStr);
            if (learningStatsService.count(queryWrapper) == 0) {
                LearningStats stats = new LearningStats();
                stats.setUserId(1L); // 假设学生ID为1
                stats.setStatsDate(dateStr);
                stats.setStudyDuration(30 + (int)(Math.random() * 60)); // 30-90分钟
                stats.setTaskCompletionRate(60 + (int)(Math.random() * 40)); // 60-100%
                stats.setCheckinCount(1);
                stats.setResourceUsageCount(2 + (int)(Math.random() * 5)); // 2-6次
                stats.setNoteCount(1 + (int)(Math.random() * 3)); // 1-3个
                stats.setDiscussionCount(1 + (int)(Math.random() * 2)); // 1-2个
                statsList.add(stats);
            }
        }
        
        // 批量保存学习统计数据
        if (!statsList.isEmpty()) {
            learningStatsService.saveBatch(statsList);
        }
    }
    
    private void initCheckinData() {
        List<Checkin> checkinList = new ArrayList<>();
        
        // 生成最近7天的打卡数据
        java.util.Date now = new java.util.Date();
        for (int i = 6; i >= 0; i--) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(java.util.Calendar.DAY_OF_MONTH, -i);
            java.util.Date date = calendar.getTime();
            
            // 检查是否已经存在该用户在该日期的打卡记录
            QueryWrapper<Checkin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", 1L);
            // 由于打卡时间是精确到秒的，我们检查日期部分是否相同
            queryWrapper.apply("DATE(checkin_time) = DATE('" + new java.sql.Date(date.getTime()) + "')");
            if (checkinService.count(queryWrapper) == 0) {
                Checkin checkin = new Checkin();
                checkin.setUserId(1L); // 假设学生ID为1
                checkin.setCheckinTime(java.time.LocalDateTime.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault()));
                checkin.setStatus("已打卡");
                checkinList.add(checkin);
            }
        }
        
        // 批量保存打卡数据
        if (!checkinList.isEmpty()) {
            checkinService.saveBatch(checkinList);
        }
    }
    
    private void initUserGoals() {
        List<UserGoal> goalsList = new ArrayList<>();
        
        // 添加短期目标
        QueryWrapper<UserGoal> shortTermGoalQuery = new QueryWrapper<>();
        shortTermGoalQuery.eq("user_id", 1L).eq("title", "完成Java课程学习");
        if (userGoalService.count(shortTermGoalQuery) == 0) {
            UserGoal shortTermGoal = new UserGoal();
            shortTermGoal.setUserId(1L); // 假设学生ID为1
            shortTermGoal.setTitle("完成Java课程学习");
            shortTermGoal.setDescription("在2周内完成Java基础课程的学习");
            shortTermGoal.setEndDate(java.time.LocalDate.now().plusWeeks(2).toString());
            shortTermGoal.setPriority("high");
            shortTermGoal.setStatus("active");
            shortTermGoal.setCreateTime(java.time.LocalDateTime.now().toString());
            goalsList.add(shortTermGoal);
        }
        
        // 添加长期目标
        QueryWrapper<UserGoal> longTermGoalQuery = new QueryWrapper<>();
        longTermGoalQuery.eq("user_id", 1L).eq("title", "获得软件工程师认证");
        if (userGoalService.count(longTermGoalQuery) == 0) {
            UserGoal longTermGoal = new UserGoal();
            longTermGoal.setUserId(1L); // 假设学生ID为1
            longTermGoal.setTitle("获得软件工程师认证");
            longTermGoal.setDescription("在6个月内通过软件工程师认证考试");
            longTermGoal.setEndDate(java.time.LocalDate.now().plusMonths(6).toString());
            longTermGoal.setPriority("medium");
            longTermGoal.setStatus("active");
            longTermGoal.setCreateTime(java.time.LocalDateTime.now().toString());
            goalsList.add(longTermGoal);
        }
        
        // 批量保存目标数据
        if (!goalsList.isEmpty()) {
            userGoalService.saveBatch(goalsList);
        }
    }
    
    private void initCareerPlan() {
        // 检查是否已经存在该用户的职业规划记录
        QueryWrapper<CareerPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", 1L);
        if (careerPlanService.count(queryWrapper) == 0) {
            CareerPlan careerPlan = new CareerPlan();
            careerPlan.setUserId(1L); // 假设学生ID为1
            careerPlan.setUserName("测试用户");
            careerPlan.setSchoolId(1L); // 假设学校ID为1
            careerPlan.setCareerGoal("成为一名全栈开发工程师");
            careerPlan.setIndustry("互联网");
            careerPlan.setPosition("全栈开发工程师");
            careerPlan.setSkillRequirements("Java、Python、前端框架、数据库、DevOps");
            careerPlan.setEducationRequirements("本科及以上学历，计算机相关专业");
            careerPlan.setExperienceRequirements("2年以上相关工作经验");
            careerPlan.setShortTermGoals("1. 完成Java基础课程学习\n2. 学习前端框架\n3. 完成一个完整的项目");
            careerPlan.setMediumTermGoals("1. 获得软件工程师认证\n2. 积累项目经验\n3. 学习DevOps相关技术");
            careerPlan.setLongTermGoals("1. 成为全栈开发工程师\n2. 参与大型项目开发\n3. 提升技术领导力");
            careerPlan.setActionPlan("1. 制定详细的学习计划\n2. 定期参加技术培训和会议\n3. 参与开源项目");
            careerPlan.setStatus("active");
            careerPlan.setCreateTime(java.time.LocalDateTime.now().toString());
            careerPlan.setUpdateTime(java.time.LocalDateTime.now().toString());
            
            // 保存职业规划数据
            careerPlanService.save(careerPlan);
        }
    }
    
    private Incentive createIncentive(String name, String description, String type, int points, String icon, String criteria) {
        Incentive incentive = new Incentive();
        incentive.setName(name);
        incentive.setDescription(description);
        incentive.setType(type);
        incentive.setPoints(points);
        incentive.setIcon(icon);
        incentive.setCriteria(criteria);
        incentive.setStatus("active");
        return incentive;
    }
    
    private Community createCommunity(String name, String description, String type, String category) {
        Community community = new Community();
        community.setName(name);
        community.setDescription(description);
        community.setType(type);
        community.setCategory(category);
        community.setCreatorId(1L); // 假设创建者ID为1
        community.setStatus("活跃");
        return community;
    }
    
    private void initUser8Data() {
        System.out.println("开始为用户8创建数据...");
        
        // 创建学习计划
        initUser8StudyPlan();
        
        // 创建学习资源
        initUser8LearningResources();
        
        // 创建职业规划
        initUser8CareerPlan();
        
        // 创建校园适应数据
        initUser8CampusAdaptation();
        
        // 创建笔记数据
        initUser8StudyNotes();
        
        // 创建学习统计数据
        initUser8LearningStats();
        
        // 创建打卡数据
        initUser8CheckinData();
        
        // 创建考试数据
        initUser8Exams();
        
        System.out.println("用户8数据初始化完成！");

    }
    
    private void initUser8StudyPlan() {
        System.out.println("创建用户8的学习计划...");
        
        // 检查是否已有学习计划
        QueryWrapper<StudyPlan> planQueryWrapper = new QueryWrapper<>();
        planQueryWrapper.eq("user_id", 8L).eq("title", "2026春季学期学习计划");
        if (studyPlanService.count(planQueryWrapper) == 0) {
            // 创建学习计划
            StudyPlan semesterPlan = new StudyPlan();
            semesterPlan.setUserId(8L);
            semesterPlan.setTitle("2026春季学期学习计划");
            semesterPlan.setDescription("2026年春季学期的主要学习任务和目标");
            semesterPlan.setStartDate("2026-02-20");
            semesterPlan.setEndDate("2026-06-30");
            semesterPlan.setStatus("active");
            semesterPlan.setProgress(0.0);
            semesterPlan.setCreateTime(java.time.LocalDateTime.now().toString());
            semesterPlan.setUpdateTime(java.time.LocalDateTime.now().toString());
            
            boolean planCreated = studyPlanService.save(semesterPlan);
            if (planCreated) {
                System.out.println("学习计划创建成功，ID: " + semesterPlan.getId());
                
                // 创建学习计划任务
                System.out.println("创建学习计划任务...");
                
                // 高等数学学习任务
                StudyPlanDetail mathTask = new StudyPlanDetail();
                mathTask.setPlanId(semesterPlan.getId());
                mathTask.setTaskName("高等数学课程学习");
                mathTask.setDescription("完成高等数学下册的学习，重点掌握微积分和线性代数");
                mathTask.setStartDate("2026-02-20");
                mathTask.setEndDate("2026-06-15");
                mathTask.setStatus("active");
                mathTask.setProgress(0.0);
                mathTask.setPriority("high");
                mathTask.setCreateTime(java.time.LocalDateTime.now().toString());
                mathTask.setUpdateTime(java.time.LocalDateTime.now().toString());
                studyPlanDetailService.save(mathTask);
                System.out.println("高等数学学习任务创建成功");
                
                // 英语学习任务
                StudyPlanDetail englishTask = new StudyPlanDetail();
                englishTask.setPlanId(semesterPlan.getId());
                englishTask.setTaskName("英语四级备考");
                englishTask.setDescription("准备英语四级考试，提高听力和阅读能力");
                englishTask.setStartDate("2026-02-20");
                englishTask.setEndDate("2026-06-10");
                englishTask.setStatus("active");
                englishTask.setProgress(0.0);
                englishTask.setPriority("high");
                englishTask.setCreateTime(java.time.LocalDateTime.now().toString());
                englishTask.setUpdateTime(java.time.LocalDateTime.now().toString());
                studyPlanDetailService.save(englishTask);
                System.out.println("英语四级备考任务创建成功");
                
                // 专业课程学习任务
                StudyPlanDetail majorTask = new StudyPlanDetail();
                majorTask.setPlanId(semesterPlan.getId());
                majorTask.setTaskName("专业课程学习");
                majorTask.setDescription("完成数据结构、计算机网络等专业课程的学习");
                majorTask.setStartDate("2026-02-20");
                majorTask.setEndDate("2026-06-20");
                majorTask.setStatus("active");
                majorTask.setProgress(0.0);
                majorTask.setPriority("high");
                majorTask.setCreateTime(java.time.LocalDateTime.now().toString());
                majorTask.setUpdateTime(java.time.LocalDateTime.now().toString());
                studyPlanDetailService.save(majorTask);
                System.out.println("专业课程学习任务创建成功");
                
                // 编程实践任务
                StudyPlanDetail codingTask = new StudyPlanDetail();
                codingTask.setPlanId(semesterPlan.getId());
                codingTask.setTaskName("编程实践");
                codingTask.setDescription("完成至少3个编程项目，提高代码能力");
                codingTask.setStartDate("2026-03-01");
                codingTask.setEndDate("2026-06-25");
                codingTask.setStatus("active");
                codingTask.setProgress(0.0);
                codingTask.setPriority("medium");
                codingTask.setCreateTime(java.time.LocalDateTime.now().toString());
                codingTask.setUpdateTime(java.time.LocalDateTime.now().toString());
                studyPlanDetailService.save(codingTask);
                System.out.println("编程实践任务创建成功");
            } else {
                System.out.println("学习计划创建失败");
            }
        } else {
            System.out.println("学习计划已存在，跳过创建");
        }
    }
    
    private void initUser8LearningResources() {
        System.out.println("创建用户8的学习资源...");
        
        try {
            // 直接使用JDBC创建学习资源
            try (java.sql.Connection conn = dataSource.getConnection();
                 java.sql.Statement stmt = conn.createStatement()) {
                
                // 检查是否已存在学习资源
                String checkSql = "SELECT COUNT(*) FROM learning_resource";
                try (java.sql.ResultSet rs = stmt.executeQuery(checkSql)) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // 插入学习资源1
                        String sql1 = "INSERT INTO learning_resource (title, description, category, tags, uploader_id, uploader_name, school_id, student_stage, file_url, file_name, file_type, file_size, view_count, download_count, status, create_time, update_time) VALUES ('高等数学下册教程', '高等数学下册的详细教程，包括微积分、线性代数等内容', '数学', '高等数学,微积分,线性代数', 1, '管理员', 1, '大学本科', '#', '高等数学下册教程.pdf', 'application/pdf', 10485760, 0, 0, 'active', '" + java.time.LocalDateTime.now() + "', '" + java.time.LocalDateTime.now() + "')";
                        stmt.executeUpdate(sql1);
                        System.out.println("学习资源1创建成功");
                        
                        // 插入学习资源2
                        String sql2 = "INSERT INTO learning_resource (title, description, category, tags, uploader_id, uploader_name, school_id, student_stage, file_url, file_name, file_type, file_size, view_count, download_count, status, create_time, update_time) VALUES ('英语四级词汇手册', '英语四级考试必备词汇手册，包含高频词汇和例句', '英语', '英语四级,词汇,备考', 1, '管理员', 1, '大学本科', '#', '英语四级词汇手册.pdf', 'application/pdf', 5242880, 0, 0, 'active', '" + java.time.LocalDateTime.now() + "', '" + java.time.LocalDateTime.now() + "')";
                        stmt.executeUpdate(sql2);
                        System.out.println("学习资源2创建成功");
                        
                        // 插入学习资源3
                        String sql3 = "INSERT INTO learning_resource (title, description, category, tags, uploader_id, uploader_name, school_id, student_stage, file_url, file_name, file_type, file_size, view_count, download_count, status, create_time, update_time) VALUES ('数据结构与算法教程', '数据结构与算法的详细教程，包括链表、树、图等数据结构和各种算法', '计算机科学', '数据结构,算法,编程', 1, '管理员', 1, '大学本科', '#', '数据结构与算法教程.pdf', 'application/pdf', 8388608, 0, 0, 'active', '" + java.time.LocalDateTime.now() + "', '" + java.time.LocalDateTime.now() + "')";
                        stmt.executeUpdate(sql3);
                        System.out.println("学习资源3创建成功");
                    } else {
                        System.out.println("学习资源已存在，跳过创建");
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("创建学习资源失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initUser8CareerPlan() {
        System.out.println("创建用户8的职业规划...");
        
        // 检查是否已经存在该用户的职业规划记录
        QueryWrapper<CareerPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", 8L);
        if (careerPlanService.count(queryWrapper) == 0) {
            CareerPlan careerPlan = new CareerPlan();
            careerPlan.setUserId(8L);
            careerPlan.setUserName("测试用户");
            careerPlan.setSchoolId(1L);
            careerPlan.setCareerGoal("成为一名全栈开发工程师");
            careerPlan.setIndustry("互联网");
            careerPlan.setPosition("全栈开发工程师");
            careerPlan.setSkillRequirements("Java、Python、前端框架、数据库、DevOps");
            careerPlan.setEducationRequirements("本科及以上学历，计算机相关专业");
            careerPlan.setExperienceRequirements("2年以上相关工作经验");
            careerPlan.setShortTermGoals("1. 完成Java基础课程学习\n2. 学习前端框架\n3. 完成一个完整的项目");
            careerPlan.setMediumTermGoals("1. 获得软件工程师认证\n2. 积累项目经验\n3. 学习DevOps相关技术");
            careerPlan.setLongTermGoals("1. 成为全栈开发工程师\n2. 参与大型项目开发\n3. 提升技术领导力");
            careerPlan.setActionPlan("1. 制定详细的学习计划\n2. 定期参加技术培训和会议\n3. 参与开源项目");
            careerPlan.setStatus("active");
            careerPlan.setCreateTime(java.time.LocalDateTime.now().toString());
            careerPlan.setUpdateTime(java.time.LocalDateTime.now().toString());
            
            // 保存职业规划数据
            boolean saved = careerPlanService.save(careerPlan);
            if (saved) {
                System.out.println("职业规划创建成功");
            } else {
                System.out.println("职业规划创建失败");
            }
        } else {
            System.out.println("职业规划已存在，跳过创建");
        }
    }
    
    private void initUser8CampusAdaptation() {
        System.out.println("创建用户8的校园适应数据...");
        
        try {
            // 直接使用JDBC创建校园适应数据
            try (java.sql.Connection conn = dataSource.getConnection();
                 java.sql.Statement stmt = conn.createStatement()) {
                
                // 检查是否已存在校园课程
                String checkCourseSql = "SELECT COUNT(*) FROM campus_course WHERE user_id = 8 AND course_code = 'MATH101'";
                try (java.sql.ResultSet rs = stmt.executeQuery(checkCourseSql)) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // 插入校园课程
                        String courseSql = "INSERT INTO campus_course (user_id, course_name, course_code, teacher, classroom, day_of_week, start_time, end_time, semester, create_time, update_time) VALUES (8, '高等数学', 'MATH101', '张教授', '教学楼A101', '周一', '08:00', '09:40', '2026春季', '" + java.time.LocalDateTime.now() + "', '" + java.time.LocalDateTime.now() + "')";
                        stmt.executeUpdate(courseSql);
                        System.out.println("校园课程创建成功");
                    } else {
                        System.out.println("校园课程已存在，跳过创建");
                    }
                }
                
                // 检查是否已存在校园活动
                String checkActivitySql = "SELECT COUNT(*) FROM campus_activity WHERE activity_name = '新生 orientation'";
                try (java.sql.ResultSet rs = stmt.executeQuery(checkActivitySql)) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // 插入校园活动
                        String activitySql = "INSERT INTO campus_activity (activity_name, activity_type, description, location, start_time, end_time, capacity, registered_count, status, create_time, update_time) VALUES ('新生 orientation', 'orientation', '帮助新生适应校园生活的 orientation 活动', '体育馆', '" + java.time.LocalDateTime.now().plusDays(1) + "', '" + java.time.LocalDateTime.now().plusDays(1).plusHours(3) + "', 200, 150, 'active', '" + java.time.LocalDateTime.now() + "', '" + java.time.LocalDateTime.now() + "')";
                        stmt.executeUpdate(activitySql);
                        System.out.println("校园活动创建成功");
                    } else {
                        System.out.println("校园活动已存在，跳过创建");
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("创建校园适应数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initUser8StudyNotes() {
        System.out.println("创建用户8的笔记数据...");
        
        try {
            // 直接使用JDBC创建笔记数据
            try (java.sql.Connection conn = dataSource.getConnection();
                 java.sql.Statement stmt = conn.createStatement()) {
                
                // 检查是否已存在用户8的笔记数据
                String checkSql = "SELECT COUNT(*) FROM study_note WHERE user_id = 8";
                try (java.sql.ResultSet rs = stmt.executeQuery(checkSql)) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // 插入笔记1
                        String noteSql1 = "INSERT INTO study_note (user_id, group_id, title, content, type, view_count, like_count) VALUES (8, 1, '高等数学笔记', '微积分基本定理：牛顿-莱布尼茨公式...', '学习笔记', 0, 0)";
                        stmt.executeUpdate(noteSql1);
                        System.out.println("笔记1创建成功");
                        
                        // 插入笔记2
                        String noteSql2 = "INSERT INTO study_note (user_id, group_id, title, content, type, view_count, like_count) VALUES (8, 1, 'Java基础笔记', 'Java面向对象编程：封装、继承、多态...', '学习笔记', 0, 0)";
                        stmt.executeUpdate(noteSql2);
                        System.out.println("笔记2创建成功");
                    } else {
                        System.out.println("笔记数据已存在，跳过创建");
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("创建笔记数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void initUser8LearningStats() {
        System.out.println("创建用户8的学习统计数据...");
        List<LearningStats> statsList = new ArrayList<>();
        
        // 生成最近7天的学习统计数据
        java.util.Date now = new java.util.Date();
        for (int i = 6; i >= 0; i--) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(java.util.Calendar.DAY_OF_MONTH, -i);
            java.util.Date date = calendar.getTime();
            String dateStr = new java.sql.Date(date.getTime()).toString();
            
            // 检查是否已经存在该用户在该日期的记录
            QueryWrapper<LearningStats> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", 8L).eq("stats_date", dateStr);
            if (learningStatsService.count(queryWrapper) == 0) {
                LearningStats stats = new LearningStats();
                stats.setUserId(8L);
                stats.setStatsDate(dateStr);
                stats.setStudyDuration(30 + (int)(Math.random() * 60)); // 30-90分钟
                stats.setTaskCompletionRate(60 + (int)(Math.random() * 40)); // 60-100%
                stats.setCheckinCount(1);
                stats.setResourceUsageCount(2 + (int)(Math.random() * 5)); // 2-6次
                stats.setNoteCount(1 + (int)(Math.random() * 3)); // 1-3个
                stats.setDiscussionCount(1 + (int)(Math.random() * 2)); // 1-2个
                statsList.add(stats);
            }
        }
        
        // 批量保存学习统计数据
        if (!statsList.isEmpty()) {
            learningStatsService.saveBatch(statsList);
            System.out.println("用户8的学习统计数据创建成功");
        }
    }
    
    private void initUser8CheckinData() {
        System.out.println("创建用户8的打卡数据...");
        List<Checkin> checkinList = new ArrayList<>();
        
        // 生成最近7天的打卡数据
        java.util.Date now = new java.util.Date();
        for (int i = 6; i >= 0; i--) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(java.util.Calendar.DAY_OF_MONTH, -i);
            java.util.Date date = calendar.getTime();
            
            // 检查是否已经存在该用户在该日期的打卡记录
            QueryWrapper<Checkin> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", 8L);
            // 由于打卡时间是精确到秒的，我们检查日期部分是否相同
            queryWrapper.apply("DATE(checkin_time) = DATE('" + new java.sql.Date(date.getTime()) + "')");
            if (checkinService.count(queryWrapper) == 0) {
                Checkin checkin = new Checkin();
                checkin.setUserId(8L);
                checkin.setCheckinTime(java.time.LocalDateTime.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault()));
                checkin.setStatus("已打卡");
                checkinList.add(checkin);
            }
        }
        
        // 批量保存打卡数据
        if (!checkinList.isEmpty()) {
            checkinService.saveBatch(checkinList);
            System.out.println("用户8的打卡数据创建成功");
        }
    }

    private void createStudyPlanTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'study_plan'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE study_plan " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "user_name VARCHAR(100), " +
                            "school_id BIGINT, " +
                            "title VARCHAR(255) NOT NULL, " +
                            "description TEXT, " +
                            "start_date VARCHAR(30), " +
                            "end_date VARCHAR(30), " +
                            "status VARCHAR(20) DEFAULT 'active', " +
                            "progress DOUBLE DEFAULT 0, " +
                            "priority VARCHAR(20), " +
                            "create_time VARCHAR(30), " +
                            "update_time VARCHAR(30))";
                    stmt.executeUpdate(createSql);
                    System.out.println("StudyPlan table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE study_plan";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("StudyPlan table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void createStudyPlanDetailTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'study_plan_detail'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE study_plan_detail " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "plan_id BIGINT NOT NULL, " +
                            "task_name VARCHAR(255) NOT NULL, " +
                            "description TEXT, " +
                            "start_date VARCHAR(30), " +
                            "end_date VARCHAR(30), " +
                            "status VARCHAR(20) DEFAULT 'active', " +
                            "progress DOUBLE DEFAULT 0, " +
                            "priority VARCHAR(20), " +
                            "create_time VARCHAR(30), " +
                            "update_time VARCHAR(30))";
                    stmt.executeUpdate(createSql);
                    System.out.println("StudyPlanDetail table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE study_plan_detail";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("StudyPlanDetail table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void createUserGoalTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'user_goal'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE user_goal " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "title VARCHAR(255) NOT NULL, " +
                            "description TEXT, " +
                            "target_date VARCHAR(30), " +
                            "priority VARCHAR(20), " +
                            "status VARCHAR(20) DEFAULT 'active', " +
                            "create_time VARCHAR(30))";
                    stmt.executeUpdate(createSql);
                    System.out.println("UserGoal table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE user_goal";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("UserGoal table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void createCareerPlanTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'career_plan'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE career_plan " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "user_name VARCHAR(100), " +
                            "school_id BIGINT, " +
                            "career_goal VARCHAR(255), " +
                            "industry VARCHAR(100), " +
                            "position VARCHAR(100), " +
                            "skill_requirements TEXT, " +
                            "education_requirements TEXT, " +
                            "experience_requirements TEXT, " +
                            "short_term_goals TEXT, " +
                            "medium_term_goals TEXT, " +
                            "long_term_goals TEXT, " +
                            "action_plan TEXT, " +
                            "status VARCHAR(20) DEFAULT 'active', " +
                            "create_time VARCHAR(30), " +
                            "update_time VARCHAR(30))";
                    stmt.executeUpdate(createSql);
                    System.out.println("CareerPlan table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE career_plan";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("CareerPlan table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void createIncentiveTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'incentive'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE incentive " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "description TEXT, " +
                            "type VARCHAR(50), " +
                            "points INT, " +
                            "quantity INT DEFAULT 0, " +
                            "icon VARCHAR(255), " +
                            "criteria TEXT, " +
                            "status VARCHAR(20) DEFAULT 'active', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("Incentive table created successfully");
                } else {
                    // 表存在，更新表结构
                    updateIncentiveTable(conn, stmt);
                    // 检查表结构
                    String checkSql = "DESCRIBE incentive";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("Incentive table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void updateIncentiveTable(java.sql.Connection conn, java.sql.Statement stmt) throws Exception {
        // 检查并添加缺少的列
        String[] columns = {
            "quantity"
        };
        
        String[] types = {
            "INT DEFAULT 0"
        };
        
        for (int i = 0; i < columns.length; i++) {
            if (!columnExists(conn, "incentive", columns[i])) {
                try {
                    String sql = "ALTER TABLE incentive ADD COLUMN " + columns[i] + " " + types[i];
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

    private void createUserIncentiveTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'user_incentive'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE user_incentive " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "incentive_id BIGINT NOT NULL, " +
                            "incentive_name VARCHAR(100) NOT NULL, " +
                            "points INT, " +
                            "obtained_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "status VARCHAR(20) DEFAULT 'active', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("UserIncentive table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE user_incentive";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("UserIncentive table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void createCheckinTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'checkin'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE checkin " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "checkin_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "status VARCHAR(20) DEFAULT '已打卡', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("Checkin table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE checkin";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("Checkin table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createCommunityTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'community'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE community " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "description TEXT, " +
                            "type VARCHAR(50) NOT NULL, " +
                            "category VARCHAR(100), " +
                            "creator_id BIGINT NOT NULL, " +
                            "member_count INT DEFAULT 0, " +
                            "status VARCHAR(20) DEFAULT '活跃', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("Community table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE community";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("Community table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createStudyGroupTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'study_group'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE study_group " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "community_id BIGINT NOT NULL, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "description TEXT, " +
                            "leader_id BIGINT NOT NULL, " +
                            "member_count INT DEFAULT 0, " +
                            "status VARCHAR(20) DEFAULT '活跃', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("StudyGroup table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE study_group";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("StudyGroup table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createGroupMemberTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'group_member'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE group_member " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "group_id BIGINT NOT NULL, " +
                            "user_id BIGINT NOT NULL, " +
                            "role VARCHAR(20) NOT NULL, " +
                            "status VARCHAR(20) DEFAULT '正常', " +
                            "join_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "UNIQUE KEY uk_group_user (group_id, user_id))";
                    stmt.executeUpdate(createSql);
                    System.out.println("GroupMember table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE group_member";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("GroupMember table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createGroupTaskTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'group_task'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE group_task " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "group_id BIGINT NOT NULL, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "description TEXT, " +
                            "start_time TIMESTAMP, " +
                            "end_time TIMESTAMP, " +
                            "status VARCHAR(20) DEFAULT '未开始', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("GroupTask table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE group_task";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("GroupTask table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createStudyNoteTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'study_note'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE study_note " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "group_id BIGINT NOT NULL, " +
                            "title VARCHAR(100) NOT NULL, " +
                            "content TEXT NOT NULL, " +
                            "type VARCHAR(50), " +
                            "view_count INT DEFAULT 0, " +
                            "like_count INT DEFAULT 0, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("StudyNote table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE study_note";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("StudyNote table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createDiscussionTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'discussion'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE discussion " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "group_id BIGINT NOT NULL, " +
                            "user_id BIGINT NOT NULL, " +
                            "title VARCHAR(100) NOT NULL, " +
                            "content TEXT NOT NULL, " +
                            "reply_count INT DEFAULT 0, " +
                            "view_count INT DEFAULT 0, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("Discussion table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE discussion";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("Discussion table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createReplyTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'reply'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE reply " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "discussion_id BIGINT NOT NULL, " +
                            "user_id BIGINT NOT NULL, " +
                            "content TEXT NOT NULL, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("Reply table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE reply";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("Reply table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createLearningStatsTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'learning_stats'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE learning_stats " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "stats_date DATE NOT NULL, " +
                            "study_duration INT DEFAULT 0, " +
                            "task_completion_rate INT DEFAULT 0, " +
                            "checkin_count INT DEFAULT 0, " +
                            "resource_usage_count INT DEFAULT 0, " +
                            "note_count INT DEFAULT 0, " +
                            "discussion_count INT DEFAULT 0, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "UNIQUE KEY uk_user_date (user_id, stats_date))";
                    stmt.executeUpdate(createSql);
                    System.out.println("LearningStats table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE learning_stats";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("LearningStats table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createLearningReviewTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'learning_review'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE learning_review " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "review_month VARCHAR(7) NOT NULL, " +
                            "total_study_duration INT DEFAULT 0, " +
                            "average_daily_duration INT DEFAULT 0, " +
                            "task_completion_rate INT DEFAULT 0, " +
                            "checkin_rate INT DEFAULT 0, " +
                            "strengths TEXT, " +
                            "weaknesses TEXT, " +
                            "suggestions TEXT, " +
                            "report_content TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "UNIQUE KEY uk_user_month (user_id, review_month))";
                    stmt.executeUpdate(createSql);
                    System.out.println("LearningReview table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE learning_review";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("LearningReview table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createSkillTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'skill'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE skill " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "type VARCHAR(50) NOT NULL, " +
                            "category VARCHAR(100), " +
                            "description TEXT, " +
                            "level INT DEFAULT 1, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("Skill table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE skill";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("Skill table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createUserSkillTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'user_skill'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE user_skill " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "skill_id BIGINT NOT NULL, " +
                            "score INT DEFAULT 0, " +
                            "level INT DEFAULT 1, " +
                            "assessment_date VARCHAR(10), " +
                            "improvement_plan TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "UNIQUE KEY uk_user_skill (user_id, skill_id))";
                    stmt.executeUpdate(createSql);
                    System.out.println("UserSkill table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE user_skill";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("UserSkill table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createSkillQuestionTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'skill_question'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE skill_question " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "skill_id BIGINT NOT NULL, " +
                            "question_text TEXT NOT NULL, " +
                            "question_type VARCHAR(50) NOT NULL, " +
                            "options TEXT, " +
                            "correct_answer TEXT NOT NULL, " +
                            "score INT DEFAULT 10, " +
                            "difficulty VARCHAR(20), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("SkillQuestion table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE skill_question";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("SkillQuestion table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createOfflineDataTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'offline_data'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE offline_data " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "data_type VARCHAR(50) NOT NULL, " +
                            "data_content TEXT NOT NULL, " +
                            "sync_status VARCHAR(20) DEFAULT '未同步', " +
                            "offline_time TIMESTAMP, " +
                            "sync_time TIMESTAMP, " +
                            "device_id VARCHAR(100), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("OfflineData table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE offline_data";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("OfflineData table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createPrivacySettingTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'privacy_setting'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE privacy_setting " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "learning_record_visible VARCHAR(20) DEFAULT 'private', " +
                            "checkin_data_visible VARCHAR(20) DEFAULT 'private', " +
                            "personal_info_visible VARCHAR(20) DEFAULT 'private', " +
                            "data_export_enabled BOOLEAN DEFAULT true, " +
                            "login_alert BOOLEAN DEFAULT true, " +
                            "device_list TEXT, " +
                            "notification_settings TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "UNIQUE KEY uk_user (user_id))";
                    stmt.executeUpdate(createSql);
                    System.out.println("PrivacySetting table created successfully");
                } else {
                    // 表存在，更新表结构
                    updatePrivacySettingTable(conn, stmt);
                    // 检查表结构
                    String checkSql = "DESCRIBE privacy_setting";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("PrivacySetting table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void updatePrivacySettingTable(java.sql.Connection conn, java.sql.Statement stmt) throws Exception {
        // 检查并添加缺少的列
        String[] columns = {
            "login_alert"
        };
        
        String[] types = {
            "BOOLEAN DEFAULT true"
        };
        
        for (int i = 0; i < columns.length; i++) {
            if (!columnExists(conn, "privacy_setting", columns[i])) {
                try {
                    String sql = "ALTER TABLE privacy_setting ADD COLUMN " + columns[i] + " " + types[i];
                    stmt.executeUpdate(sql);
                    System.out.println("Added column: " + columns[i]);
                } catch (Exception e) {
                    System.out.println("Error adding column " + columns[i] + ": " + e.getMessage());
                }
            } else {
                System.out.println("Column " + columns[i] + " already exists");
            }
        }
        
        // 修改字段类型从BOOLEAN到VARCHAR
        String[] booleanColumns = {
            "learning_record_visible",
            "checkin_data_visible",
            "personal_info_visible"
        };
        
        for (String column : booleanColumns) {
            try {
                String sql = "ALTER TABLE privacy_setting MODIFY COLUMN " + column + " VARCHAR(20) DEFAULT 'private'";
                stmt.executeUpdate(sql);
                System.out.println("Modified column type: " + column);
            } catch (Exception e) {
                System.out.println("Error modifying column " + column + ": " + e.getMessage());
            }
        }
    }
    
    private void createCampusCourseTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'campus_course'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE campus_course " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "course_name VARCHAR(100) NOT NULL, " +
                            "course_code VARCHAR(50), " +
                            "teacher VARCHAR(100), " +
                            "classroom VARCHAR(100), " +
                            "day_of_week VARCHAR(20), " +
                            "start_time VARCHAR(20), " +
                            "end_time VARCHAR(20), " +
                            "semester VARCHAR(50), " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("CampusCourse table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE campus_course";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("CampusCourse table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createCampusActivityTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'campus_activity'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE campus_activity " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "activity_name VARCHAR(100) NOT NULL, " +
                            "activity_type VARCHAR(50), " +
                            "description TEXT, " +
                            "location VARCHAR(200), " +
                            "start_time TIMESTAMP, " +
                            "end_time TIMESTAMP, " +
                            "capacity INT DEFAULT 0, " +
                            "registered_count INT DEFAULT 0, " +
                            "status VARCHAR(20) DEFAULT '报名中', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("CampusActivity table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE campus_activity";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("CampusActivity table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createCollegeTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'college'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE college " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "description TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("College table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE college";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("College table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void createMajorTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'major'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE major " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "code VARCHAR(10) NOT NULL, " +
                            "college_id BIGINT, " +
                            "description TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "UNIQUE KEY uk_code (code))";
                    stmt.executeUpdate(createSql);
                    System.out.println("Major table created successfully");
                } else {
                    // 表存在，更新表结构
                    updateMajorTable(conn, stmt);
                    // 检查表结构
                    String checkSql = "DESCRIBE major";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("Major table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void updateMajorTable(java.sql.Connection conn, java.sql.Statement stmt) throws Exception {
        // 检查并添加缺少的列
        String[] columns = {
            "code",
            "college_id"
        };
        
        String[] types = {
            "VARCHAR(10) NOT NULL",
            "BIGINT"
        };
        
        for (int i = 0; i < columns.length; i++) {
            if (!columnExists(conn, "major", columns[i])) {
                try {
                    String sql = "ALTER TABLE major ADD COLUMN " + columns[i] + " " + types[i];
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
    
    private void createStudentProfileTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'student_profile'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE student_profile " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "student_id VARCHAR(20) NOT NULL, " +
                            "name VARCHAR(100) NOT NULL, " +
                            "college_id BIGINT, " +
                            "major_id BIGINT, " +
                            "class_id BIGINT, " +
                            "privacy_level INT DEFAULT 0, " +
                            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                            "UNIQUE KEY uk_user (user_id))";
                    stmt.executeUpdate(createSql);
                    System.out.println("StudentProfile table created successfully");
                } else {
                    // 表存在，更新表结构
                    updateStudentProfileTable(conn, stmt);
                    // 检查表结构
                    String checkSql = "DESCRIBE student_profile";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("StudentProfile table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void updateStudentProfileTable(java.sql.Connection conn, java.sql.Statement stmt) throws Exception {
        // 检查并添加缺失的列
        String[] columns = {
            "student_id",
            "name",
            "class_id",
            "privacy_level",
            "created_at",
            "updated_at"
        };
        
        String[] types = {
            "VARCHAR(20) NOT NULL",
            "VARCHAR(100) NOT NULL",
            "BIGINT",
            "INT DEFAULT 0",
            "TIMESTAMP DEFAULT CURRENT_TIMESTAMP",
            "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
        };
        
        for (int i = 0; i < columns.length; i++) {
            if (!columnExists(conn, "student_profile", columns[i])) {
                try {
                    String sql = "ALTER TABLE student_profile ADD COLUMN " + columns[i] + " " + types[i];
                    stmt.executeUpdate(sql);
                    System.out.println("Added column: " + columns[i]);
                } catch (Exception e) {
                    System.out.println("Error adding column " + columns[i] + ": " + e.getMessage());
                }
            } else {
                System.out.println("Column " + columns[i] + " already exists");
            }
        }
        
        // 检查并重命名旧的时间字段
        if (columnExists(conn, "student_profile", "create_time")) {
            try {
                stmt.executeUpdate("ALTER TABLE student_profile DROP COLUMN create_time");
                System.out.println("Dropped column: create_time");
            } catch (Exception e) {
                System.out.println("Error dropping column create_time: " + e.getMessage());
            }
        }
        
        if (columnExists(conn, "student_profile", "update_time")) {
            try {
                stmt.executeUpdate("ALTER TABLE student_profile DROP COLUMN update_time");
                System.out.println("Dropped column: update_time");
            } catch (Exception e) {
                System.out.println("Error dropping column update_time: " + e.getMessage());
            }
        }
    }

    private void createUserTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'user'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE user " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "username VARCHAR(50) NOT NULL UNIQUE, " +
                            "password VARCHAR(100) NOT NULL, " +
                            "name VARCHAR(50), " +
                            "gender VARCHAR(10), " +
                            "age INT, " +
                            "grade VARCHAR(20), " +
                            "student_id VARCHAR(20) UNIQUE, " +
                            "id_card VARCHAR(20) UNIQUE, " +
                            "email VARCHAR(100) UNIQUE, " +
                            "phone VARCHAR(20) UNIQUE, " +
                            "school_id BIGINT, " +
                            "major_id BIGINT, " +
                            "student_stage VARCHAR(50), " +
                            "role VARCHAR(10) NOT NULL, " +
                            "status VARCHAR(10) NOT NULL, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)"; 
                    stmt.executeUpdate(createSql);
                    System.out.println("User table created successfully");
                } else {
                    // 表存在，更新表结构
                    updateUserTable(conn, stmt);
                    // 检查表结构
                    String checkSql = "DESCRIBE user";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("User table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void updateUserTable(java.sql.Connection conn, java.sql.Statement stmt) throws Exception {
        // 检查并添加缺少的列
        String[] columns = {
            "status",
            "student_stage"
        };
        
        String[] types = {
            "VARCHAR(10) NOT NULL",
            "VARCHAR(50)"
        };
        
        for (int i = 0; i < columns.length; i++) {
            if (!columnExists(conn, "user", columns[i])) {
                try {
                    String sql = "ALTER TABLE user ADD COLUMN " + columns[i] + " " + types[i];
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

    private void createLearningResourceTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'learning_resource'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE learning_resource " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "title VARCHAR(255) NOT NULL, " +
                            "description TEXT, " +
                            "file_url VARCHAR(255), " +
                            "file_name VARCHAR(255), " +
                            "file_type VARCHAR(50), " +
                            "file_size BIGINT, " +
                            "category VARCHAR(50), " +
                            "tags VARCHAR(255), " +
                            "uploader_id BIGINT, " +
                            "uploader_name VARCHAR(100), " +
                            "school_id BIGINT, " +
                            "student_stage VARCHAR(50), " +
                            "download_count INT DEFAULT 0, " +
                            "view_count INT DEFAULT 0, " +
                            "status VARCHAR(20) DEFAULT 'published', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("LearningResource table created successfully");

                    // 添加初始数据
                    String insertSql1 = "INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, status) " +
                            "VALUES ('高等数学学习指南', '高等数学上册知识点总结和习题解析', '/uploads/math-guide.pdf', 'math-guide.pdf', 'application/pdf', 2500000, '文档', '数学,高等数学', 1, '系统管理员', 1, 'published')";
                    stmt.executeUpdate(insertSql1);

                    String insertSql2 = "INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, status) " +
                            "VALUES ('大学英语四级词汇', '大学英语四级考试核心词汇表', '/uploads/english-vocabulary.pdf', 'english-vocabulary.pdf', 'application/pdf', 1800000, '文档', '英语,四级', 1, '系统管理员', 1, 'published')";
                    stmt.executeUpdate(insertSql2);

                    String insertSql3 = "INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, status) " +
                            "VALUES ('数据结构视频教程', '数据结构基础概念和算法讲解', '/uploads/data-structure-video.mp4', 'data-structure-video.mp4', 'video/mp4', 150000000, '视频', '数据结构,算法', 1, '系统管理员', 1, 'published')";
                    stmt.executeUpdate(insertSql3);

                    String insertSql4 = "INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, status) " +
                            "VALUES ('计算机网络课件', '计算机网络课程课件和笔记', '/uploads/computer-network.pptx', 'computer-network.pptx', 'application/vnd.openxmlformats-officedocument.presentationml.presentation', 3200000, '文档', '计算机网络', 1, '系统管理员', 1, 'published')";
                    stmt.executeUpdate(insertSql4);

                    String insertSql5 = "INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, status) " +
                            "VALUES ('操作系统原理', '操作系统基本原理和核心概念', '/uploads/os-principles.pdf', 'os-principles.pdf', 'application/pdf', 4100000, '文档', '操作系统', 1, '系统管理员', 1, 'published')";
                    stmt.executeUpdate(insertSql5);

                    System.out.println("Initial learning resources added successfully");
                } else {
                    // 表存在，更新表结构
                    updateLearningResourceTable(conn, stmt);
                    // 检查表结构
                    String checkSql = "DESCRIBE learning_resource";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("LearningResource table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void createOperationLogTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'operation_log'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE operation_log " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "username VARCHAR(50), " +
                            "operation VARCHAR(100), " +
                            "ip VARCHAR(50), " +
                            "url VARCHAR(255), " +
                            "method VARCHAR(10), " +
                            "params TEXT, " +
                            "status INT, " +
                            "error_msg TEXT, " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("OperationLog table created successfully");
                } else {
                    // 表存在，更新表结构
                    updateOperationLogTable(conn, stmt);
                    // 检查表结构
                    String checkSql = "DESCRIBE operation_log";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("OperationLog table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }

    private void updateOperationLogTable(java.sql.Connection conn, java.sql.Statement stmt) throws Exception {
        // 检查并添加缺少的列
        String[] columns = {
            "status",
            "error_msg"
        };
        
        String[] types = {
            "INT",
            "TEXT"
        };
        
        for (int i = 0; i < columns.length; i++) {
            if (!columnExists(conn, "operation_log", columns[i])) {
                try {
                    String sql = "ALTER TABLE operation_log ADD COLUMN " + columns[i] + " " + types[i];
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
    
    // 学生用户ID
    private static final Long STUDENT_USER_ID = 4L;
    
    private void initStudentDataFor2023020616() {
        try {
            System.out.println("=== 开始为学生账号2023020616创建数据 ===");
            
            // 检查是否已有学习计划
            QueryWrapper<StudyPlan> planQueryWrapper = new QueryWrapper<>();
            planQueryWrapper.eq("user_id", STUDENT_USER_ID).eq("title", "2024春季学期学习计划");
            if (studyPlanService.count(planQueryWrapper) == 0) {
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
            } else {
                System.out.println("学习计划已存在，跳过创建");
            }
            
            // 创建目标管理
            System.out.println("创建目标管理...");
            
            // 短期目标 - 学期目标
            QueryWrapper<UserGoal> shortTermGoalQuery = new QueryWrapper<>();
            shortTermGoalQuery.eq("user_id", STUDENT_USER_ID).eq("title", "学期目标");
            if (userGoalService.count(shortTermGoalQuery) == 0) {
                UserGoal shortTermGoal = new UserGoal();
                shortTermGoal.setUserId(STUDENT_USER_ID);
                shortTermGoal.setTitle("学期目标");
                shortTermGoal.setDescription("学期末所有课程成绩达到85分以上");
                shortTermGoal.setStartDate("2024-02-20");
                shortTermGoal.setEndDate("2024-06-30");
                shortTermGoal.setPriority("high");
                shortTermGoal.setStatus("active");
                shortTermGoal.setCreateTime(java.time.LocalDateTime.now().toString());
                userGoalService.save(shortTermGoal);
                System.out.println("学期目标创建成功");
            } else {
                System.out.println("学期目标已存在，跳过创建");
            }
            
            // 中期目标 - 英语四级
            QueryWrapper<UserGoal> midTermGoalQuery = new QueryWrapper<>();
            midTermGoalQuery.eq("user_id", STUDENT_USER_ID).eq("title", "英语四级考试");
            if (userGoalService.count(midTermGoalQuery) == 0) {
                UserGoal midTermGoal = new UserGoal();
                midTermGoal.setUserId(STUDENT_USER_ID);
                midTermGoal.setTitle("英语四级考试");
                midTermGoal.setDescription("通过英语四级考试，分数达到550分以上");
                midTermGoal.setStartDate("2024-02-20");
                midTermGoal.setEndDate("2024-06-15");
                midTermGoal.setPriority("high");
                midTermGoal.setStatus("active");
                midTermGoal.setCreateTime(java.time.LocalDateTime.now().toString());
                userGoalService.save(midTermGoal);
                System.out.println("英语四级目标创建成功");
            } else {
                System.out.println("英语四级目标已存在，跳过创建");
            }
            
            // 长期目标 - 职业准备
            QueryWrapper<UserGoal> longTermGoalQuery = new QueryWrapper<>();
            longTermGoalQuery.eq("user_id", STUDENT_USER_ID).eq("title", "职业准备");
            if (userGoalService.count(longTermGoalQuery) == 0) {
                UserGoal longTermGoal = new UserGoal();
                longTermGoal.setUserId(STUDENT_USER_ID);
                longTermGoal.setTitle("职业准备");
                longTermGoal.setDescription("掌握Java、Python等编程语言，为未来就业做准备");
                longTermGoal.setStartDate("2024-02-20");
                longTermGoal.setEndDate("2026-06-30");
                longTermGoal.setPriority("medium");
                longTermGoal.setStatus("active");
                longTermGoal.setCreateTime(java.time.LocalDateTime.now().toString());
                userGoalService.save(longTermGoal);
                System.out.println("职业准备目标创建成功");
            } else {
                System.out.println("职业准备目标已存在，跳过创建");
            }
            
            // 创建职业规划
            System.out.println("创建职业规划...");
            QueryWrapper<CareerPlan> careerPlanQuery = new QueryWrapper<>();
            careerPlanQuery.eq("user_id", STUDENT_USER_ID);
            if (careerPlanService.count(careerPlanQuery) == 0) {
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
                careerPlan.setCreateTime(java.time.LocalDateTime.now().toString());
                careerPlanService.createPlan(careerPlan);
                System.out.println("软件工程师职业规划创建成功");
            } else {
                System.out.println("职业规划已存在，跳过创建");
            }
            
            // 检查是否已有社区数据
            QueryWrapper<Community> communityQuery = new QueryWrapper<>();
            communityQuery.eq("name", "计算机科学与技术专业社区");
            Community community = null;
            if (communityService.count(communityQuery) == 0) {
                // 创建社区数据
                System.out.println("创建社区数据...");
                community = new Community();
                community.setName("计算机科学与技术专业社区");
                community.setDescription("计算机科学与技术专业的学习交流社区");
                community.setType("专业");
                community.setCategory("计算机");
                community.setCreatorId(STUDENT_USER_ID);
                community.setMemberCount(1);
                community.setStatus("活跃");
                communityService.save(community);
                System.out.println("社区创建成功");
            } else {
                // 处理多个结果的情况，只获取第一个
                List<Community> communities = communityService.list(communityQuery);
                if (!communities.isEmpty()) {
                    community = communities.get(0);
                    System.out.println("社区已存在，跳过创建");
                }
            }
            
            // 检查是否已有学习小组
            QueryWrapper<StudyGroup> studyGroupQuery = new QueryWrapper<>();
            studyGroupQuery.eq("name", "Java学习小组");
            StudyGroup studyGroup = null;
            if (studyGroupService.count(studyGroupQuery) == 0 && community != null) {
                // 创建学习小组
                studyGroup = new StudyGroup();
                studyGroup.setCommunityId(community.getId());
                studyGroup.setName("Java学习小组");
                studyGroup.setDescription("Java编程学习小组");
                studyGroup.setLeaderId(STUDENT_USER_ID);
                studyGroup.setMemberCount(1);
                studyGroup.setStatus("活跃");
                studyGroupService.save(studyGroup);
                System.out.println("学习小组创建成功");
            } else {
                // 处理多个结果的情况，只获取第一个
                List<StudyGroup> studyGroups = studyGroupService.list(studyGroupQuery);
                if (!studyGroups.isEmpty()) {
                    studyGroup = studyGroups.get(0);
                    System.out.println("学习小组已存在，跳过创建");
                }
            }
            
            // 检查是否已有小组成员
            if (studyGroup != null) {
                QueryWrapper<GroupMember> groupMemberQuery = new QueryWrapper<>();
                groupMemberQuery.eq("group_id", studyGroup.getId()).eq("user_id", STUDENT_USER_ID);
                if (groupMemberService.count(groupMemberQuery) == 0) {
                    // 添加小组成员
                    GroupMember groupMember = new GroupMember();
                    groupMember.setGroupId(studyGroup.getId());
                    groupMember.setUserId(STUDENT_USER_ID);
                    groupMember.setRole("组长");
                    groupMember.setStatus("正常");
                    groupMemberService.save(groupMember);
                    System.out.println("小组成员添加成功");
                } else {
                    System.out.println("小组成员已存在，跳过创建");
                }
            }
            
            // 创建学习笔记
            System.out.println("创建学习笔记...");
            // 检查是否已经存在学习笔记
            QueryWrapper<StudyNote> noteQueryWrapper = new QueryWrapper<>();
            noteQueryWrapper.eq("user_id", STUDENT_USER_ID);
            if (studyNoteService.count(noteQueryWrapper) == 0 && studyGroup != null) {
                StudyNote studyNote = new StudyNote();
                studyNote.setUserId(STUDENT_USER_ID);
                studyNote.setGroupId(studyGroup.getId());
                studyNote.setTitle("Java基础学习笔记");
                studyNote.setContent("Java基础包括：变量、数据类型、运算符、控制语句、数组、面向对象编程等。");
                studyNote.setType("学习");
                studyNoteService.save(studyNote);
                System.out.println("学习笔记创建成功");
            } else {
                System.out.println("学习笔记已存在，跳过创建");
            }
            
            // 检查是否已有讨论
            if (studyGroup != null) {
                QueryWrapper<Discussion> discussionQuery = new QueryWrapper<>();
                discussionQuery.eq("group_id", studyGroup.getId()).eq("title", "如何学习Java编程？");
                Discussion discussion = null;
                if (discussionService.count(discussionQuery) == 0) {
                    // 创建讨论
                    System.out.println("创建讨论...");
                    discussion = new Discussion();
                    discussion.setGroupId(studyGroup.getId());
                    discussion.setUserId(STUDENT_USER_ID);
                    discussion.setTitle("如何学习Java编程？");
                    discussion.setContent("大家好，我是一名初学者，想请教一下如何有效地学习Java编程？");
                    discussionService.save(discussion);
                    System.out.println("讨论创建成功");
                } else {
                    // 处理多个结果的情况，只获取第一个
                    List<Discussion> discussions = discussionService.list(discussionQuery);
                    if (!discussions.isEmpty()) {
                        discussion = discussions.get(0);
                        System.out.println("讨论已存在，跳过创建");
                    }
                }
                
                // 检查是否已有回复
                if (discussion != null) {
                    QueryWrapper<Reply> replyQuery = new QueryWrapper<>();
                    replyQuery.eq("discussion_id", discussion.getId());
                    if (replyService.count(replyQuery) == 0) {
                        // 创建回复
                        Reply reply = new Reply();
                        reply.setDiscussionId(discussion.getId());
                        reply.setUserId(STUDENT_USER_ID);
                        reply.setContent("建议从基础开始，多做练习，参加项目实践。");
                        replyService.save(reply);
                        System.out.println("回复创建成功");
                    } else {
                        System.out.println("回复已存在，跳过创建");
                    }
                }
            }
            
            // 创建学习统计数据
            System.out.println("创建学习统计数据...");
            String today = java.time.LocalDate.now().toString();
            // 检查是否已存在今天的学习统计数据
            List<LearningStats> existingStats = learningStatsService.list(new QueryWrapper<LearningStats>()
                    .eq("user_id", STUDENT_USER_ID)
                    .eq("stats_date", today));
            if (existingStats.isEmpty()) {
                LearningStats learningStats = new LearningStats();
                learningStats.setUserId(STUDENT_USER_ID);
                learningStats.setStatsDate(today);
                learningStats.setStudyDuration(120); // 分钟
                learningStats.setTaskCompletionRate(80); // 百分比
                learningStats.setCheckinCount(5);
                learningStats.setResourceUsageCount(10);
                learningStats.setNoteCount(1);
                learningStats.setDiscussionCount(1);
                learningStatsService.save(learningStats);
                System.out.println("学习统计数据创建成功");
            } else {
                System.out.println("今天的学习统计数据已存在，跳过创建");
            }
            
            // 检查是否已有技能数据
            QueryWrapper<Skill> skillQuery1 = new QueryWrapper<>();
            skillQuery1.eq("name", "Java编程");
            if (skillService.count(skillQuery1) == 0) {
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
            } else {
                System.out.println("Java编程技能已存在，跳过创建");
            }
            
            QueryWrapper<Skill> skillQuery2 = new QueryWrapper<>();
            skillQuery2.eq("name", "英语");
            if (skillService.count(skillQuery2) == 0) {
                Skill skill2 = new Skill();
                skill2.setName("英语");
                skill2.setType("语言");
                skill2.setCategory("外语");
                skill2.setDescription("英语语言技能");
                skill2.setLevel(1);
                skillService.save(skill2);
                System.out.println("技能2创建成功");
            } else {
                System.out.println("英语技能已存在，跳过创建");
            }
            
            // 检查是否已有校园活动数据
            QueryWrapper<CampusActivity> activityQuery1 = new QueryWrapper<>();
            activityQuery1.eq("activity_name", "编程大赛");
            if (campusActivityService.count(activityQuery1) == 0) {
                // 创建校园活动数据
                System.out.println("创建校园活动数据...");
                CampusActivity activity1 = new CampusActivity();
                activity1.setActivityName("编程大赛");
                activity1.setActivityType("竞赛");
                activity1.setDescription("校园编程大赛，展示学生编程技能");
                activity1.setLocation("学校计算机中心");
                activity1.setStartTime(java.time.LocalDateTime.now().plusDays(7).toString());
                activity1.setEndTime(java.time.LocalDateTime.now().plusDays(7).plusHours(4).toString());
                activity1.setCapacity(100);
                activity1.setRegisteredCount(0);
                activity1.setStatus("报名中");
                campusActivityService.save(activity1);
                System.out.println("校园活动1创建成功");
            } else {
                System.out.println("编程大赛活动已存在，跳过创建");
            }
            
            QueryWrapper<CampusActivity> activityQuery2 = new QueryWrapper<>();
            activityQuery2.eq("activity_name", "英语角");
            if (campusActivityService.count(activityQuery2) == 0) {
                CampusActivity activity2 = new CampusActivity();
                activity2.setActivityName("英语角");
                activity2.setActivityType("活动");
                activity2.setDescription("每周英语角活动，提高学生英语口语能力");
                activity2.setLocation("学校图书馆");
                activity2.setStartTime(java.time.LocalDateTime.now().plusDays(1).toString());
                activity2.setEndTime(java.time.LocalDateTime.now().plusDays(1).plusHours(2).toString());
                activity2.setCapacity(50);
                activity2.setRegisteredCount(0);
                activity2.setStatus("报名中");
                campusActivityService.save(activity2);
                System.out.println("校园活动2创建成功");
            } else {
                System.out.println("英语角活动已存在，跳过创建");
            }
            
            System.out.println("=== 学生账号2023020616数据创建完成 ===");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("学生数据初始化失败: " + e.getMessage());
        }
    }
    
    private void createExamTable() throws Exception {
        try (java.sql.Connection conn = dataSource.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            // 检查表是否存在
            String checkTableSql = "SHOW TABLES LIKE 'exam'";
            try (java.sql.ResultSet rs = stmt.executeQuery(checkTableSql)) {
                if (!rs.next()) {
                    // 表不存在，创建表
                    String createSql = "CREATE TABLE exam " +
                            "(id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "user_id BIGINT NOT NULL, " +
                            "exam_name VARCHAR(100) NOT NULL, " +
                            "exam_type VARCHAR(50), " +
                            "description TEXT, " +
                            "exam_date TIMESTAMP, " +
                            "location VARCHAR(200), " +
                            "duration INT, " +
                            "status VARCHAR(20) DEFAULT 'pending', " +
                            "create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";
                    stmt.executeUpdate(createSql);
                    System.out.println("Exam table created successfully");
                } else {
                    // 表存在，检查表结构
                    String checkSql = "DESCRIBE exam";
                    try (java.sql.ResultSet rs2 = stmt.executeQuery(checkSql)) {
                        System.out.println("Exam table structure:");
                        while (rs2.next()) {
                            System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                        }
                    }
                }
            }
        }
    }
    
    private void initUser8Exams() {
        System.out.println("创建用户8的考试数据...");
        
        try {
            // 直接使用JDBC创建考试数据
            try (java.sql.Connection conn = dataSource.getConnection();
                 java.sql.Statement stmt = conn.createStatement()) {
                
                // 检查是否已存在用户8的考试数据
                String checkSql = "SELECT COUNT(*) FROM exam WHERE user_id = 8";
                try (java.sql.ResultSet rs = stmt.executeQuery(checkSql)) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        // 插入考试1
                        String examSql1 = "INSERT INTO exam (user_id, exam_name, exam_type, description, exam_date, location, duration, status, create_time, update_time) VALUES (8, '高等数学期末考试', 'final', '高等数学下册期末考试，重点考察微积分和线性代数', '2026-06-20 09:00:00', '教学楼A101', 120, 'pending', NOW(), NOW())";
                        stmt.executeUpdate(examSql1);
                        System.out.println("考试1创建成功");
                        
                        // 插入考试2
                        String examSql2 = "INSERT INTO exam (user_id, exam_name, exam_type, description, exam_date, location, duration, status, create_time, update_time) VALUES (8, '英语四级考试', 'certification', '全国大学英语四级考试', '2026-06-15 09:00:00', '教学楼B201', 130, 'pending', NOW(), NOW())";
                        stmt.executeUpdate(examSql2);
                        System.out.println("考试2创建成功");
                        
                        // 插入考试3
                        String examSql3 = "INSERT INTO exam (user_id, exam_name, exam_type, description, exam_date, location, duration, status, create_time, update_time) VALUES (8, '数据结构期中考试', 'midterm', '数据结构期中考试，重点考察链表、树和图', '2026-04-15 14:00:00', '教学楼C301', 90, 'pending', NOW(), NOW())";
                        stmt.executeUpdate(examSql3);
                        System.out.println("考试3创建成功");
                    } else {
                        System.out.println("考试数据已存在，跳过创建");
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("创建考试数据失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}