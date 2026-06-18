import request from '@/utils/request'

// ---- 登录/注册 ----
export function login(data: { username: string; password: string }) {
  return request.post('/login', data)
}

export function register(data: { username: string; password: string; realName: string; phone: string }) {
  return request.post('/register', data)
}

// ---- 统计 ----
export function getStatistics() {
  return request.get('/statistics/today')
}

// ---- 包裹 ----
export function inboundPackage(data: any) {
  return request.post('/packages/inbound', data)
}

export function staffOutbound(data: any) {
  return request.post('/packages/outbound/staff', data)
}

export function queryPackage(params: { trackingNumber?: string; phone?: string }) {
  return request.get('/packages/query', { params })
}

export function confirmPickup(packageId: number) {
  return request.post('/packages/confirm', { packageId })
}

export function getPackageList(params: any) {
  return request.get('/packages', { params })
}

// ---- 用户中心 ----
export function getUserInfo() {
  return request.get('/user/info')
}

export function updateProfile(data: { realName: string }) {
  return request.put('/user/profile', data)
}

export function updatePassword(data: { oldPassword: string; newPassword: string }) {
  return request.put('/user/password', data)
}

export function getUserPhones() {
  return request.get('/user/phones')
}

export function addUserPhone(data: { phone: string }) {
  return request.post('/user/phones', data)
}

export function deleteUserPhone(id: number) {
  return request.delete(`/user/phones/${id}`)
}

// ---- 通知 ----
export function getNotifications() {
  return request.get('/notifications')
}

export function createNotification(data: { title: string; content: string }) {
  return request.post('/admin/notifications', data)
}

export function updateNotification(id: number, data: { title: string; content: string }) {
  return request.put(`/admin/notifications/${id}`, data)
}

export function deleteNotification(id: number) {
  return request.delete(`/admin/notifications/${id}`)
}

// ---- 我的包裹 ----
export function getMyPackages() {
  return request.get('/packages/my')
}

// ---- 快递公司 ----
export function getCompanies() {
  return request.get('/companies')
}

export function addCompany(data: any) {
  return request.post('/companies', data)
}

export function updateCompany(id: number, data: any) {
  return request.put(`/companies/${id}`, data)
}

export function deleteCompany(id: number) {
  return request.delete(`/companies/${id}`)
}

// ---- 货架 ----
export function getShelves() {
  return request.get('/shelves')
}

export function addShelf(data: any) {
  return request.post('/shelves', data)
}

export function updateShelf(id: number, data: any) {
  return request.put(`/shelves/${id}`, data)
}

export function deleteShelf(id: number) {
  return request.delete(`/shelves/${id}`)
}
