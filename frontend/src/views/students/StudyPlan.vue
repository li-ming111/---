<template>
  <div class="study-plan-container">
    <!-- 学习计划管理卡片 -->
    <el-card class="study-plan-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">学习计划管理</span>
          <div class="header-buttons">
            <el-button type="primary" @click="dialogVisible = true" class="create-btn">
              <el-icon><Plus /></el-icon>
              创建新计划
            </el-button>
            <el-button type="success" @click="aiRecommendDialogVisible = true" class="ai-recommend-btn">
              <el-icon><Cpu /></el-icon>
              AI智能推荐
            </el-button>
          </div>
        </div>
      </template>
      
      <!-- 学习计划列表 -->
      <el-table :data="studyPlans" style="width: 100%" class="study-plan-table">
        <el-table-column prop="title" label="计划名称" width="250">
          <template #default="scope">
            <div class="plan-title">{{ scope.row.title }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="计划描述">
          <template #default="scope">
            <div class="plan-description">{{ scope.row.description }}</div>
          </template>
        </el-table-column>
        <el-table-column label="开始时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.startDate || scope.row.start_date) }}
          </template>
        </el-table-column>
        <el-table-column label="结束时间" width="180">
          <template #default="scope">
            {{ formatDateTime(scope.row.endDate || scope.row.end_date) }}
          </template>
        </el-table-column>
        <el-table-column prop="progress" label="进度" width="150">
          <template #default="scope">
            <div class="progress-container">
              <el-progress 
                :percentage="scope.row.progress" 
                :color="getProgressColor(scope.row.progress)" 
                class="progress-bar"
              />
              <span class="progress-text">{{ scope.row.progress }}%</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" class="status-tag">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="scope">
            <el-button size="small" @click="viewPlan(scope.row)" class="view-btn">
              <el-icon><View /></el-icon>
              查看详情
            </el-button>
            <el-button size="small" type="primary" @click="adjustPlan(scope.row.id)" class="adjust-btn">
              <el-icon><Refresh /></el-icon>
              智能调整
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 计划详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="计划详情"
      width="800px"
      class="custom-dialog"
    >
      <div v-if="currentPlan" class="plan-detail">
        <h3 class="detail-title">{{ currentPlan.title }}</h3>
        <p class="detail-description">{{ currentPlan.description }}</p>
        <el-divider />
        <h4 class="tasks-title">任务列表</h4>
        <el-table :data="planDetails" style="width: 100%" class="tasks-table">
          <el-table-column prop="taskName" label="任务名称" width="200" />
          <el-table-column prop="description" label="任务描述" />
          <el-table-column label="开始时间" width="180">
            <template #default="scope">
              {{ formatDateTime(scope.row.startDate || scope.row.start_date) }}
            </template>
          </el-table-column>
          <el-table-column label="结束时间" width="180">
            <template #default="scope">
              {{ formatDateTime(scope.row.endDate || scope.row.end_date) }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="120">
            <template #default="scope">
              <el-tag :type="getStatusType(scope.row.status)" class="status-tag">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <!-- 创建计划对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="创建学习计划"
      width="900px"
      class="custom-dialog"
    >
      <el-form :model="newPlan" label-width="100px" class="create-form">
        <el-form-item label="计划名称">
          <el-input v-model="newPlan.title" class="input-field" prefix-icon="Document" />
        </el-form-item>
        <el-form-item label="计划描述">
          <el-input type="textarea" v-model="newPlan.description" class="textarea-field" :rows="3" />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="newPlan.startTime"
            type="datetime"
            placeholder="选择开始时间"
            class="date-picker"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="newPlan.endTime"
            type="datetime"
            placeholder="选择结束时间"
            class="date-picker"
          />
        </el-form-item>
        <el-form-item label="任务列表">
          <div v-for="(task, index) in newPlan.tasks" :key="index" class="task-item">
            <el-input v-model="task.taskName" placeholder="任务名称" class="task-name-input" prefix-icon="List" />
            <el-input v-model="task.description" placeholder="任务描述" class="task-description-input" />
            <el-date-picker
              v-model="task.startTime"
              type="datetime"
              placeholder="开始时间"
              class="task-date-picker"
            />
            <el-date-picker
              v-model="task.endTime"
              type="datetime"
              placeholder="结束时间"
              class="task-date-picker"
            />
            <el-button type="danger" @click="removeTask(index)" class="remove-task-btn">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </div>
          <el-button type="primary" @click="addTask" class="add-task-btn">
            <el-icon><Plus /></el-icon>
            添加任务
          </el-button>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="createPlan" class="confirm-btn">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- AI智能推荐学习计划对话框 -->
    <el-dialog
      v-model="aiRecommendDialogVisible"
      title="AI智能推荐学习计划"
      width="600px"
      class="custom-dialog"
    >
      <el-form :model="aiRecommendForm" label-width="120px" class="create-form">
        <el-form-item label="教育类型">
          <el-select v-model="aiRecommendForm.educationType" class="select-field">
            <el-option label="本科" value="本科" />
            <el-option label="研究生" value="研究生" />
          </el-select>
        </el-form-item>
        <el-form-item label="专业">
          <el-select v-model="aiRecommendForm.major" class="select-field">
            <el-option label="计算机科学与技术" value="计算机科学与技术" />
            <el-option label="金融学" value="金融学" />
            <el-option label="经济学" value="经济学" />
            <el-option label="管理学" value="管理学" />
            <el-option label="教育学" value="教育学" />
            <el-option label="医学" value="医学" />
          </el-select>
        </el-form-item>
        <el-form-item label="学习年限">
          <el-select v-model="aiRecommendForm.years" class="select-field">
            <el-option label="3年" value="3" />
            <el-option label="4年" value="4" />
            <el-option label="5年" value="5" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="aiRecommendDialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="success" @click="generateAIRecommendedPlan" class="confirm-btn">生成推荐计划</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, View, Refresh, Document, List, Delete, Cpu } from '@element-plus/icons-vue'

export default {
  components: {
    Plus,
    View,
    Refresh,
    Document,
    List,
    Delete,
    Cpu
  },
  data() {
    return {
      studyPlans: [],
      planDetails: [],
      currentPlan: null,
      dialogVisible: false,
      detailDialogVisible: false,
      aiRecommendDialogVisible: false,
      newPlan: {
        title: '',
        description: '',
        startTime: new Date(),
        endTime: new Date(),
        tasks: [{
          taskName: '',
          description: '',
          startTime: new Date(),
          endTime: new Date()
        }]
      },
      aiRecommendForm: {
        educationType: '本科',
        major: '计算机科学与技术',
        years: 4
      }
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      if (token && user) {
        this.getStudyPlans()
      }
    },
    getStudyPlans() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user) return
      const userId = user.id
      this.$axios.get(`/study-plans/user/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        console.log('后端返回的原始数据:', response.data)
        // 打印第一个计划的详细字段信息
        if (response.data && response.data.length > 0) {
          const firstPlan = response.data[0]
          console.log('第一个计划的所有字段:', Object.keys(firstPlan))
          console.log('startDate:', firstPlan.startDate)
          console.log('start_date:', firstPlan.start_date)
          console.log('endDate:', firstPlan.endDate)
          console.log('end_date:', firstPlan.end_date)
        }
        // 去重处理：根据ID去重
        const uniquePlans = []
        const planIds = new Set()
        response.data.forEach(plan => {
          if (!planIds.has(plan.id)) {
            planIds.add(plan.id)
            // 创建新对象，确保响应式更新
            const processedPlan = { ...plan }
            // 处理字段名映射：将下划线命名转换为驼峰命名
            if (processedPlan.start_date !== undefined && processedPlan.start_date !== null && processedPlan.start_date !== '') {
              processedPlan.startDate = processedPlan.start_date
            }
            if (processedPlan.end_date !== undefined && processedPlan.end_date !== null && processedPlan.end_date !== '') {
              processedPlan.endDate = processedPlan.end_date
            }
            // 如果没有时间字段，设置为空字符串
            if (processedPlan.startDate === undefined || processedPlan.startDate === null) {
              processedPlan.startDate = ''
            }
            if (processedPlan.endDate === undefined || processedPlan.endDate === null) {
              processedPlan.endDate = ''
            }
            uniquePlans.push(processedPlan)
          }
        })
        this.studyPlans = uniquePlans
        console.log('处理后的学习计划:', this.studyPlans)
      }).catch(error => {
        console.error('获取学习计划失败:', error)
      })
    },
    viewPlan(plan) {
      this.currentPlan = plan
      const token = localStorage.getItem('token')
      this.$axios.get(`/study-plans/detail/plan/${plan.id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        console.log('后端返回的任务原始数据:', response.data)
        // 处理任务的开始时间和结束时间字段名
        const processedDetails = response.data.map(detail => {
          if (!detail.startDate && detail.start_date) detail.startDate = detail.start_date
          if (!detail.endDate && detail.end_date) detail.endDate = detail.end_date
          if (!detail.startDate) detail.startDate = ''
          if (!detail.endDate) detail.endDate = ''
          return detail
        })
        this.planDetails = processedDetails
        console.log('处理后的任务数据:', this.planDetails)
        this.detailDialogVisible = true
      }).catch(error => {
        console.error('获取学习计划详情失败:', error)
      })
    },
    adjustPlan(planId) {
      const token = localStorage.getItem('token')
      this.$axios.put(`/study-plans/progress/${planId}`, null, {
        params: { progress: 50 },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.getStudyPlans()
      }).catch(error => {
        console.error('调整学习计划进度失败:', error)
      })
    },
    addTask() {
      this.newPlan.tasks.push({
        taskName: '',
        description: '',
        startTime: new Date(),
        endTime: new Date()
      })
    },
    removeTask(index) {
      this.newPlan.tasks.splice(index, 1)
    },
    createPlan() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      const school = JSON.parse(localStorage.getItem('school'))
      if (!user || !school) {
        this.$message.error('请先登录')
        return
      }
      const planData = {
        title: this.newPlan.title,
        description: this.newPlan.description,
        startDate: this.newPlan.startTime,
        endDate: this.newPlan.endTime,
        userId: user.id,
        schoolId: school.id
      }
      this.$axios.post('/study-plans/create', planData, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('计划创建成功')
        this.dialogVisible = false
        this.getStudyPlans()
      }).catch(error => {
        console.error('创建计划失败:', error)
        if (error.response && error.response.status === 400) {
          this.$message.error('创建失败：已存在相同标题的学习计划')
        } else {
          this.$message.error('创建计划失败，请稍后重试')
        }
      })
    },
    getProgressColor(progress) {
      if (progress >= 100) return '#67C23A'
      if (progress >= 60) return '#E6A23C'
      return '#F56C6C'
    },
    getStatusType(status) {
      switch (status) {
        case '已完成': return 'success'
        case '进行中': return 'primary'
        case '未开始': return 'info'
        case '已暂停': return 'warning'
        default: return 'info'
      }
    },
    generateAIRecommendedPlan() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      if (!user) {
        this.$message.error('请先登录')
        return
      }
      
      const userId = user.id
      const { educationType, major, years } = this.aiRecommendForm
      
      // 将years转换为整数
      const yearsInt = parseInt(years)
      
      this.$axios.post('/study-plans/ai-recommend', null, {
        params: {
          userId,
          educationType,
          major,
          years: yearsInt
        },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        console.log('后端返回的AI推荐计划:', response.data)
        this.$message.success('AI推荐学习计划生成成功')
        this.aiRecommendDialogVisible = false
        // 创建新对象，确保响应式更新
        const newPlan = { ...response.data }
        // 处理字段名映射：将下划线命名转换为驼峰命名
        if (newPlan.start_date !== undefined && newPlan.start_date !== null) {
          newPlan.startDate = newPlan.start_date
        }
        if (newPlan.end_date !== undefined && newPlan.end_date !== null) {
          newPlan.endDate = newPlan.end_date
        }
        // 如果没有时间字段，设置为空字符串
        if (newPlan.startDate === undefined || newPlan.startDate === null) {
          newPlan.startDate = ''
        }
        if (newPlan.endDate === undefined || newPlan.endDate === null) {
          newPlan.endDate = ''
        }
        
        // 检查是否已存在该计划
        const existingIndex = this.studyPlans.findIndex(plan => plan.id === newPlan.id)
        if (existingIndex > -1) {
          // 更新现有计划，使用Vue的响应式方法
          this.studyPlans.splice(existingIndex, 1, newPlan)
        } else {
          // 添加新计划
          this.studyPlans.unshift(newPlan)
        }
        console.log('更新后的学习计划列表:', this.studyPlans)
      }).catch(error => {
        console.error('生成AI推荐学习计划失败:', error)
        if (error.response && error.response.data) {
          this.$message.error('生成失败：' + error.response.data)
        } else {
          this.$message.error('生成AI推荐学习计划失败，请稍后重试')
        }
      })
    },
    formatDateTime(dateTimeStr) {
      if (!dateTimeStr || dateTimeStr === '' || dateTimeStr === null || dateTimeStr === undefined) {
        return ''
      }
      // 如果已经是格式化的字符串，直接返回
      if (dateTimeStr.length === 19 && dateTimeStr.includes('-') && dateTimeStr.includes(':')) {
        return dateTimeStr
      }
      // 尝试解析日期
      try {
        const date = new Date(dateTimeStr)
        if (isNaN(date.getTime())) {
          return dateTimeStr
        }
        const year = date.getFullYear()
        const month = String(date.getMonth() + 1).padStart(2, '0')
        const day = String(date.getDate()).padStart(2, '0')
        const hours = String(date.getHours()).padStart(2, '0')
        const minutes = String(date.getMinutes()).padStart(2, '0')
        const seconds = String(date.getSeconds()).padStart(2, '0')
        return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
      } catch (e) {
        return dateTimeStr
      }
    }
  }
}
</script>

