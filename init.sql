-- 创建数据库
CREATE DATABASE IF NOT EXISTS xueya CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE xueya;

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50) NOT NULL,
    gender VARCHAR(10),
    phone VARCHAR(20),
    email VARCHAR(100),
    student_id VARCHAR(20) UNIQUE,
    id_card VARCHAR(18) UNIQUE,
    university VARCHAR(100),
    major_id BIGINT,
    grade VARCHAR(20),
    role VARCHAR(20) DEFAULT 'student',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 专业表
CREATE TABLE IF NOT EXISTS majors (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 学习资源表
CREATE TABLE IF NOT EXISTS learning_resources (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    type VARCHAR(50) NOT NULL,
    category VARCHAR(100),
    url VARCHAR(500),
    description TEXT,
    difficulty VARCHAR(20),
    duration INT,
    score DECIMAL(3,1),
    source VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 用户目标表
CREATE TABLE IF NOT EXISTS user_goals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    target_value VARCHAR(200),
    current_value VARCHAR(200),
    start_date DATE,
    end_date DATE,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 学习计划表
CREATE TABLE IF NOT EXISTS study_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    status VARCHAR(20) DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 学习计划详情表
CREATE TABLE IF NOT EXISTS study_plan_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT NOT NULL,
    task_name VARCHAR(200) NOT NULL,
    description TEXT,
    start_time DATETIME,
    end_time DATETIME,
    priority VARCHAR(20),
    status VARCHAR(20) DEFAULT 'pending',
    completed_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES study_plans(id)
);

-- 职业规划表
CREATE TABLE IF NOT EXISTS career_plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    career_goal VARCHAR(200) NOT NULL,
    industry VARCHAR(100),
    position VARCHAR(100),
    skills TEXT,
    experience TEXT,
    education TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 用户画像表
CREATE TABLE IF NOT EXISTS user_profiles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    learning_style VARCHAR(50),
    interests TEXT,
    strengths TEXT,
    weaknesses TEXT,
    career_orientation TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 学习记录表
CREATE TABLE IF NOT EXISTS learning_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    resource_id BIGINT,
    plan_detail_id BIGINT,
    start_time DATETIME,
    end_time DATETIME,
    duration INT,
    progress DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (resource_id) REFERENCES learning_resources(id),
    FOREIGN KEY (plan_detail_id) REFERENCES study_plan_details(id)
);

