<template>
  <div class="study-note-container">
    <div class="note-header">
      <h2>学习笔记管理</h2>
      <el-button type="primary" @click="dialogVisible = true" class="add-btn">
        <el-icon><Plus /></el-icon>
        创建笔记
      </el-button>
    </div>
    
    <div class="note-filters">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索笔记"
        prefix-icon="Search"
        class="search-input"
        @keyup.enter="searchNotes"
      />
      <el-select v-model="selectedCategory" placeholder="按分类筛选" class="category-select">
        <el-option label="全部" value="all" />
        <el-option label="课程笔记" value="course" />
        <el-option label="读书笔记" value="book" />
        <el-option label="会议笔记" value="meeting" />
        <el-option label="其他" value="other" />
      </el-select>
    </div>
    
    <div class="note-list">
      <el-card v-for="note in notes" :key="note.id" class="note-card">
        <template #header>
          <div class="note-card-header">
            <h3 class="note-title">{{ note.title }}</h3>
            <div class="note-actions">
              <el-button size="small" @click="viewNote(note)" class="view-btn">
                <el-icon><View /></el-icon>
                查看
              </el-button>
              <el-button size="small" @click="editNote(note)" class="edit-btn">
                <el-icon><Edit /></el-icon>
                编辑
              </el-button>
              <el-button size="small" @click="deleteNote(note.id)" class="delete-btn">
                <el-icon><Delete /></el-icon>
                删除
              </el-button>
            </div>
          </div>
        </template>
        <div class="note-content">
          <p class="note-preview">{{ note.content.substring(0, 100) }}...</p>
          <div class="note-meta">
            <span class="note-category">{{ note.type }}</span>
            <span class="note-time">{{ note.createTime }}</span>
          </div>
        </div>
      </el-card>
    </div>
    
    <!-- 创建/编辑笔记对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑笔记' : '创建笔记'"
      width="800px"
      class="note-dialog"
    >
      <el-form :model="currentNote" label-width="100px" class="note-form">
        <el-form-item label="笔记标题">
          <el-input v-model="currentNote.title" class="input-field" />
        </el-form-item>
        <el-form-item label="笔记分类">
          <el-select v-model="currentNote.category" class="select-field">
            <el-option label="课程笔记" value="course" />
            <el-option label="读书笔记" value="book" />
            <el-option label="会议笔记" value="meeting" />
            <el-option label="其他" value="other" />
          </el-select>
        </el-form-item>
        <el-form-item label="笔记内容">
          <el-input
            type="textarea"
            v-model="currentNote.content"
            class="textarea-field"
            :rows="10"
            placeholder="输入笔记内容..."
          />
        </el-form-item>
        <el-form-item label="标签">
          <el-tag
            v-for="tag in currentNote.tags"
            :key="tag"
            closable
            @close="removeTag(tag)"
            class="note-tag"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-model="newTag"
            placeholder="添加标签"
            class="tag-input"
            @keyup.enter="addTag"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false" class="cancel-btn">取消</el-button>
          <el-button type="primary" @click="saveNote" class="confirm-btn">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { Plus, View, Edit, Delete, Search } from '@element-plus/icons-vue'

