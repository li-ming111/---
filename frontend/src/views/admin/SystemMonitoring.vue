<template>
  <div class="admin-container">
    <h2 class="page-title">系统性能监控</h2>
    
    <!-- 系统状态卡片 -->
    <div class="stats-container">
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ cpuUsage }}%</div>
          <div class="stat-label">CPU使用率</div>
          <div class="stat-bar">
            <div class="stat-bar-fill" :style="{ width: cpuUsage + '%', backgroundColor: getStatusColor(cpuUsage) }"></div>
          </div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ memoryUsage }}%</div>
          <div class="stat-label">内存使用率</div>
          <div class="stat-bar">
            <div class="stat-bar-fill" :style="{ width: memoryUsage + '%', backgroundColor: getStatusColor(memoryUsage) }"></div>
          </div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ diskUsage }}%</div>
          <div class="stat-label">磁盘使用率</div>
          <div class="stat-bar">
            <div class="stat-bar-fill" :style="{ width: diskUsage + '%', backgroundColor: getStatusColor(diskUsage) }"></div>
          </div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ apiResponseTime }}ms</div>
          <div class="stat-label">API响应时间</div>
          <div class="stat-bar">
            <div class="stat-bar-fill" :style="{ width: Math.min(apiResponseTime / 10, 100) + '%', backgroundColor: getStatusColor(apiResponseTime / 10) }"></div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 监控图表 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">系统负载趋势</span>
        </div>
      </template>
      <div class="chart-container">
        <div id="systemLoadChart" style="width: 100%; height: 400px;"></div>
      </div>
    </el-card>

    <div class="chart-row">
      <el-card class="admin-card chart-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">API调用频率</span>
          </div>
        </template>
        <div class="chart-container">
          <div id="apiCallChart" style="width: 100%; height: 300px;"></div>
        </div>
      </el-card>
      <el-card class="admin-card chart-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">异常预警</span>
          </div>
        </template>
        <div class="alerts-container">
          <el-alert
            v-for="alert in alerts"
            :key="alert.id"
            :title="alert.title"
            :description="alert.description"
            :type="alert.type"
            show-icon
            :closable="false"
            class="alert-item"
          ></el-alert>
          <el-empty v-if="alerts.length === 0" description="暂无异常预警"></el-empty>
        </div>
      </el-card>
    </div>

    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">服务器信息</span>
        </div>
      </template>
      <el-table :data="serverInfo" style="width: 100%">
        <el-table-column prop="key" label="项"></el-table-column>
        <el-table-column prop="value" label="值"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      cpuUsage: 45,
      memoryUsage: 68,
      diskUsage: 32,
      apiResponseTime: 150,
      alerts: [],
      serverInfo: [],
      isLoading: false
    }
  },
  methods: {
    getStatusColor(value) {
      if (value < 60) return '#67C23A'
      if (value < 80) return '#E6A23C'
      return '#F56C6C'
    },
    initCharts() {
      // 模拟图表初始化
      // 实际项目中可以使用 ECharts 等图表库
    },
    getSystemStatus() {
      const token = localStorage.getItem('token')
      this.$axios.get('/api/system/monitoring/status', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        const status = response.data
        this.cpuUsage = status.cpuUsage
        this.memoryUsage = status.memoryUsage
        this.diskUsage = status.diskUsage
        this.apiResponseTime = status.apiResponseTime
      }).catch(error => {
        console.error('获取系统状态失败:', error)
      })
    },
    getServerInfo() {
      const token = localStorage.getItem('token')
      this.$axios.get('/api/system/monitoring/server-info', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.serverInfo = response.data
      }).catch(error => {
        console.error('获取服务器信息失败:', error)
      })
    },
    getAlerts() {
      const token = localStorage.getItem('token')
      this.$axios.get('/api/system/monitoring/alerts', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.alerts = response.data
      }).catch(error => {
        console.error('获取异常预警失败:', error)
      })
    }
  },
  mounted() {
    this.initCharts()
    // 初始化数据
    this.getSystemStatus()
    this.getServerInfo()
    this.getAlerts()
    
    // 定时更新系统状态
    setInterval(() => {
      this.getSystemStatus()
    }, 5000)
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

.stats-container {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.stat-item {
  padding: 20px;
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
  margin-bottom: 10px;
}

.stat-bar {
  height: 8px;
  background-color: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.stat-bar-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.5s ease;
}

.admin-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.mt-20 {
  margin-top: 20px;
}

.chart-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.chart-card {
  height: 100%;
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

.chart-container {
  padding: 20px;
}

.alerts-container {
  padding: 20px;
}

.alert-item {
  margin-bottom: 10px;
}

@media (max-width: 1200px) {
  .stats-container {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .chart-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-container {
    grid-template-columns: 1fr;
  }
}
</style>