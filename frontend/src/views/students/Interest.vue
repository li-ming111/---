<template>
  <div class="interest-container">
    <div class="interest-header">
      <h2>兴趣拓展模块</h2>
      <el-button type="primary" @click="dialogVisible = true" class="add-btn">
        <el-icon><Plus /></el-icon>
        创建兴趣小组
      </el-button>
    </div>
    
    <div class="interest-filters">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索兴趣"
        prefix-icon="Search"
        class="search-input"
        @keyup.enter="searchInterests"
      />
      <el-select v-model="selectedCategory" placeholder="按分类筛选" class="category-select">
        <el-option label="全部" value="all" />
        <el-option label="学术" value="academic" />
        <el-option label="体育" value="sports" />
        <el-option label="艺术" value="art" />
        <el-option label="科技" value="tech" />
        <el-option label="其他" value="other" />
      </el-select>
    </div>
    
    <!-- 热门兴趣 -->
    <div class="popular-interests">
      <el-card class="popular-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">热门兴趣</span>
          </div>
        </template>
        <div class="interest-tags">
          <el-tag
            v-for="interest in popularInterests"
            :key="interest.id"
            :effect="'dark'"
            class="interest-tag"
            @click="viewInterest(interest)"
          >
            {{ interest.name }}
          </el-tag>
        </div>
      </el-card>
    </div>
    
    <!-- 兴趣小组 -->
    <div class="interest-groups">
      <el-card class="groups-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">兴趣小组</span>
          </div>
        </template>
        <div class="groups-list">
          <el-card v-for="group in interestGroups" :key="group.id" class="group-card">
            <div class="group-header">
              <h3 class="group-name">{{ group.name }}</h3>
              <el-tag :type="group.category === '学术' ? 'primary' : group.category === '体育' ? 'success' : group.category === '艺术' ? 'warning' : 'info'">
                {{ group.category }}
              </el-tag>
            </div>
            <div class="group-content">
              <p class="group-description">{{ group.description }}</p>
              <div class="group-meta">
                <span class="group-members">{{ group.members }} 成员</span>
                <span class="group-activities">{{ group.activities }} 活动</span>
              </div>
            </div>
            <div class="group-footer">
              <el-button size="small" @click="joinGroup(group)" class="join-btn">
                加入小组
              </el-button>
              <el-button size="small" @click="viewGroup(group)" class="view-btn">
                查看详情
              </el-button>
            </div>
          </el-card>
        </div>
      </el-card>
    </div>
    
    <!-- 活动日历 -->
    <div class="activity-calendar">
      <el-card class="calendar-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">活动日历</span>
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
          <div class="calendar-activities">
            <div v-for="activity in calendarActivities" :key="activity.id" class="activity-item">
              <div class="activity-time">{{ activity.time }}</div>
              <div class="activity-content">
                <div class="activity-title">{{ activity.title }}</div>
                <div class="activity-location">{{ activity.location }}</div>
                <div class="activity-group">{{ activity.group }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 创建兴趣小组对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="创建兴趣小组"
      width="600px"
      class="group-dialog"
    >
      <el-form :model="newGroup" label-width="100px" class="group-form">
        <el-form-item label="小组名称">
          <el-input v-model="newGroup.name" class="input-field" />
        </el-form-item>
        <el-form-item label="小组分类">
          <el-select v-model="newGroup.category" class="select-field">
            <el-option label="学术" value="academic" />
            <el-option label="体育" value="sports" />
            <el-option label="艺术" value="art" />
            <el-option label="科技" value="tech" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="小组描述">
          <el-input
            type="textarea"
            v-model="newGroup.description"
            class="textarea-field"
            :rows="4"
            placeholder="输入小组描述..."
          />
        </el-form-item>
        <el-form-item label="成员限制">
          <el-input-number v-model="newGroup.memberLimit" :min="1" :max="100" class="number-input" />
          <span class="unit">人</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="createGroup" class="confirm-btn">创建</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, Search } from '@element-plus/icons-vue'