<style scoped>
.study-plan-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.study-plan-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0;
  border-radius: 0;
  box-shadow: none;
}

.study-plan-card:hover {
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

.header-buttons {
  display: flex;
  gap: 10px;
}

.ai-recommend-btn {
  background: linear-gradient(45deg, #4CAF50, #45a049);
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.ai-recommend-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.4);
}

.select-field {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  width: 100%;
}

.select-field:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.create-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.create-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.study-plan-table {
  margin: 0;
  border-radius: 0;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #333;
}

.plan-title {
  font-weight: 500;
  color: #333;
  transition: all 0.3s ease;
}

.plan-title:hover {
  color: #667eea;
}

.plan-description {
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.progress-container {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.progress-bar {
  height: 8px;
  border-radius: 4px;
}

.progress-text {
  font-size: 12px;
  color: #666;
  text-align: right;
  font-weight: 500;
}

.status-tag {
  border-radius: 12px;
  padding: 2px 12px;
  font-size: 12px;
  font-weight: 500;
}

.view-btn,
.adjust-btn {
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

.adjust-btn {
  background: linear-gradient(45deg, #4CAF50, #45a049);
  border: none;
}

.adjust-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(76, 175, 80, 0.4);
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

.plan-detail {
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

.tasks-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 15px;
}

.tasks-table {
  border-radius: 12px;
  overflow: hidden;
}

.create-form {
  animation: fadeIn 0.3s ease;
}

.input-field,
.textarea-field,
.date-picker {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  width: 100%;
}

.input-field:focus,
.textarea-field:focus,
.date-picker:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.task-item {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-bottom: 15px;
  padding: 15px;
  background: rgba(245, 247, 250, 0.8);
  border-radius: 12px;
  transition: all 0.3s ease;
  gap: 10px;
}

.task-item:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.task-name-input {
  width: 180px;
  border-radius: 8px;
}

.task-description-input {
  flex: 1;
  min-width: 200px;
  border-radius: 8px;
}

.task-date-picker {
  width: 160px;
  border-radius: 8px;
}

.remove-task-btn {
  border-radius: 6px;
  transition: all 0.3s ease;
}

.remove-task-btn:hover {
  transform: scale(1.05);
}

.add-task-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
  margin-top: 10px;
}

.add-task-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  background: rgba(245, 247, 250, 0.8);
}

.cancel-btn,
.confirm-btn {
  border-radius: 8px;
  padding: 8px 20px;
  transition: all 0.3s ease;
}

.confirm-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.confirm-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
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
  .study-plan-container {
    padding: 20px;
  }
  
  .study-plan-table {
    margin: 10px;
  }
  
  .task-item {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }
  
  .task-name-input,
  .task-description-input,
  .task-date-picker {
    width: 100%;
    margin-right: 0;
  }
  
  .remove-task-btn {
    align-self: flex-end;
  }
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .create-btn {
    align-self: flex-end;
  }
  
  .custom-dialog {
    width: 95% !important;
  }
  
  .custom-dialog .el-dialog__body {
    padding: 20px;
  }
}
</style>