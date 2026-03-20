<template>
  <div class="incentive-container">
    <!-- 激励系统卡片 -->
    <el-card class="incentive-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">激励系统</span>
        </div>
      </template>
      
      <!-- 激励列表 -->
      <el-table :data="incentives" style="width: 100%" class="incentive-table">
        <el-table-column prop="name" label="激励名称" width="200">
          <template #default="scope">
            <div class="incentive-name">{{ scope.row.name }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="激励描述">
          <template #default="scope">
            <div class="incentive-description">{{ scope.row.description }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="points" label="所需积分" width="120">
          <template #default="scope">
            <div class="incentive-points">{{ scope.row.points }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="剩余数量" width="120">
          <template #default="scope">
            <div class="incentive-quantity">{{ scope.row.quantity }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" class="status-tag">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="viewIncentive(scope.row)" class="view-btn">
              <el-icon><View /></el-icon>
              查看
            </el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click="claimIncentive(scope.row)" 
              class="claim-btn"
              :disabled="!canClaim(scope.row)"
            >
              <el-icon><Star /></el-icon>
              领取
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 用户激励记录卡片 -->
    <el-card class="user-incentive-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">我的激励记录</span>
        </div>
      </template>
      
      <el-table :data="userIncentives" style="width: 100%" class="user-incentive-table">
        <el-table-column prop="incentiveName" label="激励名称" width="200" />
        <el-table-column prop="obtainedTime" label="领取时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" class="status-tag">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 激励详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="激励详情"
      width="600px"
      class="custom-dialog"
    >
      <div v-if="currentIncentive" class="incentive-detail">
        <h3 class="detail-title">{{ currentIncentive.name }}</h3>
        <p class="detail-description">{{ currentIncentive.description }}</p>
        <el-divider />
        <div class="detail-info">
          <div class="info-item">
            <span class="info-label">所需积分：</span>
            <span class="info-value">{{ currentIncentive.points }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">剩余数量：</span>
            <span class="info-value">{{ currentIncentive.quantity }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">状态：</span>
            <span class="info-value">{{ currentIncentive.status }}</span>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { View, Star } from '@element-plus/icons-vue'

export default {
  components: {
    View,
    Star
  },
  data() {
    return {
      incentives: [],
      userIncentives: [],
      currentIncentive: null,
      detailDialogVisible: false,
      userPoints: 0
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      if (token) {
        this.getIncentives()
        this.getUserIncentives()
        this.getUserPoints()
      }
    },
    getIncentives() {
      const token = localStorage.getItem('token')
      this.$axios.get('/incentives', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.incentives = response.data
      }).catch(error => {
        console.error('获取激励列表失败:', error)
      })
    },
    getUserIncentives() {
      const token = localStorage.getItem('token')
      this.$axios.get('/user-incentives', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.userIncentives = response.data
      }).catch(error => {
        console.error('获取用户激励记录失败:', error)
      })
    },
    getUserPoints() {
      const token = localStorage.getItem('token')
      this.$axios.get('/user-points', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.userPoints = response.data
      }).catch(error => {
        console.error('获取用户积分失败:', error)
      })
    },
    viewIncentive(incentive) {
      this.currentIncentive = incentive
      this.detailDialogVisible = true
    },
    claimIncentive(incentive) {
      const token = localStorage.getItem('token')
      this.$axios.post(`/incentives/${incentive.id}/claim`, {}, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('激励领取成功')
        this.getIncentives()
        this.getUserIncentives()
        this.getUserPoints()
      }).catch(error => {
        this.$message.error('激励领取失败: ' + error.response.data.message)
      })
    },
    canClaim(incentive) {
      return incentive.status === '可用' && incentive.quantity > 0 && this.userPoints >= incentive.points
    },
    getStatusType(status) {
      switch (status) {
        case '可用': return 'success'
        case '已领取': return 'primary'
        case '已过期': return 'warning'
        case '已用完': return 'info'
        default: return ''
      }
    }
  }
}
</script>

<style scoped>
.incentive-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.incentive-card,
.user-incentive-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0 0 20px 0;
  border-radius: 0;
  box-shadow: none;
}

.incentive-card:hover,
.user-incentive-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15);
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

.incentive-table,
.user-incentive-table {
  margin: 0;
  border-radius: 0;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #333;
}

.incentive-name {
  font-weight: 500;
  color: #333;
  transition: all 0.3s ease;
}

.incentive-name:hover {
  color: #667eea;
}

.incentive-description {
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.incentive-points {
  font-weight: bold;
  color: #ff6b6b;
}

.incentive-quantity {
  font-weight: 500;
  color: #4ecdc4;
}

.status-tag {
  border-radius: 12px;
  padding: 2px 12px;
  font-size: 12px;
  font-weight: 500;
}

.view-btn,
.claim-btn {
  border-radius: 6px;
  transition: all 0.3s ease;
  margin-right: 8px;
}

.view-btn {
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
}

.view-btn:hover {
  background-color: #e0e0e0;
}

.claim-btn {
  background: linear-gradient(45deg, #ff6b6b, #ee5a6f);
  border: none;
}

.claim-btn:hover:not(:disabled) {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(255, 107, 107, 0.4);
}

.custom-dialog {
  border-radius: 16px;
  overflow: hidden;
}

.custom-dialog .el-dialog__header {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
}

.custom-dialog .el-dialog__title {
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.custom-dialog .el-dialog__body {
  padding: 30px;
  background: rgba(255, 255, 255, 0.95);
  max-height: 60vh;
  overflow-y: auto;
}

.incentive-detail {
  animation: fadeIn 0.3s ease;
}

.detail-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.detail-description {
  color: #666;
  line-height: 1.6;
  margin-bottom: 20px;
}

.detail-info {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.info-label {
  font-weight: 500;
  color: #333;
  min-width: 100px;
}

.info-value {
  color: #666;
  font-weight: 500;
}

@keyframes fadeIn {
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
  .incentive-container {
    padding: 20px;
  }
  
  .incentive-table,
  .user-incentive-table {
    margin: 10px;
  }
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .custom-dialog {
    width: 95% !important;
  }
  
  .custom-dialog .el-dialog__body {
    padding: 20px;
  }
}
</style>