<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCompanies, addCompany, updateCompany, deleteCompany } from '@/api'

const loading = ref(false)
const tableData = ref<any[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增快递公司')
const form = ref({ id: null as number | null, companyName: '', code: '' })

async function loadData() {
  loading.value = true
  try {
    const res: any = await getCompanies()
    tableData.value = res.data
  } catch {} finally {
    loading.value = false
  }
}

function handleAdd() {
  form.value = { id: null, companyName: '', code: '' }
  dialogTitle.value = '新增快递公司'
  dialogVisible.value = true
}

function handleEdit(row: any) {
  form.value = { ...row }
  dialogTitle.value = '编辑快递公司'
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!form.value.companyName) {
    ElMessage.warning('请输入公司名称')
    return
  }
  try {
    if (form.value.id) {
      await updateCompany(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await addCompany(form.value)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch {}
}

async function handleDelete(row: any) {
  await ElMessageBox.confirm(`确认删除快递公司 ${row.companyName}？`, '确认删除')
  try {
    await deleteCompany(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch {}
}

onMounted(loadData)
</script>

<template>
  <div class="page-container">
    <div class="page-header">
      <h1 class="page-title">快递公司管理</h1>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>新增公司
      </el-button>
    </div>

    <el-card>
      <el-table :data="tableData" v-loading="loading" stripe style="width: 100%;">
        <el-table-column prop="companyName" label="公司名称" min-width="200" />
        <el-table-column prop="code" label="编码" width="120" align="center">
          <template #default="{ row }">
            <el-tag type="info" size="small">{{ row.code }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
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
        <el-form-item label="公司名称" required>
          <el-input v-model="form.companyName" placeholder="如 顺丰速运" />
        </el-form-item>
        <el-form-item label="编码">
          <el-input v-model="form.code" placeholder="如 SF" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>
