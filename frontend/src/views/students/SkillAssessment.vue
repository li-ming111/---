<template>
  <div class="skill-assessment-container">
    <div class="skill-assessment-header">
      <h2>技能测评与提升</h2>
      <el-button type="primary" @click="startAssessment">开始测评</el-button>
    </div>
    
    <div class="skill-categories">
      <el-card v-for="category in skillCategories" :key="category.id" class="category-card">
        <div class="category-header">
          <h3>{{ category.name }}</h3>
          <span class="category-count">{{ category.skillCount }} 项技能</span>
        </div>
        <div class="category-skills">
          <div v-for="skill in category.skills" :key="skill.id" class="skill-item">
            <div class="skill-info">
              <div class="skill-name">{{ skill.name }}</div>
              <div class="skill-level">等级: {{ skill.level }}/5</div>
            </div>
            <div class="skill-progress">
              <el-progress :percentage="skill.score" :color="getProgressColor(skill.score)"></el-progress>
            </div>
            <el-button size="small" @click="viewSkillDetail(skill.id)">详情</el-button>
          </div>
        </div>
      </el-card>
    </div>
    
    <div class="skill-radar">
      <el-card class="radar-card">
        <template #header>
          <div class="radar-header">
            <span>技能雷达图</span>
            <el-select v-model="radarCategory" @change="updateRadarChart">
              <el-option label="所有技能" value="all"></el-option>
              <el-option label="专业技能" value="专业技能"></el-option>
              <el-option label="通用技能" value="通用技能"></el-option>
            </el-select>
          </div>
        </template>
        <div class="radar-container">
          <div ref="radarChart" class="radar"></div>
        </div>
      </el-card>
    </div>
    
    <div class="skill-recommendations">
      <h3>推荐学习资源</h3>
      <div class="recommendation-list">
        <el-card v-for="resource in recommendedResources" :key="resource.id" class="resource-card">
          <div class="resource-header">
            <h4>{{ resource.title }}</h4>
            <span class="resource-type">{{ resource.type }}</span>
          </div>
          <div class="resource-content">
            <p>{{ resource.description }}</p>
            <div class="resource-skills">
              <span v-for="skill in resource.skills" :key="skill" class="skill-tag">{{ skill }}</span>
            </div>
          </div>
          <div class="resource-footer">
            <el-button type="primary" size="small" @click="viewResource(resource.id)">查看</el-button>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  data() {
    return {
      skillCategories: [],
      radarCategory: 'all',
      recommendedResources: [],
      radarChart: null
    }
  },
  mounted() {
    this.initData()
    this.initRadarChart()
  },
  beforeUnmount() {
    if (this.radarChart) {
      this.radarChart.dispose()
    }
  },
  methods: {
    initData() {
      // 模拟数据
      this.skillCategories = [
        {
          id: 1,
          name: '专业技能',
          skillCount: 3,
          skills: [
            {
              id: 1,
              name: 'Java编程',
              level: 4,
              score: 85
            },
            {
              id: 2,
              name: '数据库设计',
              level: 3,
              score: 70
            },
            {
              id: 3,
              name: '前端开发',
              level: 2,
              score: 55
            }
          ]
        },
        {
          id: 2,
          name: '通用技能',
          skillCount: 4,
          skills: [
            {
              id: 4,
              name: '英语',
              level: 4,
              score: 80
            },
            {
              id: 5,
              name: '办公软件',
              level: 5,
              score: 90
            },
            {
              id: 6,
              name: '沟通能力',
              level: 3,
              score: 75
            },
            {
              id: 7,
              name: '团队协作',
              level: 3,
              score: 70
            }
          ]
        }
      ]
      
      this.recommendedResources = [
        {
          id: 1,
          title: 'Java核心技术',
          type: '书籍',
          description: 'Java编程的经典书籍，涵盖Java核心概念和高级特性',
          skills: ['Java编程']
        },
        {
          id: 2,
          title: '数据库设计原理',
          type: '在线课程',
          description: '详细讲解数据库设计的基本原理和最佳实践',
          skills: ['数据库设计']
        },
        {
          id: 3,
          title: '前端开发入门',
          type: '在线课程',
          description: '从基础到进阶的前端开发课程，包括HTML、CSS和JavaScript',
          skills: ['前端开发']
        },
        {
          id: 4,
          title: '商务英语',
          type: '在线课程',
          description: '提高商务英语水平，包括邮件写作和会议沟通',
          skills: ['英语']
        }
      ]
    },
    initRadarChart() {
      this.radarChart = echarts.init(this.$refs.radarChart)
      this.updateRadarChart()
    },
    updateRadarChart() {
      let skills = []
      if (this.radarCategory === 'all') {
        this.skillCategories.forEach(category => {
          skills = skills.concat(category.skills)
        })
      } else {
        const category = this.skillCategories.find(cat => cat.name === this.radarCategory)
        if (category) {
          skills = category.skills
        }
      }
      
      const indicator = skills.map(skill => ({
        name: skill.name,
        max: 100
      }))
      
      const option = {
        tooltip: {},
        radar: {
          indicator: indicator
        },
        series: [{
          type: 'radar',
          data: [{
            value: skills.map(skill => skill.score),
            name: '技能水平',
            areaStyle: {
              color: 'rgba(79, 172, 254, 0.3)'
            },
            lineStyle: {
              color: '#4facfe'
            }
          }]
        }]
      }
      
      this.radarChart.setOption(option)
    },
    getProgressColor(score) {
      if (score >= 80) {
        return '#67C23A'
      } else if (score >= 60) {
        return '#E6A23C'
      } else {
        return '#F56C6C'
      }
    },
    startAssessment() {
      this.$message.success('开始技能测评')
    },
    viewSkillDetail(id) {
      this.$router.push(`/skill/${id}`)
    },
    viewResource(id) {
      this.$message.success('查看学习资源')
    }
  }
}
</script>

