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
