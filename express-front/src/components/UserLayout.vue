<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getNotifications } from '@/api'

const router = useRouter()
const route = useRoute()

const staffInfo = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('staffInfo') || '{}')
  } catch { return {} }
})

const hasNewNotification = ref(false)

// 检查是否有新通知
async function checkNewNotifications() {
  try {
    const res: any = await getNotifications()
    const list = res.data || []
    const lastReadCount = Number(localStorage.getItem('lastReadNotificationCount') || '0')
    if (list.length > lastReadCount) {
      hasNewNotification.value = true
    }
  } catch {}
}

onMounted(checkNewNotifications)

// 进入通知中心页面时清除红点，并记录已读数量
watch(() => route.path, (path) => {
  if (path === '/u/notifications') {
    hasNewNotification.value = false
    // 记录当前通知数量作为已读
    getNotifications().then((res: any) => {
      const list = res.data || []
      localStorage.setItem('lastReadNotificationCount', String(list.length))
    })
  }
})

function handleLogout() {
  localStorage.removeItem('token')
  localStorage.removeItem('staffInfo')
  router.push('/login')
}
</script>

<template>
  <div class="user-layout">
    <!-- 顶部导航栏 -->
    <div class="user-navbar">
      <div class="navbar-left" @click="router.push('/u')" style="cursor: pointer;">
        <el-icon :size="20"><Box /></el-icon>
        <span class="navbar-title">快递驿站</span>
      </div>
      <div class="navbar-center">
        <router-link to="/u" class="nav-link">
          <el-icon><Position /></el-icon>
          <span>自助取件</span>
        </router-link>
        <router-link to="/u/notifications" class="nav-link">
          <el-badge :is-dot="hasNewNotification" :hidden="!hasNewNotification" class="notification-badge">
            <el-icon><Bell /></el-icon>
          </el-badge>
          <span>通知中心</span>
        </router-link>
        <router-link to="/u/center" class="nav-link">
          <el-icon><User /></el-icon>
          <span>用户中心</span>
        </router-link>
      </div>
      <div class="navbar-right">
        <el-dropdown trigger="click" @command="handleLogout">
          <div class="user-info">
            <el-avatar :size="28" class="user-avatar">
              {{ staffInfo.realName?.charAt(0) || 'U' }}
            </el-avatar>
            <span class="user-name">{{ staffInfo.realName || staffInfo.username || '用户' }}</span>
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
    </div>

    <!-- 内容区 -->
    <div class="user-content">
      <router-view />
    </div>
  </div>
</template>

<style scoped>
.user-layout {
  min-height: 100vh;
  background: var(--color-bg);
  display: flex;
  flex-direction: column;
}

/* 顶部导航栏 */
.user-navbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  height: 56px;
  background: #1a2744;
  border-bottom: 1px solid rgba(255,255,255,0.08);
  position: sticky;
  top: 0;
  z-index: 100;
  flex-shrink: 0;
}

.navbar-left {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #fff;
}

.navbar-title {
  font-family: var(--font-display);
  font-size: 1.05rem;
  font-weight: 700;
}

.navbar-center {
  display: flex;
  align-items: center;
  gap: 4px;
}

.nav-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  border-radius: 8px;
  color: rgba(255,255,255,0.7);
  font-size: 0.9rem;
  transition: all 0.2s;
}

.nav-link:hover {
  color: #fff;
  background: rgba(255,255,255,0.1);
}

.nav-link.router-link-exact-active {
  color: #fff;
  background: rgba(232,145,58,0.25);
}

.nav-link .el-icon {
  font-size: 16px;
}

.notification-badge {
  display: inline-flex;
}

.notification-badge :deep(.el-badge__content.is-dot) {
  width: 8px;
  height: 8px;
  right: -2px;
  top: -2px;
}

.navbar-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 8px;
  transition: background 0.2s;
}

.user-info:hover {
  background: rgba(255,255,255,0.1);
}

.user-avatar {
  background: linear-gradient(135deg, var(--color-accent), #d07e2e);
  color: #fff;
  font-weight: 600;
  font-size: 0.8rem;
}

.user-name {
  color: rgba(255,255,255,0.85);
  font-size: 0.85rem;
  font-weight: 500;
}

.user-arrow {
  color: rgba(255,255,255,0.5);
  font-size: 0.7rem;
}

/* 内容区 */
.user-content {
  flex: 1;
  overflow-y: auto;
}
</style>
