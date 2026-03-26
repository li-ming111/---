<template>
  <div class="app">
    <!-- 登录和注册页面不显示导航栏和页脚 -->
    <template v-if="isAuthPage">
      <router-view />
    </template>
    <!-- 管理员页面 -->
    <template v-else-if="isAdminPage">
      <el-container class="main-container">
        <el-header height="60px" class="header">
          <div class="logo-container">
            <img :src="schoolLogo" alt="Logo" class="school-logo">
          </div>
          <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="transparent"
            text-color="#333"
            active-text-color="#1976D2"
          >
            <el-menu-item index="/admin">
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/admin/users">
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/schools">
              <span>学校管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/resources">
              <span>资源管理</span>
            </el-menu-item>
            <el-menu-item index="/admin/settings">
              <span>系统设置</span>
            </el-menu-item>
            <el-menu-item index="/admin/reports">
              <span>数据报表</span>
            </el-menu-item>
            <el-menu-item index="/admin/audit-logs">
              <span>审计日志</span>
            </el-menu-item>
            <el-menu-item index="/admin/backup-restore">
              <span>备份恢复</span>
            </el-menu-item>
            <el-menu-item index="/admin/bulk-operations">
              <span>批量操作</span>
            </el-menu-item>
            <el-menu-item index="/admin/content-review">
              <span>内容审核</span>
            </el-menu-item>
            <el-menu-item index="/admin/system-announcements">
              <span>系统公告</span>
            </el-menu-item>
          </el-menu>
          <div class="user-info">
            <el-dropdown v-if="isLoggedIn">
              <span class="user-dropdown">
                <el-avatar size="small" :src="userAvatar"></el-avatar>
                <span class="user-name">{{ userName }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="navigateTo('/admin/profile')">
                    <i class="el-icon-user"></i>
                    <span>个人资料</span>
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="logout">
                    <i class="el-icon-switch-button"></i>
                    <span>退出登录</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
        <el-footer height="60px" class="footer">
          <div class="footer-content">
            <p>© 2026 智学方舟 - 学生全周期成长智能规划服务平台</p>
            <p>让每个学生都能科学规划自己的学习成长旅程</p>
          </div>
        </el-footer>
      </el-container>
    </template>
    <!-- 学校管理员页面 -->
    <template v-else-if="isSchoolAdminPage">
      <el-container class="main-container">
        <el-header height="60px" class="header">
          <div class="logo-container">
            <img :src="schoolLogo" alt="Logo" class="school-logo">
          </div>
          <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="transparent"
            text-color="#333"
            active-text-color="#4CAF50"
          >
            <el-menu-item index="/school-admin">
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/school-admin/users">
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/school-admin/resources">
              <span>资源管理</span>
            </el-menu-item>
            <el-menu-item index="/school-admin/settings">
              <span>学校设置</span>
            </el-menu-item>
            <el-menu-item index="/school-admin/reports">
              <span>数据报表</span>
            </el-menu-item>
            <el-menu-item index="/school-admin/audit-logs">
              <span>操作日志</span>
            </el-menu-item>
            <el-menu-item index="/school-admin/announcements">
              <span>通知管理</span>
            </el-menu-item>
          </el-menu>
          <div class="user-info">
            <el-dropdown v-if="isLoggedIn">
              <span class="user-dropdown">
                <el-avatar size="small" :src="userAvatar"></el-avatar>
                <span class="user-name">{{ userName }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="navigateTo('/school-admin/profile')">
                    <i class="el-icon-user"></i>
                    <span>个人资料</span>
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="logout">
                    <i class="el-icon-switch-button"></i>
                    <span>退出登录</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
        <el-footer height="60px" class="footer">
          <div class="footer-content">
            <p>© 2026 智学方舟 - 学生全周期成长智能规划服务平台</p>
            <p>让每个学生都能科学规划自己的学习成长旅程</p>
          </div>
        </el-footer>
      </el-container>
    </template>
    <!-- 学生端页面显示完整布局 -->
    <template v-else>
      <el-container class="main-container">
        <el-header height="60px" class="header">
          <div class="logo-container">
            <img :src="schoolLogo" alt="Logo" class="school-logo">
          </div>
          <el-menu
            :default-active="activeIndex"
            class="el-menu-demo"
            mode="horizontal"
            @select="handleSelect"
            background-color="transparent"
            text-color="#333"
            active-text-color="#1976D2"
          >
            <el-menu-item index="/">
              <span>首页</span>
            </el-menu-item>
            <el-menu-item index="/study-plan">
              <span>学习计划</span>
            </el-menu-item>
            <el-menu-item index="/resources">
              <span>学习资源</span>
            </el-menu-item>
            <el-menu-item index="/goals">
              <span>目标管理</span>
            </el-menu-item>
            <el-menu-item index="/career">
              <span>职业规划</span>
            </el-menu-item>
             <el-menu-item index="/incentive">
              <span>激励系统</span>
            </el-menu-item>
            <el-menu-item index="/checkin">
              <span>每日打卡</span>
            </el-menu-item>
            <el-menu-item index="/community">
              <span>学习社群</span>
            </el-menu-item>
            <el-menu-item index="/dashboard">
              <span>学习数据</span>
            </el-menu-item>
            <el-menu-item index="/skill-assessment">
              <span>技能测评</span>
            </el-menu-item>
            <el-menu-item index="/offline-learning">
              <span>离线学习</span>
            </el-menu-item>
            <el-menu-item index="/campus-adaptation">
              <span>校园适应</span>
            </el-menu-item>
            <el-menu-item index="/study-note">
              <span>学习笔记</span>
            </el-menu-item>
            <el-menu-item index="/exam">
              <span>考试备考</span>
            </el-menu-item>
            <el-menu-item index="/interest">
              <span>兴趣拓展</span>
            </el-menu-item>
          </el-menu>
          <div class="user-info">
            <el-button v-if="!isLoggedIn" type="info" plain @click="navigateTo('/login')">登录</el-button>
            <el-dropdown v-else>
              <span class="user-dropdown">
                <el-avatar size="small" :src="userAvatar"></el-avatar>
                <span class="user-name">{{ userName }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="navigateTo('/profile')">
                    <i class="el-icon-user"></i>
                    <span>个人资料</span>
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="logout">
                    <i class="el-icon-switch-button"></i>
                    <span>退出登录</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main class="main-content">
          <router-view />
        </el-main>
        <el-footer height="60px" class="footer">
          <div class="footer-content">
            <p>© 2026 智学方舟 - 学生全周期成长智能规划服务平台</p>
            <p>让每个学生都能科学规划自己的学习成长旅程</p>
          </div>
        </el-footer>
      </el-container>
    </template>
    
    <!-- AI助手组件 -->
    <AIAssistant />
  </div>
</template>

<script>
import AIAssistant from './components/AIAssistant.vue'
import logo from '@/assets/logo.jpg'

export default {
  components: {
    AIAssistant
  },
  data() {
    return {
      activeIndex: '/',
      isLoggedIn: false,
      userAvatar: logo,
      userName: '用户',
      schoolLogo: logo,
      schoolName: ''
    }
  },
  computed: {
    isAuthPage() {
      const authPaths = ['/login', '/register']
      return authPaths.includes(this.$route.path)
    },
    isAdminPage() {
      return this.$route.path.startsWith('/admin')
    },
    isSchoolAdminPage() {
      return this.$route.path.startsWith('/school-admin')
    }
  },
  mounted() {
    this.activeIndex = this.$route.path
    this.checkLoginStatus()
  },
  watch: {
    $route(to) {
      this.activeIndex = to.path
      // 每次路由变化时检查登录状态
      this.checkLoginStatus()
    }
  },
  methods: {
    handleSelect(key) {
      this.$router.push(key)
    },
    navigateTo(path) {
      this.$router.push(path)
    },
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      const user = localStorage.getItem('user')
      const school = localStorage.getItem('school')
      this.isLoggedIn = !!token
      if (user) {
        const userInfo = JSON.parse(user)
        // 根据用户角色显示不同的名称
        if (userInfo.role === 'super_admin' || userInfo.role === '0') {
          this.userName = '超级管理员'
        } else if (userInfo.role === 'school_admin' || userInfo.role === 'school') {
          this.userName = '学校管理员'
        } else {
          this.userName = userInfo.name || userInfo.username
        }
      }
      if (school) {
        try {
          const schoolInfo = JSON.parse(school)
          if (schoolInfo) {
            this.schoolName = schoolInfo.name || ''
          }
        } catch (error) {
          console.error('解析school信息失败:', error)
        }
      }
      // 始终使用默认的logo
      this.schoolLogo = logo
      this.userAvatar = logo
    },
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      localStorage.removeItem('school')
      this.isLoggedIn = false
      this.schoolLogo = logo
      this.userAvatar = logo
      this.schoolName = ''
      this.$router.push('/login')
    }
  },
  created() {
    // 将checkLoginStatus方法暴露给全局，以便子组件调用
    this.$nextTick(() => {
      if (this.$root) {
        this.$root.checkLoginStatus = this.checkLoginStatus
      }
    })
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  margin: 0;
  padding: 0;
  background-color: white;
}

.app {
  min-height: 100vh;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
  width: 100%;
}

/* 非认证页面的样式 */
.main-container {
  min-height: 100vh;
  background-color: white;
  overflow: hidden;
  width: 100%;
  margin: 0;
}

.header {
  background: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 50px;
  color: #333;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  height: 60px;
  position: relative;
  z-index: 100;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 20px;
  z-index: 1;
}

.school-logo {
  height: 50px;
  width: auto;
  border-radius: 4px;
  padding: 2px;
}

.logo-text {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.school-name {
  font-size: 18px;
  font-weight: bold;
  color: #333;
  margin: 0;
  line-height: 1.2;
}

.el-menu-demo {
  flex: 1;
  margin-left: 40px;
  background-color: transparent;
  border-bottom: none;
  z-index: 1;
}

.el-menu-item {
  color: #333;
  font-size: 16px;
  padding: 0 25px;
  transition: all 0.3s ease;
  height: 70px;
  line-height: 70px;
  margin: 0;
  border-radius: 0;
  position: relative;
}

.el-menu-item:hover {
  background-color: rgba(25, 118, 210, 0.1);
  color: #1976D2;
}

.el-menu-item.is-active {
  color: #1976D2;
  background-color: rgba(25, 118, 210, 0.15);
  border-bottom: 3px solid #1976D2;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 20px;
  z-index: 1;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 8px 16px;
  border-radius: 25px;
  transition: all 0.3s ease;
  background-color: rgba(25, 118, 210, 0.05);
  border: 1px solid rgba(25, 118, 210, 0.2);
}

.user-dropdown:hover {
  background-color: rgba(25, 118, 210, 0.1);
  border-color: rgba(25, 118, 210, 0.3);
}

.user-name {
  font-size: 15px;
  font-weight: 500;
  color: #333;
}

.el-button {
  font-size: 15px;
  padding: 8px 16px;
  transition: all 0.3s ease;
  border-color: rgba(25, 118, 210, 0.5);
  color: #1976D2;
}

.el-button:hover {
  border-color: #1976D2;
  background-color: rgba(25, 118, 210, 0.1);
}

.main-content {
  padding: 40px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e3f2fd 100%);
  min-height: calc(100vh - 150px);
  position: relative;
  overflow: hidden;
}

.main-content::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 100px;
  background: linear-gradient(180deg, rgba(25, 118, 210, 0.1) 0%, rgba(25, 118, 210, 0) 100%);
  pointer-events: none;
}

.footer {
  background: linear-gradient(135deg, #1976D2 0%, #1565C0 100%);
  border-top: none;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 70px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 -4px 20px rgba(0, 0, 0, 0.15);
}

.footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(90deg, rgba(255,255,255,0.1) 0%, rgba(255,255,255,0) 100%);
  pointer-events: none;
}

.footer-content {
  text-align: center;
  color: white;
  font-size: 14px;
  z-index: 1;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.footer-content p {
  margin: 5px 0;
  transition: all 0.3s ease;
}

.footer-content p:hover {
  color: rgba(255, 255, 255, 0.8);
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .header {
    padding: 0 30px;
  }
  
  .logo-container {
    gap: 12px;
  }
  
  .school-logo {
    height: 35px;
  }
  
  .school-name {
    font-size: 15px;
  }
  
  .el-menu-demo {
    margin-left: 40px;
  }
  
  .el-menu-item {
    padding: 0 18px;
    font-size: 13px;
  }
}

@media (max-width: 992px) {
  .header {
    padding: 0 25px;
  }
  
  .logo-container {
    gap: 10px;
  }
  
  .school-logo {
    height: 32px;
  }
  
  .school-name {
    font-size: 14px;
  }
  
  .el-menu-demo {
    margin-left: 30px;
  }
  
  .el-menu-item {
    padding: 0 15px;
    font-size: 12px;
  }
  
  .user-name {
    font-size: 12px;
  }
}

@media (max-width: 768px) {
  .main-container {
    border-radius: 0;
  }
  
  .header {
    padding: 0 20px;
    height: 55px;
  }
  
  .logo-container {
    gap: 8px;
  }
  
  .school-logo {
    height: 28px;
  }
  
  .school-name {
    font-size: 12px;
  }
  
  .el-menu-demo {
    margin-left: 20px;
  }
  
  .el-menu-item {
    font-size: 11px;
    padding: 0 10px;
    height: 55px;
    line-height: 55px;
  }
  
  .main-content {
    padding: 20px;
    min-height: calc(100vh - 115px);
  }
  
  .user-name {
    display: none;
  }
  
  .user-dropdown {
    padding: 4px 8px;
  }
  
  .footer {
    height: 60px;
  }
  
  .footer-content {
    font-size: 12px;
  }
}

@media (max-width: 576px) {
  .header {
    padding: 0 15px;
  }
  
  .logo-container {
    gap: 6px;
  }
  
  .school-logo {
    height: 24px;
  }
  
  .school-name {
    font-size: 10px;
  }
  
  .el-menu-demo {
    margin-left: 10px;
  }
  
  .el-menu-item {
    font-size: 10px;
    padding: 0 8px;
  }
  
  .user-info {
    gap: 10px;
  }
  
  .el-button {
    font-size: 11px;
    padding: 4px 8px;
  }
}
</style>