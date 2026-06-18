<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getNotifications, createNotification, updateNotification, deleteNotification } from '@/api'

const notifications = ref<any[]>([])
const loading = ref(false)
const dialogVisible = ref(false)
const editingId = ref<number | null>(null)
const form = ref({ title: '', content: '' })

// 加载通知列表
async function loadNotifications() {
  loading.value = true
  try {
    const res: any = await getNotifications()
    notifications.value = res.data || []
  } catch {} finally {
    loading.value = false
  }
}

onMounted(loadNotifications)

// 打开新建对话框
function handleCreate() {
  editingId.value = null
  form.value = { title: '', content: '' }
  dialogVisible.value = true
}

// 打开编辑对话框
function handleEdit(item: any) {
  editingId.value = item.id
  form.value = { title: item.title, content: item.content || '' }
  dialogVisible.value = true
}

// 提交（新建/编辑）
async function handleSubmit() {
  if (!form.value.title.trim()) {
    ElMessage.warning('请输入通知标题')
    return
  }
  loading.value = true
  try {
    if (editingId.value) {
      await updateNotification(editingId.value, form.value)
      ElMessage.success('通知已更新')
    } else {
      await createNotification(form.value)
      ElMessage.success('通知已发布')
    }
    dialogVisible.value = false
    loadNotifications()
  } catch {} finally {
    loading.value = false
  }
}

// 删除通知
async function handleDelete(id: number) {
  try {
    await ElMessageBox.confirm('确定要删除这条通知吗？', '提示', {
      type: 'warning',
    })
    loading.value = true
    await deleteNotification(id)
    ElMessage.success('通知已删除')
    loadNotifications()
  } catch {} finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="admin-notifications">
    <div class="page-header">
      <h2 class="page-title">通知管理</h2>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        发布通知
      </el-button>
    </div>

    <el-table :data="notifications" v-loading="loading" stripe style="width: 100%;">
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="content" label="内容" min-width="300" show-overflow-tooltip />
      <el-table-column prop="publisherName" label="发布者" width="120" />
      <el-table-column prop="createTime" label="发布时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-empty v-if="!loading && notifications.length === 0" description="暂无通知" />

    <!-- 新建/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="editingId ? '编辑通知' : '发布通知'"
      width="600px"
    >
      <el-form label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入通知标题" maxlength="100" />
        </el-form-item>
        <el-form-item label="内容">
          <el-input
            v-model="form.content"
            type="textarea"
            placeholder="请输入通知内容"
            :rows="6"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          {{ editingId ? '保存修改' : '发布' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.admin-notifications {
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-family: var(--font-display);
  font-size: 1.4rem;
  font-weight: 700;
  color: var(--color-primary);
  margin: 0;
}
</style>
