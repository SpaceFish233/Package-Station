import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/Login.vue'),
      meta: { public: true },
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/Register.vue'),
      meta: { public: true },
    },
    {
      path: '/pickup',
      name: 'pickup',
      component: () => import('@/views/Pickup.vue'),
    },
    {
      path: '/',
      component: () => import('@/components/Layout.vue'),
      children: [
        { path: '', name: 'dashboard', component: () => import('@/views/Dashboard.vue') },
        { path: 'inbound', name: 'inbound', component: () => import('@/views/Inbound.vue') },
        { path: 'outbound', name: 'outbound', component: () => import('@/views/Outbound.vue') },
        { path: 'packages', name: 'packages', component: () => import('@/views/PackageList.vue') },
        { path: 'shelves', name: 'shelves', component: () => import('@/views/Shelves.vue') },
        { path: 'companies', name: 'companies', component: () => import('@/views/Companies.vue') },
      ],
    },
  ],
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')

  // 未登录：只能访问登录页
  if (!to.meta.public && !token) {
    next('/login')
    return
  }

  // 已登录：根据角色控制可访问页面
  if (token) {
    let role = 1 // 默认普通用户
    try {
      const staffInfo = JSON.parse(localStorage.getItem('staffInfo') || '{}')
      role = staffInfo.role ?? 1
    } catch { /* ignore */ }

    // 普通用户 (role=1) 只能访问 /pickup 和 /login
    if (role === 1) {
      if (to.name === 'login') {
        // 已登录普通用户访问登录页，重定向到取件页
        next('/pickup')
        return
      }
      if (to.name !== 'pickup') {
        next('/pickup')
        return
      }
    }

    // 管理员 (role=0) 访问 /pickup 时重定向到工作台
    if (role === 0 && to.name === 'pickup') {
      next('/')
      return
    }
  }

  next()
})

export default router
