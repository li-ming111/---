import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'home',
    component: () => import('../views/students/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/register',
    name: 'register',
    component: () => import('../views/Register.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/school-verify',
    name: 'schoolVerify',
    component: () => import('../views/SchoolVerify.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('../views/students/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/study-plan',
    name: 'studyPlan',
    component: () => import('../views/students/StudyPlan.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/resources',
    name: 'resources',
    component: () => import('../views/students/Resources.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/goals',
    name: 'goals',
    component: () => import('../views/students/Goals.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/career',
    name: 'career',
    component: () => import('../views/students/Career.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin',
    name: 'admin',
    component: () => import('../views/Admin.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/users',
    name: 'adminUsers',
    component: () => import('../views/admin/Users.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/schools',
    name: 'adminSchools',
    component: () => import('../views/admin/Schools.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/resources',
    name: 'adminResources',
    component: () => import('../views/admin/Resources.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/settings',
    name: 'adminSettings',
    component: () => import('../views/admin/Settings.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/reports',
    name: 'adminReports',
    component: () => import('../views/admin/Reports.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/audit-logs',
    name: 'adminAuditLogs',
    component: () => import('../views/admin/AuditLogs.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/backup-restore',
    name: 'adminBackupRestore',
    component: () => import('../views/admin/BackupRestore.vue'),
    meta: { requiresAuth: true }
  },

  {
    path: '/admin/bulk-operations',
    name: 'adminBulkOperations',
    component: () => import('../views/admin/BulkOperations.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/content-review',
    name: 'adminContentReview',
    component: () => import('../views/admin/ContentReview.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/system-announcements',
    name: 'adminSystemAnnouncements',
    component: () => import('../views/admin/SystemAnnouncements.vue'),
    meta: { requiresAuth: true }
  },
  // 学校管理员路由
  {
    path: '/school-admin',
    name: 'schoolAdmin',
    component: () => import('../views/SchoolAdmin.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/school-admin/users',
    name: 'schoolAdminUsers',
    component: () => import('../views/school-admin/Users.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/school-admin/resources',
    name: 'schoolAdminResources',
    component: () => import('../views/school-admin/Resources.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/school-admin/settings',
    name: 'schoolAdminSettings',
    component: () => import('../views/school-admin/Settings.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/school-admin/reports',
    name: 'schoolAdminReports',
    component: () => import('../views/school-admin/Reports.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/school-admin/audit-logs',
    name: 'schoolAdminAuditLogs',
    component: () => import('../views/school-admin/AuditLogs.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/school-admin/announcements',
    name: 'schoolAdminAnnouncements',
    component: () => import('../views/school-admin/Announcements.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/incentive',
    name: 'incentive',
    component: () => import('../views/students/Incentive.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/checkin',
    name: 'Checkin',
    component: () => import('../views/students/Checkin.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/community',
    name: 'Community',
    component: () => import('../views/students/Community.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/students/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/skill-assessment',
    name: 'SkillAssessment',
    component: () => import('../views/students/SkillAssessment.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/offline-learning',
    name: 'OfflineLearning',
    component: () => import('../views/students/OfflineLearning.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/privacy-setting',
    name: 'PrivacySetting',
    component: () => import('../views/PrivacySetting.vue'),
    meta: { requiresAuth: true }
  },
  {
          path: '/campus-adaptation',
          name: 'CampusAdaptation',
          component: () => import('../views/students/CampusAdaptation.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/study-note',
          name: 'StudyNote',
          component: () => import('../views/students/StudyNote.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/exam',
          name: 'Exam',
          component: () => import('../views/students/Exam.vue'),
          meta: { requiresAuth: true }
        },
        {
          path: '/interest',
          name: 'Interest',
          component: () => import('../views/students/Interest.vue'),
          meta: { requiresAuth: true }
        },
  // 通配符路由，处理所有未匹配的路径
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局导航守卫
router.beforeEach((to, from, next) => {
  // 检查路由是否需要认证
  if (to.meta.requiresAuth !== false) {
    // 检查本地存储中是否有token
    const token = localStorage.getItem('token')
    if (!token) {
      // 未登录，重定向到登录页面
      next('/login')
    } else {
      // 已登录，继续访问
      next()
    }
  } else {
    // 不需要认证的页面，直接访问
    next()
  }
})

export default router