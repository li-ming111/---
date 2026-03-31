<template>
  <div class="home-container">
    <!-- 顶部搜索栏 -->
    <div class="search-bar">
      <div class="search-content">
        <el-input
          v-model="searchQuery"
          placeholder="搜索管理功能、用户、资源..."
          prefix-icon="el-icon-search"
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch" icon="el-icon-search" class="search-button"></el-button>
          </template>
        </el-input>
      </div>
    </div>

    <!-- 管理概览 -->
    <div class="overview-section">
      <el-card class="overview-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">学校管理概览</span>
            <el-button type="primary" size="small" @click="startManaging" class="header-button">
              开始管理
            </el-button>
          </div>
        </template>
        <div class="overview-content">
          <div class="overview-item animate-fade-in">
            <div class="overview-icon task-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="overview-info">
              <h3>学生总数</h3>
              <p class="overview-value">{{ stats.totalStudents }}</p>
              <div class="progress-bar">
                <div class="progress-fill" style="width: 80%"></div>
              </div>
            </div>
          </div>
          <div class="overview-item animate-fade-in" style="animation-delay: 0.2s">
            <div class="overview-icon time-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="overview-info">
              <h3>资源数量</h3>
              <p class="overview-value">{{ stats.totalResources }}</p>
              <div class="progress-bar">
                <div class="progress-fill" style="width: 65%"></div>
              </div>
            </div>
          </div>
          <div class="overview-item animate-fade-in" style="animation-delay: 0.4s">
            <div class="overview-icon goal-icon">
              <i class="el-icon-bell"></i>
            </div>
            <div class="overview-info">
              <h3>通知数量</h3>
              <p class="overview-value">{{ stats.totalNotifications }}</p>
              <div class="progress-bar">
                <div class="progress-fill" style="width: 70%"></div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 左侧内容 -->
      <div class="left-content">
        <!-- 系统功能 -->
        <el-card class="mb-4 feature-modules-card animate-fade-in" style="animation-delay: 0.6s">
          <template #header>
            <div class="card-header">
              <span class="card-title">管理功能</span>
            </div>
          </template>
          <div class="feature-modules">
            <div class="feature-item hover-lift" @click="$router.push('/school-admin/users')">
              <div class="feature-icon plan-icon">
                <i class="el-icon-user"></i>
              </div>
              <span class="feature-text">用户管理</span>
            </div>
            <div class="feature-item hover-lift" @click="$router.push('/school-admin/resources')">
              <div class="feature-icon resource-icon">
                <i class="el-icon-document"></i>
              </div>
              <span class="feature-text">资源管理</span>
            </div>
            <div class="feature-item hover-lift" @click="$router.push('/school-admin/settings')">
              <div class="feature-icon goal-icon">
                <i class="el-icon-setting"></i>
              </div>
              <span class="feature-text">学校设置</span>
            </div>
            <div class="feature-item hover-lift" @click="$router.push('/school-admin/reports')">
              <div class="feature-icon career-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <span class="feature-text">数据报表</span>
            </div>
            <div class="feature-item hover-lift" @click="$router.push('/school-admin/audit-logs')">
              <div class="feature-icon community-icon">
                <i class="el-icon-notebook-2"></i>
              </div>
              <span class="feature-text">操作日志</span>
            </div>
            <div class="feature-item hover-lift" @click="$router.push('/school-admin/announcements')">
              <div class="feature-icon dashboard-icon">
                <i class="el-icon-bell"></i>
              </div>
              <span class="feature-text">通知管理</span>
            </div>
            <div class="feature-item hover-lift" @click="$router.push('/school-admin/roles')">
              <div class="feature-icon plan-icon">
                <i class="el-icon-s-custom"></i>
              </div>
              <span class="feature-text">角色管理</span>
            </div>
          </div>
        </el-card>
      </div>

      <!-- 右侧内容 -->
      <div class="right-content">
        <!-- 快捷入口 -->
        <el-card class="mb-4 quick-access-card animate-fade-in" style="animation-delay: 0.8s">
          <template #header>
            <div class="card-header">
              <span class="card-title">快捷入口</span>
            </div>
          </template>
          <div class="quick-access">
            <div class="quick-item hover-lift" @click="$router.push('/school-admin/users')">
              <div class="quick-icon user-icon">
                <i class="el-icon-user"></i>
              </div>
              <span class="quick-text">用户管理</span>
            </div>
            <div class="quick-item hover-lift" @click="$router.push('/school-admin/resources')">
              <div class="quick-icon resource-icon">
                <i class="el-icon-document"></i>
              </div>
              <span class="quick-text">资源管理</span>
            </div>
            <div class="quick-item hover-lift" @click="$router.push('/school-admin/settings')">
              <div class="quick-icon setting-icon">
                <i class="el-icon-setting"></i>
              </div>
              <span class="quick-text">学校设置</span>
            </div>
            <div class="quick-item hover-lift" @click="$router.push('/school-admin/reports')">
              <div class="quick-icon report-icon">
                <i class="el-icon-data-analysis"></i>
              </div>
              <span class="quick-text">数据报表</span>
            </div>
          </div>
        </el-card>

        <!-- 通知中心 -->
        <el-card class="mb-4 notification-card animate-fade-in" style="animation-delay: 1s">
          <template #header>
            <div class="card-header">
              <span class="card-title">学校通知</span>
              <el-badge :value="unreadCount" class="notification-badge"></el-badge>
            </div>
          </template>
          <div class="notifications">
            <div class="notification-item hover-highlight" v-for="(notification, index) in notifications" :key="index">
              <div class="notification-icon" :class="notification.iconClass">
                <i :class="notification.icon"></i>
              </div>
              <div class="notification-content">
                <p class="notification-text">{{ notification.text }}</p>
                <p class="notification-time">{{ notification.time }}</p>
              </div>
            </div>
          </div>
        </el-card>

        <!-- 学校数据统计 -->
        <el-card class="mb-4 stats-card animate-fade-in" style="animation-delay: 1.2s">
          <template #header>
            <div class="card-header">
              <span class="card-title">学校数据</span>
              <el-button type="text" size="small" @click="$router.push('/school-admin/reports')" class="header-text-button">
                详细分析
              </el-button>
            </div>
          </template>
          <div class="stats-content">
            <div class="stat-item hover-lift">
              <div class="stat-value">{{ stats.activeStudents }}</div>
              <div class="stat-label">活跃学生</div>
            </div>
            <div class="stat-item hover-lift">
              <div class="stat-value">{{ stats.resourceDownloads }}</div>
              <div class="stat-label">资源下载量</div>
            </div>
            <div class="stat-item hover-lift">
              <div class="stat-value">{{ stats.notificationReads }}%</div>
              <div class="stat-label">通知阅读率</div>
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
      searchQuery: '',
      unreadCount: 2,
      notifications: [
        {
          text: '新学生注册：李四',
          time: '今天 09:15',
          icon: 'el-icon-user',
          iconClass: 'info-icon'
        },
        {
          text: '新资源上传：《高等数学》课件',
          time: '昨天 14:30',
          icon: 'el-icon-document',
          iconClass: 'success-icon'
        },
        {
          text: '系统维护通知',
          time: '2天前',
          icon: 'el-icon-bell',
          iconClass: 'info-icon'
        }
      ],
      stats: {
        totalStudents: 582,
        totalResources: 1245,
        totalNotifications: 36,
        activeStudents: 421,
        resourceDownloads: 2847,
        notificationReads: 92.5
      }
    }
  },
  methods: {
    handleSearch() {
      if (this.searchQuery) {
        // 实现搜索逻辑
      }
    },
    startManaging() {
      this.$router.push('/school-admin/users')
    }
  }
}
</script>