<style scoped>
.skill-assessment-container {
  padding: 20px;
}

.skill-assessment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.skill-assessment-header h2 {
  margin: 0;
  color: #333;
}

.skill-categories {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.category-card {
  transition: all 0.3s ease;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.category-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.category-header h3 {
  margin: 0;
  color: #333;
}

.category-count {
  font-size: 12px;
  color: #666;
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 10px;
}

.skill-item {
  margin-bottom: 15px;
  padding: 10px;
  background: #f9f9f9;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.skill-item:hover {
  background: #f0f0f0;
}

.skill-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.skill-name {
  font-weight: 500;
  color: #333;
}

.skill-level {
  font-size: 12px;
  color: #666;
}

.skill-progress {
  margin-bottom: 10px;
}

.skill-radar {
  margin-bottom: 30px;
}

.radar-card {
  transition: all 0.3s ease;
}

.radar-card:hover {
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.radar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.radar-container {
  height: 400px;
}

.radar {
  width: 100%;
  height: 100%;
}

.skill-recommendations {
  margin-top: 30px;
}

.skill-recommendations h3 {
  margin-bottom: 20px;
  color: #333;
}

.recommendation-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.resource-card {
  transition: all 0.3s ease;
}

.resource-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

.resource-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.resource-header h4 {
  margin: 0;
  color: #333;
}

.resource-type {
  font-size: 12px;
  color: #666;
  background: #f0f0f0;
  padding: 2px 8px;
  border-radius: 10px;
}

.resource-content p {
  margin: 0 0 10px 0;
  font-size: 14px;
  color: #666;
  line-height: 1.4;
}

.resource-skills {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
  margin-bottom: 15px;
}

.skill-tag {
  font-size: 12px;
  color: #4facfe;
  background: rgba(79, 172, 254, 0.1);
  padding: 2px 8px;
  border-radius: 10px;
}

.resource-footer {
  display: flex;
  justify-content: flex-end;
}

@media (max-width: 768px) {
  .skill-assessment-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .skill-categories {
    grid-template-columns: 1fr;
  }
  
  .recommendation-list {
    grid-template-columns: 1fr;
  }
  
  .radar-container {
    height: 300px;
  }
}
</style>