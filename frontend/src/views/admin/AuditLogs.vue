<template>
  <div class="admin-container">
    <h2 class="page-title">审计日志管理</h2>
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
            <el-input v-model="adminName" placeholder="管理员名称" class="search-input"></el-input>
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
        <el-table-column prop="adminName" label="管理员"></el-table-column>
        <el-table-column prop="operationType" label="操作类型">
          <template #default="scope">
            <el-tag :type="getOperationType(scope.row.operationType)">{{ getOperationTypeName(scope.row.operationType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operationContent" label="操作内容"></el-table-column>
        <el-table-column prop="ipAddress" label="IP地址"></el-table-column>
        <el-table-column prop="userAgent" label="用户代理"></el-table-column>
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
      adminName: '',
      currentPage: 1,
      pageSize: 10,
      totalLogs: 0,
      logs: []
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
      this.getAuditLogs()
    },
    exportLogs() {
      const token = localStorage.getItem('token')
      const params = {
        startDate: this.dateRange && this.dateRange.length === 2 ? this.dateRange[0] : '',
        endDate: this.dateRange && this.dateRange.length === 2 ? this.dateRange[1] : '',
        operationType: this.operationType,
        adminName: this.adminName
      }
      this.$axios.post('/audit-logs/export', params, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        const result = response.data
        if (result.success) {
          // 模拟下载
          this.$message.success('日志导出成功')
        }
      }).catch(error => {
        console.error('导出日志失败:', error)
        this.$message.error('导出日志失败')
      })
    },
    handleSizeChange(size) {
      this.pageSize = size
      this.currentPage = 1
      this.getAuditLogs()
    },
    handleCurrentChange(current) {
      this.currentPage = current
      this.getAuditLogs()
    },
    getAuditLogs() {
      const token = localStorage.getItem('token')
      const params = {
        page: this.currentPage,
        size: this.pageSize
      }
      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = this.dateRange[0]
        params.endDate = this.dateRange[1]
      }
      if (this.operationType) {
        params.operationType = this.operationType
      }
      if (this.adminName) {
        params.adminName = this.adminName
      }
      this.$axios.get('/audit-logs/list', {
        params: params,
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        const result = response.data
        this.logs = result.data
        this.totalLogs = result.total
      }).catch(error => {
        console.error('获取审计日志失败:', error)
      })
    }
  },
  mounted() {
    this.getAuditLogs()
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