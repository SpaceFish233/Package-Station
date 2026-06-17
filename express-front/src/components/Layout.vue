<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const isCollapsed = ref(false)

const staffInfo = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('staffInfo') || '{}')
  } catch { return {} }
})

const allMenuItems = [
  { path: '/', icon: 'DataAnalysis', title: '工作台' },
  { path: '/inbound', icon: 'Download', title: '快递入库' },
  { path: '/outbound', icon: 'Upload', title: '快递出库' },
  { path: '/packages', icon: 'List', title: '包裹查询' },
  { path: '/shelves', icon: 'Grid', title: '货架管理' },
  { path: '/companies', icon: 'Van', title: '快递公司' },
]

// 普通用户只显示自助取件，管理员显示全部菜单
const menuItems = computed(() => {
  const role = staffInfo.value.role ?? 1
  if (role === 1) {
    return [{ path: '/pickup', icon: 'Position', title: '自助取件' }]
  }
  return allMenuItems
})

function handleLogout() {
  localStorage.removeItem('token')
  localStorage.removeItem('staffInfo')
  router.push('/login')
}
</script>

<template>
  <el-container class="layout">
    <!-- 侧栏 -->
    <el-aside :width="isCollapsed ? '64px' : '240px'" class="sidebar">
      <!-- Logo -->
      <div class="sidebar-logo" :class="{ collapsed: isCollapsed }">
        <div class="logo-icon">
          <el-icon :size="28"><Box /></el-icon>
        </div>
        <transition name="fade">
          <div v-if="!isCollapsed" class="logo-text">
            <span class="logo-title">快递驿站</span>
            <span class="logo-subtitle">管理平台</span>
          </div>
        </transition>
      </div>

      <!-- 导航菜单 -->
      <el-menu
        :default-active="route.path"
        :collapse="isCollapsed"
        :collapse-transition="false"
        class="sidebar-menu"
        background-color="transparent"
        text-color="rgba(255,255,255,0.7)"
        active-text-color="#f0a654"
        router
      >
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </el-menu>

      <!-- 底部折叠按钮 -->
      <div class="sidebar-footer">
        <el-button text class="collapse-btn" @click="isCollapsed = !isCollapsed">
          <el-icon :size="18">
            <component :is="isCollapsed ? 'Expand' : 'Fold'" />
          </el-icon>
        </el-button>
      </div>
    </el-aside>

    <!-- 右侧主区域 -->
    <el-container class="main-container">
      <!-- 顶栏 -->
      <el-header class="topbar">
        <div class="topbar-left">
          <span class="breadcrumb-title">{{ menuItems.find(m => m.path === route.path)?.title || '工作台' }}</span>
        </div>
        <div class="topbar-right">
          <el-dropdown trigger="click" @command="handleLogout">
            <div class="user-info">
              <el-avatar :size="32" class="user-avatar">
                {{ staffInfo.realName?.charAt(0) || 'A' }}
              </el-avatar>
              <span class="user-name">{{ staffInfo.realName || staffInfo.username || '管理员' }}</span>
              <el-icon class="user-arrow"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="logout">
                  <el-icon><SwitchButton /></el-icon>退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>
.layout {
  height: 100vh;
  overflow: hidden;
}

/* ---- 侧栏 ---- */
.sidebar {
  background: linear-gradient(195deg, #1a2744 0%, #0f1a30 100%);
  display: flex;
  flex-direction: column;
  transition: width 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.sidebar::after {
  content: '';
  position: absolute;
  top: 0;
  right: 0;
  width: 1px;
  height: 100%;
  background: linear-gradient(180deg, rgba(232,145,58,0.3) 0%, rgba(232,145,58,0) 100%);
}

.sidebar-logo {
  display: flex;
  align-items: center;
  padding: 24px 20px;
  gap: 12px;
  border-bottom: 1px solid rgba(255,255,255,0.06);
  min-height: 72px;
}

.sidebar-logo.collapsed {
  justify-content: center;
  padding: 24px 0;
}

.logo-icon {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, var(--color-accent), #d07e2e);
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(232, 145, 58, 0.35);
}

.logo-text {
  display: flex;
  flex-direction: column;
  white-space: nowrap;
}

.logo-title {
  font-family: var(--font-display);
  font-size: 1.1rem;
  font-weight: 700;
  color: #fff;
  line-height: 1.2;
}

.logo-subtitle {
  font-size: 0.7rem;
  color: rgba(255,255,255,0.45);
  letter-spacing: 0.1em;
  text-transform: uppercase;
}

/* 菜单 */
.sidebar-menu {
  flex: 1;
  border: none !important;
  padding: 12px 8px;
  overflow-y: auto;
}

.sidebar-menu .el-menu-item {
  border-radius: 8px;
  margin-bottom: 4px;
  height: 44px;
  line-height: 44px;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.2s ease;
}

.sidebar-menu .el-menu-item:hover {
  background: rgba(255,255,255,0.06) !important;
}

.sidebar-menu .el-menu-item.is-active {
  background: linear-gradient(135deg, rgba(232,145,58,0.2), rgba(232,145,58,0.08)) !important;
  color: var(--color-accent-light) !important;
  font-weight: 600;
}

.sidebar-menu .el-menu-item.is-active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 20px;
  background: var(--color-accent);
  border-radius: 0 2px 2px 0;
}

.sidebar-footer {
  padding: 12px 8px 16px;
  border-top: 1px solid rgba(255,255,255,0.06);
}

.collapse-btn {
  width: 100%;
  color: rgba(255,255,255,0.5) !important;
  justify-content: center;
}
.collapse-btn:hover {
  color: rgba(255,255,255,0.9) !important;
}

/* ---- 顶栏 ---- */
.topbar {
  background: var(--color-surface);
  border-bottom: 1px solid var(--color-border-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 60px;
  box-shadow: var(--shadow-sm);
  z-index: 10;
}

.breadcrumb-title {
  font-family: var(--font-display);
  font-size: 1.15rem;
  font-weight: 600;
  color: var(--color-primary);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: var(--radius-md);
  transition: background 0.2s;
}

.user-info:hover {
  background: var(--color-surface-alt);
}

.user-avatar {
  background: linear-gradient(135deg, var(--color-primary), var(--color-primary-lighter));
  color: #fff;
  font-family: var(--font-display);
  font-weight: 600;
  font-size: 0.85rem;
}

.user-name {
  font-weight: 500;
  color: var(--color-primary);
  font-size: 0.9rem;
}

.user-arrow {
  color: var(--color-text-muted);
  font-size: 0.75rem;
}

/* ---- 内容区 ---- */
.main-container {
  background: var(--color-bg);
  overflow: hidden;
}

.main-content {
  padding: 0;
  overflow-y: auto;
  height: calc(100vh - 60px);
}

/* ---- 过渡动画 ---- */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
