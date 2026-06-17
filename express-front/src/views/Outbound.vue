<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { queryPackage, staffOutbound } from '@/api'

const inputCode = ref('')
const searchType = ref<'trackingNumber' | 'phone'>('trackingNumber')
const loading = ref(false)
const packageList = ref<any[]>([])
const searched = ref(false)

async function handleSearch() {
  if (!inputCode.value.trim()) {
    ElMessage.warning('请输入查询内容')
    return
  }
  loading.value = true
  searched.value = false
  try {
    const params: any = {}
    if (searchType.value === 'trackingNumber') {
      params.trackingNumber = inputCode.value.trim()
    } else {
      params.phone = inputCode.value.trim()
    }
    const res: any = await queryPackage(params)
    packageList.value = res.data || []
    searched.value = true
  } catch {
    packageList.value = []
    searched.value = true
  } finally {
    loading.value = false
  }
}

async function handleOutbound(pkg: any) {
  loading.value = true
  try {
    await staffOutbound({ packageId: pkg.id, pickupType: 2 })
    ElMessage.success('出库成功')
    handleSearch()
  } catch {} finally {
    loading.value = false
  }
}

function statusLabel(s: number) {
  return s === 0 ? '在库' : s === 1 ? '已取件' : '异常'
}
function statusType(s: number) {
  return s === 0 ? 'warning' : s === 1 ? 'success' : 'danger'
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">快递出库</h1>
    </div>

    <!-- 搜索区 -->
    <el-card class="search-card">
      <el-row :gutter="16" align="middle">
        <el-col :span="4">
          <el-segmented v-model="searchType" :options="[{ label: '运单号', value: 'trackingNumber' }, { label: '手机号', value: 'phone' }]" />
        </el-col>
        <el-col :span="14">
          <el-input
            v-model="inputCode"
            :placeholder="searchType === 'trackingNumber' ? '请输入运单号' : '请输入收件人手机号'"
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
          <el-button type="primary" size="large" :loading="loading" @click="handleSearch" style="width: 100%">
            查询包裹
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 结果区 -->
    <div v-if="searched && packageList.length > 0" style="margin-top: 20px;">
      <el-card v-for="pkg in packageList" :key="pkg.id" class="result-card" style="margin-bottom: 16px;">
        <template #header>
          <div style="display: flex; justify-content: space-between; align-items: center;">
            <span>包裹信息</span>
            <el-tag :type="statusType(pkg.status)" size="large">{{ statusLabel(pkg.status) }}</el-tag>
          </div>
        </template>

        <el-descriptions :column="2" border>
          <el-descriptions-item label="运单号">{{ pkg.trackingNumber }}</el-descriptions-item>
          <el-descriptions-item label="取件码">
            <span class="pickup-code-display">{{ pkg.pickupCode }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="收件人">{{ pkg.receiverName }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ pkg.receiverPhone }}</el-descriptions-item>
          <el-descriptions-item label="快递公司">{{ pkg.companyName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="货架位置">{{ pkg.shelfCode || '-' }}</el-descriptions-item>
          <el-descriptions-item label="重量">{{ pkg.weight ? pkg.weight + ' KG' : '-' }}</el-descriptions-item>
          <el-descriptions-item label="入库时间">{{ pkg.inTime }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ pkg.remark || '-' }}</el-descriptions-item>
        </el-descriptions>

        <div v-if="pkg.status === 0" style="margin-top: 24px; text-align: center;">
          <el-button type="primary" size="large" :loading="loading" @click="handleOutbound(pkg)" style="width: 200px;">
            确认出库
          </el-button>
        </div>
      </el-card>
    </div>

    <el-empty v-else-if="searched && packageList.length === 0" description="未找到符合条件的包裹" style="margin-top: 40px;" />
  </div>
</template>

<style scoped>
.search-card :deep(.el-segmented) {
  width: 100%;
}
.pickup-code-display {
  font-family: var(--font-display);
  font-size: 1.3rem;
  font-weight: 800;
  color: var(--color-accent);
  letter-spacing: 0.1em;
}
</style>