<style scoped>
/* 动画效果 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.animate-fade-in {
  animation: fadeIn 0.6s ease-out forwards;
  opacity: 0;
}

/* 悬停效果 */
.hover-lift {
  transition: all 0.3s ease;
}

.hover-lift:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
}

.hover-highlight {
  transition: all 0.3s ease;
}

.hover-highlight:hover {
  background: #f8f9fa;
  transform: translateX(5px);
}

.home-container {
  padding: 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  min-height: 100vh;
}

/* 搜索栏 */
.search-bar {
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
  padding: 24px;
  transition: all 0.3s ease;
}

.search-bar:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.search-content {
  max-width: 800px;
  margin: 0 auto;
}

.search-input {
  border-radius: 25px;
  height: 48px;
  border: 2px solid #e4e7ed;
  transition: all 0.3s ease;
}

.search-input:focus {
  border-color: #4CAF50;
  box-shadow: 0 0 0 3px rgba(76, 175, 80, 0.1);
}

.search-button {
  border-radius: 25px;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.search-button:hover {
  background: #4CAF50;
  color: white;
}

/* 管理概览 */
.overview-section {
  margin-bottom: 24px;
}

.overview-card {
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: none;
  overflow: hidden;
  background: white;
  transition: all 0.3s ease;
}

.overview-card:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  height: 60px;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  display: flex;
  align-items: center;
  gap: 10px;
}

