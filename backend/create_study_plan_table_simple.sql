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
