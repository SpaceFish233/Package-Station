<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api'

const router = useRouter()
const loading = ref(false)
const form = ref({
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
})

async function handleRegister() {
  if (!form.value.phone || !form.value.password || !form.value.realName) {
    ElMessage.warning('请填写所有必填项')
    return
  }
  if (form.value.password !== form.value.confirmPassword) {
    ElMessage.warning('两次密码不一致')
    return
  }
  if (!/^1\d{10}$/.test(form.value.phone)) {
    ElMessage.warning('请输入正确的手机号')
    return
  }
  loading.value = true
  try {
    await register({
      username: form.value.phone,
      password: form.value.password,
      realName: form.value.realName,
      phone: form.value.phone,
    })
    ElMessage.success('注册成功，请登录')
    router.push('/login')
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

      <!-- 右侧注册表单 -->
      <div class="login-form-wrapper">
        <div class="form-header">
          <h2 class="form-title">用户注册</h2>
          <p class="form-desc">使用手机号注册取件账号</p>
        </div>

        <el-form :model="form" @keyup.enter="handleRegister" class="login-form">
          <el-form-item>
            <el-input
              v-model="form.phone"
              placeholder="手机号（用作登录账号）"
              prefix-icon="Iphone"
              size="large"
              maxlength="11"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.realName"
              placeholder="昵称"
              prefix-icon="User"
              size="large"
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请设置密码"
              prefix-icon="Lock"
              size="large"
              show-password
            />
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.confirmPassword"
              type="password"
              placeholder="请再次输入密码"
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
            @click="handleRegister"
          >
            {{ loading ? '注册中...' : '注 册' }}
          </el-button>
        </el-form>

        <div class="form-footer">
          <span class="footer-text">已有账号？</span>
          <router-link to="/login" class="footer-link">返回登录</router-link>
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

.login-container {
  display: flex;
  width: 900px;
  min-height: 560px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 25px 80px rgba(0,0,0,0.5);
  position: relative;
  z-index: 1;
  animation: fadeSlideUp 0.6s ease-out;
}

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
