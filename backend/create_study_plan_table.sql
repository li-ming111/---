-- 创建学习计划表（如果不存在）
CREATE TABLE IF NOT EXISTS study_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    user_name VARCHAR(50),
    school_id BIGINT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    start_date VARCHAR(20),
    end_date VARCHAR(20),
    status VARCHAR(20) DEFAULT 'active',
    progress DOUBLE DEFAULT 0.0,
    priority VARCHAR(20) DEFAULT 'medium',
    create_time VARCHAR(30),
    update_time VARCHAR(30),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (school_id) REFERENCES school(id) ON DELETE SET NULL
);

-- 创建学习计划详情表（如果不存在）
CREATE TABLE IF NOT EXISTS study_plan_detail (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    plan_id BIGINT,
    task_name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date VARCHAR(20),
    end_date VARCHAR(20),
    status VARCHAR(20) DEFAULT 'pending',
    progress DOUBLE DEFAULT 0.0,
    priority VARCHAR(20) DEFAULT 'medium',
    create_time VARCHAR(30),
    update_time VARCHAR(30),
    FOREIGN KEY (plan_id) REFERENCES study_plan(id) ON DELETE CASCADE
);

-- 插入测试数据
INSERT IGNORE INTO study_plan (user_id, user_name, school_id, title, description, start_date, end_date, status, progress, priority, create_time, update_time) VALUES
(4, '测试用户', 1, 'Java学习计划', '学习Java核心技术和Spring框架', '2026-03-01', '2026-06-30', 'active', 30.0, 'high', NOW(), NOW()),
(4, '测试用户', 1, '英语学习计划', '提高英语口语和听力能力', '2026-03-01', '2026-12-31', 'active', 10.0, 'medium', NOW(), NOW());

-- 插入测试任务数据
INSERT IGNORE INTO study_plan_detail (plan_id, task_name, description, start_date, end_date, status, progress, priority, create_time, update_time) VALUES
(1, 'Java基础', '学习Java语法和面向对象编程', '2026-03-01', '2026-04-15', 'completed', 100.0, 'high', NOW(), NOW()),
(1, 'Spring Boot', '学习Spring Boot框架', '2026-04-16', '2026-05-30', 'in_progress', 50.0, 'high', NOW(), NOW()),
(2, '日常口语', '每天练习30分钟口语', '2026-03-01', '2026-12-31', 'active', 10.0, 'medium', NOW(), NOW());
