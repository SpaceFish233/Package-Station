<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { inboundPackage, getCompanies, getShelves } from '@/api'

const loading = ref(false)
const companies = ref<any[]>([])
const shelves = ref<any[]>([])
const showResult = ref(false)
const resultData = ref<any>({})

const form = ref({
  trackingNumber: '',
  receiverName: '',
  receiverPhone: '',
  expressCompanyId: null as number | null,
  weight: null as number | null,
  shelfId: null as number | null,
  remark: '',
})

onMounted(async () => {
  const [cRes, sRes]: any = await Promise.all([getCompanies(), getShelves()])
  companies.value = cRes.data
  shelves.value = sRes.data.filter((s: any) => s.status === 1 && s.usedCount < s.capacity)
})

async function handleSubmit() {
  if (!form.value.trackingNumber || !form.value.receiverName || !form.value.receiverPhone) {
    ElMessage.warning('请填写必填项')
    return
  }
  loading.value = true
  try {
    const res: any = await inboundPackage(form.value)
    resultData.value = res.data
    showResult.value = true
    ElMessage.success('入库成功')
    // 刷新货架列表
    const sRes: any = await getShelves()
    shelves.value = sRes.data.filter((s: any) => s.status === 1 && s.usedCount < s.capacity)
  } catch {} finally {
    loading.value = false
  }
}

function handleReset() {
  form.value = { trackingNumber: '', receiverName: '', receiverPhone: '', expressCompanyId: null, weight: null, shelfId: null, remark: '' }
  showResult.value = false
  resultData.value = {}
}
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">快递入库</h1>
    </div>

    <el-row :gutter="24">
      <el-col :span="14">
        <el-card>
          <template #header><span>入库信息填写</span></template>
          <el-form :model="form" label-width="100px" label-position="right">
            <el-form-item label="运单号" required>
              <el-input v-model="form.trackingNumber" placeholder="请输入运单号" />
            </el-form-item>
            <el-form-item label="收件人" required>
              <el-input v-model="form.receiverName" placeholder="请输入收件人姓名" />
            </el-form-item>
            <el-form-item label="手机号" required>
              <el-input v-model="form.receiverPhone" placeholder="请输入收件人手机号" maxlength="11" />
            </el-form-item>
            <el-form-item label="快递公司">
              <el-select v-model="form.expressCompanyId" placeholder="请选择快递公司" style="width: 100%">
                <el-option v-for="c in companies" :key="c.id" :label="c.companyName" :value="c.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="重量(KG)">
              <el-input-number v-model="form.weight" :min="0" :precision="2" :step="0.5" style="width: 100%" />
            </el-form-item>
            <el-form-item label="存放货架">
              <el-select v-model="form.shelfId" placeholder="请选择货架" style="width: 100%">
                <el-option v-for="s in shelves" :key="s.id" :label="`${s.shelfCode} (剩余${s.capacity - s.usedCount})`" :value="s.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="可选备注信息" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="loading" @click="handleSubmit" style="width: 120px">
                确认入库
              </el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="10">
        <el-card v-if="showResult" class="result-card">
          <template #header><span>入库结果</span></template>
          <div class="result-success">
            <el-icon :size="48" color="#34c77b"><CircleCheckFilled /></el-icon>
            <p class="result-msg">入库成功</p>
          </div>
          <div class="result-info">
            <div class="info-row">
              <span class="info-label">取件码</span>
              <span class="info-value pickup-code">{{ resultData.pickupCode }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">运单号</span>
              <span class="info-value">{{ resultData.trackingNumber }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">收件人</span>
              <span class="info-value">{{ resultData.receiverName }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">货架位置</span>
              <span class="info-value">{{ resultData.shelfCode || '未分配' }}</span>
            </div>
          </div>
        </el-card>

        <el-card v-else class="result-card placeholder-card">
          <div class="placeholder-content">
            <el-icon :size="64" color="#e0dbd4"><Document /></el-icon>
            <p>填写信息并提交后，入库结果将在此显示</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped>
.result-card {
  height: 100%;
}
.result-success {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0 24px;
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: 20px;
}
.result-msg {
  font-family: var(--font-display);
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--color-success);
  margin-top: 10px;
}
.result-info {
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.info-label {
  color: var(--color-text-muted);
  font-size: 0.9rem;
}
.info-value {
  font-weight: 600;
  color: var(--color-primary);
}
.pickup-code {
  font-family: var(--font-display);
  font-size: 1.6rem;
  font-weight: 800;
  color: var(--color-accent);
  letter-spacing: 0.15em;
}
.placeholder-card {
  height: 100%;
}
.placeholder-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 300px;
  color: var(--color-text-muted);
  gap: 16px;
}
.placeholder-content p {
  font-size: 0.9rem;
}
</style>
