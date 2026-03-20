<template>
  <div class="exam-container">
    <div class="exam-header">
      <h2>考试备考系统</h2>
      <el-button type="primary" @click="dialogVisible = true" class="add-btn">
        <el-icon><Plus /></el-icon>
        添加考试
      </el-button>
    </div>
    
    <div class="exam-filters">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索考试"
        prefix-icon="Search"
        class="search-input"
        @keyup.enter="searchExams"
      />
      <el-select v-model="selectedType" placeholder="按类型筛选" class="type-select">
        <el-option label="全部" value="all" />
        <el-option label="期中" value="midterm" />
        <el-option label="期末" value="final" />
        <el-option label="考证" value="certification" />
        <el-option label="其他" value="other" />
      </el-select>
    </div>
    
    <!-- 考试日历 -->
    <div class="exam-calendar">
      <el-card class="calendar-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">考试日历</span>
          </div>
        </template>
        <div class="calendar-content">
          <el-date-picker
            v-model="selectedDate"
            type="date"
            placeholder="选择日期"
            class="date-picker"
            @change="onDateChange"
          />
          <div class="calendar-events">
            <div v-for="event in calendarEvents" :key="event.id" class="event-item">
              <div class="event-time">{{ event.time }}</div>
              <div class="event-content">
                <div class="event-title">{{ event.title }}</div>
                <div class="event-location">{{ event.location }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 考试列表 -->
    <div class="exam-list">
      <el-card class="exam-list-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">考试列表</span>
            <el-button size="small" @click="showUpcomingExams" class="upcoming-btn">
              即将到来的考试
            </el-button>
          </div>
        </template>
        <el-table :data="exams" style="width: 100%" class="exam-table">
          <el-table-column prop="examName" label="考试名称" width="200" />
          <el-table-column prop="examType" label="考试类型" width="120" />
          <el-table-column prop="examDate" label="考试日期" width="150" />
          <el-table-column prop="location" label="考试地点" width="150" />
          <el-table-column prop="duration" label="考试时长(分钟)" width="120" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="150">
            <template #default="scope">
              <el-button size="small" @click="viewExam(scope.row)" class="view-btn">
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button size="small" @click="editExam(scope.row)" class="edit-btn">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button size="small" @click="deleteExam(scope.row.id)" class="delete-btn">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>
    
    <!-- 创建/编辑考试对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑考试' : '添加考试'"
      width="600px"
      class="exam-dialog"
    >
      <el-form :model="currentExam" label-width="100px" class="exam-form">
        <el-form-item label="考试名称">
          <el-input v-model="currentExam.examName" class="input-field" />
        </el-form-item>
        <el-form-item label="考试类型">
          <el-select v-model="currentExam.examType" class="select-field">
            <el-option label="期中" value="midterm" />
            <el-option label="期末" value="final" />
            <el-option label="考证" value="certification" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="考试日期">
          <el-date-picker
            v-model="currentExam.examDate"
            type="datetime"
            placeholder="选择考试日期和时间"
            class="date-time-picker"
          />
        </el-form-item>
        <el-form-item label="考试地点">
          <el-input v-model="currentExam.location" class="input-field" />
        </el-form-item>
        <el-form-item label="考试时长">
          <el-input-number v-model="currentExam.duration" :min="1" :max="300" class="number-input" />
          <span class="unit">分钟</span>
        </el-form-item>
        <el-form-item label="考试状态">
          <el-select v-model="currentExam.status" class="select-field">
            <el-option label="未开始" value="pending" />
            <el-option label="进行中" value="ongoing" />
            <el-option label="已完成" value="completed" />
          </el-select>
        </el-form-item>
        <el-form-item label="考试描述">
          <el-input
            type="textarea"
            v-model="currentExam.description"
            class="textarea-field"
            :rows="3"
            placeholder="输入考试描述..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="saveExam" class="confirm-btn">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, View, Edit, Delete, Search } from '@element-plus/icons-vue'

export default {
  components: {
    Plus,
    View,
    Edit,
    Delete,
    Search
  },
  data() {
    return {
      exams: [],
      calendarEvents: [],
      searchKeyword: '',
      selectedType: 'all',
      selectedDate: new Date(),
      dialogVisible: false,
      isEdit: false,
      currentExam: {
        id: '',
        examName: '',
        examType: 'midterm',
        description: '',
        examDate: new Date(),
        location: '',
        duration: 120,
        status: 'pending'
      }
    }
  },
  mounted() {
    this.getExams()
    this.updateCalendarEvents()
  },
  methods: {
    getExams() {
      // 模拟数据
      this.exams = [
        {
          id: 1,
          examName: '高等数学期中考试',
          examType: 'midterm',
          description: '高等数学上册期中考试',
          examDate: '2026-04-15 14:00:00',
          location: '教学楼A301',
          duration: 120,
          status: 'pending'
        },
        {
          id: 2,
          examName: '大学英语四级考试',
          examType: 'certification',
          description: '全国大学英语四级考试',
          examDate: '2026-06-15 09:00:00',
          location: '教学楼B101',
          duration: 135,
          status: 'pending'
        },
        {
          id: 3,
          examName: '数据结构期末考试',
          examType: 'final',
          description: '数据结构课程期末考试',
          examDate: '2026-07-05 10:00:00',
          location: '教学楼A201',
          duration: 120,
          status: 'pending'
        }
      ]
    },
    updateCalendarEvents() {
      // 模拟日历事件
      this.calendarEvents = [
        {
          id: 1,
          title: '高等数学期中考试',
          time: '14:00-16:00',
          location: '教学楼A301'
        },
        {
          id: 2,
          title: '大学英语四级考试',
          time: '09:00-11:15',
          location: '教学楼B101'
        }
      ]
    },
    searchExams() {
      // 实现搜索功能
      console.log('搜索考试:', this.searchKeyword)
    },
    onDateChange() {
      // 实现日期变更功能
      console.log('选择日期:', this.selectedDate)
    },
    showUpcomingExams() {
      // 显示即将到来的考试
      console.log('显示即将到来的考试')
    },
    viewExam(exam) {
      this.$message.info(`查看考试: ${exam.examName}`)
    },
    editExam(exam) {
      this.isEdit = true
      this.currentExam = { ...exam }
      this.dialogVisible = true
    },
    deleteExam(id) {
      this.$confirm('确定要删除这条考试记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.exams = this.exams.filter(exam => exam.id !== id)
        this.$message.success('考试记录删除成功')
      }).catch(() => {
        // 取消删除
      })
    },
    saveExam() {
      if (this.isEdit) {
        // 更新考试
        const index = this.exams.findIndex(exam => exam.id === this.currentExam.id)
        if (index !== -1) {
          this.exams[index] = { ...this.currentExam }
        }
        this.$message.success('考试记录更新成功')
      } else {
        // 创建考试
        const newExam = {
          ...this.currentExam,
          id: Date.now()
        }
        this.exams.push(newExam)
        this.$message.success('考试记录创建成功')
      }
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.isEdit = false
      this.currentExam = {
        id: '',
        examName: '',
        examType: 'midterm',
        description: '',
        examDate: new Date(),
        location: '',
        duration: 120,
        status: 'pending'
      }
    }
  }
}
</script>

