import request from '@/utils/request'

/**
 * 查询安全区域列表
 */
export function listMyZones(query) {
  return request({
    url: '/app/zone/list',
    method: 'get',
    params: query
  })
}

/**
 * 获取安全区域详情
 */
export function getZoneDetail(zoneId) {
  return request({
    url: '/app/zone/' + zoneId,
    method: 'get'
  })
}

/**
 * 新增安全区域
 */
export function addZone(data) {
  return request({
    url: '/app/zone',
    method: 'post',
    data: data
  })
}

/**
 * 修改安全区域
 */
export function updateZone(data) {
  return request({
    url: '/app/zone',
    method: 'put',
    data: data
  })
}

/**
 * 删除安全区域
 */
export function deleteZone(zoneId) {
  return request({
    url: '/app/zone/' + zoneId,
    method: 'delete'
  })
}
