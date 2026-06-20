<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getMyPackages, getMyPackagesByStatus, confirmPickup } from '@/api'

const router = useRouter()

const staffInfo = computed(() => {
  try {
    return JSON.parse(localStorage.getItem('staffInfo') || '{}')
  } catch { return {} }
})

function handleLogout() {
  localStorage.removeItem('token')
  localStorage.removeItem('staffInfo')
  router.push('/login')
}

const loading = ref(false)
const packageList = ref<any[]>([])
const showSigned = ref(false)

// 页面加载时自动查询该用户关联的所有包裹（主手机号 + 额外手机号）
onMounted(() => loadPackages())

async function loadPackages() {
  loading.value = true
  try {
    const res: any = showSigned.value
      ? await getMyPackagesByStatus(1)
      : await getMyPackages()
    packageList.value = res.data || []
  } catch {} finally {
    loading.value = false
  }
}

function toggleSigned() {
  showSigned.value = !showSigned.value
  loadPackages()
}

async function handleConfirm(pkg: any) {
  loading.value = true
  try {
    await confirmPickup(pkg.id)
    ElMessage.success('确认收货成功！')
    loadPackages()
  } catch {} finally {
    loading.value = false
  }
}

function statusLabel(status: number) {
  return status === 0 ? '待取件' : status === 1 ? '已收货' : '异常'
}

function statusType(status: number) {
  return status === 0 ? 'warning' : status === 1 ? 'success' : 'danger'
}
</script>

<template>
  <div class="pickup-page">
    <!-- 顶部装饰 -->
    <div class="pickup-header">
      <div class="header-bg"></div>
      <div class="header-content">
        <div class="header-icon">
          <el-icon :size="36"><Box /></el-icon>
        </div>
        <h1 class="header-title">快递取件</h1>
        <p class="header-desc">查看您的包裹信息并确认收货</p>
      </div>
    </div>

    <!-- 取件内容 -->
    <div class="pickup-body">
      <!-- 用户信息 -->
      <div class="user-info-section">
        <el-card>
          <div class="user-phone-info">
            <el-icon><Iphone /></el-icon>
            <span>手机号：<strong>{{ staffInfo.phone || staffInfo.username }}</strong></span>
            <span class="package-count">共 {{ packageList.length }} 个包裹</span>
          </div>
          <div class="user-center-link">
            <el-button text :type="showSigned ? 'warning' : 'primary'" @click="toggleSigned" :loading="loading">
              <el-icon><component :is="showSigned ? 'Box' : 'DocumentChecked'" /></el-icon>
              {{ showSigned ? '查看待取件包裹' : '查看已签收包裹' }}
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 包裹列表 -->
      <div v-if="packageList.length > 0" class="result-section">
        <el-card v-for="pkg in packageList" :key="pkg.id" class="result-card">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>包裹信息</span>
              <el-tag :type="statusType(pkg.status)" size="large">{{ statusLabel(pkg.status) }}</el-tag>
            </div>
          </template>

          <div class="package-detail">
            <div class="detail-highlight">
              <span class="detail-label">取件码</span>
              <span class="detail-value pickup-code">{{ pkg.pickupCode }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">运单号</span>
              <span class="detail-value">{{ pkg.trackingNumber }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">收件人</span>
              <span class="detail-value">{{ pkg.receiverName }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">手机号</span>
              <span class="detail-value">{{ pkg.receiverPhone }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">快递公司</span>
              <span class="detail-value">{{ pkg.companyName || '-' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">入库时间</span>
              <span class="detail-value">{{ pkg.inTime }}</span>
            </div>
          </div>

          <div v-if="pkg.status === 0" class="action-bar">
            <el-button type="primary" size="large" :loading="loading" @click="handleConfirm(pkg)" style="width: 240px; height: 50px; font-size: 1.05rem;">
              确认收货
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 无包裹 -->
      <div v-else class="empty-section">
        <el-card class="empty-card">
          <el-empty description="暂无关联包裹" />
        </el-card>
      </div>
    </div>

    <!-- 底部链接 -->
    <div class="pickup-footer">
      <div class="footer-user">
        <span class="footer-username">{{ staffInfo.realName || staffInfo.username || '用户' }}</span>
        <el-button text type="danger" @click="handleLogout" style="font-size: 0.85rem;">
          <el-icon><SwitchButton /></el-icon>
          退出登录
        </el-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.pickup-page {
  min-height: 100vh;
  background: var(--color-bg);
  display: flex;
  flex-direction: column;
}

/* 顶部 */
.pickup-header {
  background: linear-gradient(160deg, #1a2744 0%, #243556 100%);
  padding: 48px 24px 60px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.header-bg {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255,255,255,0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,0.02) 1px, transparent 1px);
  background-size: 40px 40px;
}

.header-content {
  position: relative;
  z-index: 1;
}

.header-icon {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, var(--color-accent), #d07e2e);
  border-radius: 16px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-bottom: 16px;
  box-shadow: 0 8px 24px rgba(232,145,58,0.35);
}

.header-title {
  font-family: var(--font-display);
  font-size: 1.8rem;
  font-weight: 800;
  color: #fff;
  margin-bottom: 6px;
}

.header-desc {
  color: rgba(255,255,255,0.5);
  font-size: 0.95rem;
}

/* 内容区 */
.pickup-body {
  flex: 1;
  max-width: 720px;
  width: 100%;
  margin: -30px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 1;
}

.user-info-section {
  margin-bottom: 20px;
}

.user-phone-info {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.95rem;
  color: var(--color-primary);
}

.user-phone-info .el-icon {
  color: var(--color-accent);
}

.package-count {
  margin-left: auto;
  color: var(--color-text-muted);
  font-size: 0.85rem;
}

.user-center-link {
  margin-top: 8px;
  font-size: 0.85rem;
}

.link-text {
  color: var(--color-accent);
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

.link-text:hover {
  text-decoration: underline;
}

/* 包裹详情 */
.result-card {
  margin-bottom: 16px;
}

.package-detail {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 28px;
}

.detail-row,
.detail-highlight {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  background: var(--color-surface-alt);
  border-radius: var(--radius-sm);
}

.detail-highlight {
  background: var(--color-accent-glow);
  border: 1px solid rgba(232,145,58,0.2);
}

.detail-label {
  color: var(--color-text-muted);
  font-size: 0.9rem;
}

.detail-value {
  font-weight: 600;
  color: var(--color-primary);
}

.pickup-code {
  font-family: var(--font-display);
  font-size: 1.8rem;
  font-weight: 800;
  color: var(--color-accent);
  letter-spacing: 0.15em;
}

.action-bar {
  text-align: center;
}

/* 空状态 */
.empty-card {
  text-align: center;
  padding: 20px 0;
}

/* 底部 */
.pickup-footer {
  text-align: center;
  padding: 24px;
}

.footer-user {
  display: inline-flex;
  align-items: center;
  gap: 12px;
  color: var(--color-text-muted);
  font-size: 0.85rem;
}

.footer-username {
  font-weight: 500;
}
</style>
