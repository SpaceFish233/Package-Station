import request from '@/utils/request'

// ---- 登录 ----
export function login(data: { username: string; password: string }) {
  return request.post('/login', data)
}

// ---- 统计 ----
export function getStatistics() {
  return request.get('/statistics/today')
}

// ---- 包裹 ----
export function inboundPackage(data: any) {
  return request.post('/packages/inbound', data)
}

export function selfOutbound(data: any) {
  return request.post('/packages/outbound/self', data)
}

export function staffOutbound(data: any) {
  return request.post('/packages/outbound/staff', data)
}

export function queryPackage(params: { pickupCode?: string; phone?: string }) {
  return request.get('/packages/query', { params })
}

export function getPackageList(params: any) {
  return request.get('/packages', { params })
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
