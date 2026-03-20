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