.header-button {
  border-radius: 20px;
  font-size: 12px;
  padding: 6px 16px;
  transition: all 0.3s ease;
}

.header-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
}

.header-text-button {
  font-size: 12px;
  color: #4CAF50;
  transition: all 0.3s ease;
}

.header-text-button:hover {
  color: #388E3C;
  transform: translateY(-1px);
}

.overview-content {
  display: flex;
  gap: 24px;
  padding: 20px;
}

.overview-item {
  flex: 1;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  transition: all 0.3s ease;
  border: 1px solid #e4e7ed;
}

.overview-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  border-color: #4CAF50;
}

.overview-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.overview-icon:hover {
  transform: scale(1.05);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.overview-icon.task-icon {
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
}

.overview-icon.time-icon {
  background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%);
}

.overview-icon.goal-icon {
  background: linear-gradient(135deg, #1976D2 0%, #1565C0 100%);
}

.overview-info {
  flex: 1;
}

.overview-info h3 {
  font-size: 14px;
  font-weight: 500;
  color: #606266;
  margin-bottom: 6px;
}

.overview-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
}

.progress-bar {
  width: 100%;
  height: 8px;
  background: #e4e7ed;
  border-radius: 4px;
  overflow: hidden;
  position: relative;
}

.progress-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.8s ease;
  background: linear-gradient(90deg, #4CAF50 0%, #81C784 100%);
  position: relative;
}

.progress-fill::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, transparent 0%, rgba(255, 255, 255, 0.3) 50%, transparent 100%);
  animation: shimmer 2s infinite;
}

@keyframes shimmer {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}

/* 主要内容区域 */
.main-content {
  display: flex;
  gap: 24px;
}

.left-content {
  flex: 2;
}

.right-content {
  flex: 1;
}

/* 卡片样式 */
.feature-modules-card,
.quick-access-card,
.notification-card,
.stats-card {
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  border: none;
  transition: all 0.3s ease;
  background: white;
  overflow: hidden;
}

.feature-modules-card:hover,
.quick-access-card:hover,
.notification-card:hover,
.stats-card:hover {
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.12);
  transform: translateY(-2px);
}

/* 功能模块 */
.feature-modules {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
  padding: 20px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.feature-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  border-color: #4CAF50;
}

.feature-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, transparent 0%, #4CAF50 50%, transparent 100%);
  transition: left 0.5s ease;
}

.feature-item:hover::before {
  left: 100%;
}

