<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserInfo, updateProfile, updatePassword, getUserPhones, addUserPhone, deleteUserPhone } from '@/api'

// 用户信息
const userInfo = ref<any>({})
const nicknameForm = ref({ realName: '' })
const passwordForm = ref({ oldPassword: '', newPassword: '', confirmPassword: '' })
const phoneForm = ref({ phone: '' })
const phoneList = ref<any[]>([])
const loading = ref(false)

// 加载用户信息
async function loadUserInfo() {
  try {
    const res: any = await getUserInfo()
    userInfo.value = res.data
    nicknameForm.value.realName = res.data.realName || ''
  } catch {}
}

// 加载额外手机号
async function loadPhones() {
  try {
    const res: any = await getUserPhones()
    phoneList.value = res.data || []
  } catch {}
}

onMounted(() => {
  loadUserInfo()
  loadPhones()
})

// 修改昵称
async function handleUpdateNickname() {
  if (!nicknameForm.value.realName.trim()) {
    ElMessage.warning('昵称不能为空')
    return
  }
  loading.value = true
  try {
    await updateProfile({ realName: nicknameForm.value.realName.trim() })
    ElMessage.success('昵称修改成功')
    // 更新 localStorage 中的 staffInfo
    const staffInfo = JSON.parse(localStorage.getItem('staffInfo') || '{}')
    staffInfo.realName = nicknameForm.value.realName.trim()
    localStorage.setItem('staffInfo', JSON.stringify(staffInfo))
    loadUserInfo()
  } catch {} finally {
    loading.value = false
  }
}

// 修改密码
async function handleUpdatePassword() {
  if (!passwordForm.value.oldPassword || !passwordForm.value.newPassword) {
    ElMessage.warning('请填写完整密码信息')
    return
  }
  if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
    ElMessage.warning('两次密码不一致')
    return
  }
  if (passwordForm.value.newPassword.length < 6) {
    ElMessage.warning('新密码长度不能少于6位')
    return
  }
  loading.value = true
  try {
    await updatePassword({
      oldPassword: passwordForm.value.oldPassword,
      newPassword: passwordForm.value.newPassword,
    })
    ElMessage.success('密码修改成功，请重新登录')
    passwordForm.value = { oldPassword: '', newPassword: '', confirmPassword: '' }
  } catch {} finally {
    loading.value = false
  }
}

// 添加手机号
async function handleAddPhone() {
  if (!phoneForm.value.phone) {
    ElMessage.warning('请输入手机号')
    return
  }
  if (!/^1\d{10}$/.test(phoneForm.value.phone)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  loading.value = true
  try {
    await addUserPhone({ phone: phoneForm.value.phone })
    ElMessage.success('手机号添加成功')
    phoneForm.value.phone = ''
    loadPhones()
  } catch {} finally {
    loading.value = false
  }
}

// 删除手机号
async function handleDeletePhone(id: number) {
  loading.value = true
  try {
    await deleteUserPhone(id)
    ElMessage.success('手机号已删除')
    loadPhones()
  } catch {} finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="user-center">
    <h2 class="page-title">用户中心</h2>

    <!-- 基本信息 -->
    <el-card class="section-card">
      <template #header>
        <div class="card-header">
          <el-icon><User /></el-icon>
          <span>基本信息</span>
        </div>
      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="登录账号">{{ userInfo.username }}</el-descriptions-item>
        <el-descriptions-item label="注册手机号">{{ userInfo.phone }}</el-descriptions-item>
        <el-descriptions-item label="当前昵称">{{ userInfo.realName || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <!-- 修改昵称 -->
    <el-card class="section-card">
      <template #header>
        <div class="card-header">
          <el-icon><Edit /></el-icon>
          <span>修改昵称</span>
        </div>
      </template>
      <el-form :inline="true" @submit.prevent="handleUpdateNickname">
        <el-form-item label="新昵称">
          <el-input v-model="nicknameForm.realName" placeholder="请输入新昵称" maxlength="50" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleUpdateNickname">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 修改密码 -->
    <el-card class="section-card">
      <template #header>
        <div class="card-header">
          <el-icon><Lock /></el-icon>
          <span>修改密码</span>
        </div>
      </template>
      <el-form label-width="100px" @submit.prevent="handleUpdatePassword" style="max-width: 480px;">
        <el-form-item label="原密码">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="请输入原密码" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="请输入新密码（不少于6位）" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="请再次输入新密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleUpdatePassword">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 额外手机号管理 -->
    <el-card class="section-card">
      <template #header>
        <div class="card-header">
          <el-icon><Iphone /></el-icon>
          <span>额外手机号管理</span>
          <span class="card-desc">添加手机号后可接收该手机号关联的包裹信息</span>
        </div>
      </template>
      <el-form :inline="true" @submit.prevent="handleAddPhone" style="margin-bottom: 16px;">
        <el-form-item>
          <el-input v-model="phoneForm.phone" placeholder="请输入手机号" maxlength="11" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleAddPhone">添加</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="phoneList" stripe style="width: 100%;">
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="createTime" label="添加时间" />
        <el-table-column label="操作" width="120">
          <template #default="{ row }">
            <el-button type="danger" size="small" :loading="loading" @click="handleDeletePhone(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-empty v-if="phoneList.length === 0" description="暂未添加额外手机号" :image-size="80" />
    </el-card>
  </div>
</template>

<style scoped>
.user-center {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}

.page-title {
  font-family: var(--font-display);
  font-size: 1.4rem;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 24px;
}

.section-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  font-size: 1rem;
}

.card-header .el-icon {
  color: var(--color-accent);
}

.card-desc {
  font-size: 0.8rem;
  color: var(--color-text-muted);
  font-weight: 400;
  margin-left: 8px;
}
</style>
