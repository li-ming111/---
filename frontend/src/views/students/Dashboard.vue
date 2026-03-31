<template>
  <div class="dashboard-container">
    <!-- 学习数据统计卡片 -->
    <el-card class="dashboard-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">学习数据统计</span>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            class="date-picker"
            @change="getStats"
          />
        </div>
      </template>
      
      <!-- 统计概览 -->
      <div class="stats-overview">
        <div class="stat-card">
          <div class="stat-icon study-time">
            <el-icon><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statsSummary.totalStudyDuration || 0 }}</div>
            <div class="stat-label">学习时长(分钟)</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon completion">
            <el-icon><Check /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statsSummary.averageTaskCompletionRate || 0 }}%</div>
            <div class="stat-label">任务完成率</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon checkin">
            <el-icon><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statsSummary.totalCheckinCount || 0 }}</div>
            <div class="stat-label">打卡次数</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon resources">
            <el-icon><Reading /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ statsSummary.totalResourceUsageCount || 0 }}</div>
            <div class="stat-label">资源使用次数</div>
          </div>
        </div>
      </div>
      
      <!-- 学习时长趋势图 -->
      <div class="chart-section">
        <h3 class="chart-title">学习时长趋势</h3>
        <div id="studyDurationChart" class="chart"></div>
      </div>
      
      <!-- 任务完成率趋势图 -->
      <div class="chart-section">
        <h3 class="chart-title">任务完成率趋势</h3>
        <div id="completionRateChart" class="chart"></div>
      </div>
      
      <!-- 学习活动分布饼图 -->
      <div class="chart-section">
        <h3 class="chart-title">学习活动分布</h3>
        <div id="activityDistributionChart" class="chart"></div>
      </div>
      
      <!-- 学习效率分析图 -->
      <div class="chart-section">
        <h3 class="chart-title">学习效率分析</h3>
        <div id="efficiencyChart" class="chart"></div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { Timer, Check, Calendar, Reading } from '@element-plus/icons-vue'
import * as echarts from 'echarts'

