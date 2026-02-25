import request from '@/utils/request'

// 查询老人-监护人关联列表
export function listElderly_guardian(query) {
  return request({
    url: '/elderly/elderly_guardian/list',
    method: 'get',
    params: query
  })
}

// 查询老人-监护人关联详细
export function getElderly_guardian(id) {
  return request({
    url: '/elderly/elderly_guardian/' + id,
    method: 'get'
  })
}

// 新增老人-监护人关联
export function addElderly_guardian(data) {
  return request({
    url: '/elderly/elderly_guardian',
    method: 'post',
    data: data
  })
}

// 修改老人-监护人关联
export function updateElderly_guardian(data) {
  return request({
    url: '/elderly/elderly_guardian',
    method: 'put',
    data: data
  })
}

// 删除老人-监护人关联
export function delElderly_guardian(id) {
  return request({
    url: '/elderly/elderly_guardian/' + id,
    method: 'delete'
  })
}
