import request from '@/utils/request'

/**
 * 查询用药提醒列表
 */
export function listMedication(query) {
  return request({
    url: '/app/medication/list',
    method: 'get',
    params: query
  })
}

/**
 * 获取用药提醒详情
 */
export function getMedication(reminderId) {
  return request({
    url: '/app/medication/' + reminderId,
    method: 'get'
  })
}

/**
 * 新增用药提醒
 */
export function addMedication(data) {
  return request({
    url: '/app/medication',
    method: 'post',
    data: data
  })
}

/**
 * 修改用药提醒
 */
export function updateMedication(data) {
  return request({
    url: '/app/medication',
    method: 'put',
    data: data
  })
}

/**
 * 删除用药提醒
 */
export function delMedication(reminderIds) {
  return request({
    url: '/app/medication/' + reminderIds,
    method: 'delete'
  })
}
