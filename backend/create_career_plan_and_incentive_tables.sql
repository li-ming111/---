-- 创建职业规划表
CREATE TABLE IF NOT EXISTS career_plan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    school_id BIGINT NOT NULL,
    plan_name VARCHAR(255) NOT NULL,
    career_goal VARCHAR(500) NOT NULL,
    action_steps TEXT,
    timeline VARCHAR(255),
    resources VARCHAR(500),
    status VARCHAR(50) DEFAULT 'active',
    create_time VARCHAR(50),
    update_time VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (school_id) REFERENCES school(id) ON DELETE CASCADE
);

-- 创建激励表
CREATE TABLE IF NOT EXISTS incentive (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    type VARCHAR(50) NOT NULL,
    points INT NOT NULL,
    start_date VARCHAR(50),
    end_date VARCHAR(50),
    status VARCHAR(50) DEFAULT 'active',
    create_time VARCHAR(50),
    update_time VARCHAR(50)
);

-- 创建用户激励表
CREATE TABLE IF NOT EXISTS user_incentive (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    incentive_id BIGINT NOT NULL,
    points INT NOT NULL,
    claim_date VARCHAR(50),
    status VARCHAR(50) DEFAULT 'pending',
    create_time VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (incentive_id) REFERENCES incentive(id) ON DELETE CASCADE
);
