-- 向学习资源表中插入测试数据
INSERT IGNORE INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, create_time, update_time) VALUES
-- 编程基础资源
('Java核心技术卷I', 'Java编程语言的经典书籍，涵盖Java核心概念和面向对象编程', 'https://www.amazon.com/Core-Java-I-Fundamentals-11th/dp/0135166306', 'Core Java Volume I.pdf', 'PDF', 10485760, '编程基础', 'Java,面向对象,编程', 1, '管理员', 1, '2026-03-20 09:00:00', '2026-03-20 09:00:00'),
('Python编程从入门到实践', '适合初学者的Python编程指南，包含实际项目和练习', 'https://www.amazon.com/Python-Crash-Course-Hands-Project-Based/dp/1593276036', 'Python Crash Course.pdf', 'PDF', 8388608, '编程基础', 'Python,入门,实践', 1, '管理员', 1, '2026-03-20 09:05:00', '2026-03-20 09:05:00'),
('JavaScript高级程序设计', 'JavaScript语言的权威指南，涵盖高级特性和最佳实践', 'https://www.amazon.com/Professional-JavaScript-Developers-Nicholas-Zakas/dp/1119366447', 'Professional JavaScript.pdf', 'PDF', 12582912, '前端开发', 'JavaScript,前端,高级', 1, '管理员', 1, '2026-03-20 09:10:00', '2026-03-20 09:10:00'),

-- 数据库资源
('数据库系统概念', '数据库系统的经典教材，涵盖数据库设计和SQL', 'https://www.amazon.com/Database-System-Concepts-Abraham-Silberschatz/dp/0073523321', 'Database System Concepts.pdf', 'PDF', 15728640, '数据库', '数据库,SQL,设计', 1, '管理员', 1, '2026-03-20 09:15:00', '2026-03-20 09:15:00'),
('MySQL必知必会', 'MySQL数据库的实用指南，适合初学者', 'https://www.amazon.com/SQL-Must-Know-Knowledge-Required/dp/0672327120', 'SQL Must Know.pdf', 'PDF', 6291456, '数据库', 'MySQL,SQL,入门', 1, '管理员', 1, '2026-03-20 09:20:00', '2026-03-20 09:20:00'),

-- 前端开发资源
('HTML & CSS设计与构建网站', 'HTML和CSS的实用指南，包含响应式设计', 'https://www.amazon.com/HTML-CSS-Design-Build-Websites/dp/1118008189', 'HTML & CSS.pdf', 'PDF', 9437184, '前端开发', 'HTML,CSS,响应式设计', 1, '管理员', 1, '2026-03-20 09:25:00', '2026-03-20 09:25:00'),
('React权威指南', 'React框架的全面指南，包含最新特性和最佳实践', 'https://www.amazon.com/React-Up-Running-Development-Applications/dp/1491954620', 'React Up & Running.pdf', 'PDF', 11534336, '前端开发', 'React,JavaScript,前端框架', 1, '管理员', 1, '2026-03-20 09:30:00', '2026-03-20 09:30:00'),

-- 后端开发资源
('Spring Boot实战', 'Spring Boot框架的实用指南，包含微服务开发', 'https://www.amazon.com/Spring-Boot-Action-Craig-Walls/dp/161729254X', 'Spring Boot in Action.pdf', 'PDF', 13631488, '后端开发', 'Spring Boot,Java,微服务', 1, '管理员', 1, '2026-03-20 09:35:00', '2026-03-20 09:35:00'),
('Node.js实战', 'Node.js平台的实用指南，包含异步编程和Express框架', 'https://www.amazon.com/Node-js-in-Action-Mike-Cantelon/dp/1617290572', 'Node.js in Action.pdf', 'PDF', 10485760, '后端开发', 'Node.js,JavaScript,Express', 1, '管理员', 1, '2026-03-20 09:40:00', '2026-03-20 09:40:00'),

-- 计算机基础资源
('计算机科学概论', '计算机科学的入门教材，涵盖核心概念', 'https://www.amazon.com/Introduction-Computer-Science-Java-Approach/dp/013465515X', 'Introduction to Computer Science.pdf', 'PDF', 14680064, '计算机基础', '计算机科学,入门,Java', 1, '管理员', 1, '2026-03-20 09:45:00', '2026-03-20 09:45:00'),
('数据结构与算法分析', '数据结构和算法的经典教材，涵盖分析方法', 'https://www.amazon.com/Introduction-Algorithms-3rd-MIT-Press/dp/0262033844', 'Introduction to Algorithms.pdf', 'PDF', 16777216, '计算机基础', '数据结构,算法,分析', 1, '管理员', 1, '2026-03-20 09:50:00', '2026-03-20 09:50:00'),

-- 网络资源
('计算机网络：自顶向下方法', '计算机网络的经典教材，采用自顶向下的方法', 'https://www.amazon.com/Computer-Networking-Top-Down-Approach-7th/dp/0134433641', 'Computer Networking.pdf', 'PDF', 17825792, '网络', '计算机网络,TCP/IP,协议', 1, '管理员', 1, '2026-03-20 09:55:00', '2026-03-20 09:55:00'),
('HTTP权威指南', 'HTTP协议的权威指南，涵盖Web通信', 'https://www.amazon.com/HTTP-The-Definitive-Guide-David/dp/1565925092', 'HTTP The Definitive Guide.pdf', 'PDF', 11534336, '网络', 'HTTP,Web,协议', 1, '管理员', 1, '2026-03-20 10:00:00', '2026-03-20 10:00:00'),

-- 操作系统资源
('操作系统概念', '操作系统的经典教材，涵盖现代操作系统原理', 'https://www.amazon.com/Operating-System-Concepts-Abraham-Silberschatz/dp/1119801314', 'Operating System Concepts.pdf', 'PDF', 18874368, '操作系统', '操作系统,原理,设计', 1, '管理员', 1, '2026-03-20 10:05:00', '2026-03-20 10:05:00'),
('Linux命令行与shell脚本编程大全', 'Linux命令行和shell脚本的实用指南', 'https://www.amazon.com/Linux-Command-Line-Shell-Scripting/dp/1119377816', 'Linux Command Line.pdf', 'PDF', 12582912, '操作系统', 'Linux,命令行,shell', 1, '管理员', 1, '2026-03-20 10:10:00', '2026-03-20 10:10:00'),

-- 软件工程资源
('软件工程：实践者的研究方法', '软件工程的经典教材，涵盖开发过程和方法', 'https://www.amazon.com/Software-Engineering-Practitioners-Approach-8th/dp/0073375977', 'Software Engineering.pdf', 'PDF', 19922944, '软件工程', '软件工程,开发过程,方法', 1, '管理员', 1, '2026-03-20 10:15:00', '2026-03-20 10:15:00'),
('敏捷软件开发：原则、模式与实践', '敏捷开发的经典著作，涵盖Scrum和XP方法', 'https://www.amazon.com/Agile-Software-Development-Principles-Patterns/dp/0135974445', 'Agile Software Development.pdf', 'PDF', 13631488, '软件工程', '敏捷开发,Scrum,XP', 1, '管理员', 1, '2026-03-20 10:20:00', '2026-03-20 10:20:00');