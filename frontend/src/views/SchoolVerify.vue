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
        <el-form-item label="学校" prop="schoolName">
          <el-input
            v-model="verifyForm.schoolName"
            placeholder="请输入学校名称"
            @input="handleSchoolInput"
            clearable
          >
            <template #prefix>
              <i class="el-icon-search"></i>
            </template>
          </el-input>
          <div v-if="schoolSuggestions.length > 0" class="school-suggestions">
            <div
              v-for="school in schoolSuggestions"
              :key="school.id"
              class="school-suggestion-item"
              @click="selectSchool(school)"
            >
              {{ school.name }}
            </div>
          </div>
        </el-form-item>
        <el-form-item label="专业" prop="major">
          <el-input v-model="verifyForm.major" placeholder="请输入专业"></el-input>
        </el-form-item>
        <el-form-item label="兴趣爱好" prop="hobbies">
          <el-input
            v-model="verifyForm.hobbies"
            placeholder="请输入兴趣爱好，多个用逗号分隔"
            type="textarea"
            :rows="3"
          ></el-input>
        </el-form-item>
        <el-form-item label="年级" prop="grade">
          <el-input v-model="verifyForm.grade" placeholder="请输入年级"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="verify" style="width: 100%" :loading="isLoading">认证</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
// 使用全局axios实例，不单独导入

export default {
  data() {
    return {
      verifyForm: {
        studentId: '',
        schoolName: '',
        major: '',
        hobbies: '',
        grade: ''
      },
      schools: [],
      schoolSuggestions: [],
      isLoading: false,
      rules: {
        studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
        schoolName: [{ required: true, message: '请输入学校名称', trigger: 'blur' }],
        major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
        hobbies: [{ required: true, message: '请输入兴趣爱好', trigger: 'blur' }],
        grade: [{ required: true, message: '请输入年级', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.getSchools()
  },
  methods: {
    getSchools() {
      this.$axios.get('/users/schools')
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
    handleSchoolInput() {
      const input = this.verifyForm.schoolName
      if (input.length >= 2) {
        // 根据输入过滤学校列表
        this.schoolSuggestions = this.schools.filter(school => 
          school.name.toLowerCase().includes(input.toLowerCase())
        )
      } else {
        this.schoolSuggestions = []
      }
    },
    selectSchool(school) {
      this.verifyForm.schoolName = school.name
      this.schoolSuggestions = []
    },
    verify() {
      this.$refs.verifyForm.validate(async (valid) => {
        if (valid) {
          this.isLoading = true
          try {
            const response = await this.$axios.post('/users/verify-school', this.verifyForm, {
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

.school-suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 1px solid #dcdfe6;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  z-index: 1000;
  max-height: 200px;
  overflow-y: auto;
  margin-top: -1px;
}

.school-suggestion-item {
  padding: 10px 15px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.school-suggestion-item:hover {
  background-color: #f5f7fa;
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