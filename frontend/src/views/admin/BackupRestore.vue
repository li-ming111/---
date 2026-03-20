<template>
  <div class="admin-container">
    <h2 class="page-title">系统备份与恢复</h2>
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">备份管理</span>
          <div class="card-actions">
            <el-button type="primary" @click="createBackup">
              <i class="el-icon-download"></i>
              <span>手动备份</span>
            </el-button>
          </div>
        </div>
      </template>
      <div class="backup-settings">
        <el-form :model="backupSettings" :rules="rules" ref="backupForm">
          <el-form-item label="自动备份" prop="autoBackup">
            <el-switch v-model="backupSettings.autoBackup"></el-switch>
          </el-form-item>
          <el-form-item label="备份频率" prop="backupFrequency">
            <el-select v-model="backupSettings.backupFrequency" placeholder="请选择备份频率">
              <el-option label="每天" value="daily"></el-option>
              <el-option label="每周" value="weekly"></el-option>
              <el-option label="每月" value="monthly"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备份时间" prop="backupTime">
            <el-time-picker v-model="backupSettings.backupTime" placeholder="选择时间" format="HH:mm" value-format="HH:mm"></el-time-picker>
          </el-form-item>
          <el-form-item label="备份保留数量" prop="backupRetention">
            <el-input v-model="backupSettings.backupRetention" placeholder="请输入备份保留数量" type="number"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveBackupSettings">保存设置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">备份历史</span>
        </div>
      </template>
      <el-table :data="backups" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="backupName" label="备份名称"></el-table-column>
        <el-table-column prop="backupType" label="备份类型">
          <template #default="scope">
            <el-tag :type="scope.row.backupType === 'auto' ? 'info' : 'success'">
              {{ scope.row.backupType === 'auto' ? '自动' : '手动' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="backupSize" label="备份大小"></el-table-column>
        <el-table-column prop="createdAt" label="备份时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="restoreBackup(scope.row.id)">恢复</el-button>
            <el-button size="small" type="danger" @click="deleteBackup(scope.row.id)">删除</el-button>
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
      backupSettings: {
        autoBackup: true,
        backupFrequency: 'daily',
        backupTime: '02:00',
        backupRetention: 10
      },
      rules: {
        backupFrequency: [{ required: true, message: '请选择备份频率', trigger: 'blur' }],
        backupTime: [{ required: true, message: '请选择备份时间', trigger: 'blur' }],
        backupRetention: [{ required: true, message: '请输入备份保留数量', trigger: 'blur' }]
      },
      backups: [
        {
          id: 1,
          backupName: 'backup_20260320_100000',
          backupType: 'manual',
          backupSize: '10.5 MB',
          createdAt: '2026-03-20 10:00:00'
        },
        {
          id: 2,
          backupName: 'backup_20260319_020000',
          backupType: 'auto',
          backupSize: '9.8 MB',
          createdAt: '2026-03-19 02:00:00'
        },
        {
          id: 3,
          backupName: 'backup_20260318_020000',
          backupType: 'auto',
          backupSize: '9.2 MB',
          createdAt: '2026-03-18 02:00:00'
        },
        {
          id: 4,
          backupName: 'backup_20260317_020000',
          backupType: 'auto',
          backupSize: '8.7 MB',
          createdAt: '2026-03-17 02:00:00'
        }
      ]
    }
  },
  methods: {
    saveBackupSettings() {
      this.$refs.backupForm.validate((valid) => {
        if (valid) {
          // 保存备份设置
          localStorage.setItem('backupSettings', JSON.stringify(this.backupSettings))
          this.$message.success('备份设置保存成功')
        }
      })
    },
    createBackup() {
      // 模拟创建备份
      const newBackup = {
        id: this.backups.length + 1,
        backupName: `backup_${new Date().toISOString().slice(0, 19).replace(/[-:]/g, '').replace('T', '_')}`,
        backupType: 'manual',
        backupSize: `${(Math.random() * 5 + 5).toFixed(1)} MB`,
        createdAt: new Date().toISOString().slice(0, 19).replace('T', ' ')
      }
      this.backups.unshift(newBackup)
      this.$message.success('手动备份创建成功')
    },
    restoreBackup(id) {
      this.$confirm('确定要从该备份恢复系统吗？这将覆盖当前系统数据！', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 模拟恢复操作
        this.$message.success('系统恢复成功')
      }).catch(() => {
        // 取消恢复
      })
    },
    deleteBackup(id) {
      this.$confirm('确定要删除该备份吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const index = this.backups.findIndex(b => b.id === id)
        if (index !== -1) {
          this.backups.splice(index, 1)
          this.$message.success('备份删除成功')
        }
      }).catch(() => {
        // 取消删除
      })
    }
  },
  mounted() {
    // 从本地存储加载备份设置
    const savedSettings = localStorage.getItem('backupSettings')
    if (savedSettings) {
      this.backupSettings = JSON.parse(savedSettings)
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

.backup-settings {
  padding: 20px;
}
</style>