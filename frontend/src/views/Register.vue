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
        <el-form-item label="用户名" prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码（至少6位）"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="registerForm.studentId" placeholder="请输入10位学号" @blur="handleStudentIdBlur"></el-input>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="registerForm.major" placeholder="请输入专业" readonly></el-input>
        </el-form-item>
        <el-form-item label="学院" prop="college">
          <el-input v-model="registerForm.college" placeholder="请输入学院" readonly></el-input>
        </el-form-item>
        <el-form-item label="学校" prop="university">
          <el-input v-model="registerForm.university" placeholder="请输入学校"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="register" style="width: 100%" :loading="isLoading">注册</el-button>
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
        username: '',
        password: '',
        email: '',
        phone: '',
        studentId: '',
        major: '',
        college: '',
        university: '',
        majorId: null,
        collegeId: null
      },
      isLoading: false,
      rules: {
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, message: '密码至少6位', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
        ],
        studentId: [
          { required: true, message: '请输入学号', trigger: 'blur' },
          { pattern: /^\d{10}(Z)?$/, message: '学号格式不正确，应为10位数字或10位数字+Z', trigger: 'blur' }
        ],
        major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
        college: [{ required: true, message: '请输入学院', trigger: 'blur' }],
        university: [{ required: true, message: '请输入学校', trigger: 'blur' }]
      }
    }
  },
  methods: {
    async handleStudentIdBlur() {
      const studentId = this.registerForm.studentId
      // 支持本科（10位数字）和专升本（10位数字+Z）
      const studentIdRegex = /^\d{10}(Z)?$/
      if (studentId && studentIdRegex.test(studentId)) {
        try {
          const response = await axios.post('/auth/get-major-by-student-id', { studentId })
          if (response.data.success) {
            this.registerForm.major = response.data.major.name
            this.registerForm.college = response.data.college.name
            this.registerForm.majorId = response.data.major.id
            this.registerForm.collegeId = response.data.college.id
          } else {
            this.$message.warning(response.data.message)
          }
        } catch (error) {
          console.error('查询专业信息失败:', error)
          this.$message.error('查询专业信息失败，请检查学号是否正确')
        }
      }
    },
    register() {
      this.$refs.registerForm.validate(async (valid) => {
        if (valid) {
          this.isLoading = true
          try {
            const response = await axios.post('/auth/register/student', this.registerForm)
            if (response.data.success) {
              this.$message.success('注册成功，请登录')
              setTimeout(() => {
                this.$router.push('/login')
              }, 1000)
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