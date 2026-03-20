<template>
  <div class="career-container">
    <!-- 职业规划管理卡片 -->
    <el-card class="career-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">职业规划管理</span>
          <el-button type="primary" @click="dialogVisible = true" class="add-btn">
            <el-icon><Plus /></el-icon>
            添加职业规划
          </el-button>
        </div>
      </template>
      
      <!-- 职业规划列表 -->
      <el-table :data="careerPlans" style="width: 100%" class="career-table">
        <el-table-column prop="title" label="规划名称" width="250">
          <template #default="scope">
            <div class="plan-title">{{ scope.row.title }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="规划描述">
          <template #default="scope">
            <div class="plan-description">{{ scope.row.description }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="targetPosition" label="目标职位" width="180" />
        <el-table-column prop="targetSalary" label="目标薪资" width="150" />
        <el-table-column prop="timeframe" label="时间框架" width="150" />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" class="status-tag">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="viewCareerPlan(scope.row)" class="view-btn">
              <el-icon><View /></el-icon>
              查看详情
            </el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click="updateCareerPlanStatus(scope.row.id, '进行中')"
              class="start-btn"
              :disabled="scope.row.status === '进行中' || scope.row.status === '已完成'"
            >
              <el-icon><VideoPlay /></el-icon>
              开始执行
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 职业规划详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="职业规划详情"
      width="600px"
      class="custom-dialog"
    >
      <div v-if="currentCareerPlan" class="career-detail">
        <h3 class="detail-title">{{ currentCareerPlan.title }}</h3>
        <p class="detail-description">{{ currentCareerPlan.description }}</p>
        <el-divider />
        <div class="detail-info">
          <p><strong>目标职位:</strong> {{ currentCareerPlan.targetPosition }}</p>
          <p><strong>目标薪资:</strong> {{ currentCareerPlan.targetSalary }}</p>
          <p><strong>时间框架:</strong> {{ currentCareerPlan.timeframe }}</p>
          <p><strong>所需技能:</strong> 
            <el-tag v-for="skill in currentCareerPlan.skills.split(',')" :key="skill" class="skill-tag">
              {{ skill.trim() }}
            </el-tag>
          </p>
          <p><strong>状态:</strong> 
            <el-tag :type="getStatusType(currentCareerPlan.status)" class="status-tag">
              {{ currentCareerPlan.status }}
            </el-tag>
          </p>
          <p><strong>创建时间:</strong> {{ currentCareerPlan.createTime }}</p>
        </div>
      </div>
    </el-dialog>

    <!-- 添加职业规划对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="添加职业规划"
      width="600px"
      class="custom-dialog"
    >
      <el-form :model="newCareerPlan" label-width="100px" class="add-form">
        <el-form-item label="规划名称">
          <el-input v-model="newCareerPlan.title" class="input-field" prefix-icon="Document" />
        </el-form-item>
        <el-form-item label="规划描述">
          <el-input type="textarea" v-model="newCareerPlan.description" class="textarea-field" :rows="3" />
        </el-form-item>
        <el-form-item label="目标职位">
          <el-input v-model="newCareerPlan.targetPosition" class="input-field" prefix-icon="UserFilled" />
        </el-form-item>
        <el-form-item label="目标薪资">
          <el-input v-model="newCareerPlan.targetSalary" class="input-field" prefix-icon="Money" />
        </el-form-item>
        <el-form-item label="时间框架">
          <el-input v-model="newCareerPlan.timeframe" placeholder="如：1年、3年等" class="input-field" prefix-icon="Timer" />
        </el-form-item>
        <el-form-item label="所需技能">
          <el-input type="textarea" v-model="newCareerPlan.skills" placeholder="逗号分隔多个技能" class="textarea-field" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="addCareerPlan" class="confirm-btn">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, View, VideoPlay, Document, UserFilled, Money, Timer } from '@element-plus/icons-vue'

export default {
  components: {
    Plus,
    View,
    VideoPlay,
    Document,
    UserFilled,
    Money,
    Timer
  },
  data() {
    return {
      careerPlans: [],
      currentCareerPlan: null,
      dialogVisible: false,
      detailDialogVisible: false,
      newCareerPlan: {
        title: '',
        description: '',
        targetPosition: '',
        targetSalary: '',
        timeframe: '',
        skills: ''
      }
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      if (token) {
        this.getCareerPlans()
      }
    },
    getCareerPlans() {
      const token = localStorage.getItem('token')
      const userId = JSON.parse(localStorage.getItem('user')).id
      this.$axios.get(`/career-plans/user/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.careerPlans = response.data
      }).catch(error => {
        console.error('获取职业规划列表失败:', error)
      })
    },
    viewCareerPlan(plan) {
      this.currentCareerPlan = plan
      this.detailDialogVisible = true
    },
    updateCareerPlanStatus(planId, status) {
      const token = localStorage.getItem('token')
      const plan = this.careerPlans.find(p => p.id === planId)
      if (plan) {
        plan.status = status
        this.$axios.put('/career-plans/update', plan, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          this.$message.success('职业规划状态更新成功')
          this.getCareerPlans()
        }).catch(error => {
          console.error('更新职业规划状态失败:', error)
        })
      }
    },
    addCareerPlan() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      const school = JSON.parse(localStorage.getItem('school'))
      
      const careerPlan = {
        ...this.newCareerPlan,
        userId: user.id,
        userName: user.name || user.username,
        schoolId: school.id,
        careerGoal: this.newCareerPlan.title,
        industry: '',
        position: this.newCareerPlan.targetPosition,
        skillRequirements: this.newCareerPlan.skills,
        educationRequirements: '',
        experienceRequirements: '',
        shortTermGoals: '',
        mediumTermGoals: '',
        longTermGoals: '',
        actionPlan: '',
        status: '未开始'
      }
      
      this.$axios.post('/career-plans/create', careerPlan, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('职业规划添加成功')
        this.dialogVisible = false
        this.getCareerPlans()
      }).catch(error => {
        console.error('添加职业规划失败:', error)
      })
    },
    getStatusType(status) {
      switch (status) {
        case '已完成': return 'success'
        case '进行中': return 'primary'
        case '未开始': return 'info'
        case '已暂停': return 'warning'
        default: return ''
      }
    }
  }
}
</script>

<style scoped>
.career-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.career-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0;
  border-radius: 0;
  box-shadow: none;
}

.career-card:hover {
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

.add-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.add-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.career-table {
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

.status-tag {
  border-radius: 12px;
  padding: 2px 12px;
  font-size: 12px;
  font-weight: 500;
}

.view-btn,
.start-btn {
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

.start-btn {
  background: linear-gradient(45deg, #4CAF50, #45a049);
  border: none;
}

.start-btn:hover:not(:disabled) {
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
}

.career-detail {
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
  color: #666;
  line-height: 1.6;
}

.skill-tag {
  margin-right: 8px;
  margin-bottom: 8px;
  border-radius: 12px;
  padding: 2px 12px;
  font-size: 12px;
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
}

.add-form {
  animation: fadeIn 0.3s ease;
}

.input-field,
.textarea-field {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  width: 100%;
}

.input-field:focus,
.textarea-field:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
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
  .career-container {
    padding: 20px;
  }
  
  .career-table {
    margin: 10px;
  }
}

@media (max-width: 768px) {
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .add-btn {
    align-self: flex-end;
  }
  
  .custom-dialog {
    width: 95% !important;
  }
  
  .custom-dialog .el-dialog__body {
    padding: 20px;
  }
  
  .skill-tag {
    margin-right: 4px;
    margin-bottom: 4px;
  }
}
</style>