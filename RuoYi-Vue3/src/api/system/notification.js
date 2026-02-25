import request from '@/utils/request'

// 查询消息通知管理列表
export function listNotification(query) {
  return request({
    url: '/system/notification/list',
    method: 'get',
    params: query
  })
}

// 查询消息通知管理详细
export function getNotification(notificationId) {
  return request({
    url: '/system/notification/' + notificationId,
    method: 'get'
  })
}

// 新增消息通知管理
export function addNotification(data) {
  return request({
    url: '/system/notification',
    method: 'post',
    data: data
  })
}

// 修改消息通知管理
export function updateNotification(data) {
  return request({
    url: '/system/notification',
    method: 'put',
    data: data
  })
}

// 删除消息通知管理
export function delNotification(notificationId) {
  return request({
    url: '/system/notification/' + notificationId,
    method: 'delete'
  })
}
