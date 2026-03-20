-- 创建学习资源表（如果不存在）
CREATE TABLE IF NOT EXISTS learning_resource (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    file_url VARCHAR(255),
    file_name VARCHAR(255),
    file_type VARCHAR(100),
    file_size BIGINT,
    category VARCHAR(100),
    tags VARCHAR(255),
    uploader_id BIGINT,
    uploader_name VARCHAR(100),
    school_id BIGINT,
    download_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    create_time VARCHAR(30),
    update_time VARCHAR(30),
    FOREIGN KEY (uploader_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (school_id) REFERENCES school(id) ON DELETE SET NULL
);

-- 创建用户目标表（如果不存在）
CREATE TABLE IF NOT EXISTS user_goal (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date VARCHAR(20),
    end_date VARCHAR(20),
    status VARCHAR(20) DEFAULT 'active',
    priority VARCHAR(20) DEFAULT 'medium',
    progress DOUBLE DEFAULT 0.0,
    create_time VARCHAR(30),
    update_time VARCHAR(30),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- 插入测试数据
INSERT IGNORE INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, create_time, update_time) VALUES
('Java核心技术', 'Java编程基础教程', '/uploads/java_core.pdf', 'java_core.pdf', 'application/pdf', 1048576, '编程', 'Java,基础', 4, '测试用户', 1, NOW(), NOW()),
('Spring Boot实战', 'Spring Boot框架学习', '/uploads/spring_boot.pdf', 'spring_boot.pdf', 'application/pdf', 2097152, '编程', 'Java,Spring', 4, '测试用户', 1, NOW(), NOW());

INSERT IGNORE INTO user_goal (user_id, title, description, start_date, end_date, status, priority, progress, create_time, update_time) VALUES
(4, '完成Java学习', '掌握Java核心技术和Spring框架', '2026-03-01', '2026-06-30', 'active', 'high', 30.0, NOW(), NOW()),
(4, '通过英语四级', '提高英语水平，通过四级考试', '2026-03-01', '2026-12-31', 'active', 'medium', 10.0, NOW(), NOW());
