<template>
  <div class="school-verify-container">
    <el-card class="verify-card">
      <template #header>
        <div class="verify-title">学校信息认证</div>
      </template>
      <el-form :model="verifyForm" :rules="rules" ref="verifyForm">
        <el-form-item label="学号" prop="studentId">
          <el-input v-model="verifyForm.studentId" placeholder="请输入学号"></el-input>
        </el-form-item>
        <el-form-item label="学校" prop="schoolId">
          <el-select v-model="verifyForm.schoolId" placeholder="请选择学校">
            <el-option
              v-for="school in schools"
              :key="school.id"
              :label="school.name"
              :value="school.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="verify" style="width: 100%" :loading="isLoading">认证</el-button>
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
      verifyForm: {
        studentId: '',
        schoolId: ''
      },
      schools: [],
      isLoading: false,
      rules: {
        studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        schoolId: [{ required: true, message: '请选择学校', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.getSchools()
  },
  methods: {
    getSchools() {
      axios.get('/api/users/schools')
        .then(response => {
          if (response.data.success) {
            this.schools = response.data.schools
          }
        })
        .catch(error => {
          console.error('获取学校列表失败:', error)
          this.$message.error('获取学校列表失败，请重试')
        })
    },
    verify() {
      this.$refs.verifyForm.validate(async (valid) => {
        if (valid) {
          this.isLoading = true
          try {
            const response = await axios.post('/api/users/verify-school', this.verifyForm, {
              headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
              }
            })
            if (response.data.success) {
              this.$message.success('学校认证成功')
              setTimeout(() => {
                this.$router.push('/')
              }, 1000)
            } else {
              this.$message.error(response.data.message)
            }
          } catch (error) {
            console.error('学校认证失败:', error)
            this.$message.error('学校认证失败，请重试')
          } finally {
            this.isLoading = false
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.school-verify-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f0f2f5;
  padding: 20px;
}

.verify-card {
  width: 100%;
  max-width: 450px;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.verify-card:hover {
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
}

.verify-title {
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

.el-input,
.el-select {
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
  .verify-card {
    padding: 20px;
  }
  
  .verify-title {
    font-size: 20px;
  }
  
  .el-form-item {
    margin-bottom: 15px;
  }
  
  .el-input,
  .el-select {
    height: 40px;
  }
  
  .el-button {
    height: 40px;
  }
}
</style>