export default {
  components: {
    Plus,
    View,
    Edit,
    Delete,
    Search
  },
  data() {
    return {
      notes: [],
      searchKeyword: '',
      selectedCategory: 'all',
      dialogVisible: false,
      isEdit: false,
      currentNote: {
        id: '',
        title: '',
        content: '',
        category: 'course',
        tags: [],
        createTime: '',
        updateTime: ''
      },
      newTag: ''
    }
  },
  mounted() {
    this.getNotes()
  },
  methods: {
    getNotes() {
      const token = localStorage.getItem('token')
      const user = JSON.parse(localStorage.getItem('user'))
      const userId = user.id
      
      this.$axios.get(`/api/study-notes/user/${userId}`, {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      }).then(response => {
        this.notes = response.data
      }).catch(error => {
        console.error('获取笔记列表失败:', error)
      })
    },
    searchNotes() {
      // 实现搜索功能
      console.log('搜索笔记:', this.searchKeyword)
    },
    viewNote(note) {
      this.$message.info(`查看笔记: ${note.title}`)
    },
    editNote(note) {
      this.isEdit = true
      this.currentNote = { ...note }
      this.dialogVisible = true
    },
    deleteNote(id) {
      this.$confirm('确定要删除这条笔记吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.notes = this.notes.filter(note => note.id !== id)
        this.$message.success('笔记删除成功')
      }).catch(() => {
        // 取消删除
      })
    },
    saveNote() {
      if (this.isEdit) {
        // 更新笔记
        const index = this.notes.findIndex(note => note.id === this.currentNote.id)
        if (index !== -1) {
          this.notes[index] = { ...this.currentNote, updateTime: new Date().toLocaleString() }
        }
        this.$message.success('笔记更新成功')
      } else {
        // 创建笔记
        const newNote = {
          ...this.currentNote,
          id: Date.now(),
          createTime: new Date().toLocaleString(),
          updateTime: new Date().toLocaleString()
        }
        this.notes.unshift(newNote)
        this.$message.success('笔记创建成功')
      }
      this.dialogVisible = false
      this.resetForm()
    },
    resetForm() {
      this.isEdit = false
      this.currentNote = {
        id: '',
        title: '',
        content: '',
        category: 'course',
        tags: [],
        createTime: '',
        updateTime: ''
      }
      this.newTag = ''
    },
    addTag() {
      if (this.newTag && !this.currentNote.tags.includes(this.newTag)) {
        this.currentNote.tags.push(this.newTag)
        this.newTag = ''
      }
    },
    removeTag(tag) {
      this.currentNote.tags = this.currentNote.tags.filter(t => t !== tag)
    }
  }
}
</script>

<style scoped>
.study-note-container {
  padding: 20px;
  background-color: #f8f9fa;
  min-height: 600px;
}

.note-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.note-header h2 {
  margin: 0;
  color: #333;
}

.add-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 8px;
  padding: 8px 20px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.add-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

.note-filters {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
}

.search-input {
  width: 300px;
}

.category-select {
  width: 200px;
}

.note-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 20px;
}

.note-card {
  transition: all 0.3s ease;
  border-radius: 8px;
  overflow: hidden;
}

.note-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.note-card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 15px;
  border-bottom: 1px solid #e4e7ed;
}

.note-title {
  margin: 0;
  font-size: 16px;
  font-weight: bold;
  color: #333;
  flex: 1;
}

.note-actions {
  display: flex;
  gap: 8px;
}

.view-btn, .edit-btn, .delete-btn {
  border-radius: 4px;
  transition: all 0.3s ease;
}

.view-btn {
  background-color: #f0f0f0;
  border: 1px solid #e0e0e0;
}

.edit-btn {
  background-color: #4CAF50;
  border: none;
  color: white;
}

.delete-btn {
  background-color: #f44336;
  border: none;
  color: white;
}

.note-content {
  padding: 15px;
}

.note-preview {
  margin: 0 0 15px 0;
  color: #666;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.note-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #999;
}

.note-category {
  background-color: #f0f0f0;
  padding: 2px 8px;
  border-radius: 10px;
}

.note-dialog {
  border-radius: 16px;
  overflow: hidden;
}

.note-dialog .el-dialog__header {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 20px;
}

.note-dialog .el-dialog__title {
  color: white;
  font-size: 18px;
  font-weight: bold;
}

.note-dialog .el-dialog__body {
  padding: 30px;
  background: rgba(255, 255, 255, 0.95);
}

.note-form {
  animation: fadeIn 0.3s ease;
}

.input-field, .select-field, .textarea-field {
  border-radius: 8px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s ease;
  width: 100%;
}

.input-field:focus, .select-field:focus, .textarea-field:focus {
  border-color: #667eea;
  box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
}

.note-tag {
  margin-right: 8px;
  margin-bottom: 8px;
}

.tag-input {
  width: 200px;
  margin-top: 10px;
}

.dialog-footer {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 20px;
  background: rgba(245, 247, 250, 0.8);
}

.cancel-btn, .confirm-btn {
  border-radius: 8px;
  padding: 8px 20px;
  transition: all 0.3s ease;
}

.confirm-btn {
  background: linear-gradient(45deg, #667eea 0%, #764ba2 100%);
  border: none;
}

.confirm-btn:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.4);
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .note-filters {
    flex-direction: column;
  }
  
  .search-input,
  .category-select {
    width: 100%;
  }
  
  .note-list {
    grid-template-columns: 1fr;
  }
  
  .note-card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .note-actions {
    align-self: flex-end;
  }
}
</style>
