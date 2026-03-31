-- ============================================
-- 学涯助手系统数据初始化脚本
-- 包含：管理员账户、技能数据、学习资源、兴趣爱好、学习计划、目标管理、职业规划、打卡记录、社区数据
-- ============================================

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 第一部分：创建角色和管理员账户
-- ============================================

-- 创建超级管理员角色
INSERT INTO role (name, code, description, school_id, create_time, update_time)
SELECT '超级管理员', 'super_admin', '系统超级管理员，拥有所有权限', NULL, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM role WHERE code = 'super_admin');

-- 创建学校管理员角色
INSERT INTO role (name, code, description, school_id, create_time, update_time)
SELECT '学校管理员', 'school_admin', '学校管理员，管理本校数据', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM role WHERE code = 'school_admin');

-- 创建学生角色
INSERT INTO role (name, code, description, school_id, create_time, update_time)
SELECT '学生', 'student', '学生用户', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM role WHERE code = 'student');

-- 创建教师角色
INSERT INTO role (name, code, description, school_id, create_time, update_time)
SELECT '教师', 'teacher', '教师用户', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM role WHERE code = 'teacher');

-- 创建家长角色
INSERT INTO role (name, code, description, school_id, create_time, update_time)
SELECT '家长', 'parent', '家长用户', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM role WHERE code = 'parent');

-- 删除已存在的管理员账户
DELETE FROM user_role WHERE user_id IN (SELECT id FROM user WHERE username IN ('superadmin', 'schooladmin', 'schooladmin2'));
DELETE FROM user WHERE username IN ('superadmin', 'schooladmin', 'schooladmin2');

-- 插入超级管理员
INSERT INTO user (username, password, name, gender, age, major_id, grade, student_id, id_card, role, status, create_time, update_time, email, phone, school_id, student_stage)
VALUES ('superadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '超级管理员', '男', 35, NULL, NULL, NULL, '110101199001011234', '0', 'active', NOW(), NOW(), 'superadmin@xueya.edu.cn', '13900001000', NULL, NULL);

INSERT INTO user_role (user_id, role_id)
SELECT LAST_INSERT_ID(), id FROM role WHERE code = 'super_admin';

-- 插入学校管理员1
INSERT INTO user (username, password, name, gender, age, major_id, grade, student_id, id_card, role, status, create_time, update_time, email, phone, school_id, student_stage)
VALUES ('schooladmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '学校管理员', '女', 32, NULL, NULL, NULL, '110101199201012345', 'admin', 'active', NOW(), NOW(), 'schooladmin@xueya.edu.cn', '13900002000', 1, NULL);

INSERT INTO user_role (user_id, role_id)
SELECT LAST_INSERT_ID(), id FROM role WHERE code = 'school_admin';

-- 插入学校管理员2
INSERT INTO user (username, password, name, gender, age, major_id, grade, student_id, id_card, role, status, create_time, update_time, email, phone, school_id, student_stage)
VALUES ('schooladmin2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EO', '学校管理员2', '男', 28, NULL, NULL, NULL, '110101199501013456', 'admin', 'active', NOW(), NOW(), 'schooladmin2@xueya.edu.cn', '13900003000', 1, NULL);

INSERT INTO user_role (user_id, role_id)
SELECT LAST_INSERT_ID(), id FROM role WHERE code = 'school_admin';

-- ============================================
-- 第二部分：初始化技能数据
-- ============================================

-- 清空现有技能数据（谨慎使用）
-- DELETE FROM skill;

-- 插入技能数据（如果不存在）
INSERT INTO skill (name, type, category, description, level, create_time)
SELECT '编程', '专业技能', '专业技能', '编程语言和开发能力', 5, NOW()
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '编程');

INSERT INTO skill (name, type, category, description, level, create_time)
SELECT '数据库', '专业技能', '专业技能', '数据库设计和管理能力', 5, NOW()
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '数据库');

INSERT INTO skill (name, type, category, description, level, create_time)
SELECT '前端开发', '专业技能', '专业技能', '前端技术和用户界面开发', 5, NOW()
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '前端开发');

INSERT INTO skill (name, type, category, description, level, create_time)
SELECT '沟通能力', '通用技能', '通用技能', '与人交流和表达的能力', 5, NOW()
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '沟通能力');

