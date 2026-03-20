<template>
  <div class="admin-container">
    <h2 class="page-title">用户批量操作</h2>
    
    <!-- 批量导入导出 -->
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">批量导入导出</span>
        </div>
      </template>
      <div class="import-export-section">
        <div class="import-section">
          <h3>批量导入用户</h3>
          <el-upload
            class="upload-demo"
            action="#"
            :on-change="handleFileChange"
            :auto-upload="false"
            :show-file-list="false"
          >
            <el-button type="primary">
              <i class="el-icon-upload"></i>
              <span>选择文件</span>
            </el-button>
          </el-upload>
          <el-button type="success" @click="importUsers" :disabled="!selectedFile">导入</el-button>
          <el-button @click="downloadTemplate">下载模板</el-button>
          <p class="tip">支持Excel、CSV格式文件导入</p>
        </div>
        <div class="export-section">
          <h3>批量导出用户</h3>
          <el-button type="primary" @click="exportUsers">
            <i class="el-icon-download"></i>
            <span>导出所有用户</span>
          </el-button>
          <el-button @click="exportSelectedUsers" :disabled="selectedUsers.length === 0">
            <i class="el-icon-download"></i>
            <span>导出选中用户</span>
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 批量修改 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">批量修改用户</span>
          <div class="card-actions">
            <el-button type="danger" @click="clearSelection" :disabled="selectedUsers.length === 0">
              <i class="el-icon-delete"></i>
              <span>清空选择</span>
            </el-button>
          </div>
        </div>
      </template>
      <el-form :model="bulkForm" :rules="rules" ref="bulkForm">
        <el-form-item label="用户状态" prop="status">
          <el-select v-model="bulkForm.status" placeholder="请选择状态">
            <el-option label="活跃" value="active"></el-option>
            <el-option label="禁用" value="inactive"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="用户角色" prop="role">
          <el-select v-model="bulkForm.role" placeholder="请选择角色">
            <el-option label="超级管理员" value="super_admin"></el-option>
            <el-option label="学校管理员" value="school_admin"></el-option>
            <el-option label="教师" value="teacher"></el-option>
            <el-option label="学生" value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属学校" prop="schoolCode">
          <el-select v-model="bulkForm.schoolCode" placeholder="请选择学校">
            <el-option v-for="school in schools" :key="school.code" :label="school.name" :value="school.code"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="bulkUpdate" :disabled="selectedUsers.length === 0">批量修改</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 批量发送通知 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">批量发送通知</span>
        </div>
      </template>
      <el-form :model="notificationForm" :rules="notificationRules" ref="notificationForm">
        <el-form-item label="通知标题" prop="title">
          <el-input v-model="notificationForm.title" placeholder="请输入通知标题"></el-input>
        </el-form-item>
        <el-form-item label="通知内容" prop="content">
          <el-input v-model="notificationForm.content" type="textarea" placeholder="请输入通知内容" :rows="4"></el-input>
        </el-form-item>
        <el-form-item label="发送对象">
          <el-radio-group v-model="notificationForm.target">
            <el-radio label="all">所有用户</el-radio>
            <el-radio label="selected">选中用户</el-radio>
            <el-radio label="role">按角色</el-radio>
            <el-radio label="school">按学校</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="notificationForm.target === 'role'" label="选择角色">
          <el-select v-model="notificationForm.targetRole" placeholder="请选择角色">
            <el-option label="超级管理员" value="super_admin"></el-option>
            <el-option label="学校管理员" value="school_admin"></el-option>
            <el-option label="教师" value="teacher"></el-option>
            <el-option label="学生" value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="notificationForm.target === 'school'" label="选择学校">
          <el-select v-model="notificationForm.targetSchool" placeholder="请选择学校">
            <el-option v-for="school in schools" :key="school.code" :label="school.name" :value="school.code"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="sendNotification">发送通知</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 用户列表 -->
    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">用户列表</span>
          <div class="card-actions">
            <el-input v-model="searchQuery" placeholder="搜索用户" class="search-input" prefix-icon="el-icon-search"></el-input>
            <el-button type="primary" @click="selectAll">全选</el-button>
          </div>
        </div>
      </template>
      <el-table :data="filteredUsers" style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="username" label="用户名"></el-table-column>
        <el-table-column prop="name" label="姓名"></el-table-column>
        <el-table-column prop="role" label="角色">
          <template #default="scope">
            <el-tag :type="getRoleType(scope.row.role)">{{ getRoleName(scope.row.role) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="schoolName" label="所属学校"></el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'active' ? 'success' : 'danger'">
              {{ scope.row.status === 'active' ? '活跃' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      selectedFile: null,
      selectedUsers: [],
      searchQuery: '',
      bulkForm: {
        status: '',
        role: '',
        schoolCode: ''
      },
      notificationForm: {
        title: '',
        content: '',
        target: 'all',
        targetRole: '',
        targetSchool: ''
      },
      rules: {
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'blur' }],
        schoolCode: [{ required: true, message: '请选择学校', trigger: 'blur' }]
      },
      notificationRules: {
        title: [{ required: true, message: '请输入通知标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入通知内容', trigger: 'blur' }]
      },
      users: [
        {
          id: 1,
          username: 'admin',
          name: '超级管理员',
          role: 'super_admin',
          schoolCode: '',
          schoolName: '系统',
          status: 'active'
        },
        {
          id: 2,
          username: 'school_admin',
          name: '学校管理员',
          role: 'school_admin',
          schoolCode: 'school001',
          schoolName: '清华大学',
          status: 'active'
        },
        {
          id: 3,
          username: 'teacher001',
          name: '张老师',
          role: 'teacher',
          schoolCode: 'school001',
          schoolName: '清华大学',
          status: 'active'
        },
        {
          id: 4,
          username: 'student001',
          name: '李同学',
          role: 'student',
          schoolCode: 'school001',
          schoolName: '清华大学',
          status: 'active'
        }
      ],
      schools: [
        { code: 'school001', name: '清华大学' },
        { code: 'school002', name: '北京大学' },
        { code: 'school003', name: '复旦大学' }
      ]
    }
  },
  computed: {
    filteredUsers() {
      let result = this.users
      if (this.searchQuery) {
        result = result.filter(user => 
          user.username.includes(this.searchQuery) || 
          user.name.includes(this.searchQuery) ||
          user.schoolName.includes(this.searchQuery)
        )
      }
      return result
    }
  },
  methods: {
    getRoleType(role) {
      switch (role) {
        case 'super_admin': return 'danger'
        case 'school_admin': return 'warning'
        case 'teacher': return 'primary'
        case 'student': return 'success'
        default: return 'info'
      }
    },
    getRoleName(role) {
      switch (role) {
        case 'super_admin': return '超级管理员'
        case 'school_admin': return '学校管理员'
        case 'teacher': return '教师'
        case 'student': return '学生'
        default: return '未知'
      }
    },
    handleFileChange(file) {
      this.selectedFile = file
    },
    importUsers() {
      if (!this.selectedFile) {
        this.$message.error('请选择文件')
        return
      }
      // 模拟导入操作
      this.$message.success('用户导入成功')
      this.selectedFile = null
    },
    downloadTemplate() {
      // 模拟下载模板
      this.$message.success('模板下载成功')
    },
    exportUsers() {
      // 模拟导出所有用户
      this.$message.success('所有用户导出成功')
    },
    exportSelectedUsers() {
      if (this.selectedUsers.length === 0) {
        this.$message.error('请选择用户')
        return
      }
      // 模拟导出选中用户
      this.$message.success('选中用户导出成功')
    },
    bulkUpdate() {
      if (this.selectedUsers.length === 0) {
        this.$message.error('请选择用户')
        return
      }
      this.$refs.bulkForm.validate((valid) => {
        if (valid) {
          // 模拟批量修改
          this.$message.success('批量修改成功')
        }
      })
    },
    sendNotification() {
      this.$refs.notificationForm.validate((valid) => {
        if (valid) {
          // 模拟发送通知
          this.$message.success('通知发送成功')
        }
      })
    },
    handleSelectionChange(val) {
      this.selectedUsers = val
    },
    selectAll() {
      this.$refs.table?.toggleAllSelection()
    },
    clearSelection() {
      this.$refs.table?.clearSelection()
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

.mt-20 {
  margin-top: 20px;
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

.import-export-section {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
  padding: 20px;
}

.import-section,
.export-section {
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.import-section h3,
.export-section h3 {
  margin-bottom: 20px;
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.tip {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .import-export-section {
    grid-template-columns: 1fr;
  }
}
</style>