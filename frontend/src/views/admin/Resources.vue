<template>
  <div class="admin-container">
    <h2 class="page-title">资源管理</h2>
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">资源列表</span>
          <div class="card-actions">
            <el-input v-model="searchQuery" placeholder="搜索资源" class="search-input" prefix-icon="el-icon-search"></el-input>
            <el-button type="primary" @click="addResource" class="add-button">
              <i class="el-icon-plus"></i>
              <span>添加资源</span>
            </el-button>
          </div>
        </div>
      </template>
      <el-table :data="filteredResources" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="title" label="资源标题"></el-table-column>
        <el-table-column prop="type" label="资源类型">
          <template #default="scope">
            <el-tag :type="getResourceType(scope.row.type)">{{ getResourceTypeName(scope.row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类"></el-table-column>
        <el-table-column prop="schoolName" label="所属学校"></el-table-column>
        <el-table-column prop="uploader" label="上传者"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'published' ? 'success' : 'info'">
              {{ scope.row.status === 'published' ? '已发布' : '草稿' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="editResource(scope.row)">编辑</el-button>
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
      width="600px"
    >
      <el-form :model="resourceForm" :rules="rules" ref="resourceForm">
        <el-form-item prop="title">
          <el-input v-model="resourceForm.title" placeholder="请输入资源标题"></el-input>
        </el-form-item>
        <el-form-item prop="type">
          <el-select v-model="resourceForm.type" placeholder="请选择资源类型">
            <el-option label="文档" value="document"></el-option>
            <el-option label="视频" value="video"></el-option>
            <el-option label="音频" value="audio"></el-option>
            <el-option label="图片" value="image"></el-option>
            <el-option label="其他" value="other"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="category">
          <el-input v-model="resourceForm.category" placeholder="请输入资源分类"></el-input>
        </el-form-item>
        <el-form-item prop="schoolCode">
          <el-select v-model="resourceForm.schoolCode" placeholder="请选择所属学校">
            <el-option v-for="school in schools" :key="school.code" :label="school.name" :value="school.code"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="uploader">
          <el-input v-model="resourceForm.uploader" placeholder="请输入上传者"></el-input>
        </el-form-item>
        <el-form-item prop="description">
          <el-input v-model="resourceForm.description" type="textarea" placeholder="请输入资源描述" :rows="3"></el-input>
        </el-form-item>
        <el-form-item prop="status">
          <el-select v-model="resourceForm.status" placeholder="请选择状态">
            <el-option label="已发布" value="published"></el-option>
            <el-option label="草稿" value="draft"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveResource">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      searchQuery: '',
      currentPage: 1,
      pageSize: 10,
      totalResources: 0,
      resources: [
        {
          id: 1,
          title: '高等数学学习指南',
          type: 'document',
          category: '课程资料',
          schoolCode: 'school001',
          schoolName: '清华大学',
          uploader: '张老师',
          description: '高等数学上册学习指南，包含重点难点解析',
          status: 'published',
          createdAt: '2026-01-01 00:00:00'
        },
        {
          id: 2,
          title: '大学物理实验视频',
          type: 'video',
          category: '实验教学',
          schoolCode: 'school001',
          schoolName: '清华大学',
          uploader: '李老师',
          description: '大学物理实验操作视频教程',
          status: 'published',
          createdAt: '2026-01-02 00:00:00'
        },
        {
          id: 3,
          title: '英语听力练习音频',
          type: 'audio',
          category: '语言学习',
          schoolCode: 'school002',
          schoolName: '北京大学',
          uploader: '王老师',
          description: '大学英语四级听力练习音频',
          status: 'published',
          createdAt: '2026-01-03 00:00:00'
        },
        {
          id: 4,
          title: '校园地图',
          type: 'image',
          category: '校园生活',
          schoolCode: 'school003',
          schoolName: '复旦大学',
          uploader: '赵老师',
          description: '复旦大学 campus 地图',
          status: 'draft',
          createdAt: '2026-01-04 00:00:00'
        }
      ],
      schools: [
        { code: 'school001', name: '清华大学' },
        { code: 'school002', name: '北京大学' },
        { code: 'school003', name: '复旦大学' }
      ],
      dialogVisible: false,
      isAddMode: true,
      dialogTitle: '添加资源',
      resourceForm: {
        title: '',
        type: '',
        category: '',
        schoolCode: '',
        uploader: '',
        description: '',
        status: 'published'
      },
      rules: {
        title: [{ required: true, message: '请输入资源标题', trigger: 'blur' }],
        type: [{ required: true, message: '请选择资源类型', trigger: 'blur' }],
        category: [{ required: true, message: '请输入资源分类', trigger: 'blur' }],
        schoolCode: [{ required: true, message: '请选择所属学校', trigger: 'blur' }],
        uploader: [{ required: true, message: '请输入上传者', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }]
      }
    }
  },
  computed: {
    filteredResources() {
      let result = this.resources
      if (this.searchQuery) {
        result = result.filter(resource => 
          resource.title.includes(this.searchQuery) || 
          resource.category.includes(this.searchQuery) ||
          resource.schoolName.includes(this.searchQuery) ||
          resource.uploader.includes(this.searchQuery)
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
        case 'other': return 'info'
        default: return 'info'
      }
    },
    getResourceTypeName(type) {
      switch (type) {
        case 'document': return '文档'
        case 'video': return '视频'
        case 'audio': return '音频'
        case 'image': return '图片'
        case 'other': return '其他'
        default: return '未知'
      }
    },
    addResource() {
      this.isAddMode = true
      this.dialogTitle = '添加资源'
      this.resourceForm = {
        title: '',
        type: '',
        category: '',
        schoolCode: '',
        uploader: '',
        description: '',
        status: 'published'
      }
      this.dialogVisible = true
    },
    editResource(resource) {
      this.isAddMode = false
      this.dialogTitle = '编辑资源'
      this.resourceForm = { ...resource }
      this.dialogVisible = true
    },
    saveResource() {
      this.$refs.resourceForm.validate((valid) => {
        if (valid) {
          if (this.isAddMode) {
            // 添加新资源
            const newResource = {
              id: this.resources.length + 1,
              ...this.resourceForm,
              schoolName: this.schools.find(s => s.code === this.resourceForm.schoolCode)?.name || '',
              createdAt: new Date().toISOString().slice(0, 19).replace('T', ' ')
            }
            this.resources.push(newResource)
            this.$message.success('资源添加成功')
          } else {
            // 编辑现有资源
            const index = this.resources.findIndex(r => r.id === this.resourceForm.id)
            if (index !== -1) {
              this.resources[index] = {
                ...this.resourceForm,
                schoolName: this.schools.find(s => s.code === this.resourceForm.schoolCode)?.name || ''
              }
              this.$message.success('资源编辑成功')
            }
          }
          this.dialogVisible = false
        }
      })
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

.search-input {
  width: 200px;
}

.add-button {
  display: flex;
  align-items: center;
  gap: 5px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>