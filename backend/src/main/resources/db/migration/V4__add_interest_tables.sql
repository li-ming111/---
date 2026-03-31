-- 兴趣爱好表
CREATE TABLE IF NOT EXISTS interest (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '兴趣名称',
    category VARCHAR(50) COMMENT '兴趣分类',
    popularity INT DEFAULT 0 COMMENT '受欢迎程度',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_category (category),
    INDEX idx_popularity (popularity)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='兴趣爱好表';

-- 学习兴趣小组表
CREATE TABLE IF NOT EXISTS study_group (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL COMMENT '小组名称',
    description TEXT COMMENT '小组描述',
    leader_id BIGINT COMMENT '组长ID',
    member_count INT DEFAULT 0 COMMENT '成员数量',
    status VARCHAR(50) DEFAULT 'active' COMMENT '状态：active-活跃, inactive-不活跃',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_leader_id (leader_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学习兴趣小组表';

-- 小组成员表
CREATE TABLE IF NOT EXISTS group_member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id BIGINT NOT NULL COMMENT '小组ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role VARCHAR(50) DEFAULT 'member' COMMENT '角色：leader-组长, member-成员',
    join_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    status VARCHAR(50) DEFAULT 'active' COMMENT '状态：active-活跃, inactive-不活跃',
    INDEX idx_group_id (group_id),
    INDEX idx_user_id (user_id),
    UNIQUE KEY uk_group_user (group_id, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小组成员表';

-- 小组任务表
CREATE TABLE IF NOT EXISTS group_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_id BIGINT NOT NULL COMMENT '小组ID',
    title VARCHAR(200) NOT NULL COMMENT '任务标题',
    description TEXT COMMENT '任务描述',
    status VARCHAR(50) DEFAULT 'pending' COMMENT '状态：pending-待完成, in_progress-进行中, completed-已完成',
    assignee_id BIGINT COMMENT '负责人ID',
    due_date DATETIME COMMENT '截止日期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_group_id (group_id),
    INDEX idx_status (status),
    INDEX idx_assignee_id (assignee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='小组任务表';
