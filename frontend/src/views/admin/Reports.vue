<template>
  <div class="admin-container">
    <h2 class="page-title">数据报表</h2>
    
    <!-- 统计卡片 -->
    <div class="stats-container">
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ totalUsers }}</div>
          <div class="stat-label">总用户数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ totalSchools }}</div>
          <div class="stat-label">总学校数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ totalResources }}</div>
          <div class="stat-label">总资源数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ totalLogins }}</div>
          <div class="stat-label">今日登录数</div>
        </div>
      </el-card>
    </div>

    <!-- 图表区域 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">用户增长趋势</span>
        </div>
      </template>
      <div class="chart-container">
        <div id="userGrowthChart" style="width: 100%; height: 400px;"></div>
      </div>
    </el-card>

    <div class="chart-row">
      <el-card class="admin-card chart-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">用户角色分布</span>
          </div>
        </template>
        <div class="chart-container">
          <div id="userRoleChart" style="width: 100%; height: 300px;"></div>
        </div>
      </el-card>
      <el-card class="admin-card chart-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">资源类型分布</span>
          </div>
        </template>
        <div class="chart-container">
          <div id="resourceTypeChart" style="width: 100%; height: 300px;"></div>
        </div>
      </el-card>
    </div>

    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">系统访问统计</span>
        </div>
      </template>
      <div class="chart-container">
        <div id="accessChart" style="width: 100%; height: 400px;"></div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      totalUsers: 0,
      totalSchools: 0,
      totalResources: 0,
      totalLogins: 0,
      userGrowthData: [],
      userRoleData: [],
      resourceTypeData: [],
      accessData: []
    }
  },
  mounted() {
    this.getSystemStats()
    this.getUserGrowth()
    this.getUserRoleDistribution()
    this.getResourceTypeDistribution()
    this.getAccessStats()
    this.initCharts()
  },
  methods: {
    initCharts() {
      // 模拟图表初始化
      // 实际项目中可以使用 ECharts 等图表库
      // 这里只做示例，不实际绘制图表
    },
    getSystemStats() {
      const token = localStorage.getItem('token')
      this.$axios.get('/system/reports/stats', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        const stats = response.data
        this.totalUsers = stats.totalUsers
        this.totalSchools = stats.totalSchools
        this.totalResources = stats.totalResources
        this.totalLogins = stats.totalLogins
      }).catch(error => {
        console.error('获取系统统计数据失败:', error)
      })
    },
    getUserGrowth() {
      const token = localStorage.getItem('token')
      this.$axios.get('/system/reports/user-growth', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.userGrowthData = response.data
      }).catch(error => {
        console.error('获取用户增长趋势失败:', error)
      })
    },
    getUserRoleDistribution() {
      const token = localStorage.getItem('token')
      this.$axios.get('/system/reports/user-role-distribution', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.userRoleData = response.data
      }).catch(error => {
        console.error('获取用户角色分布失败:', error)
      })
    },
    getResourceTypeDistribution() {
      const token = localStorage.getItem('token')
      this.$axios.get('/system/reports/resource-type-distribution', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.resourceTypeData = response.data
      }).catch(error => {
        console.error('获取资源类型分布失败:', error)
      })
    },
    getAccessStats() {
      const token = localStorage.getItem('token')
      this.$axios.get('/system/reports/access-stats', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.accessData = response.data
      }).catch(error => {
        console.error('获取系统访问统计失败:', error)
      })
    }
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
  text-align: center;
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