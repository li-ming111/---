USE xueya;

-- 查看学校管理员账号信息
SELECT username, password, role FROM user WHERE username = 'school';