<style scoped>
.exam-container {
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 600px;
}

.exam-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.exam-header h2 {
  margin: 0;
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

.exam-filters {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.type-select {
  width: 200px;
}

.exam-calendar {
  margin-bottom: 20px;
}

.calendar-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.calendar-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #e4e7ed;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.calendar-content {
  padding: 15px;
}

.date-picker {
  margin-bottom: 15px;
}

.calendar-events {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
}

.event-item {
  display: flex;
  gap: 15px;
  padding: 10px;
  background-color: white;
  border-radius: 6px;
  margin-bottom: 10px;
  transition: all 0.3s ease;
}

.event-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.event-time {
  width: 100px;
  font-size: 14px;
  font-weight: 500;
  color: #667eea;
}

.event-content {
  flex: 1;
}

.event-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 5px;
}

.event-location {
  font-size: 12px;
  color: #999;
}

.exam-list-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.exam-list-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.upcoming-btn {
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.exam-table {
  margin-top: 10px;
}

.view-btn, .edit-btn, .delete-btn {
  border-radius: 4px;
  transition: all 0.3s ease;
  margin-right: 5px;
}

.view-btn {
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
}

.edit-btn {
  background-color: #4CAF50;
  border: none;
  color: white;
}

.delete-btn {
  background-color: #f44336;
  border: none;
  color: white;
}

.exam-dialog {
  border-radius: 16px;
  overflow: hidden;
}

.exam-dialog .el-dialog__header {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
}

.exam-dialog .el-dialog__title {
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.exam-dialog .el-dialog__body {
  padding: 30px;
  background: rgba(255, 255, 255, 0.95);
}

.exam-form {
  animation: fadeIn 0.3s ease;
}

.input-field, .select-field, .textarea-field {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  width: 100%;
}

.input-field:focus, .select-field:focus, .textarea-field:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.date-time-picker {
  width: 100%;
}

.number-input {
  width: 150px;
}

.unit {
  margin-left: 10px;
  color: #666;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  background: rgba(245, 247, 250, 0.8);
}

.cancel-btn, .confirm-btn {
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

@media (max-width: 768px) {
  .exam-filters {
    flex-direction: column;
  }
  
  .search-input,
  .type-select {
    width: 100%;
  }
  
  .event-item {
    flex-direction: column;
    gap: 10px;
  }
  
  .event-time {
    width: 100%;
  }
}
</style>
