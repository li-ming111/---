# 智学方舟 - 学生全周期成长智能规划服务平台

## 项目简介

智学方舟是一个面向大学生的全周期成长智能规划服务平台，旨在帮助学生科学规划学习成长旅程，提供个性化的学习规划和职业发展指导。系统包含学生端、超级管理员端和学校管理员端三个主要部分，通过智能化的功能模块，为学生、学校和管理员提供全方位的服务。

## 技术栈

### 前端
- Vue.js 3.x
- Element Plus
- Vue Router
- Axios
- ECharts

### 后端
- Spring Boot 3.x
- MySQL 8.x
- MyBatis Plus
- Spring Security
- JWT (JSON Web Token)

## 系统架构

系统采用前后端分离架构：
- 前端：负责用户界面和交互逻辑
- 后端：负责业务逻辑和数据处理
- 数据库：存储系统数据

## 核心功能

### 学生端
- 学习计划管理
- 学习资源管理
- 目标管理
- 职业规划
- 激励系统
- 每日打卡
- 学习社群
- 学习数据统计
- 技能测评
- 离线学习
- 校园适应
- 学习笔记
- 考试备考
- 兴趣拓展

### 超级管理员端
- 用户管理
- 学校管理
- 资源管理
- 系统设置
- 数据报表
- 审计日志
- 系统备份与恢复
- 批量操作
- 内容审核
- 系统公告

### 学校管理员端
- 用户管理
- 资源管理
- 学校设置
- 数据报表
- 操作日志
- 通知管理

## 环境要求

### 前端
- Node.js 16+
- npm 8+

### 后端
- Java 17+
- Maven 3.8+
- MySQL 8.0+

## 安装与部署

### 数据库初始化

1. 创建数据库
   ```sql
   CREATE DATABASE xueya_assistant CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. 执行数据初始化脚本
   ```bash
   mysql -u root -p xueya_assistant < backend/sql/init_all_data.sql
   ```

### 前端部署

1. 进入前端目录
   ```bash
   cd frontend
   ```

2. 安装依赖
   ```bash
   npm install
   ```

3. 启动开发服务器
   ```bash
   npm run dev
   ```

4. 构建生产环境
   ```bash
   npm run build
   ```

### 后端部署

1. 进入后端目录
   ```bash
   cd backend
   ```

2. 配置数据库连接
   - 修改 `src/main/resources/application.properties` 文件中的数据库连接信息

3. 编译项目
   ```bash
   mvn clean package
   ```

4. 运行项目
   ```bash
   mvn spring-boot:run
   ```
   或
   ```bash
   java -jar target/xueya-assistant-1.0.0.jar
   ```

## 系统使用

### 登录账号

#### 超级管理员
- 用户名：superadmin
- 密码：superadmin123
- 权限：系统所有权限

#### 学校管理员
- 用户名：schooladmin
- 密码：schooladmin123
- 权限：管理学校ID为1的所有数据

- 用户名：schooladmin2
- 密码：schooladmin123
- 权限：管理学校ID为1的所有数据

#### 学生账号
- 可通过注册功能创建
- 示例身份证号：232321200501064619（已有测试数据）

### 系统访问

- 前端：http://localhost:3000
- 后端API：http://localhost:8084

## 项目结构

```
BS/
├── backend/             # 后端代码
│   ├── src/             # 源代码
│   │   ├── main/java/   # Java源代码
│   │   └── main/resources/  # 配置文件
│   ├── sql/             # 数据初始化脚本
│   └── pom.xml          # Maven配置文件
├── frontend/            # 前端代码
│   ├── src/             # 源代码
│   ├── index.html       # 入口文件
│   └── package.json     # npm配置文件
├── .gitignore           # Git忽略文件
└── README.md            # 项目说明文档
```

## 开发指南

### 前端开发

1. 启动开发服务器
   ```bash
   cd frontend
   npm run dev
   ```

2. 访问开发环境
   - http://localhost:3000

### 后端开发

1. 启动开发服务器
   ```bash
   cd backend
   mvn spring-boot:run
   ```

2. 访问后端API
   - http://localhost:8084

### 数据初始化

系统提供了完整的数据初始化SQL脚本，位于 `backend/sql/init_all_data.sql`，包含：
- 管理员账户（超级管理员、学校管理员）
- 角色权限数据
- 技能数据
- 学习资源
- 兴趣爱好
- 学习计划、目标、职业规划示例数据
- 打卡记录
- 社区帖子与评论

## 系统安全

- JWT token认证
- 基于角色的权限控制（RBAC）
- 密码BCrypt加密存储
- 防SQL注入（MyBatis参数绑定）
- 防XSS攻击
- 防CSRF攻击
- 操作日志审计

## 数据库表结构

### 核心表
- user - 用户表
- role - 角色表
- user_role - 用户角色关联表
- skill - 技能表
- user_skill - 用户技能评估表
- learning_resource - 学习资源表
- study_plan - 学习计划表
- goal - 目标管理表
- career_planning - 职业规划表
- checkin - 打卡记录表
- community_post - 社区帖子表
- community_comment - 社区评论表
- interest - 兴趣爱好表

## 维护与更新

### 数据备份
- 定期备份数据库
- 系统自动备份功能

### 版本管理
- 语义化版本控制
- 版本更新日志

## 技术支持

- 在线帮助文档
- 技术支持邮箱
- 问题反馈渠道

## 许可证

MIT License

## 联系方式

- 项目地址：https://github.com/li-ming111/---
- 邮箱：your-email@example.com

---

© 2026 智学方舟 - 学生全周期成长智能规划服务平台