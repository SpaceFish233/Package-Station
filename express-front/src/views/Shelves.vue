<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getShelves, addShelf, updateShelf, deleteShelf } from '@/api'

const loading = ref(false)
const tableData = ref<any[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增货架')
const form = ref({ id: null as number | null, shelfCode: '', capacity: 50, status: 1 })

async function loadData() {
  loading.value = true
  try {
    const res: any = await getShelves()
    tableData.value = res.data
  } catch {} finally {
    loading.value = false
  }
}

function handleAdd() {
  form.value = { id: null, shelfCode: '', capacity: 50, status: 1 }
  dialogTitle.value = '新增货架'
  dialogVisible.value = true
}

function handleEdit(row: any) {
  form.value = { ...row }
  dialogTitle.value = '编辑货架'
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!form.value.shelfCode) {
    ElMessage.warning('请输入货架编号')
    return
  }
  try {
    if (form.value.id) {
      await updateShelf(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await addShelf(form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch {}
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm(`确认删除货架 ${row.shelfCode}？`, '确认删除')
  try {
    await deleteShelf(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {}
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">货架管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增货架
      </el-button>
    </div>

    <el-card>
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%;">
        <el-table-column prop="shelfCode" label="货架编号" width="150" />
        <el-table-column prop="capacity" label="容量" width="100" align="center" />
        <el-table-column label="已存放" width="100" align="center">
          <template #default="{ row }">
            <span :style="{ color: row.usedCount >= row.capacity ? 'var(--color-danger)' : 'inherit', fontWeight: 600 }">
              {{ row.usedCount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="使用率" min-width="200">
          <template #default="{ row }">
            <el-progress
              :percentage="row.capacity > 0 ? Math.round(row.usedCount / row.capacity * 100) : 0"
              :color="row.usedCount >= row.capacity ? '#e74c5e' : row.usedCount / row.capacity > 0.8 ? '#f5a623' : '#34c77b'"
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
              {{ row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="460px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="货架编号" required>
          <el-input v-model="form.shelfCode" placeholder="如 A-01" />
        </el-form-item>
        <el-form-item label="容量">
          <el-input-number v-model="form.capacity" :min="1" :max="999" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
