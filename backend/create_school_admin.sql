USE xueya;

-- 创建学校管理员账号
INSERT INTO user (username, password, role, status) VALUES ('school', '123456', 'school', '1');

-- 查看插入结果
SELECT id, username, password, role FROM user WHERE username = 'school';
