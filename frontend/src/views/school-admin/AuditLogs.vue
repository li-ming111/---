<template>
  <div class="admin-container">
    <h2 class="page-title">操作日志</h2>
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">操作日志</span>
          <div class="card-actions">
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              class="date-picker"
            ></el-date-picker>
            <el-select v-model="operationType" placeholder="操作类型" class="select-input">
              <el-option label="全部" value=""></el-option>
              <el-option label="登录" value="login"></el-option>
              <el-option label="退出" value="logout"></el-option>
              <el-option label="添加" value="add"></el-option>
              <el-option label="编辑" value="edit"></el-option>
              <el-option label="删除" value="delete"></el-option>
              <el-option label="修改设置" value="settings"></el-option>
            </el-select>
            <el-input v-model="operatorName" placeholder="操作人" class="search-input"></el-input>
            <el-button type="primary" @click="searchLogs">
              <i class="el-icon-search"></i>
              <span>查询</span>
            </el-button>
            <el-button type="success" @click="exportLogs">
              <i class="el-icon-download"></i>
              <span>导出</span>
            </el-button>
          </div>
        </div>
      </template>
      <el-table :data="filteredLogs" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="operatorName" label="操作人"></el-table-column>
        <el-table-column prop="operationType" label="操作类型">
          <template #default="scope">
            <el-tag :type="getOperationType(scope.row.operationType)">{{ getOperationTypeName(scope.row.operationType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationContent" label="操作内容"></el-table-column>
        <el-table-column prop="ipAddress" label="IP地址"></el-table-column>
        <el-table-column prop="createdAt" label="操作时间" width="180"></el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="totalLogs"
        ></el-pagination>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dateRange: [],
      operationType: '',
      operatorName: '',
      currentPage: 1,
      pageSize: 10,
      totalLogs: 0,
      logs: []
    }
  },
  mounted() {
    this.getLogs()
  },
  computed: {
    filteredLogs() {
      let result = this.logs
      if (this.dateRange && this.dateRange.length === 2) {
        const startDate = this.dateRange[0]
        const endDate = this.dateRange[1]
        result = result.filter(log => {
          const logDate = new Date(log.createdAt)
          return logDate >= startDate && logDate <= endDate
        })
      }
      if (this.operationType) {
        result = result.filter(log => log.operationType === this.operationType)
      }
      if (this.operatorName) {
        result = result.filter(log => log.operatorName.includes(this.operatorName))
      }
      this.totalLogs = result.length
      return result.slice((this.currentPage - 1) * this.pageSize, this.currentPage * this.pageSize)
    }
  },
  methods: {
    getOperationType(type) {
      switch (type) {
        case 'login': return 'primary'
        case 'logout': return 'info'
        case 'add': return 'success'
        case 'edit': return 'warning'
        case 'delete': return 'danger'
        case 'settings': return 'info'
        default: return 'info'
      }
    },
    getOperationTypeName(type) {
      switch (type) {
        case 'login': return '登录'
        case 'logout': return '退出'
        case 'add': return '添加'
        case 'edit': return '编辑'
        case 'delete': return '删除'
        case 'settings': return '修改设置'
        default: return '未知'
      }
    },
    searchLogs() {
      this.currentPage = 1
      this.getLogs()
    },
    exportLogs() {
      const token = localStorage.getItem('token')
      this.$axios.get('/audit-logs/export', {
        params: {
          startDate: this.dateRange[0] ? this.dateRange[0].toISOString() : '',
          endDate: this.dateRange[1] ? this.dateRange[1].toISOString() : '',
          operationType: this.operationType,
          operatorName: this.operatorName
        },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.$message.success('日志导出成功')
      }).catch(error => {
        console.error('导出日志失败:', error)
        this.$message.error('导出日志失败')
      })
    },
    getLogs() {
      const token = localStorage.getItem('token')
      const school = localStorage.getItem('school')
      if (!school) {
        this.$message.error('未找到学校信息')
        return
      }
      const schoolData = JSON.parse(school)
      this.$axios.get('/audit-logs', {
        params: {
          schoolId: schoolData.id,
          startDate: this.dateRange[0] ? this.dateRange[0].toISOString() : '',
          endDate: this.dateRange[1] ? this.dateRange[1].toISOString() : '',
          operationType: this.operationType,
          operatorName: this.operatorName,
          page: this.currentPage,
          pageSize: this.pageSize
        },
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.logs = response.data || []
        this.totalLogs = this.logs.length
      }).catch(error => {
        console.error('获取日志失败:', error)
        this.$message.error('获取日志失败')
        this.logs = []
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

.date-picker {
  width: 240px;
}

.select-input {
  width: 120px;
}

.search-input {
  width: 180px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 1200px) {
  .card-actions {
    flex-wrap: wrap;
  }
}
</style>