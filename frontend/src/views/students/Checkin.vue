<template>
  <div class="checkin-container">
    <!-- 打卡状态卡片 -->
    <el-card class="checkin-status-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">打卡状态</span>
        </div>
      </template>
      
      <div class="status-content">
        <div class="status-icon" :class="{ 'checked-in': hasCheckedInToday }">
          <el-icon v-if="hasCheckedInToday"><Check /></el-icon>
          <el-icon v-else><Timer /></el-icon>
        </div>
        <div class="status-info">
          <h3 class="status-title">{{ hasCheckedInToday ? '今日已打卡' : '今日未打卡' }}</h3>
          <p class="status-desc">{{ hasCheckedInToday ? '太棒了！继续保持' : '快来打卡，养成好习惯' }}</p>
        </div>
        <el-button 
          type="primary" 
          size="large" 
          :disabled="hasCheckedInToday" 
          @click="doCheckin"
          class="checkin-btn"
        >
          <el-icon v-if="!hasCheckedInToday"><Check /></el-icon>
          {{ hasCheckedInToday ? '已打卡' : '立即打卡' }}
        </el-button>
      </div>
    </el-card>

    <!-- 打卡统计卡片 -->
    <el-card class="checkin-stats-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">打卡统计</span>
        </div>
      </template>
      
      <div class="stats-content">
        <div class="stat-item">
          <div class="stat-value">{{ totalCheckinCount }}</div>
          <div class="stat-label">总打卡次数</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ currentStreak }}</div>
          <div class="stat-label">连续打卡</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ weeklyCheckinCount }}</div>
          <div class="stat-label">本周打卡</div>
        </div>
      </div>
    </el-card>

    <!-- 打卡记录卡片 -->
    <el-card class="checkin-records-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">打卡记录</span>
        </div>
      </template>
      
      <el-table :data="checkinRecords" style="width: 100%" class="records-table">
        <el-table-column prop="checkinTime" label="打卡时间" width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag type="success" class="status-tag">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { Check, Timer } from '@element-plus/icons-vue'

export default {
  components: {
    Check,
    Timer
  },
  data() {
    return {
      hasCheckedInToday: false,
      totalCheckinCount: 0,
      currentStreak: 0,
      weeklyCheckinCount: 0,
      checkinRecords: []
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      if (token) {
        this.getCheckinStatus()
        this.getCheckinRecords()
        this.getCheckinCount()
      }
    },
    getCheckinStatus() {
      const token = localStorage.getItem('token')
      this.$axios.get('/checkin/has-checked-in-today', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.hasCheckedInToday = response.data
      }).catch(error => {
        console.error('获取打卡状态失败:', error)
      })
    },
    getCheckinRecords() {
      const token = localStorage.getItem('token')
      this.$axios.get('/checkin/records', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.checkinRecords = response.data
        this.calculateStats()
      }).catch(error => {
        console.error('获取打卡记录失败:', error)
      })
    },
    getCheckinCount() {
      const token = localStorage.getItem('token')
      this.$axios.get('/checkin/count', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.totalCheckinCount = response.data
      }).catch(error => {
        console.error('获取打卡次数失败:', error)
      })
    },
    doCheckin() {
      const token = localStorage.getItem('token')
      this.$axios.post('/checkin/do-checkin', {}, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('打卡成功')
        this.hasCheckedInToday = true
        this.getCheckinRecords()
        this.getCheckinCount()
      }).catch(error => {
        this.$message.error('打卡失败: ' + error.response.data)
      })
    },
    calculateStats() {
      // 计算连续打卡天数
      this.currentStreak = this.calculateCurrentStreak()
      // 计算本周打卡次数
      this.weeklyCheckinCount = this.calculateWeeklyCheckinCount()
    },
    calculateCurrentStreak() {
      if (this.checkinRecords.length === 0) return 0
      
      let streak = 0
      const today = new Date()
      today.setHours(0, 0, 0, 0)
      
      for (const record of this.checkinRecords) {
        const checkinDate = new Date(record.checkinTime)
        checkinDate.setHours(0, 0, 0, 0)
        
        const diffTime = today.getTime() - checkinDate.getTime()
        const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24))
        
        if (diffDays === streak) {
          streak++
        } else {
          break
        }
      }
      
      return streak
    },
    calculateWeeklyCheckinCount() {
      if (this.checkinRecords.length === 0) return 0
      
      let count = 0
      const weekAgo = new Date()
      weekAgo.setDate(weekAgo.getDate() - 7)
      weekAgo.setHours(0, 0, 0, 0)
      
      for (const record of this.checkinRecords) {
        const checkinDate = new Date(record.checkinTime)
        if (checkinDate >= weekAgo) {
          count++
        } else {
          break
        }
      }
      
      return count
    }
  }
}
</script>

<style scoped>
.checkin-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.checkin-status-card,
.checkin-stats-card,
.checkin-records-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0 0 20px 0;
  border-radius: 0;
  box-shadow: none;
}

.checkin-status-card:hover,
.checkin-stats-card:hover,
.checkin-records-card:hover {
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

.status-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 40px;
  gap: 30px;
}

.status-icon {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 48px;
  background-color: #f0f0f0;
  color: #999;
  transition: all 0.3s ease;
}

.status-icon.checked-in {
  background-color: #e6f7ee;
  color: #52c41a;
}

.status-info {
  flex: 1;
}

.status-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.status-desc {
  font-size: 16px;
  color: #666;
  margin: 0;
}

.checkin-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 12px 30px;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.checkin-btn:hover:not(:disabled) {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.stats-content {
  display: flex;
  justify-content: space-around;
  padding: 30px;
  gap: 20px;
}

.stat-item {
  text-align: center;
  flex: 1;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.stat-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #667eea;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.records-table {
  margin: 0;
  border-radius: 0;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #333;
}

.status-tag {
  border-radius: 12px;
  padding: 2px 12px;
  font-size: 12px;
  font-weight: 500;
}

@media (max-width: 1200px) {
  .checkin-container {
    padding: 20px;
  }
  
  .status-content {
    flex-direction: column;
    text-align: center;
    gap: 20px;
  }
  
  .stats-content {
    flex-direction: column;
    gap: 15px;
  }
  
  .stat-item {
    padding: 15px;
  }
}

@media (max-width: 768px) {
  .card-header {
    padding: 10px 20px;
  }
  
  .card-title {
    font-size: 16px;
  }
  
  .status-content {
    padding: 20px;
  }
  
  .status-icon {
    width: 80px;
    height: 80px;
    font-size: 36px;
  }
  
  .status-title {
    font-size: 20px;
  }
  
  .status-desc {
    font-size: 14px;
  }
  
  .checkin-btn {
    padding: 10px 24px;
    font-size: 14px;
  }
  
  .stats-content {
    padding: 20px;
  }
  
  .stat-value {
    font-size: 24px;
  }
  
  .stat-label {
    font-size: 12px;
  }
}
</style>