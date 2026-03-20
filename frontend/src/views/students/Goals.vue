  <template>
  <div class="goals-container">
    <!-- 目标管理卡片 -->
    <el-card class="goals-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">学习目标管理</span>
          <el-button type="primary" @click="dialogVisible = true" class="add-btn">
            <el-icon><Plus /></el-icon>
            添加目标
          </el-button>
        </div>
      </template>
      
      <!-- 目标列表 -->
      <el-table :data="goals" style="width: 100%" class="goals-table">
        <el-table-column prop="title" label="目标名称" width="250">
          <template #default="scope">
            <div class="goal-title">{{ scope.row.title }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="目标描述">
          <template #default="scope">
            <div class="goal-description">{{ scope.row.description }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="targetDate" label="目标日期" width="180" />
        <el-table-column prop="priority" label="优先级" width="120">
          <template #default="scope">
            <el-tag :type="getPriorityType(scope.row.priority)" class="priority-tag">
              {{ scope.row.priority }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" class="status-tag">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="viewGoal(scope.row)" class="view-btn">
              <el-icon><View /></el-icon>
              查看详情
            </el-button>
            <el-button 
              size="small" 
              type="primary" 
              @click="updateGoalStatus(scope.row.id, '已完成')"
              class="complete-btn"
              :disabled="scope.row.status === '已完成'"
            >
              <el-icon><Check /></el-icon>
              标记完成
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 目标详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="目标详情"
      width="600px"
      class="custom-dialog"
    >
      <div v-if="currentGoal" class="goal-detail">
        <h3 class="detail-title">{{ currentGoal.title }}</h3>
        <p class="detail-description">{{ currentGoal.description }}</p>
        <el-divider />
        <div class="detail-info">
          <p><strong>目标日期:</strong> {{ currentGoal.targetDate }}</p>
          <p><strong>优先级:</strong> 
            <el-tag :type="getPriorityType(currentGoal.priority)" class="priority-tag">
              {{ currentGoal.priority }}
            </el-tag>
          </p>
          <p><strong>状态:</strong> 
            <el-tag :type="getStatusType(currentGoal.status)" class="status-tag">
              {{ currentGoal.status }}
            </el-tag>
          </p>
          <p><strong>创建时间:</strong> {{ currentGoal.createTime }}</p>
        </div>
      </div>
    </el-dialog>

    <!-- 添加目标对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="添加学习目标"
      width="600px"
      class="custom-dialog"
    >
      <el-form :model="newGoal" label-width="100px" class="add-form">
        <el-form-item label="目标名称">
          <el-input v-model="newGoal.title" class="input-field" prefix-icon="Flag" />
        </el-form-item>
        <el-form-item label="目标描述">
          <el-input type="textarea" v-model="newGoal.description" class="textarea-field" :rows="3" />
        </el-form-item>
        <el-form-item label="目标日期">
          <el-date-picker
            v-model="newGoal.targetDate"
            type="date"
            placeholder="选择目标日期"
            class="date-picker"
          />
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="newGoal.priority" placeholder="选择优先级" class="select-field">
            <el-option label="高" value="高" />
            <el-option label="中" value="中" />
            <el-option label="低" value="低" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="addGoal" class="confirm-btn">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, View, Check, Flag } from '@element-plus/icons-vue'

export default {
  components: {
    Plus,
    View,
    Check,
    Flag
  },
  data() {
    return {
      goals: [],
      currentGoal: null,
      dialogVisible: false,
      detailDialogVisible: false,
      newGoal: {
        title: '',
        description: '',
        targetDate: new Date(),
        priority: '中'
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
        this.getGoals()
      }
    },
    getGoals() {
      const token = localStorage.getItem('token')
      this.$axios.get('/goals', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.goals = response.data
      }).catch(error => {
        console.error('获取目标列表失败:', error)
      })
    },
    viewGoal(goal) {
      this.currentGoal = goal
      this.detailDialogVisible = true
    },
    updateGoalStatus(goalId, status) {
      const token = localStorage.getItem('token')
      this.$axios.put(`/goals/${goalId}/status`, { status }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('目标状态更新成功')
        this.getGoals()
      }).catch(error => {
        console.error('更新目标状态失败:', error)
      })
    },
    addGoal() {
      const token = localStorage.getItem('token')
      this.$axios.post('/goals', this.newGoal, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('目标添加成功')
        this.dialogVisible = false
        this.getGoals()
      }).catch(error => {
        console.error('添加目标失败:', error)
      })
    },
    getPriorityType(priority) {
      switch (priority) {
        case '高': return 'danger'
        case '中': return 'warning'
        case '低': return 'success'
        default: return ''
      }
    },
    getStatusType(status) {
      switch (status) {
        case '已完成': return 'success'
        case '进行中': return 'primary'
        case '未开始': return 'info'
        default: return ''
      }
    }
  }
}
</script>

<style scoped>
.goals-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.goals-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0;
  border-radius: 0;
  box-shadow: none;
}

.goals-card:hover {
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

.goals-table {
  margin: 0;
  border-radius: 0;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #333;
}

.goal-title {
  font-weight: 500;
  color: #333;
  transition: all 0.3s ease;
}

.goal-title:hover {
  color: #667eea;
}

.goal-description {
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.priority-tag,
.status-tag {
  border-radius: 12px;
  padding: 2px 12px;
  font-size: 12px;
  font-weight: 500;
}

.view-btn,
.complete-btn {
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

.complete-btn {
  background: linear-gradient(45deg, #4CAF50, #45a049);
  border: none;
}

.complete-btn:hover:not(:disabled) {
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

.goal-detail {
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

.add-form {
  animation: fadeIn 0.3s ease;
}

.input-field,
.select-field,
.textarea-field,
.date-picker {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  width: 100%;
}

.input-field:focus,
.select-field:focus,
.textarea-field:focus,
.date-picker:focus {
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
  .goals-container {
    padding: 20px;
  }
  
  .goals-table {
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
}
</style>