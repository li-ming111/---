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
        <input type="text" v-model="inputMessage" placeholder="输入问题" class="ai-input" :disabled="isLoading">
        <button class="send-btn" @click="sendMessage" :disabled="isLoading">
          <i v-if="!isLoading" class="el-icon-send"></i>
          <i v-else class="el-icon-loading"></i>
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
      isLoading: false,
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
      if (this.inputMessage.trim() && !this.isLoading) {
        // 添加用户消息
        this.messages.push({
          type: 'user',
          content: this.inputMessage
        })
        
        // 滚动到底部
        this.scrollToBottom()
        
        // 显示加载状态
        this.isLoading = true
        
        // 调用后端API获取AI回复
        const token = localStorage.getItem('token')
        this.$axios.post('/api/ai-assistant/ask', {
          question: this.inputMessage
        }, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        }).then(response => {
          if (response.data.success) {
            // 添加AI回复
            this.messages.push({
              type: 'bot',
              content: response.data.response
            })
          } else {
            // 添加错误消息
            this.messages.push({
              type: 'bot',
              content: '抱歉，AI助手暂时无法回答你的问题，请稍后再试。'
            })
          }
        }).catch(error => {
          console.error('获取AI回复失败:', error)
          // 添加错误消息
          this.messages.push({
            type: 'bot',
            content: '抱歉，AI助手暂时无法回答你的问题，请稍后再试。'
          })
        }).finally(() => {
          // 隐藏加载状态
          this.isLoading = false
          // 滚动到底部
          this.scrollToBottom()
        })
        
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
      
      // 显示加载状态
      this.isLoading = true
      
      // 调用后端API获取AI回复
      const token = localStorage.getItem('token')
      this.$axios.post('/api/ai-assistant/ask', {
        question: question
      }, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        if (response.data.success) {
          // 添加AI回复
          this.messages.push({
            type: 'bot',
            content: response.data.response
          })
        } else {
          // 添加错误消息
          this.messages.push({
            type: 'bot',
            content: '抱歉，AI助手暂时无法回答你的问题，请稍后再试。'
          })
        }
      }).catch(error => {
        console.error('获取AI回复失败:', error)
        // 添加错误消息
        this.messages.push({
          type: 'bot',
          content: '抱歉，AI助手暂时无法回答你的问题，请稍后再试。'
        })
      }).finally(() => {
        // 隐藏加载状态
        this.isLoading = false
        // 滚动到底部
        this.scrollToBottom()
      })
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const chatContent = document.querySelector('.ai-chat-content')
        if (chatContent) {
          chatContent.scrollTop = chatContent.scrollHeight
        }
      })
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