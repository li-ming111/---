<template>
  <div class="profile-container">
    <!-- 个人资料卡片 -->
    <el-card class="profile-card mb-6">
      <template #header>
        <div class="card-header">
          <span class="card-title">个人资料</span>
          <el-button type="primary" @click="updateProfile" class="save-btn">
            <el-icon><Check /></el-icon>
            保存修改
          </el-button>
        </div>
      </template>

      <div class="avatar-container">
        <el-upload
          class="avatar-uploader"
          action=""
          :show-file-list="false"
          :on-change="handleAvatarChange"
          :auto-upload="false"
          accept="image/*"
        >
          <img v-if="userProfile.avatar" :src="userProfile.avatar" class="avatar" />
          <el-icon v-else class="avatar-placeholder"><User /></el-icon>
        </el-upload>
        <div class="avatar-tip">点击上传头像</div>
      </div>
      <el-form :model="userProfile" label-width="120px" class="profile-form">
        <el-form-item label="用户名">
          <el-input v-model="userProfile.username" disabled class="input-field" />
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="userProfile.name" class="input-field" prefix-icon="User" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="userProfile.gender" class="gender-group">
            <el-radio value="男" class="gender-option">男</el-radio>
            <el-radio value="女" class="gender-option">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄">
          <el-input-number v-model="userProfile.age" :min="0" :max="100" class="age-input" />
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="schoolInfo.name" disabled class="input-field" />
        </el-form-item>
        <el-form-item label="专业">
          <el-select v-model="userProfile.majorId" placeholder="选择专业" class="select-field">
            <el-option v-for="major in majors" :key="major.id" :label="major.name" :value="major.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="年级">
          <el-input v-model="userProfile.grade" class="input-field" prefix-icon="School" />
        </el-form-item>
        <el-form-item label="兴趣爱好">
          <el-input type="textarea" v-model="userProfile.hobbies" class="textarea-field" :rows="3" />
        </el-form-item>
        <el-form-item label="职业目标">
          <el-input type="textarea" v-model="userProfile.careerGoal" class="textarea-field" :rows="3" />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 隐私设置卡片 -->
    <el-card class="privacy-card mb-6">
      <template #header>
        <div class="card-header">
          <span class="card-title">隐私设置</span>
          <el-button type="primary" @click="updatePrivacy" class="save-btn">
            <el-icon><Check /></el-icon>
            保存设置
          </el-button>
        </div>
      </template>
      <el-form :model="privacySettings" label-width="120px" class="privacy-form">
        <el-form-item label="个人信息可见性">
          <el-select v-model="privacySettings.profileVisibility" placeholder="选择可见性" class="select-field">
            <el-option label="公开" value="public" />
            <el-option label="仅好友可见" value="friends" />
            <el-option label="仅自己可见" value="private" />
          </el-select>
        </el-form-item>
        <el-form-item label="学习数据可见性">
          <el-select v-model="privacySettings.dataVisibility" placeholder="选择可见性" class="select-field">
            <el-option label="公开" value="public" />
            <el-option label="仅好友可见" value="friends" />
            <el-option label="仅自己可见" value="private" />
          </el-select>
        </el-form-item>
        <el-form-item label="接收消息通知">
          <el-switch v-model="privacySettings.receiveNotifications" />
        </el-form-item>
        <el-form-item label="接收邮件通知">
          <el-switch v-model="privacySettings.receiveEmails" />
        </el-form-item>
        <el-form-item label="允许他人查看我的笔记">
          <el-switch v-model="privacySettings.allowViewNotes" />
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 学习统计卡片 -->
    <el-card class="stats-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">学习统计</span>
        </div>
      </template>
      <div class="stats-container">
        <div class="stat-item" v-for="(value, key) in statsMap" :key="key">
          <div class="stat-icon" :class="key">
            <el-icon v-if="key === 'coursesCompleted'"><Check /></el-icon>
            <el-icon v-else-if="key === 'plansCreated'"><Document /></el-icon>
            <el-icon v-else-if="key === 'resourcesAccessed'"><Reading /></el-icon>
            <el-icon v-else-if="key === 'goalsSet'"><Flag /></el-icon>
          </div>
          <div class="stat-value">{{ value }}</div>
          <div class="stat-label">{{ statsLabels[key] }}</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { Check, User, School, Document, Reading, Flag } from '@element-plus/icons-vue'

