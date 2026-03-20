<template>
  <div class="login-container">
    <div class="login-left">
      <div class="login-banner">
        <h1 class="banner-title">学涯助手</h1>
        <p class="banner-subtitle">大学生成长规划系统</p>
        <div class="banner-features">
          <div class="feature-item">
            <i class="el-icon-s-grid"></i>
            <span>智能学习规划</span>
          </div>
          <div class="feature-item">
            <i class="el-icon-user"></i>
            <span>个性化成长路径</span>
          </div>
          <div class="feature-item">
            <i class="el-icon-data-line"></i>
            <span>数据驱动决策</span>
          </div>
        </div>
      </div>
    </div>
    <div class="login-right">
      <div class="login-card">
        <div class="login-header">
          <img :src="schoolLogo" alt="Logo" class="login-logo">
          <h2 class="login-title">{{ schoolName }}</h2>
          <p class="login-subtitle">欢迎回来，开始您的学习之旅</p>
        </div>
        
        <el-form :model="loginForm" :rules="rules" ref="loginForm" class="login-form">
          <el-form-item prop="schoolCode" class="form-item">
            <el-select v-model="loginForm.schoolCode" placeholder="请选择学校" class="custom-select">
              <el-option v-for="school in schools" :key="school.id" :label="school.name" :value="school.code"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="username" class="form-item">
            <el-input v-model="loginForm.username" placeholder="请输入学号" class="custom-input">
              <template #prefix>
                <i class="el-icon-user"></i>
              </template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password" class="form-item">
            <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password class="custom-input">
              <template #prefix>
                <i class="el-icon-lock"></i>
              </template>
            </el-input>
          </el-form-item>
          <!-- 验证码功能暂时注释掉 -->
          <!-- <el-form-item prop="captcha" class="form-item">
            <el-input v-model="loginForm.captcha" placeholder="请输入验证码" class="captcha-input">
              <template #prefix>
                <i class="el-icon-view"></i>
              </template>
            </el-input>
            <div class="captcha-image" @click="refreshCaptcha">
              {{ captchaCode }}
            </div>
          </el-form-item> -->
          <el-form-item class="form-item remember-item">
            <el-checkbox v-model="rememberMe" class="custom-checkbox">记住账号</el-checkbox>
            <el-button link @click="navigateTo('/register')" class="register-link">立即注册</el-button>
          </el-form-item>
          <el-form-item class="form-item">
            <el-button type="primary" @click="login" class="login-button">
              <span v-if="!isLoading">登录</span>
              <el-icon v-else class="is-loading"><i class="el-icon-loading"></i></el-icon>
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="login-footer">
          <el-button link class="footer-link">忘记密码？</el-button>
          <el-button link class="footer-link">帮助中心</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import logo from '@/assets/logo.jpg'

export default {
  data() {
    return {
      loginForm: {
        schoolCode: '',
        username: '',
        password: ''
        // captcha: '' // 验证码功能暂时注释掉
      },
      rememberMe: false,
      schools: [],
      schoolLogo: logo,
      schoolName: '学涯助手',
      isLoading: false,
      // captchaCode: '', // 验证码功能暂时注释掉
      rules: {
        schoolCode: [{ required: true, message: '请选择学校', trigger: 'blur' }],
        username: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
        // captcha: [{ required: true, message: '请输入验证码', trigger: 'blur' }] // 验证码功能暂时注释掉
      }
    }
  },
  mounted() {
    this.getSchools()
    // this.refreshCaptcha() // 验证码功能暂时注释掉
  },
  methods: {
    async getSchools() {
      try {
        const response = await axios.get('/auth/schools')
        if (response.data.schools) {
          this.schools = response.data.schools
        }
      } catch (error) {
        console.error('获取学校列表失败:', error)
        // 只使用真实 API 数据，不使用本地备用数据
      }
    },
    // refreshCaptcha() {
    //   // 生成随机验证码
    //   const chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'
    //   let code = ''
    //   for (let i = 0; i < 4; i++) {
    //     code += chars.charAt(Math.floor(Math.random() * chars.length))
    //   }
    //   this.captchaCode = code
    // }, // 验证码功能暂时注释掉
    login() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          this.isLoading = true
          try {
            console.log('登录请求数据:', {
                schoolCode: this.loginForm.schoolCode,
                username: this.loginForm.username,
                password: this.loginForm.password
                // captcha: this.loginForm.captcha // 验证码功能暂时注释掉
              })
              // 前端验证验证码暂时注释掉
              /* if (this.loginForm.captcha.toUpperCase() !== this.captchaCode.toUpperCase()) {
                this.$message.error('验证码错误');
                this.refreshCaptcha();
                this.isLoading = false;
                return;
              } */
              console.log('登录请求URL:', '/auth/login');
              console.log('登录请求数据类型:', typeof { schoolCode: this.loginForm.schoolCode, username: this.loginForm.username, password: this.loginForm.password });
              console.log('登录请求数据JSON:', JSON.stringify({ schoolCode: this.loginForm.schoolCode, username: this.loginForm.username, password: this.loginForm.password }));
              const response = await axios.post('/auth/login', {
                schoolCode: this.loginForm.schoolCode,
                username: this.loginForm.username,
                password: this.loginForm.password
              })
            console.log('登录响应数据:', response.data)
            if (response.data.success) {
              const token = response.data.token
              localStorage.setItem('token', token)
              localStorage.setItem('user', JSON.stringify(response.data.user))
              localStorage.setItem('school', JSON.stringify(response.data.school))
              if (this.rememberMe) {
                localStorage.setItem('username', this.loginForm.username)
                localStorage.setItem('schoolCode', this.loginForm.schoolCode)
              }
              axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
              this.$message.success('登录成功')
              // 根据用户角色跳转不同页面
              const userRole = response.data.user.role
              setTimeout(() => {
                if (userRole === '0' || userRole === 'super_admin') {
                  this.$router.push('/admin')
                } else if (userRole === 'school_admin' || userRole === 'school') {
                  this.$router.push('/school-admin')
                } else {
                  this.$router.push('/')
                }
              }, 1000)
            } else {
              this.$message.error(response.data.message || '登录失败，请检查用户名和密码')
            }
          } catch (error) {
            console.error('登录错误:', error)
            this.$message.error('登录失败，请检查网络连接或服务器状态')
          } finally {
            this.isLoading = false
          }
        }
      })
    },
    navigateTo(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  min-height: 100vh;
  background-color: #f0f2f5;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #409EFF 0%, #667EEA 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: white;
  animation: fadeInLeft 1s ease-in-out;
}

