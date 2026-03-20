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
        <el-table-column prop="type" label="类型">
          <template #default="scope">
            <el-tag :type="getResourceType(scope.row.type)">{{ getResourceTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="uploader" label="上传者"></el-table-column>
        <el-table-column prop="schoolName" label="所属学校"></el-table-column>
        <el-table-column prop="uploadTime" label="上传时间" width="180"></el-table-column>
        <el-table-column prop="size" label="大小" width="100"></el-table-column>
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
      resources: [
        {
          id: 1,
          title: '高等数学学习指南',
          type: 'document',
          description: '高等数学课程学习资料',
          uploader: '张老师',
          schoolCode: 'school001',
          schoolName: '清华大学',
          uploadTime: '2026-03-20 10:00:00',
          size: '1.2 MB'
        },
        {
          id: 2,
          title: '大学物理实验视频',
          type: 'video',
          description: '大学物理实验教学视频',
          uploader: '李老师',
          schoolCode: 'school001',
          schoolName: '清华大学',
          uploadTime: '2026-03-19 15:30:00',
          size: '50.5 MB'
        },
        {
          id: 3,
          title: '英语听力练习音频',
          type: 'audio',
          description: '英语听力练习材料',
          uploader: '王老师',
          schoolCode: 'school001',
          schoolName: '清华大学',
          uploadTime: '2026-03-18 09:00:00',
          size: '15.8 MB'
        },
        {
          id: 4,
          title: '校园地图',
          type: 'image',
          description: '校园平面图',
          uploader: '赵老师',
          schoolCode: 'school001',
          schoolName: '清华大学',
          uploadTime: '2026-03-17 14:00:00',
          size: '2.3 MB'
        }
      ]
    }
  },
  computed: {
    filteredResources() {
      let result = this.resources
      if (this.resourceType) {
        result = result.filter(resource => resource.type === this.resourceType)
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
          if (this.editingResource) {
            // 编辑资源
            const index = this.resources.findIndex(r => r.id === this.editingResource.id)
            if (index !== -1) {
              this.resources[index] = {
                ...this.resources[index],
                ...this.resourceForm
              }
              this.$message.success('资源编辑成功')
            }
          } else {
            // 添加资源
            const newResource = {
              id: this.resources.length + 1,
              ...this.resourceForm,
              schoolCode: 'school001',
              schoolName: '清华大学',
              uploadTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
              size: `${(Math.random() * 10 + 1).toFixed(1)} MB`
            }
            this.resources.push(newResource)
            this.$message.success('资源添加成功')
          }
          this.dialogVisible = false
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
        const index = this.resources.findIndex(r => r.id === id)
        if (index !== -1) {
          this.resources.splice(index, 1)
          this.$message.success('资源删除成功')
        }
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