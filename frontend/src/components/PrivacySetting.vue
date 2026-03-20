<template>
  <div class="privacy-setting">
    <h2>隐私设置</h2>
    <el-card class="privacy-card">
      <el-form :model="privacyForm" label-width="120px">
        <el-form-item label="学习记录可见性">
          <el-radio-group v-model="privacyForm.learningVisible">
            <el-radio value="public">公开</el-radio>
            <el-radio value="private">仅自己可见</el-radio>
            <el-radio value="friends">仅好友可见</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="打卡数据可见性">
          <el-radio-group v-model="privacyForm.checkinVisible">
            <el-radio value="public">公开</el-radio>
            <el-radio value="private">仅自己可见</el-radio>
            <el-radio value="friends">仅好友可见</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="个人资料可见性">
          <el-radio-group v-model="privacyForm.profileVisible">
            <el-radio value="public">公开</el-radio>
            <el-radio value="private">仅自己可见</el-radio>
            <el-radio value="friends">仅好友可见</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="数据导出授权">
          <el-checkbox v-model="privacyForm.exportEnabled">允许导出我的数据</el-checkbox>
        </el-form-item>
        <el-form-item label="异地登录提醒">
          <el-checkbox v-model="privacyForm.loginAlert">开启异地登录提醒</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="saveSettings">保存设置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <div class="security-center" style="margin-top: 30px;">
      <h3>账号安全中心</h3>
      <el-card class="security-card">
        <div class="security-item">
          <span>修改密码</span>
          <el-button type="primary" size="small" @click="changePassword">修改</el-button>
        </div>
        <div class="security-item">
          <span>绑定手机</span>
          <el-button type="primary" size="small" @click="bindPhone">绑定</el-button>
        </div>
        <div class="security-item">
          <span>设备管理</span>
          <el-button type="primary" size="small" @click="manageDevices">管理</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PrivacySetting',
  data() {
    return {
      privacyForm: {
        userId: 1,
        learningVisible: 'private',
        checkinVisible: 'private',
        profileVisible: 'private',
        exportEnabled: true,
        loginAlert: true
      }
    };
  },
  mounted() {
    this.loadSettings();
  },
  methods: {
    async loadSettings() {
      try {
        const response = await axios.get(`/privacy/user/${this.privacyForm.userId}`);
        if (response.data) {
          this.privacyForm = response.data;
        }
      } catch (error) {
        console.error('加载隐私设置失败:', error);
      }
    },
    async saveSettings() {
      try {
        await axios.post('/privacy/update', this.privacyForm);
        this.$message.success('隐私设置保存成功');
      } catch (error) {
        console.error('保存隐私设置失败:', error);
        this.$message.error('保存隐私设置失败');
      }
    },
    changePassword() {
      this.$message.info('修改密码功能开发中');
    },
    bindPhone() {
      this.$message.info('绑定手机功能开发中');
    },
    manageDevices() {
      this.$message.info('设备管理功能开发中');
    }
  }
};
</script>

<style scoped>
.privacy-setting {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
  min-height: 600px;
}

h2 {
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

h3 {
  color: #333;
  margin-bottom: 15px;
}

.privacy-card,
.security-card {
  margin-bottom: 20px;
}

.security-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #ebeef5;
}

.security-item:last-child {
  border-bottom: none;
}
</style>
