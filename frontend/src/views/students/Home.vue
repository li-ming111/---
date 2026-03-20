<template>
  <div class="home-container">
    <!-- 欢迎横幅 -->
    <div class="welcome-banner">
      <div class="banner-content">
        <h1>欢迎使用智学方舟</h1>
        <p>学生全周期成长智能规划服务平台，为您提供个性化的学习规划和职业发展指导。</p>
        <el-button type="primary" size="large" @click="startLearning" class="start-button">
          <i class="el-icon-notebook-2"></i>
          <span>开始使用</span>
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
              <span class="card-title">系统功能</span>
            </div>
          </template>
          <div class="system-functions">
            <div class="function-item">
              <div class="function-icon learning-plan">
                <i class="el-icon-notebook-2"></i>
              </div>
              <div class="function-content">
                <h3>学习计划</h3>
                <p>制定个性化学习计划，跟踪学习进度，提高学习效率。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon learning-resources">
                <i class="el-icon-document"></i>
              </div>
              <div class="function-content">
                <h3>学习资源</h3>
                <p>获取丰富的学习资源，包括课程资料、书籍推荐等。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon goal-management">
                <i class="el-icon-sunny"></i>
              </div>
              <div class="function-content">
                <h3>目标管理</h3>
                <p>设定学习目标，跟踪目标完成情况，激励自己不断进步。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon career-planning">
                <i class="el-icon-briefcase"></i>
              </div>
              <div class="function-content">
                <h3>职业规划</h3>
                <p>探索职业方向，制定职业发展计划，提升就业竞争力。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon community">
                <i class="el-icon-chat-dot-round"></i>
              </div>
              <div class="function-content">
                <h3>学习社群</h3>
                <p>创建和加入学习社群，与同学交流学习经验，共同进步。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon dashboard">
                <i class="el-icon-data-analysis"></i>
              </div>
              <div class="function-content">
                <h3>学习数据</h3>
                <p>分析学习数据，了解学习习惯，优化学习策略。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon skill-assessment">
                <i class="el-icon-medal"></i>
              </div>
              <div class="function-content">
                <h3>技能测评</h3>
                <p>评估个人技能水平，获取个性化提升建议。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon offline-learning">
                <i class="el-icon-download"></i>
              </div>
              <div class="function-content">
                <h3>离线学习</h3>
                <p>下载学习资源，离线学习，随时随地保持学习状态。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon privacy-setting">
                <i class="el-icon-lock"></i>
              </div>
              <div class="function-content">
                <h3>隐私设置</h3>
                <p>管理个人隐私设置，保护个人信息安全。</p>
              </div>
            </div>
            <div class="function-item">
              <div class="function-icon campus-adaptation">
                <i class="el-icon-location"></i>
              </div>
              <div class="function-content">
                <h3>校园适应</h3>
                <p>了解校园课程和活动，快速适应校园生活。</p>
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
            <div class="quick-item" @click="$router.push('/profile')">
              <div class="quick-icon user-icon">
                <i class="el-icon-user"></i>
              </div>
              <span class="quick-text">个人资料</span>
            </div>
            <div class="quick-item" @click="$router.push('/resources')">
              <div class="quick-icon resource-icon">
                <i class="el-icon-document"></i>
              </div>
              <span class="quick-text">学习资源</span>
            </div>
            <div class="quick-item" @click="$router.push('/goals')">
              <div class="quick-icon goal-icon">
                <i class="el-icon-sunny"></i>
              </div>
              <span class="quick-text">目标管理</span>
            </div>
            <div class="quick-item" @click="$router.push('/career')">
              <div class="quick-icon career-icon">
                <i class="el-icon-briefcase"></i>
              </div>
              <span class="quick-text">职业规划</span>
            </div>
            <div class="quick-item" @click="$router.push('/study-plan')">
              <div class="quick-icon plan-icon">
                <i class="el-icon-notebook-2"></i>
              </div>
              <span class="quick-text">学习计划</span>
            </div>
            <div class="quick-item" @click="$router.push('/checkin')">
              <div class="quick-icon checkin-icon">
                <i class="el-icon-date"></i>
              </div>
              <span class="quick-text">每日打卡</span>
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
    startLearning() {
      this.$router.push('/study-plan')
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
  background: linear-gradient(135deg, #1976D2 0%, #1565C0 100%);
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
  color: #1976D2;
  border: none;
  font-weight: 500;
  box-shadow: 0 4px 12px rgba(25, 118, 210, 0.3);
}

.start-button:hover {
  transform: translateY(-3px);
  box-shadow: 0 6px 16px rgba(25, 118, 210, 0.4);
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

.function-icon.learning-plan {
  background-color: #1E88E5;
}

.function-icon.learning-resources {
  background-color: #4CAF50;
}

.function-icon.goal-management {
  background-color: #FF9800;
}

.function-icon.career-planning {
  background-color: #9C27B0;
}

.function-icon.community {
  background-color: #FF5722;
}

.function-icon.dashboard {
  background-color: #00BCD4;
}

.function-icon.skill-assessment {
  background-color: #FFC107;
}

.function-icon.offline-learning {
  background-color: #795548;
}

.function-icon.privacy-setting {
  background-color: #607D8B;
}

.function-icon.campus-adaptation {
  background-color: #4CAF50;
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
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.quick-icon.goal-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.quick-icon.career-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.quick-icon.plan-icon {
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
}

.quick-icon.checkin-icon {
  background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
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