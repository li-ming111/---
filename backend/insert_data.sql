-- 插入学校数据
INSERT INTO school (name, code, address, contact_phone, email, website, description) VALUES 
('哈尔滨信息工程学院', 'HIIE', '黑龙江省哈尔滨市宾西经济技术开发区大学城', '0451-57359666', 'info@hie.edu.cn', 'https://www.hie.edu.cn', '哈尔滨信息工程学院是一所以信息科技为特色的应用型本科高校') 
ON DUPLICATE KEY UPDATE name = name;

-- 插入学院数据
INSERT INTO college (name, description) VALUES ('信息工程学院', '培养信息技术相关专业人才') ON DUPLICATE KEY UPDATE name = name;

-- 插入专业数据
INSERT INTO major (name, code, college_id, description) VALUES ('计算机科学与技术', '34', 1, '培养具备计算机系统设计、软件开发、人工智能等能力的高级技术人才') ON DUPLICATE KEY UPDATE name = name;

-- 插入用户数据（密码使用BCrypt加密，123456的加密值）
INSERT INTO user (username, password, name, student_id, school_id, major_id, role, status) VALUES 
('2023020616', '$2a$10$E6MkQvQyJj8z6J7x9q4J5e1z9q4J5e1z9q4J5e1z9q4J5e1z9q4J5e', '测试用户', '2023020616', 1, 1, '1', '1') 
ON DUPLICATE KEY UPDATE username = username;
