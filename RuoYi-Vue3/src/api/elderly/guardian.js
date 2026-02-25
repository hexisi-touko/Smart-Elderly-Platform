import request from '@/utils/request'

// 查询监护人信息列表
export function listGuardian(query) {
  return request({
    url: '/elderly/guardian/list',
    method: 'get',
    params: query
  })
}

// 查询监护人信息详细
export function getGuardian(guardianId) {
  return request({
    url: '/elderly/guardian/' + guardianId,
    method: 'get'
  })
}

// 新增监护人信息
export function addGuardian(data) {
  return request({
    url: '/elderly/guardian',
    method: 'post',
    data: data
  })
}

// 修改监护人信息
export function updateGuardian(data) {
  return request({
    url: '/elderly/guardian',
    method: 'put',
    data: data
  })
}

// 删除监护人信息
export function delGuardian(guardianId) {
  return request({
    url: '/elderly/guardian/' + guardianId,
    method: 'delete'
  })
}