export default {
  components: {
    Check,
    User,
    School,
    Document,
    Reading,
    Flag
  },
  data() {
    return {
      userProfile: {
        username: '',
        name: '',
        gender: '',
        age: 0,
        majorId: '',
        grade: '',
        hobbies: '',
        careerGoal: '',
        avatar: ''
      },
      avatarFile: null,
      schoolInfo: {
        name: ''
      },

      privacySettings: {
        profileVisibility: 'public',
        dataVisibility: 'public',
        receiveNotifications: true,
        receiveEmails: true,
        allowViewNotes: false
      },
      majors: [],
      stats: {
        coursesCompleted: 0,
        plansCreated: 0,
        resourcesAccessed: 0,
        goalsSet: 0
      },
      statsMap: {
        coursesCompleted: 0,
        plansCreated: 0,
        resourcesAccessed: 0,
        goalsSet: 0
      },
      statsLabels: {
        coursesCompleted: '已完成课程',
        plansCreated: '创建计划',
        resourcesAccessed: '访问资源',
        goalsSet: '设定目标'
      }
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      const school = localStorage.getItem('school')
      if (school) {
        try {
          const schoolInfo = JSON.parse(school)
          this.schoolInfo = schoolInfo
        } catch (error) {
          console.error('解析学校信息失败:', error)
        }
      }
      if (token) {
        this.getUserProfile()
        this.getMajors()
        this.getStats()
        this.getPrivacySettings()
      }
    },
    getPrivacySettings() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      if (user && user.id) {
        this.$axios.get(`/api/privacy/user/${user.id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          // 确保response.data是对象
          if (typeof response.data === 'object' && response.data !== null) {
            // 转换后端数据格式为前端期望的格式
            const backendData = response.data
            this.privacySettings = {
              profileVisibility: backendData.profileVisible || 'public',
              dataVisibility: backendData.learningVisible || 'public',
              receiveNotifications: backendData.loginAlert !== undefined ? backendData.loginAlert : true,
              receiveEmails: true, // 后端没有对应字段，使用默认值
              allowViewNotes: false // 后端没有对应字段，使用默认值
            }
          } else {
            // 如果不是对象，使用默认值
            console.warn('隐私设置数据格式错误，使用默认值')
          }
        }).catch(error => {
          console.error('获取隐私设置失败:', error)
        })
      }
    },
    getUserProfile() {
      const token = localStorage.getItem('token')
      this.$axios.get('/api/users/profile', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        // 确保response.data是对象
        if (typeof response.data === 'object' && response.data !== null) {
          this.userProfile = response.data
        } else {
          // 如果不是对象，使用默认值
          console.warn('个人资料数据格式错误，使用默认值')
        }
      }).catch(error => {
        console.error('获取个人资料失败:', error)
      })
    },
    getMajors() {
      const token = localStorage.getItem('token')
      this.$axios.get('/api/majors/list', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        // 确保response.data是数组
        if (Array.isArray(response.data)) {
          this.majors = response.data
        } else {
          // 如果不是数组，使用空数组
          console.warn('专业列表数据格式错误，使用空数组')
          this.majors = []
        }
      }).catch(error => {
        console.error('获取专业列表失败:', error)
        this.majors = []
      })
    },
    getStats() {
      const token = localStorage.getItem('token')
      this.$axios.get('/api/users/stats', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        // 确保response.data是对象
        if (typeof response.data === 'object' && response.data !== null) {
          this.stats = response.data
          this.statsMap = response.data
        } else {
          // 如果不是对象，使用默认值
          console.warn('统计数据格式错误，使用默认值')
          this.stats = {
            coursesCompleted: 0,
            plansCreated: 0,
            resourcesAccessed: 0,
            goalsSet: 0
          }
          this.statsMap = this.stats
        }
      }).catch(error => {
        console.error('获取统计数据失败:', error)
        // 出错时使用默认值
        this.stats = {
          coursesCompleted: 0,
          plansCreated: 0,
          resourcesAccessed: 0,
          goalsSet: 0
        }
        this.statsMap = this.stats
      })
    },

    handleAvatarChange(file) {
      this.avatarFile = file.raw
    },
    updateProfile() {
      const token = localStorage.getItem('token')
      
      // 如果有头像文件，先上传头像
      if (this.avatarFile) {
        this.uploadAvatar(token)
      } else {
        // 没有头像上传，直接更新用户信息
        this.updateUserProfile(token)
      }
    },
    uploadAvatar(token) {
      const formData = new FormData()
      formData.append('avatar', this.avatarFile)
      
      this.$axios.post('/api/users/avatar', formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        if (response.data.success) {
          this.$message.success('头像上传成功')
          this.userProfile.avatar = response.data.avatarUrl
          // 上传成功后更新用户信息
          this.updateUserProfile(token)
        } else {
          this.$message.error('头像上传失败')
        }
      }).catch(error => {
        console.error('上传头像失败:', error)
        this.$message.error('上传头像失败')
        // 头像上传失败后仍然更新用户信息
        this.updateUserProfile(token)
      })
    },
    updateUserProfile(token) {
      this.$axios.put('/api/users/profile', this.userProfile, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('个人资料更新成功')
        // 更新localStorage中的用户信息
        const user = JSON.parse(localStorage.getItem('user'))
        if (user) {
          user.name = this.userProfile.name
          localStorage.setItem('user', JSON.stringify(user))
          // 强制更新App.vue中的用户信息
          if (this.$root && this.$root.checkLoginStatus) {
            this.$root.checkLoginStatus()
          } else {
            // 如果无法直接调用，刷新页面
            window.location.reload()
          }
        }
      }).catch(error => {
        console.error('更新个人资料失败:', error)
        this.$message.error('更新个人资料失败')
      })
    },
    updatePrivacy() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      if (user && user.id) {
        // 转换前端数据格式为后端期望的格式
        const setting = {
          userId: user.id,
          profileVisible: this.privacySettings.profileVisibility,
          learningVisible: this.privacySettings.dataVisibility,
          loginAlert: this.privacySettings.receiveNotifications,
          exportEnabled: true, // 后端需要的字段，使用默认值
          deviceList: '', // 后端需要的字段，使用默认值
          notificationSettings: '' // 后端需要的字段，使用默认值
        }
        this.$axios.post('/api/privacy/update', setting, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          this.$message.success('隐私设置更新成功')
        }).catch(error => {
          console.error('更新隐私设置失败:', error)
        })
      }
    }
  }
}
</script>

<style scoped>
.profile-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.profile-card,
.privacy-card,
.stats-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0;
  border-radius: 0;
  box-shadow: none;
}

.profile-card:hover,
.privacy-card:hover,
.stats-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
}

.privacy-form {
  padding: 30px;
}

.privacy-form .el-form-item {
  margin-bottom: 20px;
}

@media (max-width: 768px) {
  .privacy-form {
    padding: 20px;
  }
  
  .privacy-form .el-form-item {
    margin-bottom: 15px;
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 30px;
  border-bottom: 1px solid #e4e7ed;
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.save-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.save-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.profile-form {
  padding: 30px;
}

.input-field,
.select-field,
.textarea-field {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
}

.input-field:focus,
.select-field:focus,
.textarea-field:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.gender-group {
  display: flex;
  gap: 20px;
}

.gender-option {
  padding: 8px 20px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.gender-option:hover {
  background-color: #f0f0f0;
}

.age-input {
  width: 200px;
  border-radius: 8px;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding: 30px;
}

.stat-item {
  text-align: center;
  padding: 30px 20px;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 16px;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
}

.stat-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  margin: 0 auto 20px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  transition: all 0.3s ease;
}

.stat-icon.coursesCompleted {
  background: linear-gradient(45deg, #4CAF50, #45a049);
}

.stat-icon.plansCreated {
  background: linear-gradient(45deg, #2196F3, #0b7dda);
}

.stat-icon.resourcesAccessed {
  background: linear-gradient(45deg, #FF9800, #f57c00);
}

.stat-icon.goalsSet {
  background: linear-gradient(45deg, #9C27B0, #7b1fa2);
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
  animation: countUp 1s ease-out;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

@keyframes countUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 1200px) {
  .stats-container {
    grid-template-columns: repeat(2, 1fr);
  }
}

.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
  width: 120px;
  height: 120px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  transition: all 0.3s ease;
}

.avatar-uploader:hover {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.avatar {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  font-size: 48px;
  color: #999;
}

.avatar-tip {
  margin-top: 10px;
  font-size: 14px;
  color: #666;
}

@media (max-width: 768px) {
  .profile-container {
    padding: 20px;
  }
  
  .profile-form {
    padding: 20px;
  }
  
  .stats-container {
    grid-template-columns: 1fr;
    padding: 20px;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .save-btn {
    align-self: flex-end;
  }
  
  .avatar-uploader {
    width: 100px;
    height: 100px;
  }
  
  .avatar-placeholder {
    font-size: 40px;
  }
}
</style>