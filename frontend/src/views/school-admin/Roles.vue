<template>
  <div class="admin-container">
    <h2 class="page-title">角色管理</h2>
    
    <!-- 角色列表 -->
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">角色列表</span>
          <el-button type="primary" @click="openAddRoleDialog">
            <i class="el-icon-plus"></i>
            <span>添加角色</span>
          </el-button>
        </div>
      </template>
      <el-table :data="roles" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="name" label="角色名称"></el-table-column>
        <el-table-column prop="description" label="角色描述"></el-table-column>
        <el-table-column prop="schoolId" label="所属学校"></el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="editRole(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteRole(scope.row.id)">删除</el-button>
            <el-button size="small" @click="assignPermissions(scope.row)">分配权限</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑角色对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="500px"
    >
      <el-form :model="roleForm" :rules="rules" ref="roleForm">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" placeholder="请输入角色名称"></el-input>
        </el-form-item>
        <el-form-item label="角色描述" prop="description">
          <el-input v-model="roleForm.description" type="textarea" placeholder="请输入角色描述" :rows="4"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRole">确定</el-button>
      </span>
    </el-dialog>

    <!-- 分配权限对话框 -->
    <el-dialog
      title="分配权限"
      :visible.sync="permissionDialogVisible"
      width="600px"
    >
      <el-form>
        <el-form-item label="角色名称">
          <el-input v-model="currentRole.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="权限列表">
          <el-checkbox-group v-model="selectedPermissions">
            <el-checkbox v-for="permission in permissions" :key="permission.id" :label="permission.id">
              {{ permission.name }} - {{ permission.description }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="savePermissions">保存权限</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dialogVisible: false,
      permissionDialogVisible: false,
      dialogTitle: '添加角色',
      editingRole: null,
      currentRole: {},
      roleForm: {
        name: '',
        description: ''
      },
      rules: {
        name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入角色描述', trigger: 'blur' }]
      },
      roles: [],
      permissions: [],
      selectedPermissions: []
    }
  },
  mounted() {
    this.getRoles()
    this.getPermissions()
  },
  methods: {
    openAddRoleDialog() {
      this.editingRole = null
      this.dialogTitle = '添加角色'
      this.roleForm = {
        name: '',
        description: ''
      }
      this.dialogVisible = true
    },
    editRole(role) {
      this.editingRole = role
      this.dialogTitle = '编辑角色'
      this.roleForm = {
        name: role.name,
        description: role.description
      }
      this.dialogVisible = true
    },
    saveRole() {
      this.$refs.roleForm.validate((valid) => {
        if (valid) {
          const token = localStorage.getItem('token')
          const school = localStorage.getItem('school')
          if (!school) {
            this.$message.error('未找到学校信息')
            return
          }
          const schoolData = JSON.parse(school)
          const role = {
            ...this.roleForm,
            schoolId: schoolData.id
          }
          
          if (this.editingRole) {
            // 编辑角色
            role.id = this.editingRole.id
            this.$axios.put(`/roles/${role.id}`, role, {
              headers: {
                'Authorization': `Bearer ${token}`
              }
            }).then(response => {
              this.$message.success('角色编辑成功')
              this.getRoles()
              this.dialogVisible = false
            }).catch(error => {
              console.error('编辑角色失败:', error)
              this.$message.error('编辑角色失败')
            })
          } else {
            // 添加角色
            this.$axios.post('/roles', role, {
              headers: {
                'Authorization': `Bearer ${token}`
              }
            }).then(response => {
              this.$message.success('角色添加成功')
              this.getRoles()
              this.dialogVisible = false
            }).catch(error => {
              console.error('添加角色失败:', error)
              this.$message.error('添加角色失败')
            })
          }
        }
      })
    },
    deleteRole(id) {
      this.$confirm('确定要删除该角色吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const token = localStorage.getItem('token')
        this.$axios.delete(`/roles/${id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          this.$message.success('角色删除成功')
          this.getRoles()
        }).catch(error => {
          console.error('删除角色失败:', error)
          this.$message.error('删除角色失败')
        })
      }).catch(() => {
        // 取消删除
      })
    },
    assignPermissions(role) {
      this.currentRole = role
      this.permissionDialogVisible = true
      this.getSelectedPermissions(role.id)
    },
    savePermissions() {
      const token = localStorage.getItem('token')
      this.$axios.post(`/roles/${this.currentRole.id}/permissions`, this.selectedPermissions, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('权限分配成功')
        this.permissionDialogVisible = false
      }).catch(error => {
        console.error('分配权限失败:', error)
        this.$message.error('分配权限失败')
      })
    },
    getRoles() {
      const token = localStorage.getItem('token')
      const school = localStorage.getItem('school')
      if (!school) {
        this.$message.error('未找到学校信息')
        return
      }
      const schoolData = JSON.parse(school)
      this.$axios.get(`/roles/school/${schoolData.id}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.roles = response.data || []
      }).catch(error => {
        console.error('获取角色列表失败:', error)
        this.$message.error('获取角色列表失败')
        this.roles = []
      })
    },
    getPermissions() {
      const token = localStorage.getItem('token')
      this.$axios.get('/roles/permissions', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.permissions = response.data || []
      }).catch(error => {
        console.error('获取权限列表失败:', error)
        this.$message.error('获取权限列表失败')
        this.permissions = []
      })
    },
    getSelectedPermissions(roleId) {
      const token = localStorage.getItem('token')
      this.$axios.get(`/roles/${roleId}/permissions`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.selectedPermissions = response.data || []
      }).catch(error => {
        console.error('获取角色权限失败:', error)
        this.selectedPermissions = []
      })
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
</style>