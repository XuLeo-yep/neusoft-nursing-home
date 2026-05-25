import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    component: () => import('@/components/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'customer/checkin',
        name: 'CheckIn',
        component: () => import('@/views/customer/CheckIn.vue'),
        meta: { title: '入住登记', role: ['admin'] }
      },
      {
        path: 'customer/checkout',
        name: 'CheckOut',
        component: () => import('@/views/customer/CheckOut.vue'),
        meta: { title: '退住登记', role: ['admin'] }
      },
      {
        path: 'customer/goout',
        name: 'GoOut',
        component: () => import('@/views/customer/GoOut.vue'),
        meta: { title: '外出登记', role: ['admin'] }
      },
      {
        path: 'bed/diagram',
        name: 'BedDiagram',
        component: () => import('@/views/bed/BedDiagram.vue'),
        meta: { title: '床位示意图', role: ['admin'] }
      },
      {
        path: 'bed/manage',
        name: 'BedManage',
        component: () => import('@/views/bed/BedManage.vue'),
        meta: { title: '床位管理', role: ['admin'] }
      },
      {
        path: 'nurse/level',
        name: 'NurseLevel',
        component: () => import('@/views/nurse/NurseLevel.vue'),
        meta: { title: '护理级别', role: ['admin'] }
      },
      {
        path: 'nurse/project',
        name: 'NurseProject',
        component: () => import('@/views/nurse/NurseProject.vue'),
        meta: { title: '护理项目', role: ['admin'] }
      },
      {
        path: 'nurse/setting',
        name: 'NurseSetting',
        component: () => import('@/views/nurse/NurseSetting.vue'),
        meta: { title: '客户护理设置', role: ['admin'] }
      },
      {
        path: 'nurse/record',
        name: 'NurseRecord',
        component: () => import('@/views/nurse/NurseRecord.vue'),
        meta: { title: '护理记录', role: ['admin', 'health_manager'] }
      },
      {
        path: 'manager/service',
        name: 'ServiceObject',
        component: () => import('@/views/manager/ServiceObject.vue'),
        meta: { title: '设置服务对象', role: ['admin'] }
      },
      {
        path: 'manager/daily',
        name: 'DailyNurse',
        component: () => import('@/views/manager/DailyNurse.vue'),
        meta: { title: '日常护理', role: ['health_manager'] }
      },
      {
        path: 'user/manage',
        name: 'UserManage',
        component: () => import('@/views/user/UserManage.vue'),
        meta: { title: '用户管理', role: ['admin'] }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = to.meta.title || '东软颐养中心'
  const token = localStorage.getItem('token')
  if (to.path === '/login') {
    next()
  } else {
    if (!token) {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
