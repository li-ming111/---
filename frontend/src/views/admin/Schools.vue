<template>
  <div class="admin-container">
    <h2 class="page-title">学校管理</h2>
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">学校列表</span>
          <div class="card-actions">
            <el-input v-model="searchQuery" placeholder="搜索学校" class="search-input" prefix-icon="el-icon-search"></el-input>
            <el-button type="primary" @click="addSchool" class="add-button">
              <i class="el-icon-plus"></i>
              <span>添加学校</span>
            </el-button>
          </div>
        </div>
      </template>
      <el-table :data="filteredSchools" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="code" label="学校代码"></el-table-column>
        <el-table-column prop="name" label="学校名称"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>
        <el-table-column prop="contact" label="联系电话"></el-table-column>
        <el-table-column prop="email" label="邮箱"></el-table-column>
        <el-table-column prop="website" label="网站"></el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="editSchool(scope.row)">编辑</el-button>
            <el-button size="small" type="danger" @click="deleteSchool(scope.row.id)">删除</el-button>
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
          :total="totalSchools"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 添加/编辑学校对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
    >
      <el-form :model="schoolForm" :rules="rules" ref="schoolForm">
        <el-form-item prop="code">
          <el-input v-model="schoolForm.code" placeholder="请输入学校代码"></el-input>
        </el-form-item>
        <el-form-item prop="name">
          <el-input v-model="schoolForm.name" placeholder="请输入学校名称"></el-input>
        </el-form-item>
        <el-form-item prop="address">
          <el-input v-model="schoolForm.address" placeholder="请输入学校地址"></el-input>
        </el-form-item>
        <el-form-item prop="contact">
          <el-input v-model="schoolForm.contact" placeholder="请输入联系电话"></el-input>
        </el-form-item>
        <el-form-item prop="email">
          <el-input v-model="schoolForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
        <el-form-item prop="website">
          <el-input v-model="schoolForm.website" placeholder="请输入网站"></el-input>
        </el-form-item>
        <el-form-item prop="description">
          <el-input type="textarea" v-model="schoolForm.description" placeholder="请输入学校描述"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveSchool">确定</el-button>
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
      totalSchools: 0,
      schools: [],
      dialogVisible: false,
      isAddMode: true,
      dialogTitle: '添加学校',
      schoolForm: {
        code: '',
        name: '',
        address: '',
        contact: '',
        email: '',
        website: '',
        description: ''
      },
      rules: {
        code: [{ required: true, message: '请输入学校代码', trigger: 'blur' }],
        name: [{ required: true, message: '请输入学校名称', trigger: 'blur' }],
        address: [{ required: true, message: '请输入学校地址', trigger: 'blur' }],
        contact: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
        email: [{ required: true, message: '请输入邮箱', trigger: 'blur' }],
        website: [{ required: true, message: '请输入网站', trigger: 'blur' }],
        description: [{ required: true, message: '请输入学校描述', trigger: 'blur' }]
      }
    }
  },
  computed: {
    filteredSchools() {
      let result = this.schools
      if (this.searchQuery) {
        result = result.filter(school => 
          school.code.includes(this.searchQuery) || 
          school.name.includes(this.searchQuery) ||
          school.address.includes(this.searchQuery) ||
          school.contact.includes(this.searchQuery)
        )
      }
      this.totalSchools = result.length
      return result.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  methods: {
    getSchools() {
      const token = localStorage.getItem('token')
      this.$axios.get('/school/list', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Accept': 'application/json; charset=utf-8'
        },
        responseType: 'json'
      }).then(response => {
        this.schools = response.data
      }).catch(error => {
        console.error('获取学校列表失败:', error)
      })
    },
    addSchool() {
      this.isAddMode = true
      this.dialogTitle = '添加学校'
      this.schoolForm = {
        code: '',
        name: '',
        address: '',
        contact: '',
        email: '',
        website: '',
        description: ''
      }
      this.dialogVisible = true
    },
    editSchool(school) {
      this.isAddMode = false
      this.dialogTitle = '编辑学校'
      this.schoolForm = { ...school }
      this.dialogVisible = true
    },
    saveSchool() {
      const token = localStorage.getItem('token')
      this.$refs.schoolForm.validate((valid) => {
        if (valid) {
          if (this.isAddMode) {
            // 添加新学校
            this.$axios.post('/school/add', this.schoolForm, {
              headers: {
                'Authorization': `Bearer ${token}`
              }
            }).then(response => {
              this.$message.success('学校添加成功')
              this.dialogVisible = false
              this.getSchools()
            }).catch(error => {
              console.error('添加学校失败:', error)
              this.$message.error('添加学校失败')
            })
          } else {
            // 编辑现有学校
            this.$axios.put(`/school/${this.schoolForm.id}`, this.schoolForm, {
              headers: {
                'Authorization': `Bearer ${token}`
              }
            }).then(response => {
              this.$message.success('学校编辑成功')
              this.dialogVisible = false
              this.getSchools()
            }).catch(error => {
              console.error('编辑学校失败:', error)
              this.$message.error('编辑学校失败')
            })
          }
        }
      })
    },
    deleteSchool(id) {
      const token = localStorage.getItem('token')
      this.$confirm('确定要删除该学校吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete(`/school/${id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          this.$message.success('学校删除成功')
          this.getSchools()
        }).catch(error => {
          console.error('删除学校失败:', error)
          this.$message.error('删除学校失败')
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
    }
  },
  mounted() {
    this.getSchools()
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