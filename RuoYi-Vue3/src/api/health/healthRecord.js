import request from '@/utils/request'

// 查询健康记录管理列表
export function listHealthRecord(query) {
  return request({
    url: '/health/healthRecord/list',
    method: 'get',
    params: query
  })
}

// 查询健康记录管理详细
export function getHealthRecord(recordId) {
  return request({
    url: '/health/healthRecord/' + recordId,
    method: 'get'
  })
}

// 新增健康记录管理
export function addHealthRecord(data) {
  return request({
    url: '/health/healthRecord',
    method: 'post',
    data: data
  })
}

// 修改健康记录管理
export function updateHealthRecord(data) {
  return request({
    url: '/health/healthRecord',
    method: 'put',
    data: data
  })
}

// 删除健康记录管理
export function delHealthRecord(recordId) {
  return request({
    url: '/health/healthRecord/' + recordId,
    method: 'delete'
  })
}
