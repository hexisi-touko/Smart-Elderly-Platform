import request from '@/utils/request'

// 查询健康阈值管理列表
export function listHealthThreshold(query) {
  return request({
    url: '/health/healthThreshold/list',
    method: 'get',
    params: query
  })
}

// 查询健康阈值管理详细
export function getHealthThreshold(thresholdId) {
  return request({
    url: '/health/healthThreshold/' + thresholdId,
    method: 'get'
  })
}

// 新增健康阈值管理
export function addHealthThreshold(data) {
  return request({
    url: '/health/healthThreshold',
    method: 'post',
    data: data
  })
}

// 修改健康阈值管理
export function updateHealthThreshold(data) {
  return request({
    url: '/health/healthThreshold',
    method: 'put',
    data: data
  })
}

// 删除健康阈值管理
export function delHealthThreshold(thresholdId) {
  return request({
    url: '/health/healthThreshold/' + thresholdId,
    method: 'delete'
  })
}