INSERT INTO skill (name, type, category, description, level, create_time)
SELECT '团队协作', '通用技能', '通用技能', '与团队成员合作的能力', 5, NOW()
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '团队协作');

INSERT INTO skill (name, type, category, description, level, create_time)
SELECT '问题解决', '通用技能', '通用技能', '分析和解决问题的能力', 5, NOW()
WHERE NOT EXISTS (SELECT 1 FROM skill WHERE name = '问题解决');

-- ============================================
-- 第三部分：为用户添加技能评估数据
-- ============================================

-- 获取用户ID（假设用户身份证号为232321200501064619）
SET @user_id = (SELECT id FROM user WHERE id_card = '232321200501064619' LIMIT 1);

-- 如果用户存在，则添加技能评估数据
INSERT INTO user_skill (user_id, skill_id, score, level, assessment_date, improvement_plan, create_time, update_time)
SELECT @user_id, s.id, FLOOR(60 + RAND() * 40), FLOOR(1 + RAND() * 5), NULL, NULL, NOW(), NULL
FROM skill s
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM user_skill us WHERE us.user_id = @user_id AND us.skill_id = s.id);

-- ============================================
-- 第四部分：初始化学习资源数据
-- ============================================

-- 插入学习资源数据（如果不存在）
INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, student_stage, download_count, view_count, create_time, update_time, status)
SELECT 'Java编程基础', 'Java编程语言基础教程', '#', 'java_basic.pdf', 'application/pdf', 5242880, '课程', 'Java,编程,后端', 1, '管理员', 1, '大一', 0, 0, NOW(), NOW(), 'published'
WHERE NOT EXISTS (SELECT 1 FROM learning_resource WHERE title = 'Java编程基础');

INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, student_stage, download_count, view_count, create_time, update_time, status)
SELECT 'Python数据分析', 'Python数据分析入门教程', '#', 'python_data.pdf', 'application/pdf', 4194304, '课程', 'Python,数据分析,人工智能', 1, '管理员', 1, '大二', 0, 0, NOW(), NOW(), 'published'
WHERE NOT EXISTS (SELECT 1 FROM learning_resource WHERE title = 'Python数据分析');

INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, student_stage, download_count, view_count, create_time, update_time, status)
SELECT '前端开发实战', 'HTML/CSS/JavaScript实战教程', '#', 'frontend_dev.pdf', 'application/pdf', 6291456, '课程', '前端,HTML,CSS,JavaScript', 1, '管理员', 1, '大一', 0, 0, NOW(), NOW(), 'published'
WHERE NOT EXISTS (SELECT 1 FROM learning_resource WHERE title = '前端开发实战');

INSERT INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, student_stage, download_count, view_count, create_time, update_time, status)
SELECT '数据库设计指南', 'MySQL数据库设计最佳实践', '#', 'database_design.pdf', 'application/pdf', 3145728, '文档', '数据库,SQL,MySQL', 1, '管理员', 1, '大二', 0, 0, NOW(), NOW(), 'published'
WHERE NOT EXISTS (SELECT 1 FROM learning_resource WHERE title = '数据库设计指南');

-- ============================================
-- 第五部分：初始化兴趣爱好数据
-- ============================================

-- 插入兴趣爱好数据（如果不存在）
INSERT INTO interest (name, category, popularity, create_time)
SELECT '编程', '科技', 95, NOW()
WHERE NOT EXISTS (SELECT 1 FROM interest WHERE name = '编程');

INSERT INTO interest (name, category, popularity, create_time)
SELECT '人工智能', '科技', 88, NOW()
WHERE NOT EXISTS (SELECT 1 FROM interest WHERE name = '人工智能');

INSERT INTO interest (name, category, popularity, create_time)
SELECT '绘画', '艺术', 75, NOW()
WHERE NOT EXISTS (SELECT 1 FROM interest WHERE name = '绘画');

INSERT INTO interest (name, category, popularity, create_time)
SELECT '篮球', '体育', 82, NOW()
WHERE NOT EXISTS (SELECT 1 FROM interest WHERE name = '篮球');

INSERT INTO interest (name, category, popularity, create_time)
SELECT '数学', '学术', 70, NOW()
WHERE NOT EXISTS (SELECT 1 FROM interest WHERE name = '数学');

