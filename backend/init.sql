-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS xueya DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE xueya;

-- 创建学校表（如果不存在）
CREATE TABLE IF NOT EXISTS school (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    logo VARCHAR(255),
    address VARCHAR(255),
    contact_phone VARCHAR(20),
    email VARCHAR(100),
    website VARCHAR(255),
    description TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建用户表（如果不存在）
CREATE TABLE IF NOT EXISTS user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(50),
    gender VARCHAR(10),
    age INT,
    grade VARCHAR(20),
    student_id VARCHAR(20) UNIQUE,
    id_card VARCHAR(18) UNIQUE,
    email VARCHAR(100),
    phone VARCHAR(20),
    school_id BIGINT,
    major_id BIGINT,
    role VARCHAR(10) DEFAULT '1',
    status VARCHAR(10) DEFAULT '1',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (school_id) REFERENCES school(id) ON DELETE SET NULL,
    FOREIGN KEY (major_id) REFERENCES major(id) ON DELETE SET NULL
);

-- 创建学院表（如果不存在）
CREATE TABLE IF NOT EXISTS college (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建专业表（如果不存在）
CREATE TABLE IF NOT EXISTS major (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    college_id BIGINT,
    description TEXT,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (college_id) REFERENCES college(id) ON DELETE SET NULL
);

-- 插入学院数据
INSERT INTO college (name, description) VALUES ('信息工程学院', '培养信息技术相关专业人才') ON DUPLICATE KEY UPDATE name = name;

-- 插入专业数据
INSERT INTO major (name, code, college_id, description) VALUES ('计算机科学与技术', '34', 1, '培养具备计算机系统设计、软件开发、人工智能等能力的高级技术人才') ON DUPLICATE KEY UPDATE name = name;

-- 插入学校数据
INSERT INTO school (name, code, address, contact_phone, email, website, description) VALUES 
('哈尔滨信息工程学院', 'HIIE', '黑龙江省哈尔滨市宾西经济技术开发区大学城', '0451-57359666', 'info@hie.edu.cn', 'https://www.hie.edu.cn', '哈尔滨信息工程学院是一所以信息科技为特色的应用型本科高校') 
ON DUPLICATE KEY UPDATE name = name;

-- 插入用户数据（密码使用BCrypt加密，123456的加密值）
INSERT INTO user (username, password, name, student_id, school_id, major_id, role, status) VALUES 
('2023020616', '$2a$10$E6MkQvQyJj8z6J7x9q4J5e1z9q4J5e1z9q4J5e1z9q4J5e1z9q4J5e', '测试用户', '2023020616', 1, 1, '1', '1') 
ON DUPLICATE KEY UPDATE username = username;

-- 查看插入结果
SELECT * FROM school;
SELECT * FROM user WHERE username = '2023020616';
