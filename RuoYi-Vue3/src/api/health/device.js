import request from '@/utils/request'

// 查询智能设备管理列表
export function listDevice(query) {
  return request({
    url: '/health/device/list',
    method: 'get',
    params: query
  })
}

// 查询智能设备管理详细
export function getDevice(deviceId) {
  return request({
    url: '/health/device/' + deviceId,
    method: 'get'
  })
}

// 新增智能设备管理
export function addDevice(data) {
  return request({
    url: '/health/device',
    method: 'post',
    data: data
  })
}

// 修改智能设备管理
export function updateDevice(data) {
  return request({
    url: '/health/device',
    method: 'put',
    data: data
  })
}

// 删除智能设备管理
export function delDevice(deviceId) {
  return request({
    url: '/health/device/' + deviceId,
    method: 'delete'
  })
}