INSERT INTO interest (name, category, popularity, create_time)
SELECT '烹饪', '生活', 65, NOW()
WHERE NOT EXISTS (SELECT 1 FROM interest WHERE name = '烹饪');

-- ============================================
-- 第六部分：初始化学习计划数据
-- ============================================

-- 创建学习计划表（如果不存在）
CREATE TABLE IF NOT EXISTS study_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(255) NOT NULL COMMENT '计划标题',
    description TEXT COMMENT '计划描述',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    status VARCHAR(50) DEFAULT 'active' COMMENT '状态',
    progress INT DEFAULT 0 COMMENT '完成进度',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习计划表';

-- 创建计划任务表（如果不存在）
CREATE TABLE IF NOT EXISTS plan_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id BIGINT NOT NULL COMMENT '计划ID',
    title VARCHAR(255) NOT NULL COMMENT '任务标题',
    description TEXT COMMENT '任务描述',
    due_date DATE NOT NULL COMMENT '截止日期',
    status VARCHAR(50) DEFAULT 'pending' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_plan_id (plan_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='计划任务表';

-- 插入学习计划数据（如果用户存在）
SET @user_id = (SELECT id FROM user WHERE id_card = '232321200501064619' LIMIT 1);

INSERT INTO study_plan (user_id, title, description, start_date, end_date, status, progress, create_time, update_time)
SELECT @user_id, 'Java学习计划', '学习Java核心知识点和框架', '2024-09-01', '2024-12-31', 'active', 30, NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM study_plan WHERE user_id = @user_id AND title = 'Java学习计划');

INSERT INTO study_plan (user_id, title, description, start_date, end_date, status, progress, create_time, update_time)
SELECT @user_id, '英语学习计划', '提高英语听说读写能力', '2024-09-01', '2025-06-30', 'active', 15, NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM study_plan WHERE user_id = @user_id AND title = '英语学习计划');

INSERT INTO study_plan (user_id, title, description, start_date, end_date, status, progress, create_time, update_time)
SELECT @user_id, '数据结构与算法', '学习常见数据结构和算法', '2024-10-01', '2025-01-31', 'active', 0, NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM study_plan WHERE user_id = @user_id AND title = '数据结构与算法');

-- ============================================
-- 第七部分：初始化目标管理数据
-- ============================================

-- 创建目标管理表（如果不存在）
CREATE TABLE IF NOT EXISTS goal (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(255) NOT NULL COMMENT '目标标题',
    description TEXT COMMENT '目标描述',
    type VARCHAR(50) NOT NULL COMMENT '目标类型',
    target_date DATE NOT NULL COMMENT '目标日期',
    status VARCHAR(50) DEFAULT 'active' COMMENT '状态',
    progress INT DEFAULT 0 COMMENT '完成进度',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='目标管理表';

-- 插入目标数据（如果用户存在）
INSERT INTO goal (user_id, title, description, type, target_date, status, progress, create_time, update_time)
SELECT @user_id, '通过英语四级考试', '在12月份的考试中通过英语四级', 'short_term', '2024-12-15', 'active', 40, NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM goal WHERE user_id = @user_id AND title = '通过英语四级考试');

INSERT INTO goal (user_id, title, description, type, target_date, status, progress, create_time, update_time)
SELECT @user_id, '获得计算机二级证书', '在明年3月份的考试中获得计算机二级证书', 'medium_term', '2025-03-20', 'active', 0, NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM goal WHERE user_id = @user_id AND title = '获得计算机二级证书');

INSERT INTO goal (user_id, title, description, type, target_date, status, progress, create_time, update_time)
SELECT @user_id, '成为全栈开发工程师', '掌握前端和后端开发技能，成为全栈开发工程师', 'long_term', '2026-06-30', 'active', 20, NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM goal WHERE user_id = @user_id AND title = '成为全栈开发工程师');

-- ============================================
-- 第八部分：初始化职业规划数据
-- ============================================

