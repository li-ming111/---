<template>
  <div class="admin-container">
    <h2 class="page-title">数据报表</h2>
    
    <!-- 统计卡片 -->
    <div class="stats-container">
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ totalStudents }}</div>
          <div class="stat-label">学生总数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ totalTeachers }}</div>
          <div class="stat-label">教师总数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ totalResources }}</div>
          <div class="stat-label">资源总数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-item">
          <div class="stat-value">{{ totalCourses }}</div>
          <div class="stat-label">课程总数</div>
        </div>
      </el-card>
    </div>

    <!-- 图表区域 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">学生分布</span>
        </div>
      </template>
      <div class="chart-container">
        <div id="studentDistributionChart" style="width: 100%; height: 400px;"></div>
      </div>
    </el-card>

    <div class="chart-row">
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
      <el-card class="admin-card chart-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">用户活跃度</span>
          </div>
        </template>
        <div class="chart-container">
          <div id="userActivityChart" style="width: 100%; height: 300px;"></div>
        </div>
      </el-card>
    </div>

    <!-- 详细数据表格 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">详细数据</span>
        </div>
      </template>
      <el-table :data="detailedData" style="width: 100%">
        <el-table-column prop="category" label="分类"></el-table-column>
        <el-table-column prop="count" label="数量"></el-table-column>
        <el-table-column prop="percentage" label="占比"></el-table-column>
        <el-table-column prop="trend" label="趋势">
          <template #default="scope">
            <el-tag :type="scope.row.trend > 0 ? 'success' : 'danger'">
              {{ scope.row.trend > 0 ? '↑' : '↓' }}{{ Math.abs(scope.row.trend) }}%
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      totalStudents: 1250,
      totalTeachers: 85,
      totalResources: 320,
      totalCourses: 120,
      detailedData: [
        { category: '大一学生', count: 320, percentage: '25.6%', trend: 5 },
        { category: '大二学生', count: 310, percentage: '24.8%', trend: 3 },
        { category: '大三学生', count: 300, percentage: '24.0%', trend: -2 },
        { category: '大四学生', count: 320, percentage: '25.6%', trend: 1 },
        { category: '教授', count: 15, percentage: '17.6%', trend: 0 },
        { category: '副教授', count: 25, percentage: '29.4%', trend: 2 },
        { category: '讲师', count: 45, percentage: '52.9%', trend: 1 }
      ]
    }
  },
  methods: {
    initCharts() {
      // 模拟图表初始化
      console.log('初始化报表图表...')
      // 实际项目中可以使用 ECharts 等图表库
    }
  },
  mounted() {
    this.initCharts()
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
  text-align: center;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #4CAF50;
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