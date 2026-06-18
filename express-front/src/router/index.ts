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
    // 管理员页面（左侧菜单布局）
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
        { path: 'admin/notifications', name: 'admin-notifications', component: () => import('@/views/AdminNotifications.vue') },
      ],
    },
    // 普通用户页面（顶部导航布局）
    {
      path: '/u',
      component: () => import('@/components/UserLayout.vue'),
      children: [
        { path: '', name: 'pickup', component: () => import('@/views/Pickup.vue') },
        { path: 'notifications', name: 'user-notifications', component: () => import('@/views/UserNotifications.vue') },
        { path: 'center', name: 'user-center', component: () => import('@/views/UserCenter.vue') },
      ],
    },
  ],
})

// 路由守卫
router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')

  // 未登录：只能访问公开页面
  if (!to.meta.public && !token) {
    next('/login')
    return
  }

  // 已登录：根据角色分流
  if (token) {
    let role = 1
    try {
      const staffInfo = JSON.parse(localStorage.getItem('staffInfo') || '{}')
      role = staffInfo.role ?? 1
    } catch { /* ignore */ }

    // 普通用户 (role=1)
    if (role === 1) {
      // 访问登录页 → 重定向到取件页
      if (to.name === 'login') {
        next('/u')
        return
      }
      // 访问管理员页面 → 重定向到取件页
      if (to.path !== '/u' && !to.path.startsWith('/u/')) {
        next('/u')
        return
      }
    }

    // 管理员 (role=0)
    if (role === 0) {
      // 访问用户页面 → 重定向到工作台
      if (to.path === '/u' || to.path.startsWith('/u/')) {
        next('/')
        return
      }
    }
  }

  next()
})

export default router
