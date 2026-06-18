<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getNotifications } from '@/api'

const notifications = ref<any[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res: any = await getNotifications()
    notifications.value = res.data || []
  } catch {} finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="notifications-page">
    <h2 class="page-title">通知中心</h2>

    <div v-loading="loading">
      <div v-if="notifications.length > 0" class="notification-list">
        <el-card v-for="item in notifications" :key="item.id" class="notification-card">
          <div class="notification-header">
            <h3 class="notification-title">{{ item.title }}</h3>
            <span class="notification-time">{{ item.createTime }}</span>
          </div>
          <div class="notification-content">{{ item.content }}</div>
          <div class="notification-footer">
            <span class="notification-publisher">
              <el-icon><User /></el-icon>
              {{ item.publisherName || '管理员' }}
            </span>
          </div>
        </el-card>
      </div>
      <el-empty v-else description="暂无通知" />
    </div>
  </div>
</template>

<style scoped>
.notifications-page {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}

.page-title {
  font-family: var(--font-display);
  font-size: 1.4rem;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 24px;
}

.notification-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.notification-card {
  transition: box-shadow 0.2s;
}

.notification-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.notification-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--color-primary);
  margin: 0;
}

.notification-time {
  font-size: 0.8rem;
  color: var(--color-text-muted);
  white-space: nowrap;
  margin-left: 16px;
}

.notification-content {
  font-size: 0.95rem;
  color: var(--color-text-regular);
  line-height: 1.6;
  white-space: pre-wrap;
}

.notification-footer {
  margin-top: 12px;
  padding-top: 12px;
  border-top: 1px solid var(--color-border-lighter);
}

.notification-publisher {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 0.85rem;
  color: var(--color-text-muted);
}

.notification-publisher .el-icon {
  color: var(--color-accent);
}
</style>