-- 创建职业规划表（如果不存在）
CREATE TABLE IF NOT EXISTS career_planning (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    career_goal VARCHAR(255) NOT NULL COMMENT '职业目标',
    industry VARCHAR(100) DEFAULT NULL COMMENT '目标行业',
    position VARCHAR(100) DEFAULT NULL COMMENT '目标职位',
    required_skills TEXT COMMENT '所需技能',
    action_plan TEXT COMMENT '行动计划',
    timeline TEXT COMMENT '时间线',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职业规划表';

-- 插入职业规划数据（如果用户存在）
INSERT INTO career_planning (user_id, career_goal, industry, position, required_skills, action_plan, timeline, create_time, update_time)
SELECT @user_id, '成为高级软件工程师', '互联网', '高级软件工程师', 'Java, Spring Boot, MySQL, Redis, Docker', '1. 学习Java高级特性\n2. 掌握Spring Boot框架\n3. 学习数据库优化\n4. 学习微服务架构', '2024-09 开始学习Java高级特性\n2024-12 掌握Spring Boot框架\n2025-06 学习微服务架构\n2025-12 准备面试', NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM career_planning WHERE user_id = @user_id);

-- ============================================
-- 第九部分：初始化打卡记录数据
-- ============================================

-- 创建打卡记录表（如果不存在）
CREATE TABLE IF NOT EXISTS checkin (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    checkin_date DATE NOT NULL COMMENT '打卡日期',
    checkin_time TIME NOT NULL COMMENT '打卡时间',
    status VARCHAR(50) DEFAULT 'completed' COMMENT '状态',
    notes TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id (user_id),
    INDEX idx_checkin_date (checkin_date),
    UNIQUE KEY uk_user_date (user_id, checkin_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='打卡记录表';

-- 插入打卡记录数据（如果用户存在）
INSERT INTO checkin (user_id, checkin_date, checkin_time, status, notes)
SELECT @user_id, '2024-09-01', '08:30:00', 'completed', '第一天打卡'
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM checkin WHERE user_id = @user_id AND checkin_date = '2024-09-01');

INSERT INTO checkin (user_id, checkin_date, checkin_time, status, notes)
SELECT @user_id, '2024-09-02', '08:45:00', 'completed', '第二天打卡'
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM checkin WHERE user_id = @user_id AND checkin_date = '2024-09-02');

INSERT INTO checkin (user_id, checkin_date, checkin_time, status, notes)
SELECT @user_id, '2024-09-03', '09:00:00', 'completed', '第三天打卡'
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM checkin WHERE user_id = @user_id AND checkin_date = '2024-09-03');

INSERT INTO checkin (user_id, checkin_date, checkin_time, status, notes)
SELECT @user_id, '2024-09-04', '08:30:00', 'completed', '第四天打卡'
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM checkin WHERE user_id = @user_id AND checkin_date = '2024-09-04');

INSERT INTO checkin (user_id, checkin_date, checkin_time, status, notes)
SELECT @user_id, '2024-09-05', '08:40:00', 'completed', '第五天打卡'
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM checkin WHERE user_id = @user_id AND checkin_date = '2024-09-05');

-- ============================================
-- 第十部分：初始化社区数据
-- ============================================

-- 创建社区帖子表（如果不存在）
CREATE TABLE IF NOT EXISTS community_post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(255) NOT NULL COMMENT '帖子标题',
    content TEXT NOT NULL COMMENT '帖子内容',
    category VARCHAR(50) DEFAULT NULL COMMENT '帖子分类',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    comment_count INT DEFAULT 0 COMMENT '评论次数',
    status VARCHAR(50) DEFAULT 'published' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区帖子表';

-- 创建社区评论表（如果不存在）
CREATE TABLE IF NOT EXISTS community_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    status VARCHAR(50) DEFAULT 'approved' COMMENT '状态',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社区评论表';

-- 插入社区帖子数据（如果用户存在）
INSERT INTO community_post (user_id, title, content, category, view_count, comment_count, status, create_time, update_time)
SELECT @user_id, 'Java学习经验分享', '最近学习Java的一些心得和体会...', '技术分享', 120, 2, 'published', NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM community_post WHERE user_id = @user_id AND title = 'Java学习经验分享');

INSERT INTO community_post (user_id, title, content, category, view_count, comment_count, status, create_time, update_time)
SELECT @user_id, '英语学习方法', '分享一下我的英语学习方法...', '学习方法', 80, 1, 'published', NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM community_post WHERE user_id = @user_id AND title = '英语学习方法');

