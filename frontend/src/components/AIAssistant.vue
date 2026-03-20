<template>
  <div class="ai-assistant-container">
    <!-- 悬浮图标 -->
    <div v-if="!isOpen" class="ai-floating-icon" @click="toggleOpen">
      <div class="icon-circle">
        <img :src="logo" alt="AI助手" class="logo-img">
      </div>
    </div>
    
    <!-- AI助手窗口 -->
    <div v-else class="ai-assistant-window" :class="{ 'minimized': isMinimized }">
      <!-- 窗口头部 -->
      <div class="ai-window-header">
        <div class="header-left">
          <div class="ai-avatar">
            <img :src="logo" alt="AI助手" class="avatar-img">
          </div>
          <h3 class="ai-title" v-if="!isMinimized">学涯助手系统的AI助手</h3>
        </div>
        <div class="header-right">
          <button class="minimize-btn" @click="toggleMinimize">
            <span>{{ isMinimized ? '↑' : '➖' }}</span>
          </button>
          <button class="close-btn" @click="toggleOpen">
            <span>×</span>
          </button>
        </div>
      </div>
      
      <!-- 聊天内容区域 -->
      <div class="ai-chat-content" v-if="!isMinimized">
        <!-- 欢迎消息 -->
        <div class="chat-message bot-message">
          <div class="message-content">
            <p>你好，我是学涯助手系统的AI助手</p>
            <p>有什么可以帮助你的吗？</p>
          </div>
        </div>
        
        <!-- 快速问题选项 -->
        <div class="quick-questions">
          <div class="quick-question" @click="askQuestion('你是谁')">你是谁</div>
          <div class="quick-question" @click="askQuestion('如何使用')">如何使用</div>
        </div>
        
        <!-- 聊天记录 -->
        <div v-for="(message, index) in messages" :key="index" class="chat-message" :class="message.type === 'user' ? 'user-message' : 'bot-message'">
          <div class="message-content">
            <p>{{ message.content }}</p>
          </div>
        </div>
      </div>
      
      <!-- 输入区域 -->
      <div class="ai-input-area" v-if="!isMinimized">
        <input type="text" v-model="inputMessage" placeholder="输入问题" class="ai-input">
        <button class="send-btn" @click="sendMessage">
          <i class="el-icon-send"></i>
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import logo from '@/assets/tubiao.png'

export default {
  data() {
    return {
      isOpen: false,
      isMinimized: false,
      inputMessage: '',
      messages: [],
      logo: logo
    }
  },
  methods: {
    toggleOpen() {
      this.isOpen = !this.isOpen
      this.isMinimized = false
    },
    toggleMinimize() {
      this.isMinimized = !this.isMinimized
    },
    sendMessage() {
      if (this.inputMessage.trim()) {
        // 添加用户消息
        this.messages.push({
          type: 'user',
          content: this.inputMessage
        })
        
        // 滚动到底部
        this.scrollToBottom()
        
        // 模拟AI回复
        setTimeout(() => {
          this.messages.push({
            type: 'bot',
            content: this.getAIResponse(this.inputMessage)
          })
          
          // 滚动到底部
          this.scrollToBottom()
        }, 500)
        
        // 清空输入框
        this.inputMessage = ''
      }
    },
    askQuestion(question) {
      // 添加用户消息
      this.messages.push({
        type: 'user',
        content: question
      })
      
      // 滚动到底部
      this.scrollToBottom()
      
      // 模拟AI回复
      setTimeout(() => {
        this.messages.push({
          type: 'bot',
          content: this.getAIResponse(question)
        })
        
        // 滚动到底部
        this.scrollToBottom()
      }, 500)
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const chatContent = document.querySelector('.ai-chat-content')
        if (chatContent) {
          chatContent.scrollTop = chatContent.scrollHeight
        }
      })
    },
    getAIResponse(question) {
      // 简单的回复逻辑
      const responses = {
        '你是谁': '我是学涯助手系统的AI助手，专门为学生提供系统使用指导和帮助，包括学习计划、目标管理、职业规划等功能的使用说明。',
        '如何使用': '你可以通过我了解学涯助手系统的各项功能，包括学习计划制定、学习资源查找、目标管理、职业规划、激励系统和每日打卡等。如果有具体问题，可以随时向我咨询。',
        '学习计划': '学习计划功能可以帮助你制定合理的学习目标和时间表，创建详细的学习任务，跟踪学习进度，提高学习效率。你可以在"学习计划"页面创建和管理你的学习计划。',
        '学习资源': '学习资源功能提供了丰富的学习材料，包括课程资料、参考书籍、视频教程等。你可以在"学习资源"页面浏览和下载这些资源，支持按类别和关键词搜索。',
        '目标管理': '目标管理功能可以帮助你设定短期和长期目标，监控目标完成情况，及时调整学习策略。你可以在"目标管理"页面创建和跟踪你的学习目标。',
        '职业规划': '职业规划功能可以根据你的专业和兴趣，提供职业发展建议和相关资源。你可以在"职业规划"页面了解不同职业路径，获取职业指导。',
        '激励系统': '激励系统通过积分和奖励机制，鼓励你积极参与学习和完成任务。你可以在"激励系统"页面查看你的积分和获得的奖励。',
        '每日打卡': '每日打卡功能让你记录每天的学习情况，养成良好的学习习惯。你可以在"每日打卡"页面进行打卡，查看历史打卡记录。',
        '个人中心': '个人中心页面展示你的个人信息，包括基本资料、学习统计、获得的奖励等。你可以在个人中心更新你的个人信息和查看学习数据。'
      }
      
      return responses[question] || '抱歉，我还不能回答这个问题。你可以尝试询问关于学习计划、学习资源、目标管理、职业规划、激励系统或每日打卡的问题。'
    }
  }
}
</script>

