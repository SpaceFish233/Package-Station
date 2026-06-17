<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getStatistics } from '@/api'

const stats = ref({ inCount: 0, outCount: 0, stockCount: 0 })

onMounted(async () => {
  try {
    const res: any = await getStatistics()
    stats.value = res.data
  } catch {}
})
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">工作台</h1>
      <span class="date-label">{{ new Date().toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' }) }}</span>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-grid">
      <div class="stat-card stat-card--inbound">
        <div class="stat-card__icon">
          <el-icon :size="24"><Download /></el-icon>
        </div>
        <div class="stat-card__value">{{ stats.inCount }}</div>
        <div class="stat-card__label">今日入库</div>
      </div>

      <div class="stat-card stat-card--outbound">
        <div class="stat-card__icon">
          <el-icon :size="24"><Upload /></el-icon>
        </div>
        <div class="stat-card__value">{{ stats.outCount }}</div>
        <div class="stat-card__label">今日出库</div>
      </div>

      <div class="stat-card stat-card--stock">
        <div class="stat-card__icon">
          <el-icon :size="24"><Box /></el-icon>
        </div>
        <div class="stat-card__value">{{ stats.stockCount }}</div>
        <div class="stat-card__label">当前库存</div>
      </div>
    </div>

    <!-- 快捷操作 -->
    <el-card class="quick-actions">
      <template #header>
        <span>快捷操作</span>
      </template>
      <div class="actions-grid">
        <router-link to="/inbound" class="action-item">
          <div class="action-icon action-icon--inbound">
            <el-icon :size="28"><Download /></el-icon>
          </div>
          <span class="action-label">快递入库</span>
        </router-link>
        <router-link to="/outbound" class="action-item">
          <div class="action-icon action-icon--outbound">
            <el-icon :size="28"><Upload /></el-icon>
          </div>
          <span class="action-label">快递出库</span>
        </router-link>
        <router-link to="/packages" class="action-item">
          <div class="action-icon action-icon--search">
            <el-icon :size="28"><Search /></el-icon>
          </div>
          <span class="action-label">包裹查询</span>
        </router-link>
        <router-link to="/shelves" class="action-item">
          <div class="action-icon action-icon--shelf">
            <el-icon :size="28"><Grid /></el-icon>
          </div>
          <span class="action-label">货架管理</span>
        </router-link>
      </div>
    </el-card>
  </div>
</template>

<style scoped>
.date-label {
  font-size: 0.9rem;
  color: var(--color-text-muted);
  font-weight: 500;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.quick-actions {
  margin-top: 8px;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding: 24px 16px;
  border-radius: var(--radius-md);
  background: var(--color-surface-alt);
  border: 1px solid var(--color-border-light);
  transition: all 0.25s ease;
  cursor: pointer;
}

.action-item:hover {
  transform: translateY(-3px);
  box-shadow: var(--shadow-md);
  border-color: var(--color-accent);
}

.action-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.action-icon--inbound { background: linear-gradient(135deg, #5b8def, #3d6fd1); }
.action-icon--outbound { background: linear-gradient(135deg, #34c77b, #28a866); }
.action-icon--search { background: linear-gradient(135deg, #a78bfa, #7c5ce0); }
.action-icon--shelf { background: linear-gradient(135deg, var(--color-accent), #d07e2e); }

.action-label {
  font-weight: 600;
  color: var(--color-primary);
  font-size: 0.9rem;
}

@media (max-width: 768px) {
  .stats-grid { grid-template-columns: 1fr; }
  .actions-grid { grid-template-columns: repeat(2, 1fr); }
}
</style>
