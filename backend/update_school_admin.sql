USE xueya;

-- 更新学校管理员密码为BCrypt格式
UPDATE user 
SET password = '$2a$10$Fs1MSff70OZKfMw7E9oH9uEqGcSRE649TsPL1f9iqy5sY2norYyS' 
WHERE username = 'school';

-- 验证更新结果
SELECT id, username, password, role FROM user WHERE username = 'school';