-- 激励体系表
CREATE TABLE IF NOT EXISTS incentives (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    value INT,
    obtained_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 心理测评表
CREATE TABLE IF NOT EXISTS psychological_evaluations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    evaluation_type VARCHAR(50) NOT NULL,
    score VARCHAR(100),
    result TEXT,
    evaluation_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 初始化专业数据
INSERT INTO majors (name, code, description) VALUES
('计算机科学与技术', 'CS', '培养具备计算机系统设计、软件开发等能力的专业人才'),
('软件工程', 'SE', '培养具备软件系统分析、设计、开发和维护能力的专业人才'),
('数据科学与大数据技术', 'DS', '培养具备大数据分析、处理和应用能力的专业人才'),
('人工智能', 'AI', '培养具备人工智能算法设计、应用开发能力的专业人才'),
('电子信息工程', 'EE', '培养具备电子电路设计、信息系统开发能力的专业人才');

-- 初始化学习资源数据
INSERT INTO learning_resources (title, type, category, url, description, difficulty, duration, score, source) VALUES
('Java核心技术', '视频', '专业核心课', 'https://example.com/java', 'Java编程语言的核心概念和应用', '中等', 480, 4.5, '中国大学MOOC'),
('数据结构与算法', '文档', '专业核心课', 'https://example.com/algorithm', '数据结构与算法的基本原理和应用', '困难', 360, 4.7, 'Coursera'),
('Python编程基础', '视频', '专业核心课', 'https://example.com/python', 'Python编程语言的基础语法和应用', '简单', 240, 4.3, 'edX'),
('数据库系统原理', '视频', '专业核心课', 'https://example.com/database', '数据库系统的基本原理和应用', '中等', 300, 4.4, '中国大学MOOC'),
('计算机网络', '文档', '专业核心课', 'https://example.com/network', '计算机网络的基本原理和应用', '中等', 320, 4.2, 'Coursera');

-- 初始化用户数据（密码为123456，已加密）
INSERT INTO users (username, password, name, gender, phone, email, student_id, id_card, university, major_id, grade, role) VALUES
('admin', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '管理员', '男', '13800138000', 'admin@example.com', 'admin001', '110101199001010001', '测试大学', 1, '管理员', 'admin'),
('student1', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '张三', '男', '13800138001', 'student1@example.com', '20210001', '110101199901010001', '测试大学', 1, '2021级', 'student'),
('student2', '$2a$10$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGga31lW', '李四', '女', '13800138002', 'student2@example.com', '20210002', '110101199901010002', '测试大学', 2, '2021级', 'student');

-- 初始化用户画像数据
INSERT INTO user_profiles (user_id, learning_style, interests, strengths, weaknesses, career_orientation) VALUES
(2, '视觉型', '编程、算法、人工智能', '逻辑思维、问题解决', '沟通表达', '软件工程师'),
(3, '听觉型', '设计、用户体验、前端开发', '创造力、审美能力', '数学推理', '前端工程师');

-- 初始化用户目标数据
INSERT INTO user_goals (user_id, type, title, description, target_value, current_value, start_date, end_date, status) VALUES
(2, '学习目标', '学期绩点', '提高本学期绩点', '3.8', '3.5', '2023-09-01', '2024-01-15', 'active'),
(2, '学习目标', '英语六级', '通过英语六级考试', '550', '480', '2023-09-01', '2023-12-15', 'active'),
(2, '职业目标', '实习', '找到软件工程师实习', '1', '0', '2023-11-01', '2024-02-28', 'active'),
(3, '学习目标', '学期绩点', '提高本学期绩点', '3.6', '3.2', '2023-09-01', '2024-01-15', 'active'),
(3, '职业目标', '前端开发', '掌握前端开发技能', '100', '60', '2023-09-01', '2024-06-30', 'active');

-- 初始化学习计划数据
INSERT INTO study_plans (user_id, name, description, start_date, end_date, status) VALUES
(2, '日常学习计划', '2023-2024学年第一学期日常学习计划', '2023-09-01', '2024-01-15', 'active'),
(2, '考研备考计划', '2024年考研备考计划', '2023-03-01', '2024-01-01', 'active'),
(3, '日常学习计划', '2023-2024学年第一学期日常学习计划', '2023-09-01', '2024-01-15', 'active');

-- 初始化学习计划详情数据
INSERT INTO study_plan_details (plan_id, task_name, description, start_time, end_time, priority, status) VALUES
(1, 'Java课程学习', '学习Java核心技术第三章', '2023-10-01 09:00:00', '2023-10-01 11:00:00', '高', 'pending'),
(1, '数据结构作业', '完成数据结构第三章习题', '2023-10-01 14:00:00', '2023-10-01 16:00:00', '中', 'pending'),
(1, '英语单词背诵', '背诵50个英语单词', '2023-10-01 19:00:00', '2023-10-01 20:00:00', '中', 'pending'),
(2, '数学复习', '复习高等数学第一章', '2023-10-01 08:00:00', '2023-10-01 10:00:00', '高', 'pending'),
(2, '专业课复习', '复习操作系统第一章', '2023-10-01 10:30:00', '2023-10-01 12:30:00', '高', 'pending'),
(3, '前端开发学习', '学习HTML5和CSS3', '2023-10-01 09:00:00', '2023-10-01 11:00:00', '高', 'pending'),
(3, 'JavaScript练习', '完成JavaScript基础练习', '2023-10-01 14:00:00', '2023-10-01 16:00:00', '中', 'pending');

-- 初始化职业规划数据
INSERT INTO career_plans (user_id, career_goal, industry, position, skills, experience, education) VALUES
(2, '成为高级软件工程师', '互联网', '后端开发工程师', 'Java, Spring Boot, MySQL, Redis', '无', '本科'),
(3, '成为前端开发工程师', '互联网', '前端开发工程师', 'HTML, CSS, JavaScript, React', '无', '本科');

-- 初始化激励数据
INSERT INTO incentives (user_id, type, name, value) VALUES
(2, '积分', '每日学习打卡', 5),
(2, '积分', '完成学习计划任务', 10),
(3, '积分', '每日学习打卡', 5),
(3, '勋章', '打卡达人', 1);

-- 初始化心理测评数据
INSERT INTO psychological_evaluations (user_id, evaluation_type, score, result, evaluation_date) VALUES
(2, 'SCL-90', '120', '心理健康状况良好', '2023-09-01'),
(3, 'SCL-90', '115', '心理健康状况良好', '2023-09-01');

-- 创建学校表
CREATE TABLE IF NOT EXISTS school (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    contact VARCHAR(100),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入全国主要高校数据
INSERT INTO school (name, code, address, contact) VALUES
('北京大学', 'PKU', '北京市海淀区颐和园路5号', '010-62751234'),
('清华大学', 'THU', '北京市海淀区清华园', '010-62785001'),
('复旦大学', 'FUDAN', '上海市杨浦区邯郸路220号', '021-65642222'),
('上海交通大学', 'SJTU', '上海市闵行区东川路800号', '021-54740000'),
('浙江大学', 'ZJU', '浙江省杭州市西湖区余杭塘路866号', '0571-87951000'),
('南京大学', 'NJU', '江苏省南京市栖霞区仙林大道163号', '025-83593186'),
('中国科学技术大学', 'USTC', '安徽省合肥市蜀山区金寨路96号', '0551-63602453'),
('哈尔滨工业大学', 'HIT', '黑龙江省哈尔滨市南岗区西大直街92号', '0451-86414060'),
('西安交通大学', 'XJTU', '陕西省西安市碑林区咸宁西路28号', '029-82668888'),
('北京理工大学', 'BIT', '北京市海淀区中关村南大街5号', '010-68913345'),
('北京航空航天大学', 'BUAA', '北京市海淀区学院路37号', '010-82317114'),
('同济大学', 'TONGJI', '上海市杨浦区四平路1239号', '021-65982200'),
('武汉大学', 'WHU', '湖北省武汉市武昌区八一路299号', '027-68754231'),
('华中科技大学', 'HUST', '湖北省武汉市洪山区珞喻路1037号', '027-87541114'),
('中山大学', 'SYSU', '广东省广州市海珠区新港西路135号', '020-84111598'),
('华南理工大学', 'SCUT', '广东省广州市天河区五山路381号', '020-87110000'),
('四川大学', 'SCU', '四川省成都市武侯区一环路南一段24号', '028-85406437'),
('电子科技大学', 'UESTC', '四川省成都市郫都区西源大道2006号', '028-61830114'),
('天津大学', 'TJU', '天津市南开区卫津路92号', '022-27403536'),
('南开大学', 'NANKAI', '天津市南开区卫津路94号', '022-23508219'),
('山东大学', 'SDU', '山东省济南市历城区山大南路27号', '0531-88364737'),
('吉林大学', 'JLU', '吉林省长春市朝阳区前进大街2699号', '0431-85168888'),
('厦门大学', 'XMU', '福建省厦门市思明区思明南路422号', '0592-2186110'),
('东南大学', 'SEU', '江苏省南京市玄武区四牌楼2号', '025-83792452'),
('大连理工大学', 'DUT', '辽宁省大连市甘井子区凌工路2号', '0411-84708322'),
('西北工业大学', 'NPU', '陕西省西安市碑林区友谊西路127号', '029-88492114'),
('中南大学', 'CSU', '湖南省长沙市岳麓区麓山南路932号', '0731-88876114'),
('重庆大学', 'CQU', '重庆市沙坪坝区沙正街174号', '023-65102371'),
('兰州大学', 'LZU', '甘肃省兰州市城关区天水南路222号', '0931-8912114'),
('中国人民大学', 'RUC', '北京市海淀区中关村大街59号', '010-62511601');

-- 创建角色表
CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    school_id BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES school(id)
);

-- 创建权限表
CREATE TABLE IF NOT EXISTS permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL,
    url VARCHAR(255),
    method VARCHAR(20),
    description VARCHAR(255),
    parent_id BIGINT,
    sort INT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建角色权限关联表
CREATE TABLE IF NOT EXISTS role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(id),
    FOREIGN KEY (permission_id) REFERENCES permission(id)
);