export default {
  components: {
    Timer,
    Check,
    Calendar,
    Reading
  },
  data() {
    return {
      dateRange: [],
      statsSummary: {},
      statsList: [],
      studyDurationChart: null,
      completionRateChart: null,
      activityDistributionChart: null,
      efficiencyChart: null
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      if (token) {
        this.getStats()
      }
    },
    getStats() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      const userId = user.id
      
      let startDate, endDate
      if (this.dateRange && this.dateRange.length === 2) {
        startDate = this.formatDate(this.dateRange[0])
        endDate = this.formatDate(this.dateRange[1])
      } else {
        // 默认获取最近7天的数据
        endDate = this.formatDate(new Date())
        const start = new Date()
        start.setDate(start.getDate() - 6)
        startDate = this.formatDate(start)
      }
      
      this.$axios.get(`/learning-stats/range/${userId}?startDate=${startDate}&endDate=${endDate}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.statsList = response.data
        this.calculateSummary()
        this.initCharts()
      }).catch(error => {
        console.error('获取学习统计数据失败:', error)
      })
    },
    calculateSummary() {
      let totalStudyDuration = 0
      let totalTaskCompletionRate = 0
      let totalCheckinCount = 0
      let totalResourceUsageCount = 0
      let totalNoteCount = 0
      let totalDiscussionCount = 0
      
      for (const stats of this.statsList) {
        totalStudyDuration += stats.studyDuration || 0
        totalTaskCompletionRate += stats.taskCompletionRate || 0
        totalCheckinCount += stats.checkinCount || 0
        totalResourceUsageCount += stats.resourceUsageCount || 0
        totalNoteCount += stats.noteCount || 0
        totalDiscussionCount += stats.discussionCount || 0
      }
      
      this.statsSummary = {
        totalStudyDuration,
        averageTaskCompletionRate: this.statsList.length > 0 ? Math.round(totalTaskCompletionRate / this.statsList.length) : 0,
        totalCheckinCount,
        totalResourceUsageCount,
        totalNoteCount,
        totalDiscussionCount
      }
    },
    initCharts() {
      // 学习时长趋势图
      const studyDurationChart = echarts.init(document.getElementById('studyDurationChart'))
      const dates = this.statsList.map(item => item.statsDate)
      const studyDurations = this.statsList.map(item => item.studyDuration || 0)
      
      const studyDurationOption = {
        title: {
          text: '学习时长趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: {
          type: 'value',
          name: '分钟'
        },
        series: [{
          data: studyDurations,
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#667eea'
          }
        }]
      }
      
      studyDurationChart.setOption(studyDurationOption)
      
      // 任务完成率趋势图
      const completionRateChart = echarts.init(document.getElementById('completionRateChart'))
      const completionRates = this.statsList.map(item => item.taskCompletionRate || 0)
      
      const completionRateOption = {
        title: {
          text: '任务完成率趋势',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: {
          type: 'value',
          name: '%',
          max: 100
        },
        series: [{
          data: completionRates,
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#4CAF50'
          }
        }]
      }
      
      completionRateChart.setOption(completionRateOption)
      
      // 学习活动分布饼图
      const activityDistributionChart = echarts.init(document.getElementById('activityDistributionChart'))
      const activityData = [
        { name: '课程学习', value: this.statsSummary.totalStudyDuration || 0 },
        { name: '资源使用', value: this.statsSummary.totalResourceUsageCount || 0 },
        { name: '笔记记录', value: this.statsSummary.totalNoteCount || 0 },
        { name: '讨论交流', value: this.statsSummary.totalDiscussionCount || 0 },
        { name: '打卡', value: this.statsSummary.totalCheckinCount || 0 }
      ]
      
      const activityDistributionOption = {
        title: {
          text: '学习活动分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [{
          name: '活动类型',
          type: 'pie',
          radius: '60%',
          data: activityData,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      
      activityDistributionChart.setOption(activityDistributionOption)
      
      // 学习效率分析图
      const efficiencyChart = echarts.init(document.getElementById('efficiencyChart'))
      const efficiencyData = this.statsList.map(item => ({
        date: item.statsDate,
        efficiency: Math.round(Math.random() * 30 + 70) // 模拟数据
      }))
      
      const efficiencyOption = {
        title: {
          text: '学习效率分析',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: efficiencyData.map(item => item.date)
        },
        yAxis: {
          type: 'value',
          name: '效率指数',
          max: 100
        },
        series: [{
          data: efficiencyData.map(item => item.efficiency),
          type: 'bar',
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      }
      
      efficiencyChart.setOption(efficiencyOption)
      
      // 保存图表实例，以便后续销毁
      this.studyDurationChart = studyDurationChart
      this.completionRateChart = completionRateChart
      this.activityDistributionChart = activityDistributionChart
      this.efficiencyChart = efficiencyChart
      
      // 监听窗口大小变化，调整图表大小
      window.addEventListener('resize', () => {
        studyDurationChart.resize()
        completionRateChart.resize()
        activityDistributionChart.resize()
        efficiencyChart.resize()
      })
    },
    formatDate(date) {
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.dashboard-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0;
  border-radius: 0;
  box-shadow: none;
}

.dashboard-card:hover {
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

.date-picker {
  width: 240px;
}

.stats-overview {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding: 30px;
  border-bottom: 1px solid #e4e7ed;
}

.stat-card {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  border-radius: 16px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  color: white;
}

.stat-icon.study-time {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.completion {
  background: linear-gradient(45deg, #4CAF50, #45a049);
}

.stat-icon.checkin {
  background: linear-gradient(45deg, #FF9800, #f57c00);
}

.stat-icon.resources {
  background: linear-gradient(45deg, #9C27B0, #7b1fa2);
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #333;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.chart-section {
  padding: 30px;
  border-bottom: 1px solid #e4e7ed;
}

.chart-section:last-child {
  border-bottom: none;
}

.chart-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 20px;
}

.chart {
  width: 100%;
  height: 300px;
}

@media (max-width: 1200px) {
  .stats-overview {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .dashboard-container {
    padding: 20px;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .date-picker {
    width: 100%;
  }
  
  .stats-overview {
    grid-template-columns: 1fr;
    padding: 20px;
  }
  
  .chart-section {
    padding: 20px;
  }
  
  .chart {
    height: 250px;
  }
}
</style>
