<template>
  <div class="admin-container">
    <h2 class="page-title">内容审核系统</h2>
    
    <!-- 审核规则设置 -->
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">审核规则设置</span>
        </div>
      </template>
      <el-form :model="reviewRules" :rules="rules" ref="rulesForm">
        <el-form-item label="自动审核" prop="autoReview">
          <el-switch v-model="reviewRules.autoReview"></el-switch>
        </el-form-item>
        <el-form-item label="敏感词过滤" prop="sensitiveWordFilter">
          <el-switch v-model="reviewRules.sensitiveWordFilter"></el-switch>
        </el-form-item>
        <el-form-item label="文件类型限制" prop="fileTypeLimit">
          <el-switch v-model="reviewRules.fileTypeLimit"></el-switch>
        </el-form-item>
        <el-form-item label="文件大小限制" prop="fileSizeLimit">
          <el-input v-model="reviewRules.fileSizeLimit" placeholder="请输入文件大小限制（MB）" type="number"></el-input>
        </el-form-item>
        <el-form-item label="敏感词列表" prop="sensitiveWords">
          <el-input v-model="reviewRules.sensitiveWords" type="textarea" placeholder="请输入敏感词，用逗号分隔" :rows="3"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveRules">保存规则</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 待审核内容 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">待审核内容</span>
          <div class="card-actions">
            <el-select v-model="reviewType" placeholder="内容类型" class="select-input">
              <el-option label="全部" value=""></el-option>
              <el-option label="文档" value="document"></el-option>
              <el-option label="视频" value="video"></el-option>
              <el-option label="音频" value="audio"></el-option>
              <el-option label="图片" value="image"></el-option>
            </el-select>
          </div>
        </div>
      </template>
      <el-table :data="pendingContents" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="type" label="类型">
          <template #default="scope">
            <el-tag :type="getResourceType(scope.row.type)">{{ getResourceTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="uploader" label="上传者"></el-table-column>
        <el-table-column prop="schoolName" label="所属学校"></el-table-column>
        <el-table-column prop="uploadTime" label="上传时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewContent(scope.row.id)">查看</el-button>
            <el-button size="small" type="success" @click="approveContent(scope.row.id)">通过</el-button>
            <el-button size="small" type="danger" @click="rejectContent(scope.row.id)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 审核历史 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">审核历史</span>
          <div class="card-actions">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              class="date-picker"
            ></el-date-picker>
            <el-button type="primary" @click="searchHistory">查询</el-button>
          </div>
        </div>
      </template>
      <el-table :data="reviewHistory" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="type" label="类型">
          <template #default="scope">
            <el-tag :type="getResourceType(scope.row.type)">{{ getResourceTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="uploader" label="上传者"></el-table-column>
        <el-table-column prop="reviewer" label="审核员"></el-table-column>
        <el-table-column prop="status" label="审核状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'approved' ? 'success' : 'danger'">
              {{ scope.row.status === 'approved' ? '通过' : '拒绝' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reviewTime" label="审核时间" width="180"></el-table-column>
        <el-table-column prop="reason" label="审核理由"></el-table-column>
      </el-table>
    </el-card>

    <!-- 审核统计 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">审核统计</span>
        </div>
      </template>
      <div class="stats-grid">
        <div class="stat-item">
          <div class="stat-value">{{ totalPending }}</div>
          <div class="stat-label">待审核</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ totalApproved }}</div>
          <div class="stat-label">已通过</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ totalRejected }}</div>
          <div class="stat-label">已拒绝</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ approvalRate }}%</div>
          <div class="stat-label">通过率</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      reviewRules: {
        autoReview: true,
        sensitiveWordFilter: true,
        fileTypeLimit: true,
        fileSizeLimit: 50,
        sensitiveWords: '敏感词1,敏感词2,敏感词3'
      },
      rules: {
        fileSizeLimit: [{ required: true, message: '请输入文件大小限制', trigger: 'blur' }]
      },
      reviewType: '',
      dateRange: [],
      pendingContents: [],
      reviewHistory: [],
      totalPending: 0,
      totalApproved: 0,
      totalRejected: 0,
      approvalRate: 0
    }
  },
  methods: {
    getResourceType(type) {
      switch (type) {
        case 'document': return 'primary'
        case 'video': return 'warning'
        case 'audio': return 'success'
        case 'image': return 'danger'
        case 'other': return 'info'
        default: return 'info'
      }
    },
    getResourceTypeName(type) {
      switch (type) {
        case 'document': return '文档'
        case 'video': return '视频'
        case 'audio': return '音频'
        case 'image': return '图片'
        case 'other': return '其他'
        default: return '未知'
      }
    },
    saveRules() {
      const token = localStorage.getItem('token')
      this.$refs.rulesForm.validate((valid) => {
        if (valid) {
          this.$axios.post('/content-review/rules', this.reviewRules, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          }).then(response => {
            this.$message.success('审核规则保存成功')
          }).catch(error => {
            console.error('保存审核规则失败:', error)
            this.$message.error('保存审核规则失败')
          })
        }
      })
    },
    viewContent(id) {
      const token = localStorage.getItem('token')
      this.$axios.get(`/content-review/content/${id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        const content = response.data
        // 可以在这里打开一个对话框显示内容详情
        console.log('内容详情:', content)
      }).catch(error => {
        console.error('查看内容失败:', error)
        this.$message.error('查看内容失败')
      })
    },
    approveContent(id) {
      const token = localStorage.getItem('token')
      this.$axios.post(`/content-review/approve/${id}`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.getPendingContents()
        this.getReviewHistory()
        this.getReviewStats()
        this.$message.success('内容审核通过')
      }).catch(error => {
        console.error('审核通过失败:', error)
        this.$message.error('审核通过失败')
      })
    },
    rejectContent(id) {
      const token = localStorage.getItem('token')
      this.$axios.post(`/content-review/reject/${id}`, { reason: '内容不符合要求' }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.getPendingContents()
        this.getReviewHistory()
        this.getReviewStats()
        this.$message.success('内容审核拒绝')
      }).catch(error => {
        console.error('审核拒绝失败:', error)
        this.$message.error('审核拒绝失败')
      })
    },
    searchHistory() {
      this.getReviewHistory()
    },
    updateApprovalRate() {
      const total = this.totalApproved + this.totalRejected
      this.approvalRate = total > 0 ? Math.round((this.totalApproved / total) * 100) : 0
    },
    getReviewRules() {
      const token = localStorage.getItem('token')
      this.$axios.get('/content-review/rules', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.reviewRules = response.data
      }).catch(error => {
        console.error('获取审核规则失败:', error)
      })
    },
    getPendingContents() {
      const token = localStorage.getItem('token')
      this.$axios.get('/content-review/pending', {
        params: { type: this.reviewType },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.pendingContents = response.data
      }).catch(error => {
        console.error('获取待审核内容失败:', error)
      })
    },
    getReviewHistory() {
      const token = localStorage.getItem('token')
      const params = {}
      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = this.dateRange[0]
        params.endDate = this.dateRange[1]
      }
      this.$axios.get('/content-review/history', {
        params: params,
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.reviewHistory = response.data
      }).catch(error => {
        console.error('获取审核历史失败:', error)
      })
    },
    getReviewStats() {
      const token = localStorage.getItem('token')
      this.$axios.get('/content-review/stats', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        const stats = response.data
        this.totalPending = stats.totalPending
        this.totalApproved = stats.totalApproved
        this.totalRejected = stats.totalRejected
        this.approvalRate = stats.approvalRate
      }).catch(error => {
        console.error('获取审核统计失败:', error)
      })
    }
  },
  mounted() {
    this.getReviewRules()
    this.getPendingContents()
    this.getReviewHistory()
    this.getReviewStats()
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

.date-picker {
  width: 240px;
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