USE xueya;

-- 查看学校管理员的密码详细信息
SELECT username, HEX(password) as password_hex, LENGTH(password) as password_length FROM user WHERE username = 'school';
