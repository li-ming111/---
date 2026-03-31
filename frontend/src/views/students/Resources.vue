<template>
  <div class="resources-container">
    <!-- 资源管理卡片 -->
    <el-card class="resources-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">学习资源管理</span>
          <el-button v-if="isAdminOrTeacher" type="primary" @click="dialogVisible = true" class="add-btn">
            <el-icon><Plus /></el-icon>
            添加资源
          </el-button>
        </div>
      </template>
      
      <!-- 搜索表单 -->
      <el-form :inline="true" :model="searchForm" class="search-form mb-6">
        <el-form-item label="资源类型">
          <el-select v-model="searchForm.type" placeholder="选择类型" class="select-field">
            <el-option label="课程" value="课程" />
            <el-option label="书籍" value="书籍" />
            <el-option label="视频" value="视频" />
            <el-option label="文档" value="文档" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="searchForm.keyword" placeholder="输入关键词" class="input-field" prefix-icon="Search" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchResources" class="search-btn">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
        </el-form-item>
      </el-form>
      
      <!-- 资源列表 -->
      <el-table :data="resources" style="width: 100%" class="resources-table">
        <el-table-column prop="title" label="资源名称" width="250">
          <template #default="scope">
            <div class="resource-title">{{ scope.row.title }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="资源类型" width="120">
          <template #default="scope">
            <el-tag :type="getTypeTagType(scope.row.category)" size="small" class="type-tag">
              {{ scope.row.category }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="资源描述">
          <template #default="scope">
            <div class="resource-description">{{ scope.row.description }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="fileUrl" label="资源链接" width="200">
          <template #default="scope">
            <a :href="scope.row.fileUrl" target="_blank" class="resource-link">
              <el-icon><Link /></el-icon>
              查看链接
            </a>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button size="small" @click="viewResource(scope.row)" class="view-btn">
              <el-icon><View /></el-icon>
              查看详情
            </el-button>
            <el-button v-if="isAdminOrTeacher" size="small" type="danger" @click="deleteResource(scope.row.id)" class="delete-btn">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 资源详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="资源详情"
      width="600px"
      class="custom-dialog"
    >
      <div v-if="currentResource" class="resource-detail">
        <h3 class="detail-title">{{ currentResource.title }}</h3>
        <p class="detail-description">{{ currentResource.description }}</p>
        <el-divider />
        <div class="detail-info">
          <p><strong>资源类型:</strong> 
            <el-tag :type="getTypeTagType(currentResource.category)" size="small">
              {{ currentResource.category }}
            </el-tag>
          </p>
          <p><strong>资源链接:</strong> 
            <a :href="currentResource.fileUrl" target="_blank" class="detail-link">
              {{ currentResource.fileUrl }}
            </a>
          </p>
          <p><strong>创建时间:</strong> {{ currentResource.createTime }}</p>
        </div>
      </div>
    </el-dialog>

    <!-- 添加资源对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="添加学习资源"
      width="600px"
      class="custom-dialog"
    >
      <el-form :model="newResource" label-width="100px" class="add-form">
        <el-form-item label="资源名称">
          <el-input v-model="newResource.title" class="input-field" prefix-icon="Document" />
        </el-form-item>
        <el-form-item label="资源类型">
          <el-select v-model="newResource.type" placeholder="选择类型" class="select-field">
            <el-option label="课程" value="课程" />
            <el-option label="书籍" value="书籍" />
            <el-option label="视频" value="视频" />
            <el-option label="文档" value="文档" />
          </el-select>
        </el-form-item>
        <el-form-item label="资源描述">
          <el-input type="textarea" v-model="newResource.description" class="textarea-field" :rows="3" />
        </el-form-item>
        <el-form-item label="资源链接">
          <el-input v-model="newResource.url" class="input-field" prefix-icon="Link" />
        </el-form-item>
        <el-form-item label="相关专业">
          <el-select v-model="newResource.majorId" placeholder="选择专业" class="select-field">
            <el-option v-for="major in majors" :key="major.id" :label="major.name" :value="major.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="addResource" class="confirm-btn">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, Search, Link, View, Delete, Document } from '@element-plus/icons-vue'

export default {
  components: {
    Plus,
    Search,
    Link,
    View,
    Delete,
    Document
  },
  data() {
    return {
      resources: [],
      currentResource: null,
      dialogVisible: false,
      detailDialogVisible: false,
      searchForm: {
        type: '',
        keyword: ''
      },
      newResource: {
        title: '',
        type: '',
        description: '',
        url: '',
        majorId: ''
      },
      majors: []
    }
  },
  computed: {
    isAdminOrTeacher() {
      const user = localStorage.getItem('user')
      if (user) {
        const userInfo = JSON.parse(user)
        return userInfo.role === 'admin'
      }
      return false
    }
  },
  mounted() {
    this.checkLoginStatus()
  },
  methods: {
    checkLoginStatus() {
      const token = localStorage.getItem('token')
      if (token) {
        this.getResources()
        this.getMajors()
      }
    },
    getResources() {
      const token = localStorage.getItem('token')
      this.$axios.get('/resources/list', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          const data = response.data || []
          console.log('原始资源数据:', data)
          // 去重处理：根据title和category去重
          const uniqueResources = []
          const resourceKeys = new Set()
          data.forEach(resource => {
            const key = resource.title + '-' + resource.category
            if (!resourceKeys.has(key)) {
              resourceKeys.add(key)
              uniqueResources.push(resource)
            }
          })
          console.log('去重后资源数据:', uniqueResources)
          this.resources = uniqueResources
        }).catch(error => {
          console.error('获取资源列表失败:', error)
        })
    },
    getMajors() {
      const token = localStorage.getItem('token')
      this.$axios.get('/majors/list', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.majors = response.data
      }).catch(error => {
        console.error('获取专业列表失败:', error)
      })
    },
    searchResources() {
      const token = localStorage.getItem('token')
      this.$axios.get('/resources/search', {
        params: { keyword: this.searchForm.keyword },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        const data = response.data || []
        // 去重处理：根据title和category去重
        const uniqueResources = []
        const resourceKeys = new Set()
        data.forEach(resource => {
          const key = resource.title + '-' + resource.category
          if (!resourceKeys.has(key)) {
            resourceKeys.add(key)
            uniqueResources.push(resource)
          }
        })
        this.resources = uniqueResources
      }).catch(error => {
        console.error('搜索资源失败:', error)
      })
    },
    viewResource(resource) {
      this.currentResource = resource
      this.detailDialogVisible = true
    },
    deleteResource(resourceId) {
      this.$confirm('确定要删除这个资源吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const token = localStorage.getItem('token')
        this.$axios.delete(`/resources/${resourceId}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          this.$message.success('资源删除成功')
          this.getResources()
        }).catch(error => {
          console.error('删除资源失败:', error)
          this.$message.error('删除资源失败')
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },
    addResource() {
      const token = localStorage.getItem('token')
      const formData = new FormData()
      formData.append('title', this.newResource.title)
      formData.append('description', this.newResource.description)
      formData.append('category', this.newResource.type)
      formData.append('tags', '')
      formData.append('uploaderId', JSON.parse(localStorage.getItem('user')).id)
      formData.append('uploaderName', JSON.parse(localStorage.getItem('user')).name || JSON.parse(localStorage.getItem('user')).username)
      formData.append('schoolId', JSON.parse(localStorage.getItem('school')).id)
      formData.append('studentStage', '大学') // 添加学生阶段参数
      
      this.$axios.post('/resources/upload', formData, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'multipart/form-data'
        }
      }).then(response => {
        this.$message.success('资源上传成功')
        this.dialogVisible = false
        this.getResources()
      }).catch(error => {
        console.error('上传资源失败:', error)
        this.$message.error('上传资源失败')
      })
    },
    getTypeTagType(type) {
      switch (type) {
        case '课程':
          return 'primary'
        case '书籍':
          return 'success'
        case '视频':
          return 'warning'
        case '文档':
          return 'info'
        default:
          return 'default'
      }
    }
  }
}
</script>

<style scoped>
.resources-container {
  padding: 0;
  min-height: 100%;
  background-color: #f8f9fa;
}

.resources-card {
  background: white;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
  margin: 0;
  border-radius: 0;
  box-shadow: none;
}

.resources-card:hover {
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

.search-form {
  padding: 20px 30px;
  background: #f8f9fa;
  margin: 0;
  border-bottom: 1px solid #e4e7ed;
}

.input-field,
.select-field,
.textarea-field {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  width: 200px;
}

.input-field:focus,
.select-field:focus,
.textarea-field:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.search-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.search-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.resources-table {
  margin: 0;
  border-radius: 0;
  overflow: hidden;
}

.el-table th {
  background-color: #f5f7fa;
  font-weight: bold;
  color: #333;
}

.resource-title {
  font-weight: 500;
  color: #333;
  transition: all 0.3s ease;
}

.resource-title:hover {
  color: #667eea;
}

.resource-description {
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.type-tag {
  border-radius: 12px;
  padding: 2px 12px;
  font-size: 12px;
}

.resource-link {
  color: #667eea;
  text-decoration: none;
  transition: all 0.3s ease;
  display: inline-flex;
  align-items: center;
  gap: 5px;
}

.resource-link:hover {
  color: #764ba2;
  text-decoration: underline;
}

.view-btn,
.delete-btn {
  border-radius: 6px;
  transition: all 0.3s ease;
}

.view-btn {
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
}

.view-btn:hover {
  background-color: #e0e0e0;
}

.delete-btn {
  border: none;
}

.delete-btn:hover {
  transform: scale(1.05);
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

.resource-detail {
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

.detail-link {
  color: #667eea;
  text-decoration: none;
  transition: all 0.3s ease;
}

.detail-link:hover {
  color: #764ba2;
  text-decoration: underline;
}

.add-form {
  animation: fadeIn 0.3s ease;
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
  .resources-container {
    padding: 20px;
  }
  
  .input-field,
  .select-field {
    width: 150px;
  }
  
  .resources-table {
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
  
  .search-form {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }
  
  .input-field,
  .select-field {
    width: 100%;
  }
  
  .custom-dialog {
    width: 95% !important;
  }
  
  .custom-dialog .el-dialog__body {
    padding: 20px;
  }
}
</style>