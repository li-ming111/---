USE xueya;

-- 查看学校管理员的密码
SELECT username, LENGTH(password) as password_length, password FROM user WHERE username = 'school';