INSERT INTO community_post (user_id, title, content, category, view_count, comment_count, status, create_time, update_time)
SELECT @user_id, '校园生活感悟', '大学生活的一些感悟和思考...', '生活感悟', 150, 1, 'published', NOW(), NOW()
WHERE @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM community_post WHERE user_id = @user_id AND title = '校园生活感悟');

-- 插入社区评论数据
SET @post1_id = (SELECT id FROM community_post WHERE title = 'Java学习经验分享' LIMIT 1);
SET @post2_id = (SELECT id FROM community_post WHERE title = '英语学习方法' LIMIT 1);
SET @post3_id = (SELECT id FROM community_post WHERE title = '校园生活感悟' LIMIT 1);

INSERT INTO community_comment (post_id, user_id, content, status, create_time)
SELECT @post1_id, @user_id, '感谢分享，很有帮助！', 'approved', NOW()
WHERE @post1_id IS NOT NULL AND @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM community_comment WHERE post_id = @post1_id AND content = '感谢分享，很有帮助！');

INSERT INTO community_comment (post_id, user_id, content, status, create_time)
SELECT @post1_id, @user_id, '请问Java的并发编程怎么学习？', 'approved', NOW()
WHERE @post1_id IS NOT NULL AND @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM community_comment WHERE post_id = @post1_id AND content = '请问Java的并发编程怎么学习？');

INSERT INTO community_comment (post_id, user_id, content, status, create_time)
SELECT @post2_id, @user_id, '这个方法不错，我试试', 'approved', NOW()
WHERE @post2_id IS NOT NULL AND @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM community_comment WHERE post_id = @post2_id AND content = '这个方法不错，我试试');

INSERT INTO community_comment (post_id, user_id, content, status, create_time)
SELECT @post3_id, @user_id, '同感，大学生活确实很美好', 'approved', NOW()
WHERE @post3_id IS NOT NULL AND @user_id IS NOT NULL
AND NOT EXISTS (SELECT 1 FROM community_comment WHERE post_id = @post3_id AND content = '同感，大学生活确实很美好');

-- ============================================
-- 第十一部分：数据完整性校验
-- ============================================

SELECT '数据初始化完成' as message;

-- 校验1: 管理员账户
SELECT 
    '管理员账户' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 3 THEN '通过' ELSE '失败' END as result
FROM user 
WHERE username IN ('superadmin', 'schooladmin', 'schooladmin2');

-- 校验2: 技能数据
SELECT 
    '技能数据' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 6 THEN '通过' ELSE '失败' END as result
FROM skill;

-- 校验3: 学习资源
SELECT 
    '学习资源' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 4 THEN '通过' ELSE '失败' END as result
FROM learning_resource;

-- 校验4: 兴趣爱好
SELECT 
    '兴趣爱好' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 6 THEN '通过' ELSE '失败' END as result
FROM interest;

-- 校验5: 学习计划
SELECT 
    '学习计划' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 3 THEN '通过' ELSE '失败' END as result
FROM study_plan;

-- 校验6: 目标管理
SELECT 
    '目标管理' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 3 THEN '通过' ELSE '失败' END as result
FROM goal;

-- 校验7: 职业规划
SELECT 
    '职业规划' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 1 THEN '通过' ELSE '失败' END as result
FROM career_planning;

-- 校验8: 打卡记录
SELECT 
    '打卡记录' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 5 THEN '通过' ELSE '失败' END as result
FROM checkin;

-- 校验9: 社区帖子
SELECT 
    '社区帖子' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 3 THEN '通过' ELSE '失败' END as result
FROM community_post;

-- 校验10: 社区评论
SELECT 
    '社区评论' as check_item,
    COUNT(*) as count,
    CASE WHEN COUNT(*) >= 4 THEN '通过' ELSE '失败' END as result
FROM community_comment;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 使用说明
-- ============================================
-- 1. 在MySQL命令行中执行此脚本：
--    mysql -u root -p xueya_assistant < init_all_data.sql
--
-- 2. 或者在MySQL客户端中执行：
--    source d:/BS/backend/sql/init_all_data.sql
--
-- 3. 管理员登录信息：
--    超级管理员: superadmin / superadmin123
--    学校管理员: schooladmin / schooladmin123
--    第二学校管理员: schooladmin2 / schooladmin123
-- ============================================