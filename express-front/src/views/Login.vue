<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api'

const router = useRouter()
const loading = ref(false)
const form = ref({ username: '', password: '' })

// 测试账号列表
const testAccounts = [
  { label: '管理员', username: 'admin', password: '123456' },
  { label: '测试用户', username: '18864667916', password: '123456' },
]

function fillAccount(account: { username: string; password: string }) {
  form.value.username = account.username
  form.value.password = account.password
}

async function handleLogin() {
  if (!form.value.username || !form.value.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res: any = await login(form.value)
    localStorage.setItem('token', res.data.token)
    localStorage.setItem('staffInfo', JSON.stringify(res.data.staffInfo))
    ElMessage.success({ message: '登录成功', duration: 2000 })
    // 根据角色跳转：管理员->工作台，普通用户->取件页
    const role = res.data.staffInfo?.role ?? 1
    if (role === 0) {
      router.push('/')
    } else {
      router.push('/u')
    }
  } catch {
    // 错误已在拦截器处理
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <!-- 装饰背景 -->
    <div class="login-bg">
      <div class="bg-grid"></div>
      <div class="bg-glow bg-glow--1"></div>
      <div class="bg-glow bg-glow--2"></div>
    </div>

    <div class="login-container">
      <!-- 左侧品牌区 -->
      <div class="login-brand">
        <div class="brand-content">
          <div class="brand-icon">
            <el-icon :size="48"><Box /></el-icon>
          </div>
          <h1 class="brand-title">快递驿站</h1>
          <p class="brand-subtitle">出入库与取件管理系统</p>
          <div class="brand-features">
            <div class="feature-item">
              <el-icon><CircleCheck /></el-icon>
              <span>高效出入库管理</span>
            </div>
            <div class="feature-item">
              <el-icon><CircleCheck /></el-icon>
              <span>智能取件码生成</span>
            </div>
            <div class="feature-item">
              <el-icon><CircleCheck /></el-icon>
              <span>实时库存统计</span>
            </div>
          </div>
        </div>
        <div class="brand-deco"></div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-form-wrapper">
        <div class="form-header">
          <h2 class="form-title">欢迎回来</h2>
          <p class="form-desc">请输入您的账号登录系统</p>
        </div>

        <el-form :model="form" @keyup.enter="handleLogin" class="login-form">
          <el-form-item>
            <el-input
              v-model="form.username"
              placeholder="请输入用户名"
              prefix-icon="User"
              size="large"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>
          <el-button
            type="primary"
            size="large"
            :loading="loading"
            class="login-btn"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form>

        <!-- 测试账号快捷登录 -->
        <div class="test-accounts">
          <div class="test-title">测试账号快捷登录</div>
          <div class="test-buttons">
            <el-button
              v-for="account in testAccounts"
              :key="account.username"
              size="small"
              plain
              @click="fillAccount(account)"
            >
              {{ account.label }}
            </el-button>
          </div>
        </div>

        <!-- 注册链接 -->
        <div class="form-footer" style="margin-top: 16px;">
          <span class="footer-text">没有账号？</span>
          <router-link to="/register" class="footer-link">手机号注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #0f1a30;
  position: relative;
  overflow: hidden;
}

/* 背景装饰 */
.login-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255,255,255,0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255,255,255,0.03) 1px, transparent 1px);
  background-size: 60px 60px;
}

.bg-glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
}

.bg-glow--1 {
  width: 600px;
  height: 600px;
  background: rgba(232, 145, 58, 0.12);
  top: -200px;
  right: -100px;
}

.bg-glow--2 {
  width: 400px;
  height: 400px;
  background: rgba(91, 141, 239, 0.08);
  bottom: -100px;
  left: -100px;
}

/* 登录容器 */
.login-container {
  display: flex;
  width: 900px;
  min-height: 520px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 25px 80px rgba(0,0,0,0.5);
  position: relative;
  z-index: 1;
  animation: fadeSlideUp 0.6s ease-out;
}

/* 左侧品牌 */
.login-brand {
  flex: 1;
  background: linear-gradient(160deg, #1a2744 0%, #243556 100%);
  padding: 48px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.brand-deco {
  position: absolute;
  bottom: -60px;
  right: -60px;
  width: 200px;
  height: 200px;
  border: 3px solid rgba(232,145,58,0.15);
  border-radius: 50%;
}

.brand-deco::before {
  content: '';
  position: absolute;
  top: 20px;
  left: 20px;
  width: 160px;
  height: 160px;
  border: 2px solid rgba(232,145,58,0.1);
  border-radius: 50%;
}

.brand-icon {
  width: 72px;
  height: 72px;
  background: linear-gradient(135deg, var(--color-accent), #d07e2e);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  margin-bottom: 24px;
  box-shadow: 0 8px 24px rgba(232,145,58,0.35);
}

.brand-title {
  font-family: var(--font-display);
  font-size: 2rem;
  font-weight: 800;
  color: #fff;
  margin-bottom: 8px;
}

.brand-subtitle {
  font-size: 0.95rem;
  color: rgba(255,255,255,0.5);
  margin-bottom: 36px;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  color: rgba(255,255,255,0.7);
  font-size: 0.9rem;
}

.feature-item .el-icon {
  color: var(--color-accent);
}

/* 右侧表单 */
.login-form-wrapper {
  flex: 1;
  background: #fff;
  padding: 48px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.form-header {
  margin-bottom: 32px;
}

.form-title {
  font-family: var(--font-display);
  font-size: 1.6rem;
  font-weight: 700;
  color: var(--color-primary);
  margin-bottom: 6px;
}

.form-desc {
  color: var(--color-text-muted);
  font-size: 0.9rem;
}

.login-form {
  margin-bottom: 24px;
}

.login-form .el-form-item {
  margin-bottom: 20px;
}

.login-btn {
  width: 100%;
  height: 46px;
  font-size: 1rem;
  font-weight: 700;
  letter-spacing: 0.1em;
  border-radius: 10px !important;
  box-shadow: var(--shadow-accent);
}

.form-footer {
  text-align: center;
  font-size: 0.9rem;
  color: var(--color-text-muted);
}

.footer-link {
  color: var(--color-accent);
  font-weight: 500;
  margin-left: 4px;
}

.footer-link:hover {
  text-decoration: underline;
}

/* 测试账号 */
.test-accounts {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px dashed var(--color-border-light);
  text-align: center;
}

.test-title {
  font-size: 0.8rem;
  color: var(--color-text-muted);
  margin-bottom: 12px;
}

.test-buttons {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 8px;
}

.test-buttons .el-button {
  font-size: 0.8rem;
}

@keyframes fadeSlideUp {
  from { opacity: 0; transform: translateY(24px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 768px) {
  .login-container {
    flex-direction: column;
    width: 92%;
    min-height: auto;
  }
  .login-brand {
    padding: 32px;
  }
  .login-form-wrapper {
    padding: 32px;
  }
}
</style>
