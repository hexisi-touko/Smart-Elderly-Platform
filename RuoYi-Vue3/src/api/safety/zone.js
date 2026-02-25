import request from '@/utils/request'

// 查询安全区域/地理围栏列表
export function listZone(query) {
  return request({
    url: '/safety/zone/list',
    method: 'get',
    params: query
  })
}

// 查询安全区域/地理围栏详细
export function getZone(zoneId) {
  return request({
    url: '/safety/zone/' + zoneId,
    method: 'get'
  })
}

// 新增安全区域/地理围栏
export function addZone(data) {
  return request({
    url: '/safety/zone',
    method: 'post',
    data: data
  })
}

// 修改安全区域/地理围栏
export function updateZone(data) {
  return request({
    url: '/safety/zone',
    method: 'put',
    data: data
  })
}

// 删除安全区域/地理围栏
export function delZone(zoneId) {
  return request({
    url: '/safety/zone/' + zoneId,
    method: 'delete'
  })
}
