<template>
  <div class="admin-container">
    <h2 class="page-title">系统公告管理</h2>
    
    <!-- 发布公告 -->
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">发布公告</span>
        </div>
      </template>
      <el-form :model="announcementForm" :rules="rules" ref="announcementForm">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="announcementForm.title" placeholder="请输入公告标题"></el-input>
        </el-form-item>
        <el-form-item label="公告内容" prop="content">
          <el-input v-model="announcementForm.content" type="textarea" placeholder="请输入公告内容" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="announcementForm.priority" placeholder="请选择优先级">
            <el-option label="低" value="low"></el-option>
            <el-option label="中" value="medium"></el-option>
            <el-option label="高" value="high"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="有效期" prop="expiryDate">
          <el-date-picker v-model="announcementForm.expiryDate" type="datetime" placeholder="选择有效期" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss"></el-date-picker>
        </el-form-item>
        <el-form-item label="发布范围" prop="scope">
          <el-radio-group v-model="announcementForm.scope">
            <el-radio value="all">所有用户</el-radio>
            <el-radio value="role">按角色</el-radio>
            <el-radio value="school">按学校</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="announcementForm.scope === 'role'" label="选择角色">
          <el-select v-model="announcementForm.targetRole" placeholder="请选择角色">
            <el-option label="超级管理员" value="super_admin"></el-option>
            <el-option label="学校管理员" value="school_admin"></el-option>
            <el-option label="教师" value="teacher"></el-option>
            <el-option label="学生" value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="announcementForm.scope === 'school'" label="选择学校">
          <el-select v-model="announcementForm.targetSchool" placeholder="请选择学校">
            <el-option v-for="school in schools" :key="school.code" :label="school.name" :value="school.code"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="publishAnnouncement">发布公告</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 公告列表 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">公告列表</span>
          <div class="card-actions">
            <el-select v-model="statusFilter" placeholder="状态" class="select-input">
              <el-option label="全部" value=""></el-option>
              <el-option label="生效中" value="active"></el-option>
              <el-option label="已过期" value="expired"></el-option>
            </el-select>
            <el-input v-model="searchQuery" placeholder="搜索公告" class="search-input" prefix-icon="el-icon-search"></el-input>
          </div>
        </div>
      </template>
      <el-table :data="filteredAnnouncements" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="priority" label="优先级">
          <template #default="scope">
            <el-tag :type="getPriorityType(scope.row.priority)">{{ getPriorityName(scope.row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="scope" label="发布范围">{{ getScopeName }}</el-table-column>
        <el-table-column prop="publishTime" label="发布时间" width="180"></el-table-column>
        <el-table-column prop="expiryDate" label="有效期" width="180"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'info'">
              {{ scope.row.status === 'active' ? '生效中' : '已过期' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="readCount" label="阅读数"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewAnnouncement(scope.row.id)">查看</el-button>
            <el-button size="small" type="danger" @click="deleteAnnouncement(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 公告统计 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">公告统计</span>
        </div>
      </template>
      <div class="stats-grid">
        <div class="stat-item">
          <div class="stat-value">{{ totalAnnouncements }}</div>
          <div class="stat-label">总公告数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ activeAnnouncements }}</div>
          <div class="stat-label">生效中</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ expiredAnnouncements }}</div>
          <div class="stat-label">已过期</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ totalReads }}</div>
          <div class="stat-label">总阅读数</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      announcementForm: {
        title: '',
        content: '',
        priority: 'medium',
        expiryDate: '',
        scope: 'all',
        targetRole: '',
        targetSchool: ''
      },
      rules: {
        title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }],
        expiryDate: [{ required: true, message: '请选择有效期', trigger: 'blur' }]
      },
      statusFilter: '',
      searchQuery: '',
      announcements: [],
      schools: [
        { code: 'school001', name: '清华大学' },
        { code: 'school002', name: '北京大学' },
        { code: 'school003', name: '复旦大学' }
      ],
      announcementStats: {
        totalAnnouncements: 0,
        activeAnnouncements: 0,
        expiredAnnouncements: 0,
        totalReads: 0
      }
    }
  },
  computed: {
    filteredAnnouncements() {
      let result = this.announcements
      if (this.statusFilter) {
        result = result.filter(announcement => announcement.status === this.statusFilter)
      }
      if (this.searchQuery) {
        result = result.filter(announcement => 
          announcement.title.includes(this.searchQuery) || 
          announcement.content.includes(this.searchQuery)
        )
      }
      return result
    },
    totalAnnouncements() {
      return this.announcementStats.totalAnnouncements
    },
    activeAnnouncements() {
      return this.announcementStats.activeAnnouncements
    },
    expiredAnnouncements() {
      return this.announcementStats.expiredAnnouncements
    },
    totalReads() {
      return this.announcementStats.totalReads
    }
  },
  methods: {
    getPriorityType(priority) {
      switch (priority) {
        case 'high': return 'danger'
        case 'medium': return 'warning'
        case 'low': return 'info'
        default: return 'info'
      }
    },
    getPriorityName(priority) {
      switch (priority) {
        case 'high': return '高'
        case 'medium': return '中'
        case 'low': return '低'
        default: return '未知'
      }
    },
    getScopeName(scope) {
      switch (scope) {
        case 'all': return '所有用户'
        case 'super_admin': return '超级管理员'
        case 'school_admin': return '学校管理员'
        case 'teacher': return '教师'
        case 'student': return '学生'
        default: return '未知'
      }
    },
    publishAnnouncement() {
      const token = localStorage.getItem('token')
      this.$refs.announcementForm.validate((valid) => {
        if (valid) {
          this.$axios.post('/announcements/publish', this.announcementForm, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          }).then(response => {
            this.getAnnouncements()
            this.getAnnouncementStats()
            this.$message.success('公告发布成功')
            // 重置表单
            this.announcementForm = {
              title: '',
              content: '',
              priority: 'medium',
              expiryDate: '',
              scope: 'all',
              targetRole: '',
              targetSchool: ''
            }
          }).catch(error => {
            console.error('发布公告失败:', error)
            this.$message.error('发布公告失败')
          })
        }
      })
    },
    viewAnnouncement(id) {
      const token = localStorage.getItem('token')
      this.$axios.get(`/announcements/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        const announcement = response.data
        // 可以在这里打开一个对话框显示公告详情
        console.log('公告详情:', announcement)
      }).catch(error => {
        console.error('查看公告失败:', error)
        this.$message.error('查看公告失败')
      })
    },
    deleteAnnouncement(id) {
      this.$confirm('确定要删除该公告吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const token = localStorage.getItem('token')
        this.$axios.delete(`/announcements/${id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          this.getAnnouncements()
          this.getAnnouncementStats()
          this.$message.success('公告删除成功')
        }).catch(error => {
          console.error('删除公告失败:', error)
          this.$message.error('删除公告失败')
        })
      }).catch(() => {
        // 取消删除
      })
    },
    getAnnouncements() {
      const token = localStorage.getItem('token')
      this.$axios.get('/announcements/list', {
        params: {
          status: this.statusFilter,
          search: this.searchQuery
        },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.announcements = response.data
      }).catch(error => {
        console.error('获取公告列表失败:', error)
      })
    },
    getAnnouncementStats() {
      const token = localStorage.getItem('token')
      this.$axios.get('/announcements/stats', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.announcementStats = response.data
      }).catch(error => {
        console.error('获取公告统计失败:', error)
      })
    }
  },
  mounted() {
    this.getAnnouncements()
    this.getAnnouncementStats()
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

.card-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.select-input {
  width: 120px;
}

.search-input {
  width: 200px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding: 20px;
}

.stat-item {
  text-align: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #1976D2;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 16px;
  color: #606266;
}

@media (max-width: 1200px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>