-- 创建用户角色关联表
CREATE TABLE IF NOT EXISTS user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- 创建操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    username VARCHAR(100),
    operation VARCHAR(255),
    ip VARCHAR(50),
    url VARCHAR(255),
    method VARCHAR(20),
    params TEXT,
    result TEXT,
    school_id BIGINT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (school_id) REFERENCES school(id)
);

-- 插入默认角色
INSERT INTO role (name, code, description, school_id) VALUES
('超级管理员', 'SUPER_ADMIN', '系统最高权限', 1),
('学校管理员', 'SCHOOL_ADMIN', '学校级管理员', 1),
('教师', 'TEACHER', '教师角色', 1),
('学生', 'STUDENT', '学生角色', 1);

-- 插入默认权限
INSERT INTO permission (name, code, url, method, description, parent_id, sort) VALUES
('用户管理', 'USER_MANAGE', '/api/users', 'GET', '用户管理权限', NULL, 1),
('角色管理', 'ROLE_MANAGE', '/api/roles', 'GET', '角色管理权限', NULL, 2),
('学校管理', 'SCHOOL_MANAGE', '/api/schools', 'GET', '学校管理权限', NULL, 3),
('专业管理', 'MAJOR_MANAGE', '/api/majors', 'GET', '专业管理权限', NULL, 4),
('学习资源管理', 'RESOURCE_MANAGE', '/api/resources', 'GET', '学习资源管理权限', NULL, 5),
('职业规划管理', 'CAREER_MANAGE', '/api/career-plans', 'GET', '职业规划管理权限', NULL, 6),
('学习计划管理', 'STUDY_PLAN_MANAGE', '/api/study-plans', 'GET', '学习计划管理权限', NULL, 7),
('用户目标管理', 'GOAL_MANAGE', '/api/goals', 'GET', '用户目标管理权限', NULL, 8),
('激励管理', 'INCENTIVE_MANAGE', '/api/incentives', 'GET', '激励管理权限', NULL, 9),
('日志管理', 'LOG_MANAGE', '/api/logs', 'GET', '日志管理权限', NULL, 10);