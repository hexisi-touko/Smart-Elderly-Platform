import request from '@/utils/request'

// 查询用药提醒管理列表
export function listMedicationReminder(query) {
  return request({
    url: '/health/medicationReminder/list',
    method: 'get',
    params: query
  })
}

// 查询用药提醒管理详细
export function getMedicationReminder(reminderId) {
  return request({
    url: '/health/medicationReminder/' + reminderId,
    method: 'get'
  })
}

// 新增用药提醒管理
export function addMedicationReminder(data) {
  return request({
    url: '/health/medicationReminder',
    method: 'post',
    data: data
  })
}

// 修改用药提醒管理
export function updateMedicationReminder(data) {
  return request({
    url: '/health/medicationReminder',
    method: 'put',
    data: data
  })
}

// 删除用药提醒管理
export function delMedicationReminder(reminderId) {
  return request({
    url: '/health/medicationReminder/' + reminderId,
    method: 'delete'
  })
}
