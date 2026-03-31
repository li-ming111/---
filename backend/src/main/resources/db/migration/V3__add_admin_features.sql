-- 内容审核表
CREATE TABLE IF NOT EXISTS content_review (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '内容标题',
    type VARCHAR(50) COMMENT '内容类型：document-文档, video-视频, audio-音频, image-图片, other-其他',
    description TEXT COMMENT '内容描述',
    uploader_id BIGINT COMMENT '上传者ID',
    uploader_name VARCHAR(100) COMMENT '上传者名称',
    school_id BIGINT COMMENT '学校ID',
    school_name VARCHAR(100) COMMENT '学校名称',
    file_url VARCHAR(500) COMMENT '文件URL',
    file_size BIGINT COMMENT '文件大小（字节）',
    status VARCHAR(50) DEFAULT 'pending' COMMENT '审核状态：pending-待审核, approved-已通过, rejected-已拒绝',
    reviewer_id BIGINT COMMENT '审核人ID',
    reviewer_name VARCHAR(100) COMMENT '审核人名称',
    review_comment TEXT COMMENT '审核意见',
    review_time DATETIME COMMENT '审核时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_type (type),
    INDEX idx_uploader_id (uploader_id),
    INDEX idx_school_id (school_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容审核表';

-- 系统公告表
CREATE TABLE IF NOT EXISTS system_announcement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL COMMENT '公告标题',
    content TEXT NOT NULL COMMENT '公告内容',
    priority VARCHAR(20) DEFAULT 'medium' COMMENT '优先级：low-低, medium-中, high-高',
    scope VARCHAR(50) DEFAULT 'all' COMMENT '发布范围：all-所有用户, role-按角色, school-按学校',
    target_role VARCHAR(50) COMMENT '目标角色',
    target_school_id BIGINT COMMENT '目标学校ID',
    publisher_id BIGINT COMMENT '发布人ID',
    publisher_name VARCHAR(100) COMMENT '发布人名称',
    status VARCHAR(50) DEFAULT 'active' COMMENT '状态：active-生效中, expired-已过期',
    read_count INT DEFAULT 0 COMMENT '阅读数',
    publish_time DATETIME COMMENT '发布时间',
    expiry_date DATETIME COMMENT '有效期',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_scope (scope),
    INDEX idx_publish_time (publish_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- 审计日志表
CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    admin_id BIGINT COMMENT '管理员ID',
    admin_name VARCHAR(100) COMMENT '管理员名称',
    operation_type VARCHAR(50) COMMENT '操作类型：login-登录, logout-退出, add-添加, edit-编辑, delete-删除, settings-修改设置',
    operation_content TEXT COMMENT '操作内容',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    request_url VARCHAR(500) COMMENT '请求URL',
    request_method VARCHAR(10) COMMENT '请求方法',
    request_params TEXT COMMENT '请求参数',
    response_status INT COMMENT '响应状态码',
    execution_time BIGINT COMMENT '执行时间（毫秒）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_admin_id (admin_id),
    INDEX idx_operation_type (operation_type),
    INDEX idx_create_time (create_time),
    INDEX idx_ip_address (ip_address)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';

-- 系统备份表
CREATE TABLE IF NOT EXISTS system_backup (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    backup_name VARCHAR(255) NOT NULL COMMENT '备份名称',
    backup_type VARCHAR(50) COMMENT '备份类型：auto-自动, manual-手动',
    backup_size BIGINT COMMENT '备份大小（字节）',
    file_path VARCHAR(500) COMMENT '备份文件路径',
    description TEXT COMMENT '备份描述',
    status VARCHAR(50) DEFAULT 'success' COMMENT '状态：success-成功, failed-失败',
    error_message TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '备份时间',
    INDEX idx_backup_type (backup_type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统备份表';

-- 系统设置表
CREATE TABLE IF NOT EXISTS system_setting (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE COMMENT '设置键',
    setting_value TEXT COMMENT '设置值',
    description VARCHAR(255) COMMENT '设置描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_setting_key (setting_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统设置表';

-- 插入默认系统设置
INSERT INTO system_setting (setting_key, setting_value, description) VALUES
('backup.auto_backup', 'true', '是否开启自动备份'),
('backup.backup_frequency', 'daily', '备份频率：daily-每天, weekly-每周, monthly-每月'),
('backup.backup_time', '02:00', '备份时间'),
('backup.backup_retention', '10', '备份保留数量'),
('review.auto_review', 'false', '是否开启自动审核'),
('review.sensitive_word_filter', 'true', '是否开启敏感词过滤'),
('review.file_type_limit', 'true', '是否限制文件类型'),
('review.file_size_limit', '52428800', '文件大小限制（字节，默认50MB)')
ON DUPLICATE KEY UPDATE setting_value = VALUES(setting_value);