.feature-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  margin-bottom: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.feature-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.feature-icon.plan-icon {
  background: linear-gradient(135deg, #4CAF50 0%, #388E3C 100%);
}

.feature-icon.resource-icon {
  background: linear-gradient(135deg, #FF9800 0%, #F57C00 100%);
}

.feature-icon.goal-icon {
  background: linear-gradient(135deg, #1976D2 0%, #1565C0 100%);
}

.feature-icon.career-icon {
  background: linear-gradient(135deg, #9C27B0 0%, #7B1FA2 100%);
}

.feature-icon.community-icon {
  background: linear-gradient(135deg, #FF5722 0%, #E64A19 100%);
}

.feature-icon.dashboard-icon {
  background: linear-gradient(135deg, #00BCD4 0%, #0097A7 100%);
}

.feature-text {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  text-align: center;
  margin-top: 8px;
}

/* 快捷入口 */
.quick-access {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  padding: 20px;
}

.quick-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24px 15px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 3px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  cursor: pointer;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.quick-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
  border-color: #667eea;
}

.quick-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, transparent 0%, #667eea 50%, transparent 100%);
  transition: left 0.5s ease;
}

.quick-item:hover::before {
  left: 100%;
}

.quick-icon {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  margin-bottom: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.quick-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.2);
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

.quick-text {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  text-align: center;
  margin-top: 8px;
}

/* 通知中心 */
.notifications {
  display: flex;
  flex-direction: column;
  gap: 12px;
  padding: 20px;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  border-radius: 10px;
  transition: all 0.3s ease;
  border: 1px solid #f0f0f0;
}

.notification-item:hover {
  background: #f8f9fa;
  transform: translateX(6px);
  border-color: #e4e7ed;
}

.notification-icon {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
  flex-shrink: 0;
  margin-top: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

.notification-icon:hover {
  transform: scale(1.1);
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
}

.notification-icon.info-icon {
  background: #1976D2;
}

.notification-icon.success-icon {
  background: #4CAF50;
}

.notification-content {
  flex: 1;
}

.notification-text {
  font-size: 14px;
  color: #303133;
  margin-bottom: 3px;
  line-height: 1.4;
}

.notification-time {
  font-size: 12px;
  color: #909399;
}

.notification-badge {
  margin-left: 8px;
  background: #ff4d4f;
  box-shadow: 0 2px 8px rgba(255, 77, 79, 0.3);
}

/* 学校数据统计 */
.stats-content {
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding: 20px;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 12px;
  transition: all 0.3s ease;
  border: 1px solid #e4e7ed;
  position: relative;
  overflow: hidden;
}

.stat-item:hover {
  transform: translateY(-4px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.1);
  border-color: #4CAF50;
}

.stat-item::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #4CAF50 0%, #81C784 100%);
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #4CAF50;
  margin-bottom: 6px;
  transition: all 0.3s ease;
}

.stat-item:hover .stat-value {
  transform: scale(1.05);
}

.stat-label {
  font-size: 14px;
  color: #606266;
  text-align: center;
  line-height: 1.4;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .main-content {
    flex-direction: column;
  }
  
  .overview-content {
    flex-direction: column;
    gap: 16px;
  }
  
  .feature-modules {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .quick-access {
    grid-template-columns: repeat(4, 1fr);
  }
}

@media (max-width: 768px) {
  .home-container {
    padding: 16px;
  }
  
  .search-bar {
    padding: 20px;
    margin-bottom: 20px;
  }
  
  .overview-content {
    flex-direction: column;
    padding: 16px;
  }
  
  .overview-item {
    padding: 16px;
  }
  
  .feature-modules {
    grid-template-columns: repeat(2, 1fr);
    padding: 16px;
    gap: 16px;
  }
  
  .feature-item {
    padding: 20px;
  }
  
  .quick-access {
    grid-template-columns: repeat(2, 1fr);
    padding: 16px;
  }
  
  .search-input {
    height: 44px;
  }
  
  .search-button {
    width: 44px;
    height: 44px;
  }
  
  .card-title {
    font-size: 16px;
  }
  
  .overview-icon,
  .feature-icon,
  .quick-icon {
    width: 50px;
    height: 50px;
    font-size: 24px;
  }
  
  .overview-value {
    font-size: 18px;
  }
  
  .stat-value {
    font-size: 24px;
  }
  
  .card-header {
    padding: 0 16px;
    height: 56px;
  }
}

@media (max-width: 480px) {
  .home-container {
    padding: 12px;
  }
  
  .search-bar {
    padding: 16px;
    margin-bottom: 16px;
  }
  
  .overview-content {
    padding: 12px;
    gap: 12px;
  }
  
  .overview-item {
    padding: 12px;
    gap: 12px;
  }
  
  .feature-modules {
    grid-template-columns: 1fr;
    padding: 12px;
    gap: 12px;
  }
  
  .feature-item {
    padding: 16px;
  }
  
  .quick-access {
    grid-template-columns: repeat(2, 1fr);
    padding: 12px;
    gap: 12px;
  }
  
  .quick-item {
    padding: 16px 12px;
  }
  
  .notifications,
  .stats-content {
    padding: 12px;
  }
  
  .notification-item,
  .stat-item {
    padding: 10px;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
    padding: 0 12px;
    height: auto;
    padding-top: 12px;
    padding-bottom: 12px;
  }
  
  .header-button {
    align-self: flex-end;
  }
  
  .overview-icon,
  .feature-icon,
  .quick-icon {
    width: 44px;
    height: 44px;
    font-size: 20px;
  }
  
  .overview-value {
    font-size: 16px;
  }
  
  .stat-value {
    font-size: 20px;
  }
  
  .card-title {
    font-size: 14px;
  }
}
</style>