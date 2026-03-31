<template>
  <div class="admin-container">
    <h2 class="page-title">资源管理</h2>
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">资源列表</span>
          <div class="card-actions">
            <el-select v-model="resourceType" placeholder="资源类型" class="select-input">
              <el-option label="全部" value=""></el-option>
              <el-option label="文档" value="document"></el-option>
              <el-option label="视频" value="video"></el-option>
              <el-option label="音频" value="audio"></el-option>
              <el-option label="图片" value="image"></el-option>
            </el-select>
            <el-input v-model="searchQuery" placeholder="搜索资源" class="search-input" prefix-icon="el-icon-search"></el-input>
            <el-button type="primary" @click="openAddResourceDialog">
              <i class="el-icon-plus"></i>
              <span>添加资源</span>
            </el-button>
          </div>
        </div>
      </template>
      <el-table :data="filteredResources" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="标题"></el-table-column>
        <el-table-column prop="category" label="类型">
          <template #default="scope">
            <el-tag :type="getResourceType(scope.row.category)">{{ getResourceTypeName(scope.row.category) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="uploaderName" label="上传者"></el-table-column>
        <el-table-column prop="schoolId" label="所属学校"></el-table-column>
        <el-table-column prop="createTime" label="上传时间" width="180"></el-table-column>
        <el-table-column prop="fileSize" label="大小" width="100"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewResource(scope.row.id)">查看</el-button>
            <el-button size="small" type="danger" @click="deleteResource(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalResources"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 添加/编辑资源对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
    >
      <el-form :model="resourceForm" :rules="rules" ref="resourceForm">
        <el-form-item label="资源标题" prop="title">
          <el-input v-model="resourceForm.title" placeholder="请输入资源标题"></el-input>
        </el-form-item>
        <el-form-item label="资源类型" prop="type">
          <el-select v-model="resourceForm.type" placeholder="请选择资源类型">
            <el-option label="文档" value="document"></el-option>
            <el-option label="视频" value="video"></el-option>
            <el-option label="音频" value="audio"></el-option>
            <el-option label="图片" value="image"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="资源描述" prop="description">
          <el-input v-model="resourceForm.description" type="textarea" placeholder="请输入资源描述" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="上传者" prop="uploader">
          <el-input v-model="resourceForm.uploader" placeholder="请输入上传者"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveResource">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: '',
      resourceType: '',
      currentPage: 1,
      pageSize: 10,
      totalResources: 0,
      dialogVisible: false,
      dialogTitle: '添加资源',
      editingResource: null,
      resourceForm: {
        title: '',
        type: 'document',
        description: '',
        uploader: ''
      },
      rules: {
        title: [{ required: true, message: '请输入资源标题', trigger: 'blur' }],
        type: [{ required: true, message: '请选择资源类型', trigger: 'blur' }],
        description: [{ required: true, message: '请输入资源描述', trigger: 'blur' }],
        uploader: [{ required: true, message: '请输入上传者', trigger: 'blur' }]
      },
      resources: []
    }
  },
  mounted() {
    this.getResources()
  },
  computed: {
    filteredResources() {
      let result = this.resources
      if (this.resourceType) {
        result = result.filter(resource => resource.category === this.resourceType)
      }
      if (this.searchQuery) {
        result = result.filter(resource => 
          resource.title.includes(this.searchQuery) || 
          resource.description.includes(this.searchQuery)
        )
      }
      this.totalResources = result.length
      return result.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  methods: {
    getResourceType(type) {
      switch (type) {
        case 'document': return 'primary'
        case 'video': return 'warning'
        case 'audio': return 'success'
        case 'image': return 'danger'
        default: return 'info'
      }
    },
    getResourceTypeName(type) {
      switch (type) {
        case 'document': return '文档'
        case 'video': return '视频'
        case 'audio': return '音频'
        case 'image': return '图片'
        default: return '未知'
      }
    },
    openAddResourceDialog() {
      this.editingResource = null
      this.dialogTitle = '添加资源'
      this.resourceForm = {
        title: '',
        type: 'document',
        description: '',
        uploader: ''
      }
      this.dialogVisible = true
    },
    saveResource() {
      this.$refs.resourceForm.validate((valid) => {
        if (valid) {
          const token = localStorage.getItem('token')
          const user = localStorage.getItem('user')
          const school = localStorage.getItem('school')
          
          if (!user || !school) {
            this.$message.error('未找到用户或学校信息')
            return
          }
          
          const userInfo = JSON.parse(user)
          const schoolInfo = JSON.parse(school)
          
          if (this.editingResource) {
            // 编辑资源
            const formData = new FormData()
            formData.append('id', this.editingResource.id)
            formData.append('title', this.resourceForm.title)
            formData.append('description', this.resourceForm.description)
            formData.append('category', this.resourceForm.type)
            formData.append('tags', '')
            formData.append('uploaderId', userInfo.id)
            formData.append('uploaderName', userInfo.name || userInfo.username)
            formData.append('schoolId', schoolInfo.id)
            formData.append('file', null) // 由于没有文件上传，添加一个空文件
            
            this.$axios.put('/resources/update', formData, {
              headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'
              }
            }).then(response => {
              this.$message.success('资源编辑成功')
              this.getResources()
              this.dialogVisible = false
            }).catch(error => {
              console.error('编辑资源失败:', error)
              this.$message.error('编辑资源失败')
            })
          } else {
            // 添加资源
            const formData = new FormData()
            formData.append('title', this.resourceForm.title)
            formData.append('description', this.resourceForm.description)
            formData.append('category', this.resourceForm.type)
            formData.append('tags', '')
            formData.append('uploaderId', userInfo.id)
            formData.append('uploaderName', userInfo.name || userInfo.username)
            formData.append('schoolId', schoolInfo.id)
            formData.append('file', null) // 由于没有文件上传，添加一个空文件
            
            this.$axios.post('/resources/upload', formData, {
              headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'multipart/form-data'
              }
            }).then(response => {
              this.$message.success('资源添加成功')
              this.getResources()
              this.dialogVisible = false
            }).catch(error => {
              console.error('添加资源失败:', error)
              this.$message.error('添加资源失败')
            })
          }
        }
      })
    },
    viewResource(id) {
      // 模拟查看资源
      this.$message.info('查看资源功能开发中')
    },
    deleteResource(id) {
      this.$confirm('确定要删除该资源吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const token = localStorage.getItem('token')
        this.$axios.delete(`/resources/${id}`, {
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
        // 取消删除
      })
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
    },
    handleCurrentChange(current) {
      this.currentPage = current
    },
    getResources() {
      const token = localStorage.getItem('token')
      const school = localStorage.getItem('school')
      if (!school) {
        this.$message.error('未找到学校信息')
        return
      }
      try {
        const schoolData = JSON.parse(school)
        if (!schoolData || !schoolData.id) {
          this.$message.error('学校信息格式错误')
          return
        }
        const schoolId = schoolData.id
        console.log('School ID:', schoolId)
        
        const apiUrl = `/resources/school/${schoolId}`
        
        this.$axios.get(apiUrl, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          this.resources = response.data || []
        }).catch(error => {
          console.error('获取资源列表失败:', error)
          this.$message.error('获取资源列表失败')
          this.resources = []
        })
      } catch (error) {
        console.error('解析学校信息失败:', error)
        this.$message.error('解析学校信息失败')
        this.resources = []
      }
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

.admin-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
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

.card-actions {
  display: flex;
  gap: 10px;
  align-items: center;
}

.select-input {
  width: 120px;
}

.search-input {
  width: 200px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>