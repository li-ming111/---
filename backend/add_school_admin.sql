USE xueya;

INSERT INTO user (username, password, role, status) VALUES 
('school', '123456', 'school', '1') 
ON DUPLICATE KEY UPDATE username = username;

SELECT * FROM user WHERE username = 'school';