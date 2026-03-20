<template>
  <div class="home-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-content">
        <h1>欢迎使用智学方舟学校管理员系统</h1>
        <p>全周期学生成长智能规划服务平台的学校管理中心，为您提供学校管理和数据分析功能。</p>
        <el-button type="primary" size="large" @click="startManaging" class="start-button">
          <i class="el-icon-s-grid"></i>
          <span>开始管理</span>
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧：系统功能 -->
      <div class="left-content">
        <!-- 系统功能 -->
        <el-card class="mb-4 function-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">管理功能</span>
            </div>
          </template>
          <div class="system-functions">
            <div class="function-item">
              <div class="function-icon user-management">
                <i class="el-icon-user"></i>
              </div>
              <div class="function-content">
                <h3>用户管理</h3>
                <p>管理学校用户账号，包括教师和学生账户。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon resource-management">
                <i class="el-icon-document"></i>
              </div>
              <div class="function-content">
                <h3>资源管理</h3>
                <p>管理学校学习资源，包括课程资料、书籍推荐等。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon system-settings">
                <i class="el-icon-setting"></i>
              </div>
              <div class="function-content">
                <h3>学校设置</h3>
                <p>配置学校参数，管理学校信息和课程设置。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon data-reports">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="function-content">
                <h3>数据报表</h3>
                <p>查看学校数据统计和分析报表。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon audit-logs">
                <i class="el-icon-notebook-2"></i>
              </div>
              <div class="function-content">
                <h3>操作日志</h3>
                <p>查看学校管理操作日志，追踪用户行为。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon notification-management">
                <i class="el-icon-bell"></i>
              </div>
              <div class="function-content">
                <h3>通知管理</h3>
                <p>发送学校通知，管理消息推送。</p>
              </div>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧：快捷入口 -->
      <div class="right-content">
        <!-- 快捷入口 -->
        <el-card class="mb-4 quick-access-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">快捷入口</span>
            </div>
          </template>
          <div class="quick-access">
            <div class="quick-item" @click="$router.push('/school-admin/users')">
              <div class="quick-icon user-icon">
                <i class="el-icon-user"></i>
              </div>
              <span class="quick-text">用户管理</span>
            </div>
            <div class="quick-item" @click="$router.push('/school-admin/resources')">
              <div class="quick-icon resource-icon">
                <i class="el-icon-document"></i>
              </div>
              <span class="quick-text">资源管理</span>
            </div>
            <div class="quick-item" @click="$router.push('/school-admin/settings')">
              <div class="quick-icon setting-icon">
                <i class="el-icon-setting"></i>
              </div>
              <span class="quick-text">学校设置</span>
            </div>
            <div class="quick-item" @click="$router.push('/school-admin/reports')">
              <div class="quick-icon report-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <span class="quick-text">数据报表</span>
            </div>
            <div class="quick-item" @click="$router.push('/school-admin/audit-logs')">
              <div class="quick-icon audit-icon">
                <i class="el-icon-notebook-2"></i>
              </div>
              <span class="quick-text">操作日志</span>
            </div>
            <div class="quick-item" @click="$router.push('/school-admin/announcements')">
              <div class="quick-icon announcement-icon">
                <i class="el-icon-bell"></i>
              </div>
              <span class="quick-text">通知管理</span>
            </div>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
    }
  },
  methods: {
    startManaging() {
      this.$router.push('/school-admin/users')
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 0;
}

/* 欢迎横幅 */
.welcome-banner {
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
  color: white;
  padding: 60px 30px;
  margin-bottom: 30px;
  border-radius: 0;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  position: relative;
  overflow: hidden;
}

.welcome-banner::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 100%);
  pointer-events: none;
}

.welcome-banner::after {
  content: '';
  position: absolute;
  bottom: -50px;
  right: -50px;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  pointer-events: none;
}

.banner-content {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
  text-align: center;
}

.banner-content h1 {
  font-size: 36px;
  font-weight: bold;
  margin-bottom: 15px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  animation: fadeInDown 0.8s ease;
}

.banner-content p {
  font-size: 18px;
  margin-bottom: 30px;
  opacity: 0.95;
  animation: fadeInUp 0.8s ease 0.2s both;
}

.start-button {
  font-size: 16px;
  padding: 12px 30px;
  border-radius: 30px;
  transition: all 0.3s ease;
  animation: fadeInUp 0.8s ease 0.4s both;
  background: white;
  color: #4CAF50;
  border: none;
  font-weight: 500;
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.3);
}

.start-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(76, 175, 80, 0.4);
  background: #f8f9fa;
}

/* 动画效果 */
@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 主要内容区域 */
.main-content {
  display: flex;
  gap: 20px;
  margin: 0 30px 30px 30px;
}

.left-content {
  flex: 2;
}

.right-content {
  flex: 1;
}

/* 卡片样式 */
.function-card,
.quick-access-card {
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  box-shadow: none;
  transition: all 0.3s ease;
}

.function-card:hover,
.quick-access-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0;
  border-bottom: 1px solid #e4e7ed;
  margin-bottom: 20px;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  padding: 15px 0;
}

/* 系统功能 */
.system-functions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.function-item {
  display: flex;
  align-items: flex-start;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.function-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.function-icon {
  width: 40px;
  height: 40px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  margin-right: 15px;
  color: white;
  flex-shrink: 0;
}

.function-icon.user-management {
  background-color: #1E88E5;
}

.function-icon.resource-management {
  background-color: #FF9800;
}

.function-icon.system-settings {
  background-color: #9C27B0;
}

.function-icon.data-reports {
  background-color: #FF5722;
}

.function-icon.audit-logs {
  background-color: #00BCD4;
}

.function-icon.notification-management {
  background-color: #FFC107;
}

.function-content {
  flex: 1;
}

.function-content h3 {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.function-content p {
  font-size: 12px;
  color: #606266;
  line-height: 1.4;
}

/* 快捷入口 */
.quick-access {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
  margin-top: 10px;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px 15px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 1px solid #f0f0f0;
}

.quick-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
  border-color: #e4e7ed;
}

.quick-icon {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-bottom: 10px;
  color: white;
}

.quick-icon.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.quick-icon.resource-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.quick-icon.setting-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.quick-icon.report-icon {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.quick-icon.audit-icon {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
}

.quick-icon.announcement-icon {
  background: linear-gradient(135deg, #fad0c4 0%, #ffd1ff 100%);
}

.quick-text {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  text-align: center;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
  
  .system-functions {
    grid-template-columns: 1fr;
  }
  
  .quick-access {
    grid-template-columns: repeat(3, 1fr);
  }
}

@media (max-width: 768px) {
  .welcome-banner {
    padding: 20px;
  }
  
  .banner-content h1 {
    font-size: 20px;
  }
  
  .main-content {
    margin: 0 20px 20px 20px;
  }
  
  .function-item {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }
  
  .function-icon {
    margin-right: 0;
    margin-bottom: 10px;
  }
  
  .quick-access {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .quick-item {
    padding: 15px 10px;
  }
  
  .quick-icon {
    width: 40px;
    height: 40px;
    font-size: 20px;
  }
  
  .quick-text {
    font-size: 12px;
  }
}
</style>