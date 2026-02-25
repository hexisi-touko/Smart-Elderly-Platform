import request from '@/utils/request'

// 查询用药记录管理列表
export function listMedicationLog(query) {
  return request({
    url: '/health/medicationLog/list',
    method: 'get',
    params: query
  })
}

// 查询用药记录管理详细
export function getMedicationLog(logId) {
  return request({
    url: '/health/medicationLog/' + logId,
    method: 'get'
  })
}

// 新增用药记录管理
export function addMedicationLog(data) {
  return request({
    url: '/health/medicationLog',
    method: 'post',
    data: data
  })
}

// 修改用药记录管理
export function updateMedicationLog(data) {
  return request({
    url: '/health/medicationLog',
    method: 'put',
    data: data
  })
}

// 删除用药记录管理
export function delMedicationLog(logId) {
  return request({
    url: '/health/medicationLog/' + logId,
    method: 'delete'
  })
}
