<template>
  <div class="admin-container">
    <h2 class="page-title">系统设置</h2>
    <el-card class="admin-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">系统配置</span>
        </div>
      </template>
      <el-form :model="settings" :rules="rules" ref="settingsForm" label-width="120px">
        <el-form-item label="系统名称" prop="systemName">
          <el-input v-model="settings.systemName" placeholder="请输入系统名称"></el-input>
        </el-form-item>
        <el-form-item label="系统版本" prop="systemVersion">
          <el-input v-model="settings.systemVersion" placeholder="请输入系统版本"></el-input>
        </el-form-item>
        <el-form-item label="系统状态" prop="systemStatus">
          <el-select v-model="settings.systemStatus" placeholder="请选择系统状态">
            <el-option label="运行中" value="running"></el-option>
            <el-option label="维护中" value="maintenance"></el-option>
            <el-option label="暂停服务" value="paused"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="允许注册" prop="allowRegistration">
          <el-switch v-model="settings.allowRegistration"></el-switch>
        </el-form-item>
        <el-form-item label="验证码功能" prop="enableCaptcha">
          <el-switch v-model="settings.enableCaptcha"></el-switch>
        </el-form-item>
        <el-form-item label="最大上传大小" prop="maxUploadSize">
          <el-input v-model="settings.maxUploadSize" placeholder="请输入最大上传大小（MB）" type="number"></el-input>
        </el-form-item>
        <el-form-item label="会话超时时间" prop="sessionTimeout">
          <el-input v-model="settings.sessionTimeout" placeholder="请输入会话超时时间（分钟）" type="number"></el-input>
        </el-form-item>
        <el-form-item label="系统通知" prop="systemNotification">
          <el-input v-model="settings.systemNotification" type="textarea" placeholder="请输入系统通知" :rows="3"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSettings">保存设置</el-button>
          <el-button @click="resetSettings">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="admin-card mt-20">
      <template #header>
        <div class="card-header">
          <span class="card-title">安全设置</span>
        </div>
      </template>
      <el-form :model="securitySettings" :rules="securityRules" ref="securityForm" label-width="120px">
        <el-form-item label="密码复杂度" prop="passwordComplexity">
          <el-select v-model="securitySettings.passwordComplexity" placeholder="请选择密码复杂度">
            <el-option label="低" value="low"></el-option>
            <el-option label="中" value="medium"></el-option>
            <el-option label="高" value="high"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="密码过期时间" prop="passwordExpiryDays">
          <el-input v-model="securitySettings.passwordExpiryDays" placeholder="请输入密码过期时间（天）" type="number"></el-input>
        </el-form-item>
        <el-form-item label="登录失败限制" prop="loginFailLimit">
          <el-input v-model="securitySettings.loginFailLimit" placeholder="请输入登录失败限制次数" type="number"></el-input>
        </el-form-item>
        <el-form-item label="IP访问限制" prop="enableIPLimit">
          <el-switch v-model="securitySettings.enableIPLimit"></el-switch>
        </el-form-item>
        <el-form-item label="API访问频率" prop="apiRateLimit">
          <el-input v-model="securitySettings.apiRateLimit" placeholder="请输入API访问频率（次/分钟）" type="number"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSecuritySettings">保存安全设置</el-button>
          <el-button @click="resetSecuritySettings">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      settings: {
        systemName: '智学方舟',
        systemVersion: '1.0.0',
        systemStatus: 'running',
        allowRegistration: true,
        enableCaptcha: false,
        maxUploadSize: 50,
        sessionTimeout: 30,
        systemNotification: '欢迎使用智学方舟系统！'
      },
      securitySettings: {
        passwordComplexity: 'medium',
        passwordExpiryDays: 90,
        loginFailLimit: 5,
        enableIPLimit: false,
        apiRateLimit: 100
      },
      rules: {
        systemName: [{ required: true, message: '请输入系统名称', trigger: 'blur' }],
        systemVersion: [{ required: true, message: '请输入系统版本', trigger: 'blur' }],
        systemStatus: [{ required: true, message: '请选择系统状态', trigger: 'blur' }],
        maxUploadSize: [{ required: true, message: '请输入最大上传大小', trigger: 'blur' }],
        sessionTimeout: [{ required: true, message: '请输入会话超时时间', trigger: 'blur' }]
      },
      securityRules: {
        passwordComplexity: [{ required: true, message: '请选择密码复杂度', trigger: 'blur' }],
        passwordExpiryDays: [{ required: true, message: '请输入密码过期时间', trigger: 'blur' }],
        loginFailLimit: [{ required: true, message: '请输入登录失败限制次数', trigger: 'blur' }],
        apiRateLimit: [{ required: true, message: '请输入API访问频率', trigger: 'blur' }]
      }
    }
  },
  methods: {
    saveSettings() {
      this.$refs.settingsForm.validate((valid) => {
        if (valid) {
          // 保存系统设置
          localStorage.setItem('systemSettings', JSON.stringify(this.settings))
          this.$message.success('系统设置保存成功')
        }
      })
    },
    resetSettings() {
      this.settings = {
        systemName: '智学方舟',
        systemVersion: '1.0.0',
        systemStatus: 'running',
        allowRegistration: true,
        enableCaptcha: false,
        maxUploadSize: 50,
        sessionTimeout: 30,
        systemNotification: '欢迎使用智学方舟系统！'
      }
    },
    saveSecuritySettings() {
      this.$refs.securityForm.validate((valid) => {
        if (valid) {
          // 保存安全设置
          localStorage.setItem('securitySettings', JSON.stringify(this.securitySettings))
          this.$message.success('安全设置保存成功')
        }
      })
    },
    resetSecuritySettings() {
      this.securitySettings = {
        passwordComplexity: 'medium',
        passwordExpiryDays: 90,
        loginFailLimit: 5,
        enableIPLimit: false,
        apiRateLimit: 100
      }
    }
  },
  mounted() {
    // 从本地存储加载设置
    const savedSettings = localStorage.getItem('systemSettings')
    if (savedSettings) {
      this.settings = JSON.parse(savedSettings)
    }
    const savedSecuritySettings = localStorage.getItem('securitySettings')
    if (savedSecuritySettings) {
      this.securitySettings = JSON.parse(savedSecuritySettings)
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
</style>