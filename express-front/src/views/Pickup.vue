<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { queryPackage, selfOutbound } from '@/api'

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

const inputCode = ref('')
const searchType = ref<'pickupCode' | 'phone'>('pickupCode')
const loading = ref(false)
const packageInfo = ref<any>(null)
const searched = ref(false)
const pickupSuccess = ref(false)

async function handleSearch() {
  if (!inputCode.value.trim()) {
    ElMessage.warning('请输入查询内容')
    return
  }
  loading.value = true
  searched.value = false
  pickupSuccess.value = false
  try {
    const params: any = {}
    if (searchType.value === 'pickupCode') {
      params.pickupCode = inputCode.value.trim()
    } else {
      params.phone = inputCode.value.trim()
    }
    const res: any = await queryPackage(params)
    packageInfo.value = res.data
    searched.value = true
  } catch {
    packageInfo.value = null
    searched.value = true
  } finally {
    loading.value = false
  }
}

async function handlePickup() {
  if (!packageInfo.value) return
  loading.value = true
  try {
    const payload: any = { pickupType: 1 }
    if (searchType.value === 'pickupCode') {
      payload.pickupCode = inputCode.value.trim()
    } else {
      payload.receiverPhone = inputCode.value.trim()
    }
    await selfOutbound(payload)
    ElMessage.success('取件成功！')
    pickupSuccess.value = true
    packageInfo.value = null
  } catch {} finally {
    loading.value = false
  }
}

function handleReset() {
  inputCode.value = ''
  packageInfo.value = null
  searched.value = false
  pickupSuccess.value = false
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
        <p class="header-desc">输入取件码或手机号查询您的包裹</p>
      </div>
    </div>

    <!-- 取件内容 -->
    <div class="pickup-body">
      <!-- 搜索卡 -->
      <div class="search-section">
        <el-card class="search-card">
          <el-row :gutter="12" align="middle">
            <el-col :span="5">
              <el-segmented v-model="searchType" :options="[{ label: '取件码', value: 'pickupCode' }, { label: '手机号', value: 'phone' }]" style="width: 100%;" />
            </el-col>
            <el-col :span="13">
              <el-input
                v-model="inputCode"
                :placeholder="searchType === 'pickupCode' ? '请输入6位取件码' : '请输入收件人手机号'"
                size="large"
                clearable
                @keyup.enter="handleSearch"
              >
                <template #prefix>
                  <el-icon><Search /></el-icon>
                </template>
              </el-input>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" size="large" :loading="loading" @click="handleSearch" style="width: 100%;">
                查询包裹
              </el-button>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <!-- 取件成功 -->
      <div v-if="pickupSuccess" class="success-section">
        <el-card class="success-card">
          <div class="success-content">
            <el-icon :size="72" color="#34c77b"><CircleCheckFilled /></el-icon>
            <h2 class="success-title">取件成功</h2>
            <p class="success-desc">请妥善保管您的包裹，祝您生活愉快！</p>
            <el-button type="primary" size="large" @click="handleReset" style="margin-top: 20px;">
              继续查询
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 包裹信息 -->
      <div v-else-if="searched && packageInfo" class="result-section">
        <el-card class="result-card">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>包裹信息</span>
              <el-tag type="warning" size="large">待取件</el-tag>
            </div>
          </template>

          <div class="package-detail">
            <div class="detail-row">
              <span class="detail-label">运单号</span>
              <span class="detail-value">{{ packageInfo.trackingNumber }}</span>
            </div>
            <div class="detail-highlight">
              <span class="detail-label">取件码</span>
              <span class="detail-value pickup-code">{{ packageInfo.pickupCode }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">收件人</span>
              <span class="detail-value">{{ packageInfo.receiverName }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">快递公司</span>
              <span class="detail-value">{{ packageInfo.companyName || '-' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">货架位置</span>
              <span class="detail-value shelf-code">{{ packageInfo.shelfCode || '-' }}</span>
            </div>
            <div class="detail-row">
              <span class="detail-label">入库时间</span>
              <span class="detail-value">{{ packageInfo.inTime }}</span>
            </div>
          </div>

          <div class="action-bar">
            <el-button type="primary" size="large" :loading="loading" @click="handlePickup" style="width: 240px; height: 50px; font-size: 1.05rem;">
              确认取件
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 未找到 -->
      <div v-else-if="searched && !packageInfo" class="empty-section">
        <el-card class="empty-card">
          <el-empty description="未找到符合条件的包裹，请核实后重试">
            <el-button type="primary" @click="handleReset">重新查询</el-button>
          </el-empty>
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

.search-section {
  margin-bottom: 24px;
}

/* 成功 */
.success-card {
  text-align: center;
}

.success-content {
  padding: 32px 0;
}

.success-title {
  font-family: var(--font-display);
  font-size: 1.5rem;
  font-weight: 700;
  color: var(--color-success);
  margin: 16px 0 8px;
}

.success-desc {
  color: var(--color-text-muted);
}

/* 包裹详情 */
.result-card {
  margin-bottom: 24px;
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

.shelf-code {
  font-family: var(--font-display);
  font-weight: 700;
  color: var(--color-info);
  font-size: 1.05rem;
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