export default {
  components: {
    Plus,
    Search
  },
  data() {
    return {
      popularInterests: [],
      interestGroups: [],
      calendarActivities: [],
      searchKeyword: '',
      selectedCategory: 'all',
      selectedDate: new Date(),
      dialogVisible: false,
      newGroup: {
        name: '',
        category: 'academic',
        description: '',
        memberLimit: 20
      }
    }
  },
  mounted() {
    this.getPopularInterests()
    this.getInterestGroups()
    this.updateCalendarActivities()
  },
  methods: {
    getPopularInterests() {
      // 模拟数据
      this.popularInterests = [
        { id: 1, name: '编程', category: 'tech', popularity: 95 },
        { id: 2, name: '摄影', category: 'art', popularity: 88 },
        { id: 3, name: '篮球', category: 'sports', popularity: 85 },
        { id: 4, name: '演讲', category: 'other', popularity: 80 },
        { id: 5, name: '辩论', category: 'academic', popularity: 75 },
        { id: 6, name: '吉他', category: 'art', popularity: 70 },
        { id: 7, name: '人工智能', category: 'tech', popularity: 65 },
        { id: 8, name: '马拉松', category: 'sports', popularity: 60 }
      ]
    },
    getInterestGroups() {
      // 模拟数据
      this.interestGroups = [
        {
          id: 1,
          name: '编程爱好者协会',
          category: 'tech',
          description: '专注于编程技术交流和项目开发的兴趣小组',
          members: 45,
          activities: 12
        },
        {
          id: 2,
          name: '摄影社',
          category: 'art',
          description: '热爱摄影的同学交流学习的平台',
          members: 32,
          activities: 8
        },
        {
          id: 3,
          name: '篮球俱乐部',
          category: 'sports',
          description: '组织篮球比赛和训练的兴趣小组',
          members: 28,
          activities: 15
        },
        {
          id: 4,
          name: '辩论队',
          category: 'academic',
          description: '参加各类辩论比赛的学生组织',
          members: 15,
          activities: 6
        }
      ]
    },
    updateCalendarActivities() {
      // 模拟日历活动
      this.calendarActivities = []
    },
    searchInterests() {
      // 实现搜索功能
    },
    onDateChange() {
      // 实现日期变更功能
    },
    viewInterest(interest) {
      this.$message.info(`查看兴趣: ${interest.name}`)
    },
    joinGroup(group) {
      this.$message.success(`加入小组: ${group.name} 成功`)
    },
    viewGroup(group) {
      this.$message.info(`查看小组: ${group.name}`)
    },
    createGroup() {
      const newGroup = {
        ...this.newGroup,
        id: Date.now(),
        members: 1,
        activities: 0
      }
      this.interestGroups.push(newGroup)
      this.$message.success('兴趣小组创建成功')
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.newGroup = {
        name: '',
        category: 'academic',
        description: '',
        memberLimit: 20
      }
    }
  }
}
</script>

<style scoped>
.interest-container {
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 600px;
}

.interest-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.interest-header h2 {
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

.interest-filters {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.category-select {
  width: 200px;
}

.popular-interests {
  margin-bottom: 20px;
}

.popular-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.popular-card:hover {
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

.interest-tags {
  padding: 15px;
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.interest-tag {
  padding: 8px 16px;
  border-radius: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
}

.interest-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.interest-groups {
  margin-bottom: 20px;
}

.groups-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.groups-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.groups-list {
  padding: 15px;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 15px;
}

.group-card {
  border-radius: 8px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.group-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.group-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  border-bottom: 1px solid #e4e7ed;
}

.group-name {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.group-content {
  padding: 15px;
}

.group-description {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.group-meta {
  display: flex;
  gap: 15px;
  font-size: 12px;
  color: #999;
}

.group-footer {
  padding: 15px;
  border-top: 1px solid #e4e7ed;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.join-btn, .view-btn {
  border-radius: 4px;
  transition: all 0.3s ease;
}

.join-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
}

.view-btn {
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
}

.activity-calendar {
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

.calendar-content {
  padding: 15px;
}

.date-picker {
  margin-bottom: 15px;
}

.calendar-activities {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
}

.activity-item {
  display: flex;
  gap: 15px;
  padding: 10px;
  background-color: white;
  border-radius: 6px;
  margin-bottom: 10px;
  transition: all 0.3s ease;
}

.activity-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.activity-time {
  width: 100px;
  font-size: 14px;
  font-weight: 500;
  color: #667eea;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin-bottom: 5px;
}

.activity-location {
  font-size: 12px;
  color: #999;
  margin-bottom: 5px;
}

.activity-group {
  font-size: 12px;
  color: #667eea;
}

.group-dialog {
  border-radius: 16px;
  overflow: hidden;
}

.group-dialog .el-dialog__header {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
}

.group-dialog .el-dialog__title {
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.group-dialog .el-dialog__body {
  padding: 30px;
  background: rgba(255, 255, 255, 0.95);
}

.group-form {
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
  .interest-filters {
    flex-direction: column;
  }
  
  .search-input,
  .category-select {
    width: 100%;
  }
  
  .groups-list {
    grid-template-columns: 1fr;
  }
  
  .activity-item {
    flex-direction: column;
    gap: 10px;
  }
  
  .activity-time {
    width: 100%;
  }
}
</style>
