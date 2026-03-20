<template>
  <div class="community-container">
    <!-- 社群管理卡片 -->
    <el-card class="community-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">学习社群</span>
          <el-button type="primary" @click="dialogVisible = true" class="add-btn">
            <el-icon><Plus /></el-icon>
            创建社群
          </el-button>
        </div>
      </template>
      
      <!-- 社群分类标签 -->
      <div class="category-tabs">
        <el-tag 
          v-for="category in categories" 
          :key="category.value"
          :type="activeCategory === category.value ? 'primary' : undefined"
          @click="activeCategory = category.value"
          class="category-tag"
        >
          {{ category.label }}
        </el-tag>
      </div>
      
      <!-- 社群列表 -->
      <el-table :data="communities" style="width: 100%" class="community-table">
        <el-table-column prop="name" label="社群名称" width="200">
          <template #default="scope">
            <div class="community-name" @click="viewCommunity(scope.row)">
              {{ scope.row.name }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="社群描述">
          <template #default="scope">
            <div class="community-description">{{ scope.row.description }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="类型" width="120" />
        <el-table-column prop="memberCount" label="成员数" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '活跃' ? 'success' : 'info'" class="status-tag">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button size="small" @click="joinCommunity(scope.row)" class="join-btn">
              <el-icon><UserFilled /></el-icon>
              加入
            </el-button>
            <el-button size="small" @click="viewCommunity(scope.row)" class="view-btn">
              <el-icon><View /></el-icon>
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 社群详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="社群详情"
      width="600px"
      class="custom-dialog"
    >
      <div v-if="currentCommunity" class="community-detail">
        <h3 class="detail-title">{{ currentCommunity.name }}</h3>
        <p class="detail-description">{{ currentCommunity.description }}</p>
        <el-divider />
        <div class="detail-info">
          <p><strong>类型:</strong> {{ currentCommunity.type }}</p>
          <p><strong>分类:</strong> {{ currentCommunity.category }}</p>
          <p><strong>成员数:</strong> {{ currentCommunity.memberCount }}</p>
          <p><strong>状态:</strong> 
            <el-tag :type="currentCommunity.status === '活跃' ? 'success' : 'info'" class="status-tag">
              {{ currentCommunity.status }}
            </el-tag>
          </p>
          <p><strong>创建时间:</strong> {{ currentCommunity.createTime }}</p>
        </div>
      </div>
    </el-dialog>

    <!-- 创建社群对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="创建社群"
      width="600px"
      class="custom-dialog"
    >
      <el-form :model="newCommunity" label-width="100px" class="add-form">
        <el-form-item label="社群名称">
          <el-input v-model="newCommunity.name" class="input-field" prefix-icon="Document" />
        </el-form-item>
        <el-form-item label="社群描述">
          <el-input type="textarea" v-model="newCommunity.description" class="textarea-field" :rows="3" />
        </el-form-item>
        <el-form-item label="社群类型">
          <el-select v-model="newCommunity.type" class="select-field">
            <el-option value="专业" label="专业" />
            <el-option value="目标" label="目标" />
            <el-option value="兴趣" label="兴趣" />
          </el-select>
        </el-form-item>
        <el-form-item label="具体分类">
          <el-input v-model="newCommunity.category" class="input-field" placeholder="如：计算机科学、考研、篮球等" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="createCommunity" class="confirm-btn">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, UserFilled, View, Document } from '@element-plus/icons-vue'

export default {
  components: {
    Plus,
    UserFilled,
    View,
    Document
  },
  data() {
    return {
      communities: [],
      currentCommunity: null,
      dialogVisible: false,
      detailDialogVisible: false,
      activeCategory: 'all',
      categories: [
        { label: '全部', value: 'all' },
        { label: '专业', value: '专业' },
        { label: '目标', value: '目标' },
        { label: '兴趣', value: '兴趣' }
      ],
      newCommunity: {
        name: '',
        description: '',
        type: '',
        category: ''
      }
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  watch: {
    activeCategory() {
      this.getCommunities()
    }
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      if (token) {
        this.getCommunities()
      }
    },
    getCommunities() {
      const token = localStorage.getItem('token')
      let url = '/communities/all'
      if (this.activeCategory !== 'all') {
        url = `/communities/type/${this.activeCategory}`
      }
      this.$axios.get(url, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.communities = response.data
      }).catch(error => {
        console.error('获取社群列表失败:', error)
      })
    },
    viewCommunity(community) {
      this.currentCommunity = community
      this.detailDialogVisible = true
    },
    joinCommunity(community) {
      this.$message.success(`加入社群 ${community.name} 成功`)
    },
    createCommunity() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      
      const community = {
        ...this.newCommunity,
        creatorId: user.id
      }
      
      this.$axios.post('/communities/create', community, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('社群创建成功')
        this.dialogVisible = false
        this.getCommunities()
      }).catch(error => {
        console.error('创建社群失败:', error)
      })
    }
  }
}
</script>

<style scoped>
.community-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.community-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0;
  border-radius: 0;
  box-shadow: none;
}

.community-card:hover {
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

.category-tabs {
  padding: 20px 30px;
  border-bottom: 1px solid #e4e7ed;
}

.category-tag {
  margin-right: 10px;
  cursor: pointer;
  padding: 6px 16px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.category-tag:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.community-table {
  margin: 0;
  border-radius: 0;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #333;
}

.community-name {
  font-weight: 500;
  color: #333;
  transition: all 0.3s ease;
  cursor: pointer;
}

.community-name:hover {
  color: #667eea;
}

.community-description {
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

.join-btn, .view-btn {
  border-radius: 6px;
  transition: all 0.3s ease;
  margin-right: 8px;
}

.join-btn {
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
}

.join-btn:hover {
  background-color: #e0e0e0;
}

.view-btn {
  background: linear-gradient(45deg, #4CAF50, #45a049);
  border: none;
  color: white;
}

.view-btn:hover {
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

.community-detail {
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

@media (max-width: 1200px) {
  .community-container {
    padding: 20px;
  }
  
  .community-table {
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
  
  .category-tabs {
    padding: 15px 20px;
  }
  
  .category-tag {
    margin-bottom: 10px;
  }
  
  .custom-dialog {
    width: 95% !important;
  }
  
  .custom-dialog .el-dialog__body {
    padding: 20px;
  }
}
</style>
