import request from '@/utils/request'

// 查询安全预警管理列表
export function listAlert(query) {
  return request({
    url: '/safety/alert/list',
    method: 'get',
    params: query
  })
}

// 查询安全预警管理详细
export function getAlert(alertId) {
  return request({
    url: '/safety/alert/' + alertId,
    method: 'get'
  })
}

// 新增安全预警管理
export function addAlert(data) {
  return request({
    url: '/safety/alert',
    method: 'post',
    data: data
  })
}

// 修改安全预警管理
export function updateAlert(data) {
  return request({
    url: '/safety/alert',
    method: 'put',
    data: data
  })
}

// 删除安全预警管理
export function delAlert(alertId) {
  return request({
    url: '/safety/alert/' + alertId,
    method: 'delete'
  })
}

// 接警响应
export function respondAlert(data) {
  return request({
    url: '/safety/alert/respond',
    method: 'put',
    data: data
  })
}

// 完成处置
export function completeAlert(data) {
  return request({
    url: '/safety/alert/complete',
    method: 'put',
    data: data
  })
}

// 关闭预警
export function closeAlert(alertId) {
  return request({
    url: '/safety/alert/close/' + alertId,
    method: 'put'
  })
}
