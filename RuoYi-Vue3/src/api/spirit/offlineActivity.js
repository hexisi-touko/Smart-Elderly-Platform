import request from '@/utils/request'

// 查询线下活动管理列表
export function listOfflineActivity(query) {
  return request({
    url: '/spirit/offlineActivity/list',
    method: 'get',
    params: query
  })
}

// 查询线下活动管理详细
export function getOfflineActivity(activityId) {
  return request({
    url: '/spirit/offlineActivity/' + activityId,
    method: 'get'
  })
}

// 新增线下活动管理
export function addOfflineActivity(data) {
  return request({
    url: '/spirit/offlineActivity',
    method: 'post',
    data: data
  })
}

// 修改线下活动管理
export function updateOfflineActivity(data) {
  return request({
    url: '/spirit/offlineActivity',
    method: 'put',
    data: data
  })
}

// 删除线下活动管理
export function delOfflineActivity(activityId) {
  return request({
    url: '/spirit/offlineActivity/' + activityId,
    method: 'delete'
  })
}
