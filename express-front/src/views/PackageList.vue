<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getPackageList, staffOutbound } from '@/api'

const loading = ref(false)
const tableData = ref<any[]>([])
const total = ref(0)
const queryParams = ref({
  trackingNumber: '',
  pickupCode: '',
  phone: '',
  status: null as number | null,
  page: 1,
  size: 10,
})

async function loadData() {
  loading.value = true
  try {
    const res: any = await getPackageList(queryParams.value)
    tableData.value = res.data.list
    total.value = res.data.total
  } catch {} finally {
    loading.value = false
  }
}

function handleSearch() {
  queryParams.value.page = 1
  loadData()
}

function handlePageChange(page: number) {
  queryParams.value.page = page
  loadData()
}

function handleSizeChange(size: number) {
  queryParams.value.size = size
  queryParams.value.page = 1
  loadData()
}

async function handleOutbound(row: any) {
  await ElMessageBox.confirm(`确认对运单号 ${row.trackingNumber} 执行出库操作？`, '确认出库')
  try {
    await staffOutbound({ packageId: row.id, pickupType: 2 })
    ElMessage.success('出库成功')
    loadData()
  } catch {}
}

function statusLabel(s: number) {
  return s === 0 ? '在库' : s === 1 ? '已取件' : '异常'
}
function statusType(s: number) {
  return s === 0 ? 'warning' : s === 1 ? 'success' : 'danger'
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">包裹查询</h1>
    </div>

    <!-- 搜索栏 -->
    <el-card style="margin-bottom: 20px;">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="运单号">
          <el-input v-model="queryParams.trackingNumber" placeholder="运单号" clearable />
        </el-form-item>
        <el-form-item label="取件码">
          <el-input v-model="queryParams.pickupCode" placeholder="取件码" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="queryParams.phone" placeholder="手机号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 120px;">
            <el-option label="在库" :value="0" />
            <el-option label="已取件" :value="1" />
            <el-option label="异常" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>查询
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card>
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%;">
        <el-table-column prop="trackingNumber" label="运单号" min-width="150" />
        <el-table-column label="取件码" width="120">
          <template #default="{ row }">
            <span style="font-family: var(--font-display); font-weight: 700; color: var(--color-accent);">{{ row.pickupCode }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="receiverName" label="收件人" width="100" />
        <el-table-column prop="receiverPhone" label="手机号" width="130" />
        <el-table-column prop="companyName" label="快递公司" width="120" />
        <el-table-column prop="shelfCode" label="货架" width="80" />
        <el-table-column label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="inTime" label="入库时间" width="170" />
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{ row }">
            <el-button v-if="row.status === 0" type="primary" link size="small" @click="handleOutbound(row)">
              出库
            </el-button>
            <span v-else style="color: var(--color-text-muted); font-size: 0.85rem;">-</span>
          </template>
        </el-table-column>
      </el-table>

      <div style="margin-top: 16px; display: flex; justify-content: flex-end;">
        <el-pagination
          v-model:current-page="queryParams.page"
          v-model:page-size="queryParams.size"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>
