<template>
  <div class="offline-learning">
    <h2>离线学习</h2>
    <div class="offline-features">
      <div class="feature-card">
        <h3>资源下载</h3>
        <p>下载学习资源，离线查看</p>
        <el-button type="primary" @click="downloadResource">下载资源</el-button>
      </div>
      <div class="feature-card">
        <h3>离线打卡</h3>
        <p>无网络环境下也能打卡</p>
        <el-button type="success" @click="offlineCheckin">离线打卡</el-button>
      </div>
      <div class="feature-card">
        <h3>数据同步</h3>
        <p>网络恢复后自动同步数据</p>
        <el-button type="warning" @click="syncData">同步数据</el-button>
      </div>
    </div>
    <div class="sync-status" v-if="syncStatus">
      <el-alert
        :title="syncStatus"
        type="success"
        show-icon
        :closable="false"
      />
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'OfflineLearning',
  data() {
    return {
      syncStatus: ''
    };
  },
  methods: {
    downloadResource() {
      // 模拟资源下载
      this.$message.success('资源下载功能已触发');
    },
    offlineCheckin() {
      // 模拟离线打卡
      const checkinData = {
        userId: 1,
        date: new Date().toISOString().split('T')[0],
        time: new Date().toTimeString().split(' ')[0],
        status: 'completed'
      };
      // 存储到localStorage
      localStorage.setItem('offlineCheckin', JSON.stringify(checkinData));
      this.$message.success('离线打卡成功');
    },
    async syncData() {
      try {
        // 从localStorage获取离线数据
        const offlineCheckin = localStorage.getItem('offlineCheckin');
        if (offlineCheckin) {
          const checkinData = JSON.parse(offlineCheckin);
          // 同步到服务器
          await axios.post('/checkin/do-checkin', checkinData);
          // 清除localStorage中的数据
          localStorage.removeItem('offlineCheckin');
          this.syncStatus = '数据同步成功';
          setTimeout(() => {
            this.syncStatus = '';
          }, 3000);
        } else {
          this.$message.info('没有离线数据需要同步');
        }
      } catch (error) {
        console.error('同步数据失败:', error);
        this.$message.error('同步数据失败');
      }
    }
  }
};
</script>

<style scoped>
.offline-learning {
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
  min-height: 400px;
}

h2 {
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.offline-features {
  display: flex;
  gap: 20px;
  margin-bottom: 30px;
}

.feature-card {
  flex: 1;
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.feature-card h3 {
  color: #409eff;
  margin-bottom: 10px;
}

.feature-card p {
  color: #666;
  margin-bottom: 20px;
}

.sync-status {
  margin-top: 20px;
}
</style>
