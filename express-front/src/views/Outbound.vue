<script setup lang="ts">
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { queryPackage, staffOutbound } from '@/api'

const inputCode = ref('')
const searchType = ref<'pickupCode' | 'phone'>('pickupCode')
const loading = ref(false)
const packageInfo = ref<any>(null)
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

async function handleOutbound() {
  if (!packageInfo.value) return
  loading.value = true
  try {
    await staffOutbound({ packageId: packageInfo.value.id, pickupType: 2 })
    ElMessage.success('出库成功')
    packageInfo.value = null
    inputCode.value = ''
    searched.value = false
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
          <el-segmented v-model="searchType" :options="[{ label: '取件码', value: 'pickupCode' }, { label: '手机号', value: 'phone' }]" />
        </el-col>
        <el-col :span="14">
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
          <el-button type="primary" size="large" :loading="loading" @click="handleSearch" style="width: 100%">
            查询包裹
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 结果区 -->
    <el-card v-if="searched && packageInfo" class="result-card" style="margin-top: 20px;">
      <template #header>
        <div style="display: flex; justify-content: space-between; align-items: center;">
          <span>包裹信息</span>
          <el-tag :type="statusType(packageInfo.status)" size="large">{{ statusLabel(packageInfo.status) }}</el-tag>
        </div>
      </template>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="运单号">{{ packageInfo.trackingNumber }}</el-descriptions-item>
        <el-descriptions-item label="取件码">
          <span class="pickup-code-display">{{ packageInfo.pickupCode }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="收件人">{{ packageInfo.receiverName }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ packageInfo.receiverPhone }}</el-descriptions-item>
        <el-descriptions-item label="快递公司">{{ packageInfo.companyName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="货架位置">{{ packageInfo.shelfCode || '-' }}</el-descriptions-item>
        <el-descriptions-item label="重量">{{ packageInfo.weight ? packageInfo.weight + ' KG' : '-' }}</el-descriptions-item>
        <el-descriptions-item label="入库时间">{{ packageInfo.inTime }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ packageInfo.remark || '-' }}</el-descriptions-item>
      </el-descriptions>

      <div v-if="packageInfo.status === 0" style="margin-top: 24px; text-align: center;">
        <el-button type="primary" size="large" :loading="loading" @click="handleOutbound" style="width: 200px;">
          确认出库
        </el-button>
      </div>
    </el-card>

    <el-empty v-else-if="searched && !packageInfo" description="未找到符合条件的包裹" style="margin-top: 40px;" />
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