<style scoped>
.ai-assistant-container {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 1000;
}

.ai-floating-icon {
  cursor: pointer;
  transition: all 0.3s ease;
}

.ai-floating-icon:hover {
  transform: scale(1.1);
}

.icon-circle {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.4);
}

.icon-circle i {
  font-size: 24px;
  color: white;
}

.logo-img {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.avatar-img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.ai-assistant-window.minimized .avatar-img {
  width: 24px;
  height: 24px;
}

.ai-assistant-window {
  width: 350px;
  height: 450px;
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  animation: slideIn 0.3s ease;
  transition: all 0.3s ease;
}

.ai-assistant-window.minimized {
  height: 50px;
  width: 200px;
}

.ai-assistant-window.minimized .ai-avatar {
  width: 24px;
  height: 24px;
}

.ai-assistant-window.minimized .ai-avatar i {
  font-size: 12px;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.ai-window-header {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.ai-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
}

.ai-avatar i {
  font-size: 16px;
  color: white;
}

.ai-title {
  margin: 0;
  font-size: 16px;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 8px;
}

.minimize-btn, .close-btn {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  padding: 6px 10px;
  border-radius: 8px;
  transition: all 0.3s ease;
  backdrop-filter: blur(10px);
  display: flex;
  align-items: center;
  justify-content: center;
  min-width: 32px;
  height: 32px;
}

.minimize-btn:hover, .close-btn:hover {
  background: rgba(255, 255, 255, 0.2);
  transform: translateY(-1px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.minimize-btn {
  background: linear-gradient(135deg, rgba(255, 255, 255, 0.15), rgba(255, 255, 255, 0.05));
}

.close-btn {
  background: linear-gradient(135deg, rgba(255, 87, 34, 0.2), rgba(255, 87, 34, 0.1));
  border-color: rgba(255, 87, 34, 0.3);
}

.close-btn:hover {
  background: linear-gradient(135deg, rgba(255, 87, 34, 0.3), rgba(255, 87, 34, 0.2));
}

.ai-chat-content {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #f8f9fa;
}

.chat-message {
  margin-bottom: 15px;
  display: flex;
  flex-direction: column;
}

.bot-message {
  align-items: flex-start;
}

.user-message {
  align-items: flex-end;
}

.message-content {
  max-width: 80%;
  padding: 10px 15px;
  border-radius: 18px;
  line-height: 1.4;
}

.bot-message .message-content {
  background: white;
  border-bottom-left-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.user-message .message-content {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border-bottom-right-radius: 4px;
}

.quick-questions {
  margin: 20px 0;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.quick-question {
  background: white;
  padding: 8px 12px;
  border-radius: 20px;
  font-size: 14px;
  color: #667eea;
  cursor: pointer;
  transition: all 0.2s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.quick-question:hover {
  background: #f0f0f0;
  transform: translateY(-2px);
}

.ai-input-area {
  padding: 15px;
  background: white;
  border-top: 1px solid #e0e0e0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.ai-input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #e0e0e0;
  border-radius: 20px;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s ease;
}

.ai-input:focus {
  border-color: #4facfe;
}

.send-btn {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border: none;
  color: white;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.2s ease;
  box-shadow: 0 2px 8px rgba(79, 172, 254, 0.3);
}

.send-btn:hover {
  transform: scale(1.1);
  box-shadow: 0 4px 12px rgba(79, 172, 254, 0.4);
}

.send-btn i {
  font-size: 16px;
}

/* 滚动条样式 */
.ai-chat-content::-webkit-scrollbar {
  width: 6px;
}

.ai-chat-content::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.ai-chat-content::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.ai-chat-content::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .ai-assistant-container {
    bottom: 20px;
    right: 20px;
  }
  
  .icon-circle {
    width: 50px;
    height: 50px;
  }
  
  .icon-circle i {
    font-size: 20px;
  }
  
  .ai-assistant-window {
    width: 300px;
    height: 400px;
  }
  
  .ai-window-header {
    padding: 12px 15px;
  }
  
  .ai-title {
    font-size: 14px;
  }
  
  .ai-chat-content {
    padding: 15px;
  }
  
  .ai-input-area {
    padding: 12px;
  }
}
</style>