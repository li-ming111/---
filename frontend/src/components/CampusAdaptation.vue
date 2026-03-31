<template>
  <div class="campus-adaptation">
    <h2>校园适应</h2>
    <div class="campus-tabs">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="校园课程" name="courses">
          <div class="course-list">
            <el-card v-for="course in campusCourses" :key="course.id" class="course-card">
              <div class="course-header">
                <h3>{{ course.courseName }}</h3>
                <el-tag :type="course.type === 'compulsory' ? 'danger' : 'success'">
                  {{ course.type === 'compulsory' ? '必修课' : '选修课' }}
                </el-tag>
              </div>
              <div class="course-info">
                <p><strong>教师:</strong> {{ course.teacher }}</p>
                <p><strong>学分:</strong> {{ course.credit }} 分</p>
                <p><strong>上课时间:</strong> {{ course.time }}</p>
                <p><strong>上课地点:</strong> {{ course.location }}</p>
              </div>
              <div class="course-footer">
                <el-button type="primary" size="small" @click="viewCourseDetail(course.id)">查看详情</el-button>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
        <el-tab-pane label="校园活动" name="activities">
          <div class="activity-list">
            <el-card v-for="activity in campusActivities" :key="activity.id" class="activity-card">
              <div class="activity-header">
                <h3>{{ activity.activityName }}</h3>
                <el-tag type="info">{{ activity.type }}</el-tag>
              </div>
              <div class="activity-info">
                <p><strong>时间:</strong> {{ activity.time }}</p>
                <p><strong>地点:</strong> {{ activity.location }}</p>
                <p><strong>负责人:</strong> {{ activity.contactPerson }}</p>
                <p><strong>联系电话:</strong> {{ activity.contactPhone }}</p>
              </div>
              <div class="activity-footer">
                <el-button type="primary" size="small" @click="joinActivity(activity.id)">报名参加</el-button>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CampusAdaptation',
  data() {
    return {
      activeTab: 'courses',
      campusCourses: [],
      campusActivities: []
    };
  },
  mounted() {
    this.loadCampusData();
  },
  methods: {
    async loadCampusData() {
      try {
        // 加载校园课程
        const coursesResponse = await this.$axios.get('/campus/courses');
        this.campusCourses = coursesResponse.data;
        
        // 加载校园活动
        const activitiesResponse = await this.$axios.get('/campus/activities');
        this.campusActivities = activitiesResponse.data;
      } catch (error) {
        console.error('加载校园数据失败:', error);
        this.$message.error('加载校园数据失败');
      }
    },
    viewCourseDetail(courseId) {
      this.$message.info(`查看课程 ${courseId} 详情`);
    },
    joinActivity(activityId) {
      this.$message.success(`报名参加活动 ${activityId} 成功`);
    }
  }
};
</script>

<style scoped>
.campus-adaptation {
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

.campus-tabs {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.course-list,
.activity-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.course-card,
.activity-card {
  transition: transform 0.3s ease;
}

.course-card:hover,
.activity-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 16px 0 rgba(0, 0, 0, 0.15);
}

.course-header,
.activity-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.course-header h3,
.activity-header h3 {
  margin: 0;
  color: #333;
}

.course-info,
.activity-info {
  margin-bottom: 20px;
}

.course-info p,
.activity-info p {
  margin: 5px 0;
  color: #666;
}

.course-footer,
.activity-footer {
  text-align: right;
}
</style>