.login-banner {
  max-width: 400px;
}

.banner-title {
  font-size: 48px;
  font-weight: bold;
  margin-bottom: 20px;
  line-height: 1.2;
}

.banner-subtitle {
  font-size: 20px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.banner-features {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 16px;
  opacity: 0.9;
  transition: all 0.3s ease;
}

.feature-item:hover {
  transform: translateX(10px);
  opacity: 1;
}

.feature-item i {
  font-size: 24px;
  background: rgba(255, 255, 255, 0.2);
  padding: 10px;
  border-radius: 50%;
}

.login-right {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  animation: fadeInRight 1s ease-in-out;
}

.login-card {
  width: 100%;
  max-width: 420px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 40px;
  transition: all 0.3s ease;
}

.login-card:hover {
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-logo {
  width: 80px;
  height: 80px;
  margin: 0 auto 20px;
  border-radius: 50%;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.login-logo:hover {
  transform: scale(1.05);
}

.login-title {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 8px;
}

.login-subtitle {
  font-size: 16px;
  color: #606266;
  margin: 0;
}

.login-form {
  width: 100%;
}

.form-item {
  margin-bottom: 24px;
}

.custom-input,
.custom-select {
  width: 100%;
  height: 48px;
  border-radius: 8px;
  font-size: 16px;
  transition: all 0.3s ease;
}

.custom-input:focus,
.custom-select:focus {
  border-color: #409EFF;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
}

.captcha-input {
  width: 60%;
  height: 48px;
  border-radius: 8px 0 0 8px;
  font-size: 16px;
}

.captcha-image {
  width: 38%;
  height: 48px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 0 8px 8px 0;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  font-weight: bold;
  color: #409EFF;
  user-select: none;
  cursor: pointer;
  transition: all 0.3s ease;
  letter-spacing: 2px;
}

.captcha-image:hover {
  background: linear-gradient(135deg, #e0e7ff 0%, #c7d2fe 100%);
  transform: scale(1.02);
}

.remember-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.custom-checkbox .el-checkbox__label {
  font-size: 14px;
  color: #606266;
}

.register-link {
  font-size: 14px;
  color: #409EFF;
  transition: all 0.3s ease;
}

.register-link:hover {
  color: #66b1ff;
}

.login-button {
  width: 100%;
  height: 48px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #409EFF 0%, #667EEA 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(64, 158, 255, 0.4);
}

.login-button:active {
  transform: translateY(0);
}

.login-footer {
  display: flex;
  justify-content: center;
  gap: 30px;
  margin-top: 30px;
}

.footer-link {
  font-size: 14px;
  color: #909399;
  transition: all 0.3s ease;
}

.footer-link:hover {
  color: #409EFF;
}

@keyframes fadeInLeft {
  from {
    opacity: 0;
    transform: translateX(-50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

@keyframes fadeInRight {
  from {
    opacity: 0;
    transform: translateX(50px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
  }
  
  .login-left {
    flex: none;
    width: 100%;
    padding: 40px 20px;
  }
  
  .login-right {
    flex: none;
    width: 100%;
    padding: 40px 20px;
  }
  
  .banner-title {
    font-size: 36px;
  }
  
  .login-card {
    padding: 30px 20px;
  }
  
  .login-title {
    font-size: 24px;
  }
  
  .form-item {
    margin-bottom: 20px;
  }
  
  .custom-input,
  .custom-select {
    height: 44px;
  }
  
  .captcha-input {
    height: 44px;
  }
  
  .captcha-image {
    height: 44px;
  }
  
  .login-button {
    height: 44px;
  }
}
</style>