-- 插入哈尔滨信息工程学院
INSERT INTO school (id, name, code, created_at, updated_at) VALUES
(1, '哈尔滨信息工程学院', 'HIIE', NOW(), NOW())
ON DUPLICATE KEY UPDATE
name = VALUES(name),
code = VALUES(code),
updated_at = NOW();

-- 插入用户账号 2023020616
INSERT INTO user (id, username, password, name, gender, age, grade, student_id, id_card, email, phone, school_id, major_id, role, status, created_at, updated_at) VALUES
(1, '2023020616', '$2a$10$E4jvZJ6B2vQ8Yb4Y8Y8Y8e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7e7', '测试用户', '男', 20, '2023', '2023020616', '123456789012345678', 'test@example.com', '13800138000', 1, NULL, '1', '1', NOW(), NOW())
ON DUPLICATE KEY UPDATE
password = VALUES(password),
name = VALUES(name),
gender = VALUES(gender),
age = VALUES(age),
grade = VALUES(grade),
student_id = VALUES(student_id),
id_card = VALUES(id_card),
email = VALUES(email),
phone = VALUES(phone),
school_id = VALUES(school_id),
major_id = VALUES(major_id),
role = VALUES(role),
status = VALUES(status),
updated_at = NOW();
