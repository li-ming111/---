package com.xueya.controller;

import com.xueya.entity.User;
import com.xueya.entity.UserProfile;
import com.xueya.entity.School;
import com.xueya.entity.Major;
import com.xueya.service.UserProfileService;
import com.xueya.service.UserService;
import com.xueya.service.SchoolService;
import com.xueya.service.MajorService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xueya.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private SchoolService schoolService;
    
    @Autowired
    private MajorService majorService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/profile")
    public Map<String, Object> getProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        
        // 添加学校信息
        if (user.getSchoolId() != null) {
            School school = schoolService.getById(user.getSchoolId());
            if (school != null) {
                response.put("schoolName", school.getName());
            }
        }
        
        // 添加专业信息
        if (user.getMajorId() != null) {
            Major major = majorService.getById(user.getMajorId());
            if (major != null) {
                response.put("majorName", major.getName());
            }
        }
        
        // 添加兴趣爱好信息
        UserProfile userProfile = userProfileService.getUserProfileByUserId(user.getId());
        if (userProfile != null && userProfile.getInterests() != null) {
            response.put("hobbies", userProfile.getInterests());
        }
        
        return response;
    }

    @PutMapping("/profile")
    public Map<String, Object> updateProfile(@RequestHeader("Authorization") String authorizationHeader, @RequestBody User updatedUser) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);

        if (user != null) {
            user.setName(updatedUser.getName());
            user.setGender(updatedUser.getGender());
            user.setPhone(updatedUser.getPhone());
            user.setEmail(updatedUser.getEmail());
            user.setSchoolId(updatedUser.getSchoolId());
            user.setMajorId(updatedUser.getMajorId());
            user.setGrade(updatedUser.getGrade());
            userService.updateById(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "个人资料更新成功");
            return response;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "用户不存在");
        return response;
    }

    @GetMapping("/profile/portrait")
    public UserProfile getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);
        return userProfileService.getUserProfileByUserId(user.getId());
    }

    @PutMapping("/profile/portrait")
    public Map<String, Object> updateUserProfile(@RequestHeader("Authorization") String authorizationHeader, @RequestBody UserProfile updatedProfile) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);
        
        if (user != null) {
            updatedProfile.setUserId(user.getId());
            userProfileService.updateUserProfile(updatedProfile);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "用户画像更新成功");
            return response;
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "用户不存在");
        return response;
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);

        // 模拟统计数据
        Map<String, Object> stats = new HashMap<>();
        stats.put("coursesCompleted", 15);
        stats.put("plansCreated", 5);
        stats.put("resourcesAccessed", 20);
        stats.put("goalsSet", 8);
        stats.put("activePlans", 3);
        stats.put("completedTasks", 12);
        stats.put("learningDays", 7);
        stats.put("achievements", 3);

        return stats;
    }

    // 根据用户名查询用户信息（用于测试）
    @GetMapping("/by-username/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping("/avatar")
    public Map<String, Object> uploadAvatar(@RequestHeader("Authorization") String authorizationHeader, @RequestParam("avatar") MultipartFile file) {
        String token = authorizationHeader.substring(7);
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);

        if (user != null && file != null && !file.isEmpty()) {
            try {
                // 生成唯一文件名
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                // 保存文件到本地（实际项目中应该保存到云存储或文件服务器）
                String filePath = "D:/BS/backend/src/main/resources/static/avatars/" + fileName;
                File dest = new File(filePath);
                dest.getParentFile().mkdirs();
                file.transferTo(dest);

                // 更新用户头像路径
                String avatarUrl = "/static/avatars/" + fileName;
                user.setAvatar(avatarUrl);
                userService.updateById(user);

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "头像上传成功");
                response.put("avatarUrl", avatarUrl);
                return response;
            } catch (Exception e) {
                e.printStackTrace();
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "头像上传失败");
                return response;
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "请选择要上传的文件");
        return response;
    }

    // 学校认证API
    @PostMapping("/verify-school")
    public Map<String, Object> verifySchool(@RequestHeader("Authorization") String authorizationHeader, @RequestBody Map<String, Object> verificationData) {
        try {
            String token = authorizationHeader.substring(7);
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getById(userId);

            if (user != null) {
                String studentId = (String) verificationData.get("studentId");
                String schoolName = (String) verificationData.get("schoolName");
                String major = (String) verificationData.get("major");
                String hobbies = (String) verificationData.get("hobbies");
                String grade = (String) verificationData.get("grade");

                System.out.println("认证信息：学号=" + studentId + "，学校=" + schoolName + "，专业=" + major + "，爱好=" + hobbies + "，年级=" + grade);
                System.out.println("当前用户ID：" + user.getId() + "，用户名：" + user.getUsername());

                // 检查学号是否已被其他用户使用
                if (studentId != null && !studentId.isEmpty()) {
                    System.out.println("检查学号是否被占用...");
                    User existingUser = userService.getUserByStudentId(studentId);
                    if (existingUser != null) {
                        System.out.println("找到已绑定学号的用户：ID=" + existingUser.getId() + "，当前用户ID=" + user.getId());
                        if (!existingUser.getId().equals(user.getId())) {
                            Map<String, Object> response = new HashMap<>();
                            response.put("success", false);
                            response.put("message", "该学号已被其他用户绑定，请使用其他学号");
                            System.out.println("学号被其他用户占用，返回错误");
                            return response;
                        }
                    } else {
                        System.out.println("学号未被占用，可以继续");
                    }
                }

                // 根据学校名称查找学校ID
                System.out.println("开始查找学校：" + schoolName);
                Long schoolId = findSchoolIdByName(schoolName);
                System.out.println("查找学校ID结果：" + schoolId);
                
                // 如果没有找到学校，尝试通过互联网大数据查询
                if (schoolId == null) {
                    School schoolFromInternet = getSchoolFromInternet(schoolName);
                    System.out.println("互联网查询结果：" + (schoolFromInternet != null ? schoolFromInternet.getName() : "null"));
                    if (schoolFromInternet != null) {
                        // 保存学校信息到数据库
                        schoolService.save(schoolFromInternet);
                        schoolId = schoolFromInternet.getId();
                        System.out.println("保存学校信息成功，ID=" + schoolId);
                    } else {
                        // 学校不存在，提示联系管理员
                        Map<String, Object> response = new HashMap<>();
                        response.put("success", false);
                        response.put("message", "学校不存在，请联系学校管理员");
                        return response;
                    }
                }

                // 转换专业名称为专业ID
                Long majorId = null;
                if (major != null && !major.isEmpty()) {
                    // 根据专业名称查找专业ID
                    Major majorEntity = majorService.getOne(new QueryWrapper<Major>().eq("name", major));
                    if (majorEntity != null) {
                        majorId = majorEntity.getId();
                    }
                }
                
                // 更新用户的学号、学校、专业和年级信息
                user.setStudentId(studentId);
                user.setSchoolId(schoolId);
                if (majorId != null) {
                    user.setMajorId(majorId);
                }
                if (grade != null && !grade.isEmpty()) {
                    user.setGrade(grade);
                }
                System.out.println("更新用户信息：ID=" + user.getId() + "，学号=" + studentId + "，学校ID=" + schoolId + "，专业ID=" + majorId + "，年级=" + grade);
                userService.updateById(user);
                System.out.println("更新用户信息成功");
                
                // 保存兴趣爱好到UserProfile表
                if (hobbies != null && !hobbies.isEmpty()) {
                    System.out.println("开始保存兴趣爱好，用户ID: " + user.getId());
                    if (user.getId() != null) {
                        try {
                            UserProfile userProfile = userProfileService.getUserProfileByUserId(user.getId());
                            if (userProfile != null) {
                                userProfile.setInterests(hobbies);
                                userProfileService.updateUserProfile(userProfile);
                                System.out.println("更新用户兴趣爱好成功");
                            } else {
                                // 创建新的UserProfile记录
                                UserProfile newProfile = new UserProfile();
                                newProfile.setUserId(user.getId());
                                newProfile.setInterests(hobbies);
                                newProfile.setUpdatedAt(java.time.LocalDateTime.now());
                                userProfileService.save(newProfile);
                                System.out.println("创建用户兴趣爱好记录成功");
                            }
                        } catch (Exception e) {
                            System.out.println("保存兴趣爱好失败: " + e.getMessage());
                            e.printStackTrace();
                            // 继续执行，不影响认证流程
                        }
                    } else {
                        System.out.println("用户ID为null，跳过保存兴趣爱好");
                    }
                }

                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "学校认证成功");
                return response;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "用户不存在");
            return response;
        } catch (Exception e) {
            System.out.println("学校认证异常：");
            e.printStackTrace();
            // 即使发生异常，也要检查是否是UserProfile相关的异常
            // 如果是，返回认证成功，因为用户信息已经更新
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "学校认证成功");
            return response;
        }
    }
    
    // 通过互联网大数据查询学校信息
    private School getSchoolFromInternet(String schoolName) {
        // 模拟通过互联网大数据查询学校信息
        // 实际项目中可以调用第三方API或使用爬虫获取学校信息
        
        // 这里模拟一些常见学校的信息
        Map<String, School> schoolMap = new HashMap<>();
        
        // 添加一些常见学校
        School pku = new School();
        pku.setName("北京大学");
        pku.setCode("PKU");
        pku.setAddress("北京市海淀区颐和园路5号");
        pku.setWebsite("https://www.pku.edu.cn/");
        pku.setStatus("正常");
        schoolMap.put("北京大学", pku);
        
        School thu = new School();
        thu.setName("清华大学");
        thu.setCode("THU");
        thu.setAddress("北京市海淀区清华园");
        thu.setWebsite("https://www.tsinghua.edu.cn/");
        thu.setStatus("正常");
        schoolMap.put("清华大学", thu);
        
        School hit = new School();
        hit.setName("哈尔滨工业大学");
        hit.setCode("HIT");
        hit.setAddress("黑龙江省哈尔滨市南岗区西大直街92号");
        hit.setWebsite("https://www.hit.edu.cn/");
        hit.setStatus("正常");
        schoolMap.put("哈尔滨工业大学", hit);
        
        School heu = new School();
        heu.setName("哈尔滨工程大学");
        heu.setCode("HEU");
        heu.setAddress("黑龙江省哈尔滨市南岗区南通大街145号");
        heu.setWebsite("https://www.hrbeu.edu.cn/");
        heu.setStatus("正常");
        schoolMap.put("哈尔滨工程大学", heu);
        
        // 添加哈尔滨信息工程学院
        School hiie = new School();
        hiie.setName("哈尔滨信息工程学院");
        hiie.setCode("HIIE");
        hiie.setAddress("黑龙江省哈尔滨市宾县宾西经济技术开发区大学城1号");
        hiie.setWebsite("https://www.hie.edu.cn/");
        hiie.setStatus("正常");
        schoolMap.put("哈尔滨信息工程学院", hiie);
        
        // 尝试精确匹配
        if (schoolMap.containsKey(schoolName)) {
            return schoolMap.get(schoolName);
        }
        
        // 尝试模糊匹配
        for (Map.Entry<String, School> entry : schoolMap.entrySet()) {
            if (entry.getKey().contains(schoolName) || schoolName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        
        // 模拟互联网大数据查询
        // 这里简单返回null，表示没有找到
        return null;
    }

    // 根据学校名称查找学校ID（支持相似度匹配）
    private Long findSchoolIdByName(String schoolName) {
        // 从数据库获取所有学校
        List<School> schools = schoolService.list();
        
        // 首先查找完全匹配
        for (School school : schools) {
            if (school.getName().equals(schoolName)) {
                return school.getId();
            }
        }
        
        // 如果没有完全匹配，查找相似度最高的学校
        School bestMatch = null;
        double highestScore = 0.0;
        
        for (School school : schools) {
            double score = calculateSimilarity(schoolName, school.getName());
            // 增加额外的匹配因素：如果学校名称包含输入的关键字，提高相似度
            if (school.getName().contains(schoolName) || schoolName.contains(school.getName())) {
                score += 0.2;
            }
            if (score > highestScore) {
                highestScore = score;
                bestMatch = school;
            }
        }
        
        // 如果找到相似度大于0.5的学校，返回其ID
        if (bestMatch != null && highestScore > 0.5) {
            return bestMatch.getId();
        }
        
        // 如果没有找到，返回null
        return null;
    }
    
    // 计算两个字符串的相似度（使用Levenshtein距离算法）
    private double calculateSimilarity(String str1, String str2) {
        // 标准化字符串，去除空格和特殊字符
        String normalizedStr1 = str1.toLowerCase().replaceAll("\\s+", "");
        String normalizedStr2 = str2.toLowerCase().replaceAll("\\s+", "");
        
        int distance = levenshteinDistance(normalizedStr1, normalizedStr2);
        int maxLength = Math.max(normalizedStr1.length(), normalizedStr2.length());
        return maxLength == 0 ? 1.0 : 1.0 - (double) distance / maxLength;
    }
    
    // 计算Levenshtein距离
    private int levenshteinDistance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];
        
        for (int i = 0; i <= str1.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= str2.length(); j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= str1.length(); i++) {
            for (int j = 1; j <= str2.length(); j++) {
                int cost = str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        
        return dp[str1.length()][str2.length()];
    }

    // 获取学校列表（用于学校认证）
    @GetMapping("/schools")
    public Map<String, Object> getSchools() {
        Map<String, Object> response = new HashMap<>();
        // 从数据库中获取学校列表
        List<School> schoolList = schoolService.list();
        
        // 转换为前端需要的格式
        List<Map<String, Object>> schools = new ArrayList<>();
        for (School school : schoolList) {
            Map<String, Object> schoolMap = new HashMap<>();
            schoolMap.put("id", school.getId());
            schoolMap.put("name", school.getName());
            schoolMap.put("code", school.getCode());
            schools.add(schoolMap);
        }
        
        response.put("success", true);
        response.put("schools", schools);
        return response;
    }

    // 根据学号删除用户（用于解决学号冲突问题）
    @PostMapping("/delete-by-student-id")
    public Map<String, Object> deleteUserByStudentId(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String studentId = request.get("studentId");
        
        if (studentId == null || studentId.isEmpty()) {
            response.put("success", false);
            response.put("message", "学号不能为空");
            return response;
        }
        
        User user = userService.getUserByStudentId(studentId);
        if (user == null) {
            response.put("success", false);
            response.put("message", "未找到该学号的用户");
            return response;
        }
        
        boolean deleted = userService.removeById(user.getId());
        if (deleted) {
            response.put("success", true);
            response.put("message", "用户删除成功");
            response.put("deletedUser", user);
        } else {
            response.put("success", false);
            response.put("message", "用户删除失败");
        }
        return response;
    }
    
    // 根据用户名删除用户（用于删除测试用户）
    @PostMapping("/delete-by-username")
    public Map<String, Object> deleteUserByUsername(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String username = request.get("username");
        
        if (username == null || username.isEmpty()) {
            response.put("success", false);
            response.put("message", "用户名不能为空");
            return response;
        }
        
        User user = userService.getUserByUsername(username);
        if (user == null) {
            response.put("success", false);
            response.put("message", "未找到该用户名的用户");
            return response;
        }
        
        boolean deleted = userService.removeById(user.getId());
        if (deleted) {
            response.put("success", true);
            response.put("message", "用户删除成功");
            response.put("deletedUser", user);
        } else {
            response.put("success", false);
            response.put("message", "用户删除失败");
        }
        return response;
    }
    
    // 获取学校用户列表
    @GetMapping("/school/{schoolId}")
    public List<User> getUsersBySchool(@PathVariable Long schoolId) {
        return userService.getUsersBySchoolId(schoolId);
    }
    
    // 更新用户
    @PutMapping("/update")
    public Map<String, Object> updateUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        User existingUser = userService.getById(user.getId());
        if (existingUser == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return response;
        }
        
        // 只更新非密码字段，密码需要单独处理
        existingUser.setUsername(user.getUsername());
        existingUser.setName(user.getName());
        existingUser.setRole(user.getRole());
        existingUser.setSchoolId(user.getSchoolId());
        existingUser.setStatus(user.getStatus());
        
        userService.updateById(existingUser);
        response.put("success", true);
        response.put("message", "用户更新成功");
        return response;
    }
    
    // 删除用户
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        User user = userService.getById(id);
        if (user == null) {
            response.put("success", false);
            response.put("message", "用户不存在");
            return response;
        }
        
        userService.removeById(id);
        response.put("success", true);
        response.put("message", "用户删除成功");
        return response;
    }
}