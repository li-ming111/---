<template>
  <div class="admin-container">
    <h2 class="page-title">用户管理</h2>
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">用户列表</span>
          <div class="card-actions">
            <el-input v-model="searchQuery" placeholder="搜索用户" class="search-input" prefix-icon="el-icon-search"></el-input>
            <el-button type="primary" @click="openAddUserDialog">
              <i class="el-icon-plus"></i>
              <span>添加用户</span>
            </el-button>
          </div>
        </div>
      </template>
      <el-table :data="filteredUsers" style="width: 100%">
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
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="editUser(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteUser(scope.row.id)">删除</el-button>
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
          :total="totalUsers"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 添加/编辑用户对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
    >
      <el-form :model="userForm" :rules="rules" ref="userForm">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="userForm.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!editingUser">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择角色">
            <el-option label="学生" value="student"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="userForm.status" placeholder="请选择状态">
            <el-option label="活跃" value="active"></el-option>
            <el-option label="禁用" value="inactive"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveUser">确定</el-button>
      </span>
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
      totalUsers: 0,
      dialogVisible: false,
      dialogTitle: '添加用户',
      editingUser: null,
      userForm: {
        username: '',
        name: '',
        password: '',
        role: 'student',
        status: 'active'
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        role: [{ required: true, message: '请选择角色', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'blur' }]
      },
      users: []
    }
  },
  mounted() {
    this.getUsers()
  },
  computed: {
    filteredUsers() {
      let result = this.users
      if (this.searchQuery) {
        result = result.filter(user => 
          user.username.includes(this.searchQuery) || 
          user.name.includes(this.searchQuery)
        )
      }
      this.totalUsers = result.length
      return result.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  methods: {
    getRoleType(role) {
      switch (role) {
        case 'teacher': return 'primary'
        case 'student': return 'success'
        default: return 'info'
      }
    },
    getRoleName(role) {
      switch (role) {
        case 'teacher': return '教师'
        case 'student': return '学生'
        default: return '未知'
      }
    },
    openAddUserDialog() {
      this.editingUser = null
      this.dialogTitle = '添加用户'
      this.userForm = {
        username: '',
        name: '',
        password: '',
        role: 'student',
        status: 'active'
      }
      this.dialogVisible = true
    },
    editUser(user) {
      this.editingUser = user
      this.dialogTitle = '编辑用户'
      this.userForm = {
        username: user.username,
        name: user.name,
        password: '',
        role: user.role,
        status: user.status
      }
      this.dialogVisible = true
    },
    saveUser() {
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          const token = localStorage.getItem('token')
          const school = localStorage.getItem('school')
          if (!school) {
            this.$message.error('未找到学校信息')
            return
          }
          const schoolData = JSON.parse(school)
          const userData = {
            ...this.userForm,
            schoolId: schoolData.id
          }
          
          if (this.editingUser) {
            // 编辑用户
            userData.id = this.editingUser.id
            this.$axios.put('/users/update', userData, {
              headers: {
                'Authorization': `Bearer ${token}`
              }
            }).then(response => {
              this.$message.success('用户编辑成功')
              this.getUsers()
              this.dialogVisible = false
            }).catch(error => {
              console.error('编辑用户失败:', error)
              this.$message.error('编辑用户失败')
            })
          } else {
            // 添加用户
            this.$axios.post('/auth/register', userData, {
              headers: {
                'Authorization': `Bearer ${token}`
              }
            }).then(response => {
              this.$message.success('用户添加成功')
              this.getUsers()
              this.dialogVisible = false
            }).catch(error => {
              console.error('添加用户失败:', error)
              this.$message.error('添加用户失败')
            })
          }
        }
      })
    },
    deleteUser(id) {
      this.$confirm('确定要删除该用户吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const token = localStorage.getItem('token')
        this.$axios.delete(`/users/${id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          this.$message.success('用户删除成功')
          this.getUsers()
        }).catch(error => {
          console.error('删除用户失败:', error)
          this.$message.error('删除用户失败')
        })
      }).catch(() => {
        // 取消删除
      })
    },
    getUsers() {
      const token = localStorage.getItem('token')
      const school = localStorage.getItem('school')
      if (!school) {
        this.$message.error('未找到学校信息')
        return
      }
      const schoolData = JSON.parse(school)
      this.$axios.get(`/users/school/${schoolData.id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.users = response.data || []
      }).catch(error => {
        console.error('获取用户列表失败:', error)
        this.$message.error('获取用户列表失败')
        this.users = []
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

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>