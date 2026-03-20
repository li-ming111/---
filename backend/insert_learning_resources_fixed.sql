-- Insert test data into learning_resource table
INSERT IGNORE INTO learning_resource (title, description, file_url, file_name, file_type, file_size, category, tags, uploader_id, uploader_name, school_id, create_time, update_time) VALUES
-- Programming Basics
('Core Java Volume I', 'Classic Java programming book covering core concepts and object-oriented programming', 'https://www.amazon.com/Core-Java-I-Fundamentals-11th/dp/0135166306', 'Core Java Volume I.pdf', 'PDF', 10485760, 'Programming Basics', 'Java,Object-Oriented,Programming', 1, 'Admin', 1, '2026-03-20 09:00:00', '2026-03-20 09:00:00'),
('Python Crash Course', 'Python programming guide for beginners with practical projects and exercises', 'https://www.amazon.com/Python-Crash-Course-Hands-Project-Based/dp/1593276036', 'Python Crash Course.pdf', 'PDF', 8388608, 'Programming Basics', 'Python,Beginner,Practical', 1, 'Admin', 1, '2026-03-20 09:05:00', '2026-03-20 09:05:00'),
('Professional JavaScript', 'Comprehensive guide to JavaScript covering advanced features and best practices', 'https://www.amazon.com/Professional-JavaScript-Developers-Nicholas-Zakas/dp/1119366447', 'Professional JavaScript.pdf', 'PDF', 12582912, 'Frontend Development', 'JavaScript,Frontend,Advanced', 1, 'Admin', 1, '2026-03-20 09:10:00', '2026-03-20 09:10:00'),

-- Database Resources
('Database System Concepts', 'Classic database textbook covering database design and SQL', 'https://www.amazon.com/Database-System-Concepts-Abraham-Silberschatz/dp/0073523321', 'Database System Concepts.pdf', 'PDF', 15728640, 'Database', 'Database,SQL,Design', 1, 'Admin', 1, '2026-03-20 09:15:00', '2026-03-20 09:15:00'),
('SQL Must Know', 'Practical guide to MySQL database for beginners', 'https://www.amazon.com/SQL-Must-Know-Knowledge-Required/dp/0672327120', 'SQL Must Know.pdf', 'PDF', 6291456, 'Database', 'MySQL,SQL,Beginner', 1, 'Admin', 1, '2026-03-20 09:20:00', '2026-03-20 09:20:00'),

-- Frontend Development
('HTML & CSS', 'Practical guide to HTML and CSS including responsive design', 'https://www.amazon.com/HTML-CSS-Design-Build-Websites/dp/1118008189', 'HTML & CSS.pdf', 'PDF', 9437184, 'Frontend Development', 'HTML,CSS,Responsive Design', 1, 'Admin', 1, '2026-03-20 09:25:00', '2026-03-20 09:25:00'),
('React Up & Running', 'Comprehensive guide to React framework with latest features and best practices', 'https://www.amazon.com/React-Up-Running-Development-Applications/dp/1491954620', 'React Up & Running.pdf', 'PDF', 11534336, 'Frontend Development', 'React,JavaScript,Frontend Framework', 1, 'Admin', 1, '2026-03-20 09:30:00', '2026-03-20 09:30:00'),

-- Backend Development
('Spring Boot in Action', 'Practical guide to Spring Boot framework including microservices development', 'https://www.amazon.com/Spring-Boot-Action-Craig-Walls/dp/161729254X', 'Spring Boot in Action.pdf', 'PDF', 13631488, 'Backend Development', 'Spring Boot,Java,Microservices', 1, 'Admin', 1, '2026-03-20 09:35:00', '2026-03-20 09:35:00'),
('Node.js in Action', 'Practical guide to Node.js platform including asynchronous programming and Express framework', 'https://www.amazon.com/Node-js-in-Action-Mike-Cantelon/dp/1617290572', 'Node.js in Action.pdf', 'PDF', 10485760, 'Backend Development', 'Node.js,JavaScript,Express', 1, 'Admin', 1, '2026-03-20 09:40:00', '2026-03-20 09:40:00'),

-- Computer Science Basics
('Introduction to Computer Science', 'Introduction to computer science covering core concepts', 'https://www.amazon.com/Introduction-Computer-Science-Java-Approach/dp/013465515X', 'Introduction to Computer Science.pdf', 'PDF', 14680064, 'Computer Science Basics', 'Computer Science,Introduction,Java', 1, 'Admin', 1, '2026-03-20 09:45:00', '2026-03-20 09:45:00'),
('Introduction to Algorithms', 'Classic textbook on data structures and algorithms', 'https://www.amazon.com/Introduction-Algorithms-3rd-MIT-Press/dp/0262033844', 'Introduction to Algorithms.pdf', 'PDF', 16777216, 'Computer Science Basics', 'Data Structures,Algorithms,Analysis', 1, 'Admin', 1, '2026-03-20 09:50:00', '2026-03-20 09:50:00'),

-- Networking
('Computer Networking: A Top-Down Approach', 'Classic networking textbook using top-down approach', 'https://www.amazon.com/Computer-Networking-Top-Down-Approach-7th/dp/0134433641', 'Computer Networking.pdf', 'PDF', 17825792, 'Networking', 'Computer Networks,TCP/IP,Protocols', 1, 'Admin', 1, '2026-03-20 09:55:00', '2026-03-20 09:55:00'),
('HTTP: The Definitive Guide', 'Comprehensive guide to HTTP protocol covering web communication', 'https://www.amazon.com/HTTP-The-Definitive-Guide-David/dp/1565925092', 'HTTP The Definitive Guide.pdf', 'PDF', 11534336, 'Networking', 'HTTP,Web,Protocols', 1, 'Admin', 1, '2026-03-20 10:00:00', '2026-03-20 10:00:00'),

-- Operating Systems
('Operating System Concepts', 'Classic operating systems textbook covering modern OS principles', 'https://www.amazon.com/Operating-System-Concepts-Abraham-Silberschatz/dp/1119801314', 'Operating System Concepts.pdf', 'PDF', 18874368, 'Operating Systems', 'Operating Systems,Principles,Design', 1, 'Admin', 1, '2026-03-20 10:05:00', '2026-03-20 10:05:00'),
('Linux Command Line', 'Practical guide to Linux command line and shell scripting', 'https://www.amazon.com/Linux-Command-Line-Shell-Scripting/dp/1119377816', 'Linux Command Line.pdf', 'PDF', 12582912, 'Operating Systems', 'Linux,Command Line,Shell', 1, 'Admin', 1, '2026-03-20 10:10:00', '2026-03-20 10:10:00'),

-- Software Engineering
('Software Engineering: A Practitioner''s Approach', 'Classic software engineering textbook covering development processes and methods', 'https://www.amazon.com/Software-Engineering-Practitioners-Approach-8th/dp/0073375977', 'Software Engineering.pdf', 'PDF', 19922944, 'Software Engineering', 'Software Engineering,Development Process,Methods', 1, 'Admin', 1, '2026-03-20 10:15:00', '2026-03-20 10:15:00'),
('Agile Software Development', 'Classic book on agile development covering Scrum and XP methods', 'https://www.amazon.com/Agile-Software-Development-Principles-Patterns/dp/0135974445', 'Agile Software Development.pdf', 'PDF', 13631488, 'Software Engineering', 'Agile Development,Scrum,XP', 1, 'Admin', 1, '2026-03-20 10:20:00', '2026-03-20 10:20:00');