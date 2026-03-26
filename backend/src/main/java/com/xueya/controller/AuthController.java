 package com.xueya.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueya.entity.User;
import com.xueya.entity.School;
import com.xueya.service.UserService;
import com.xueya.service.SchoolService;
import com.xueya.service.MajorService;
import com.xueya.service.CollegeService;
import com.xueya.service.StudentProfileService;
import com.xueya.entity.Major;
import com.xueya.entity.College;
import com.xueya.entity.StudentProfile;
import com.xueya.utils.JwtUtil;
import com.xueya.utils.ValidationUtil;
import com.xueya.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/auth", produces = "application/json; charset=utf-8")
public class AuthController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private SchoolService schoolService;
    
    @Autowired
    private MajorService majorService;
    
    @Autowired
    private CollegeService collegeService;
    
    @Autowired
    private StudentProfileService studentProfileService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    // 生成BCrypt密码（用于测试）
    @GetMapping("/generate-password")
    public Map<String, Object> generatePassword() {
        Map<String, Object> response = new HashMap<>();
        String password = "123456";
        String encodedPassword = passwordEncoder.encode(password);
        response.put("success", true);
        response.put("password", password);
        response.put("encodedPassword", encodedPassword);
        response.put("encodedPasswordLength", encodedPassword.length());
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, Object> loginData) {
        System.out.println("=== 登录请求开始 ===");
        System.out.println("登录请求数据: " + loginData);
        System.out.println("登录请求数据类型: " + loginData.getClass().getName());
        System.out.println("登录请求数据大小: " + loginData.size());
        for (Map.Entry<String, Object> entry : loginData.entrySet()) {
            System.out.println("键: " + entry.getKey() + ", 值: " + entry.getValue() + ", 值类型: " + (entry.getValue() != null ? entry.getValue().getClass().getName() : "null"));
        }
        String idCard = (String) loginData.get("idCard");
        String password = (String) loginData.get("password");
        System.out.println("身份证号/用户名: " + idCard);
        System.out.println("密码: " + password);
        System.out.println("=== 登录请求处理中 ===");
        
        // 生成并打印BCrypt密码（用于测试）
        if (idCard != null && idCard.equals("generate")) {
            String encodedPassword = passwordEncoder.encode(password);
            System.out.println("=== 生成BCrypt密码 ===");
            System.out.println("原始密码: " + password);
            System.out.println("BCrypt密码: " + encodedPassword);
            System.out.println("密码长度: " + encodedPassword.length());
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "BCrypt密码生成成功");
            response.put("originalPassword", password);
            response.put("encodedPassword", encodedPassword);
            return response;
        }

        System.out.println("开始查找用户...");
        // 先通过身份证号查找用户，如果找不到则通过用户名查找
        User user = userService.getUserByIdCard(idCard);
        System.out.println("通过身份证号查找用户: " + user);
        if (user == null) {
            System.out.println("通过身份证号未找到用户，尝试通过用户名查找...");
            user = userService.getUserByUsername(idCard);
            System.out.println("通过用户名查找用户: " + user);
        }

        if (user == null) {
            System.out.println("用户不存在: " + idCard);
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "身份证号或密码错误");
            return response;
        } else {
            System.out.println("用户存在: " + user.getIdCard());
            System.out.println("存储的密码: " + user.getPassword());
            System.out.println("存储的密码长度: " + user.getPassword().length());
            System.out.println("存储的密码前10个字符: " + user.getPassword().substring(0, Math.min(10, user.getPassword().length())));
            System.out.println("输入的密码: " + password);
            try {
                boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
                System.out.println("密码匹配结果: " + passwordMatch);
                if (!passwordMatch) {
                    System.out.println("密码不匹配: " + idCard);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("message", "身份证号或密码错误");
                    return response;
                }
            } catch (Exception e) {
                System.out.println("密码验证异常: " + e.getMessage());
                e.printStackTrace();
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "密码验证失败");
                return response;
            }
        }
        System.out.println("用户验证成功: " + idCard);

        String token = jwtUtil.generateToken(user.getId(), user.getIdCard(), user.getRole(), user.getSchoolId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getIdCard());

        // 获取学校信息
        School school = null;
        if (user.getSchoolId() != null) {
            school = schoolService.getById(user.getSchoolId());
        }
        
        // 如果是管理员且没有学校信息，创建默认学校信息
        System.out.println("用户角色: " + user.getRole());
        System.out.println("学校信息: " + school);
        if (school == null && ("0".equals(user.getRole()) || "super_admin".equals(user.getRole()) || "school_admin".equals(user.getRole()) || "school".equals(user.getRole()))) {
            System.out.println("创建默认学校信息");
            school = new School();
            school.setId(1L);
            school.setName("系统管理");
            school.setCode("SYS");
            school.setStatus("active");
        }
        System.out.println("最终学校信息: " + school);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", token);
        response.put("refreshToken", refreshToken);
        response.put("user", user);
        response.put("school", school);
        return response;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, Object> registerData) {
        System.out.println("=== 注册请求开始 ===");
        System.out.println("注册请求数据: " + registerData);
        System.out.println("注册请求数据类型: " + registerData.getClass().getName());
        System.out.println("注册请求数据大小: " + registerData.size());
        for (Map.Entry<String, Object> entry : registerData.entrySet()) {
            System.out.println("键: " + entry.getKey() + ", 值: " + entry.getValue() + ", 值类型: " + (entry.getValue() != null ? entry.getValue().getClass().getName() : "null"));
        }
        User user = new User();
        user.setIdCard((String) registerData.get("idCard"));
        user.setUsername((String) registerData.get("idCard")); // 将username设置为身份证号
        user.setPhone((String) registerData.get("phone"));
        user.setEmail((String) registerData.get("email"));
        user.setPassword((String) registerData.get("password"));
        user.setName((String) registerData.get("name"));

        // 验证身份证号是否已存在
        if (user.getIdCard() != null && userService.getUserByIdCard(user.getIdCard()) != null) {
            return ResponseUtil.error("身份证号已存在");
        }

        // 验证手机号是否已存在
        if (user.getPhone() != null && userService.getUserByPhone(user.getPhone()) != null) {
            return ResponseUtil.error("手机号已存在");
        }

        // 验证身份证号格式（18位）
        if (!ValidationUtil.isValidIdCardFormat(user.getIdCard())) {
            return ResponseUtil.error("身份证号格式不正确，必须为18位");
        }

        // 验证身份证号校验码
        if (!ValidationUtil.validateIdCardChecksum(user.getIdCard())) {
            return ResponseUtil.error("身份证号校验码不正确");
        }

        // 验证手机号格式（11位纯数字）
        if (!ValidationUtil.isValidPhoneFormat(user.getPhone())) {
            return ResponseUtil.error("手机号格式不正确，必须为11位纯数字");
        }

        // 验证邮箱格式
        if (!ValidationUtil.isValidEmailFormat(user.getEmail())) {
            return ResponseUtil.error("邮箱格式不正确");
        }

        // 验证密码复杂度
        if (!ValidationUtil.isPasswordLengthValid(user.getPassword())) {
            return ResponseUtil.error("密码至少6位");
        }
        if (!ValidationUtil.isPasswordContainsLetter(user.getPassword())) {
            return ResponseUtil.error("密码必须包含字母");
        }
        if (!ValidationUtil.isPasswordContainsDigit(user.getPassword())) {
            return ResponseUtil.error("密码必须包含数字");
        }
        if (!ValidationUtil.isPasswordContainsSpecialChar(user.getPassword())) {
            return ResponseUtil.error("密码必须包含特殊字符");
        }

        // 从身份证号提取出生日期并计算年龄
        String idCard = user.getIdCard();
        String birthDateStr = idCard.substring(6, 14); // 第7-14位是出生日期
        int birthYear = Integer.parseInt(birthDateStr.substring(0, 4));
        int birthMonth = Integer.parseInt(birthDateStr.substring(4, 6));
        int birthDay = Integer.parseInt(birthDateStr.substring(6, 8));

        // 计算当前年龄
        java.time.LocalDate now = java.time.LocalDate.now();
        int currentYear = now.getYear();
        int currentMonth = now.getMonthValue();
        int currentDay = now.getDayOfMonth();

        int age = currentYear - birthYear;
        if (currentMonth < birthMonth || (currentMonth == birthMonth && currentDay < birthDay)) {
            age--;
        }

        user.setAge(age);

        // 根据年龄确定学生阶段
        String studentStage = "";
        if (age >= 6 && age <= 12) {
            studentStage = "小学教育";
        } else if (age >= 12 && age <= 15) {
            studentStage = "初中教育";
        } else if (age >= 15 && age <= 18) {
            studentStage = "高中教育";
        } else if (age >= 18 && age <= 21) {
            studentStage = "大学专科";
        } else if (age >= 18 && age <= 22) {
            studentStage = "大学本科";
        } else if (age >= 22 && age <= 25) {
            studentStage = "硕士研究生";
        } else if (age >= 25) {
            studentStage = "博士研究生";
        } else {
            studentStage = "未入学";
        }

        user.setStudentStage(studentStage);

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("1"); // 设置role=1
        user.setStatus("1"); // 设置status=1（正常）
        try {
            userService.save(user);
            System.out.println("用户保存成功");
        } catch (Exception e) {
            System.out.println("用户保存失败: " + e.getMessage());
            e.printStackTrace();
            return ResponseUtil.error("注册失败: " + e.getMessage());
        }

        // 检查是否为大学生，如果是，提示认证学校信息
        boolean isCollegeStudent = studentStage.equals("大学专科") || studentStage.equals("大学本科") || studentStage.equals("硕士研究生") || studentStage.equals("博士研究生");

        Map<String, Object> data = new HashMap<>();
        data.put("studentStage", studentStage);
        if (isCollegeStudent) {
            data.put("needSchoolVerification", true);
            data.put("schoolVerificationMessage", "请认证学校信息，包括学号和学校");
        } else {
            data.put("needSchoolVerification", false);
        }

        return ResponseUtil.success("注册成功", data);
    }
    
    @PostMapping("/register/student")
    public Map<String, Object> registerStudent(@RequestBody Map<String, Object> registerData) {
        User user = new User();
        user.setUsername((String) registerData.get("username"));
        user.setPassword((String) registerData.get("password"));
        user.setName((String) registerData.get("name"));
        user.setEmail((String) registerData.get("email"));
        user.setPhone((String) registerData.get("phone"));
        user.setStudentId((String) registerData.get("studentId"));

        // 验证用户名是否已存在
        if (userService.getUserByUsername(user.getUsername()) != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户名已存在");
            return response;
        }

        // 验证学号是否已存在
        if (user.getStudentId() != null && userService.getUserByStudentId(user.getStudentId()) != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "学号已存在");
            return response;
        }

        // 验证学号格式（支持本科10位数字和专升本10位数字+Z）
        if (user.getStudentId() != null && !user.getStudentId().matches("^\\d{10}(Z)?$")) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "学号格式不正确，应为10位数字或10位数字+Z");
            return response;
        }

        // 验证手机号格式（11位纯数字）
        if (user.getPhone() != null && !user.getPhone().matches("^1[3-9]\\d{9}$")) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "手机号格式不正确，必须为11位纯数字");
            return response;
        }

        // 验证邮箱格式（含@和域名）
        if (user.getEmail() != null && !user.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "邮箱格式不正确");
            return response;
        }

        // 验证密码长度
        if (user.getPassword() != null && user.getPassword().length() < 6) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "密码至少6位");
            return response;
        }

        // 智能填充专业信息
        Long majorId = (Long) registerData.get("majorId");
        Long collegeId = (Long) registerData.get("collegeId");
        if (user.getStudentId() != null && (majorId == null || collegeId == null)) {
            // 截取学号第5-6位作为专业代码
            String majorCode = user.getStudentId().substring(4, 6);
            // 查询专业信息
            Major major = majorService.getOne(new QueryWrapper<Major>().eq("code", majorCode));
            if (major != null) {
                majorId = major.getId();
                collegeId = major.getCollegeId();
            }
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("1"); // 设置role=1
        user.setStatus("1"); // 设置status=1（正常）
        userService.save(user);

        // 创建学生档案记录
        if (majorId != null && collegeId != null) {
            StudentProfile studentProfile = new StudentProfile();
            studentProfile.setUserId(user.getId());
            studentProfile.setStudentId(user.getStudentId());
            studentProfile.setName(user.getName());
            studentProfile.setCollegeId(collegeId);
            studentProfile.setMajorId(majorId);
            studentProfile.setClassId(null); // 暂时设置为null，后续可通过其他接口完善
            studentProfile.setPrivacyLevel(0); // 设置默认隐私级别为0（公开）
            studentProfile.setCreated_at(java.time.LocalDateTime.now().toString());
            studentProfile.setUpdated_at(java.time.LocalDateTime.now().toString());
            studentProfileService.save(studentProfile);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "注册成功");
        return response;
    }
    
    // 根据学号查询专业信息
    @PostMapping("/get-major-by-student-id")
    public Map<String, Object> getMajorByStudentId(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String studentId = request.get("studentId");
        
        if (studentId == null || !studentId.matches("^\\d{10}(Z)?$")) {
            response.put("success", false);
            response.put("message", "学号格式不正确，应为10位数字或10位数字+Z");
            return response;
        }
        
        // 截取学号第5-6位作为专业代码
        String majorCode = studentId.substring(4, 6);
        // 查询专业信息
        Major major = majorService.getOne(new QueryWrapper<Major>().eq("code", majorCode));
        
        if (major == null) {
            response.put("success", false);
            response.put("message", "未找到对应的专业信息");
            return response;
        }
        
        // 查询学院信息
        College college = collegeService.getById(major.getCollegeId());
        
        response.put("success", true);
        response.put("major", major);
        response.put("college", college);
        return response;
    }

    // 获取学校列表（用于注册时选择学校）
    @GetMapping("/schools")
    public Map<String, Object> getSchools() {
        Map<String, Object> response = new HashMap<>();
        // 直接硬编码返回学校列表，确保字符编码正确
        List<Map<String, Object>> schools = new ArrayList<>();
        
        // 添加哈尔滨信息工程学院
        Map<String, Object> school1 = new HashMap<>();
        school1.put("id", 1);
        school1.put("name", "哈尔滨信息工程学院");
        school1.put("code", "HIIE");
        schools.add(school1);
        
        // 添加其他学校
        Map<String, Object> school2 = new HashMap<>();
        school2.put("id", 2);
        school2.put("name", "北京大学");
        school2.put("code", "PKU");
        schools.add(school2);
        
        Map<String, Object> school3 = new HashMap<>();
        school3.put("id", 3);
        school3.put("name", "清华大学");
        school3.put("code", "THU");
        schools.add(school3);
        
        Map<String, Object> school4 = new HashMap<>();
        school4.put("id", 4);
        school4.put("name", "哈尔滨工业大学");
        school4.put("code", "HIT");
        schools.add(school4);
        
        Map<String, Object> school5 = new HashMap<>();
        school5.put("id", 5);
        school5.put("name", "哈尔滨工程大学");
        school5.put("code", "HEU");
        schools.add(school5);
        
        response.put("success", true);
        response.put("schools", schools);
        return response;
    }

    // 获取硬编码的学校列表（测试用）
    @GetMapping("/schools/test")
    public Map<String, Object> getSchoolsTest() {
        Map<String, Object> response = new HashMap<>();
        // 从数据库中获取学校列表
        List<School> schools = schoolService.list();
        response.put("success", true);
        response.put("schools", schools);
        return response;
    }

    // 刷新令牌
    @PostMapping("/refresh")
    public Map<String, Object> refreshToken(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String refreshToken = request.get("refreshToken");
            if (refreshToken == null) {
                response.put("success", false);
                response.put("message", "刷新令牌不能为空");
                return response;
            }

            if (jwtUtil.isRefreshTokenExpired(refreshToken)) {
                response.put("success", false);
                response.put("message", "刷新令牌已过期");
                return response;
            }

            Long userId = jwtUtil.getUserIdFromToken(refreshToken);
            String username = jwtUtil.getUsernameFromToken(refreshToken);

            User user = userService.getById(userId);
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return response;
            }

            String newToken = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole(), user.getSchoolId());
            String newRefreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());

            response.put("success", true);
            response.put("token", newToken);
            response.put("refreshToken", newRefreshToken);
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "刷新令牌失败");
            return response;
        }
    }

    // 密码重置
    @PostMapping("/reset-password")
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String email = request.get("email");
            String newPassword = request.get("newPassword");

            if (email == null || newPassword == null) {
                response.put("success", false);
                response.put("message", "邮箱和新密码不能为空");
                return response;
            }

            // 查找用户
            User user = userService.getOne(new QueryWrapper<User>().eq("email", email));
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户不存在");
                return response;
            }

            // 更新密码
            user.setPassword(passwordEncoder.encode(newPassword));
            userService.updateById(user);

            response.put("success", true);
            response.put("message", "密码重置成功");
            return response;
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "密码重置失败");
            return response;
        }
    }

    // 创建默认用户（用于测试）
    @GetMapping("/create-default-user")
    public Map<String, Object> createDefaultUser() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查是否已有用户
            long userCount = userService.count();
            System.out.println("当前用户数量: " + userCount);
            if (userCount > 0) {
                response.put("success", false);
                response.put("message", "已有用户存在，无需创建默认用户");
                return response;
            }

            // 创建默认管理员用户
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("admin123"));
            adminUser.setName("管理员");
            adminUser.setEmail("admin@example.com");
            adminUser.setPhone("13800138000");
            adminUser.setRole("0"); // 0表示管理员
            adminUser.setStatus("1"); // 1表示正常
            System.out.println("准备保存管理员用户");
            userService.save(adminUser);
            System.out.println("管理员用户保存成功");

            // 创建默认学生用户
            User studentUser = new User();
            studentUser.setUsername("student");
            studentUser.setPassword(passwordEncoder.encode("student123"));
            studentUser.setName("学生");
            studentUser.setEmail("student@example.com");
            studentUser.setPhone("13900139000");
            studentUser.setRole("1"); // 1表示学生
            studentUser.setStatus("1"); // 1表示正常
            studentUser.setStudentId("2023123456");
            System.out.println("准备保存学生用户");
            userService.save(studentUser);
            System.out.println("学生用户保存成功");

            response.put("success", true);
            response.put("message", "默认用户创建成功");
            response.put("admin", "admin / admin123");
            response.put("student", "student / student123");
            return response;
        } catch (Exception e) {
            System.out.println("创建默认用户失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "默认用户创建失败: " + e.getMessage());
            return response;
        }
    }
    
    // 创建测试用户2023020616（用于测试）
    @GetMapping("/create-test-user")
    public Map<String, Object> createTestUser() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查是否已有用户
            User existingUser = userService.getUserByUsername("2023020616");
            if (existingUser != null) {
                response.put("success", false);
                response.put("message", "用户已存在，无需创建");
                return response;
            }

            // 创建测试用户
            User testUser = new User();
            testUser.setUsername("2023020616");
            testUser.setIdCard("2023020616"); // 设置身份证号与用户名相同
            testUser.setPassword(passwordEncoder.encode("123456"));
            testUser.setName("测试用户");
            testUser.setGender("男");
            testUser.setAge(20);
            testUser.setGrade("2023");
            testUser.setStudentId("2023020616");
            testUser.setEmail("test2023020616@example.com");
            testUser.setPhone("13800138001");
            testUser.setSchoolId(1L); // 设置学校ID为1（哈尔滨信息工程学院）
            testUser.setRole("1"); // 1表示学生
            testUser.setStatus("1"); // 1表示正常
            System.out.println("准备保存测试用户");
            userService.save(testUser);
            System.out.println("测试用户保存成功");

            response.put("success", true);
            response.put("message", "测试用户创建成功");
            response.put("user", "2023020616 / 123456");
            return response;
        } catch (Exception e) {
            System.out.println("创建测试用户失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "创建测试用户失败: " + e.getMessage());
            return response;
        }
    }
    


    // 检查用户信息（用于测试）
    @GetMapping("/check-user")
    public Map<String, Object> checkUser() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查用户2023020616
            User user = userService.getUserByUsername("2023020616");
            if (user != null) {
                response.put("success", true);
                response.put("user", user);
                
                // 检查学校信息
                if (user.getSchoolId() != null) {
                    School school = schoolService.getById(user.getSchoolId());
                    response.put("school", school);
                }
            } else {
                response.put("success", false);
                response.put("message", "用户不存在");
            }
            
            // 检查所有学校
            List<School> schools = schoolService.list();
            response.put("schools", schools);
            
            return response;
        } catch (Exception e) {
            System.out.println("检查用户失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "检查用户失败: " + e.getMessage());
            return response;
        }
    }
    
    // 创建学校管理员账号（用于测试）
    @GetMapping("/create-school-admin")
    public Map<String, Object> createSchoolAdmin() {
        Map<String, Object> response = new HashMap<>();
        try {
            // 检查是否已有学校管理员账号
            User existingUser = userService.getUserByUsername("school");
            if (existingUser != null) {
                // 更新密码、学校ID和状态
                existingUser.setPassword(passwordEncoder.encode("123456"));
                existingUser.setSchoolId(1L); // 设置默认学校ID为1
                existingUser.setStatus("1"); // 1表示正常
                userService.updateById(existingUser);
                response.put("success", true);
                response.put("message", "学校管理员密码已更新");
                response.put("user", existingUser);
            } else {
                // 创建新的学校管理员账号
                User schoolAdmin = new User();
                schoolAdmin.setUsername("school");
                schoolAdmin.setPassword(passwordEncoder.encode("123456"));
                schoolAdmin.setName("学校管理员");
                schoolAdmin.setEmail("school@example.com");
                schoolAdmin.setPhone("13800138000");
                schoolAdmin.setRole("school_admin"); // 设置角色为学校管理员
                schoolAdmin.setStatus("1"); // 1表示正常
                schoolAdmin.setSchoolId(1L); // 设置默认学校ID为1
                userService.save(schoolAdmin);
                response.put("success", true);
                response.put("message", "学校管理员账号创建成功");
                response.put("user", schoolAdmin);
            }
            return response;
        } catch (Exception e) {
            System.out.println("创建学校管理员失败: " + e.getMessage());
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "创建学校管理员失败: " + e.getMessage());
            return response;
        }
    }
}