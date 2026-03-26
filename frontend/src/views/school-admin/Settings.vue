<template>
  <div class="admin-container">
    <h2 class="page-title">学校设置</h2>
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">学校信息</span>
        </div>
      </template>
      <el-form :model="schoolSettings" :rules="rules" ref="schoolForm">
        <el-form-item label="学校名称" prop="schoolName">
          <el-input v-model="schoolSettings.schoolName" placeholder="请输入学校名称"></el-input>
        </el-form-item>
        <el-form-item label="学校代码" prop="schoolCode">
          <el-input v-model="schoolSettings.schoolCode" placeholder="请输入学校代码"></el-input>
        </el-form-item>
        <el-form-item label="学校地址" prop="schoolAddress">
          <el-input v-model="schoolSettings.schoolAddress" placeholder="请输入学校地址"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="schoolSettings.contactPhone" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱地址" prop="contactEmail">
          <el-input v-model="schoolSettings.contactEmail" placeholder="请输入邮箱地址"></el-input>
        </el-form-item>
        <el-form-item label="学校简介" prop="schoolDescription">
          <el-input v-model="schoolSettings.schoolDescription" type="textarea" placeholder="请输入学校简介" :rows="4"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSchoolSettings">保存设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">课程设置</span>
        </div>
      </template>
      <el-form :model="courseSettings" :rules="courseRules" ref="courseForm">
        <el-form-item label="课程分类" prop="courseCategories">
          <el-input v-model="courseSettings.courseCategories" placeholder="请输入课程分类，用逗号分隔"></el-input>
        </el-form-item>
        <el-form-item label="学分设置" prop="creditSettings">
          <el-input v-model="courseSettings.creditSettings" placeholder="请输入学分设置"></el-input>
        </el-form-item>
        <el-form-item label="课程评价" prop="courseEvaluation">
          <el-switch v-model="courseSettings.courseEvaluation"></el-switch>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveCourseSettings">保存设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      schoolSettings: {
        schoolName: '清华大学',
        schoolCode: 'school001',
        schoolAddress: '北京市海淀区清华大学',
        contactPhone: '010-12345678',
        contactEmail: 'contact@tsinghua.edu.cn',
        schoolDescription: '清华大学是中国著名的高等学府，创建于1911年，是中国最顶尖的综合性大学之一。'
      },
      courseSettings: {
        courseCategories: '公共基础课,专业基础课,专业课,选修课',
        creditSettings: '一般课程1-3学分，实践课程2-4学分',
        courseEvaluation: true
      },
      rules: {
        schoolName: [{ required: true, message: '请输入学校名称', trigger: 'blur' }],
        schoolCode: [{ required: true, message: '请输入学校代码', trigger: 'blur' }],
        schoolAddress: [{ required: true, message: '请输入学校地址', trigger: 'blur' }],
        contactPhone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        contactEmail: [{ required: true, message: '请输入邮箱地址', trigger: 'blur' }],
        schoolDescription: [{ required: true, message: '请输入学校简介', trigger: 'blur' }]
      },
      courseRules: {
        courseCategories: [{ required: true, message: '请输入课程分类', trigger: 'blur' }],
        creditSettings: [{ required: true, message: '请输入学分设置', trigger: 'blur' }]
      }
    }
  },
  methods: {
    getSchoolInfo() {
      const token = localStorage.getItem('token')
      const school = localStorage.getItem('school')
      if (school) {
        try {
          const schoolInfo = JSON.parse(school)
          if (schoolInfo && schoolInfo.id) {
            this.$axios.get(`/api/school/${schoolInfo.id}`, {
              headers: {
                'Authorization': `Bearer ${token}`
              }
            }).then(response => {
              const schoolData = response.data
              this.schoolSettings = {
                schoolName: schoolData.name || '',
                schoolCode: schoolData.code || '',
                schoolAddress: schoolData.address || '',
                contactPhone: schoolData.contactPhone || '',
                contactEmail: schoolData.email || '',
                schoolDescription: schoolData.description || ''
              }
            }).catch(error => {
              console.error('获取学校信息失败:', error)
            })
          }
        } catch (error) {
          console.error('解析学校信息失败:', error)
        }
      }
    },
    saveSchoolSettings() {
      this.$refs.schoolForm.validate((valid) => {
        if (valid) {
          const token = localStorage.getItem('token')
          const school = localStorage.getItem('school')
          if (school) {
            try {
              const schoolInfo = JSON.parse(school)
              if (schoolInfo.id) {
                const schoolData = {
                  id: schoolInfo.id,
                  name: this.schoolSettings.schoolName,
                  code: this.schoolSettings.schoolCode,
                  address: this.schoolSettings.schoolAddress,
                  contactPhone: this.schoolSettings.contactPhone,
                  email: this.schoolSettings.contactEmail,
                  description: this.schoolSettings.schoolDescription
                }
                this.$axios.put(`/api/school/${schoolInfo.id}`, schoolData, {
                  headers: {
                    'Authorization': `Bearer ${token}`
                  }
                }).then(response => {
                  this.$message.success('学校设置保存成功')
                }).catch(error => {
                  console.error('保存学校设置失败:', error)
                  this.$message.error('保存学校设置失败')
                })
              }
            } catch (error) {
              console.error('解析学校信息失败:', error)
              this.$message.error('保存学校设置失败')
            }
          }
        }
      })
    },
    saveCourseSettings() {
      this.$refs.courseForm.validate((valid) => {
        if (valid) {
          const token = localStorage.getItem('token')
          const school = localStorage.getItem('school')
          if (school) {
            try {
              const schoolInfo = JSON.parse(school)
              if (schoolInfo.id) {
                // 这里可以添加课程设置的后端 API 调用
                this.$message.success('课程设置保存成功')
              }
            } catch (error) {
              console.error('解析学校信息失败:', error)
              this.$message.error('保存课程设置失败')
            }
          }
        }
      })
    }
  },
  mounted() {
    this.getSchoolInfo()
  }
}
</script>

<style scoped>
.admin-container {
  padding: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 20px;
}

.admin-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.mt-20 {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid #e4e7ed;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}
</style>