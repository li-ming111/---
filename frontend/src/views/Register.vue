<template>
  <div class="register-container">
    <el-card class="register-card">
      <template #header>
        <div class="register-title">智能注册</div>
      </template>
      <el-form :model="registerForm" :rules="rules" ref="registerForm">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="registerForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="registerForm.idCard" placeholder="请输入18位身份证号"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码（至少6位）" @input="checkPasswordStrength"></el-input>
          <div class="password-strength" v-if="registerForm.password">
            <div class="strength-label">密码强度：{{ passwordStrengthText }}</div>
            <div class="strength-bars">
              <div class="strength-bar" :class="{ 'weak': passwordStrength >= 1, 'medium': passwordStrength >= 2, 'strong': passwordStrength >= 3 }"></div>
              <div class="strength-bar" :class="{ 'medium': passwordStrength >= 2, 'strong': passwordStrength >= 3 }"></div>
              <div class="strength-bar" :class="{ 'strong': passwordStrength >= 3 }"></div>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input v-model="registerForm.confirmPassword" type="password" placeholder="请再次输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="agreement">
          <el-checkbox v-model="registerForm.agreement">
            我已阅读并同意 <a href="#" @click.prevent>《用户协议》</a> 和 <a href="#" @click.prevent>《隐私政策》</a>
          </el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" style="width: 100%" :loading="isLoading">
            <span v-if="!isLoading">注册</span>
            <span v-else>
              <i class="el-icon-loading"></i>
              <span style="margin-left: 8px">注册中...</span>
            </span>
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button @click="navigateTo('/login')" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      registerForm: {
        name: '',
        idCard: '',
        phone: '',
        email: '',
        password: '',
        confirmPassword: '',
        agreement: false
      },
      isLoading: false,
      passwordStrength: 0,
      passwordStrengthText: '弱',
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'input' }],
        idCard: [
          { required: true, message: '请输入身份证号', trigger: 'input' },
          { pattern: /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/, message: '身份证号格式不正确', trigger: 'input' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'input' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'input' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'input' },
          { min: 6, message: '密码至少6位', trigger: 'input' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'input' },
          {
            validator: (rule, value, callback) => {
              if (value !== this.registerForm.password) {
                callback(new Error('两次输入的密码不一致'))
              } else {
                callback()
              }
            },
            trigger: 'input'
          }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'input' },
          { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '邮箱格式不正确', trigger: 'input' }
        ],
        agreement: [
          { required: true, message: '请阅读并同意用户协议和隐私政策', trigger: 'change' }
        ]
      }
    }
  },
  methods: {
    register() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          this.isLoading = true
          try {
            // 只发送必要的字段，不包含confirmPassword
            const registerData = {
              name: this.registerForm.name,
              idCard: this.registerForm.idCard,
              phone: this.registerForm.phone,
              email: this.registerForm.email,
              password: this.registerForm.password
            }
            const response = await axios.post('/api/auth/register', registerData)
            if (response.data.success) {
              if (response.data.needSchoolVerification) {
                this.$message.success('注册成功，请认证学校信息')
                // 跳转到学校认证页面
                setTimeout(() => {
                  this.$router.push('/school-verify')
                }, 1000)
              } else {
                this.$message.success('注册成功，请登录')
                setTimeout(() => {
                  this.$router.push('/login')
                }, 1000)
              }
            } else {
              this.$message.error(response.data.message)
            }
          } catch (error) {
            console.error('注册失败:', error)
            this.$message.error('注册失败，请重试')
          } finally {
            this.isLoading = false
          }
        }
      })
    },
    navigateTo(path) {
      this.$router.push(path)
    },
    checkPasswordStrength() {
      const password = this.registerForm.password
      let strength = 0
      
      // 检查长度
      if (password.length >= 6) strength++
      if (password.length >= 10) strength++
      
      // 检查包含的字符类型
      if (/[A-Z]/.test(password)) strength++
      if (/[a-z]/.test(password)) strength++
      if (/\d/.test(password)) strength++
      if (/[^A-Za-z0-9]/.test(password)) strength++
      
      // 限制强度等级为0-3
      this.passwordStrength = Math.min(strength, 3)
      
      // 设置强度文本
      switch (this.passwordStrength) {
        case 0:
          this.passwordStrengthText = '弱'
          break
        case 1:
          this.passwordStrengthText = '弱'
          break
        case 2:
          this.passwordStrengthText = '中等'
          break
        case 3:
          this.passwordStrengthText = '强'
          break
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
  padding: 20px;
}

.register-card {
  width: 100%;
  max-width: 450px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.register-card:hover {
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
}

.register-title {
  font-size: 24px;
  font-weight: bold;
  text-align: center;
  color: #303133;
}

.el-form-item {
  margin-bottom: 20px;
}

.el-form-item__label {
  font-weight: 500;
}

.el-input {
  height: 44px;
  border-radius: 8px;
  font-size: 16px;
}

.el-button {
  height: 44px;
  border-radius: 8px;
  font-size: 16px;
  font-weight: 500;
}

.password-strength {
  margin-top: 8px;
}

.strength-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 4px;
}

.strength-bars {
  display: flex;
  gap: 4px;
  height: 8px;
}

.strength-bar {
  flex: 1;
  background-color: #e4e7ed;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.strength-bar.weak {
  background-color: #f56c6c;
}

.strength-bar.medium {
  background-color: #e6a23c;
}

.strength-bar.strong {
  background-color: #67c23a;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .register-card {
    padding: 20px;
  }
  
  .register-title {
    font-size: 20px;
  }
  
  .el-form-item {
    margin-bottom: 15px;
  }
  
  .el-input {
    height: 40px;
  }
  
  .el-button {
    height: 40px;
  }
}
</style>