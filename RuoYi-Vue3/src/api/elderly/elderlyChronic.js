import request from '@/utils/request'

// 查询老人慢病关联管理列表
export function listElderlyChronic(query) {
  return request({
    url: '/elderly/elderlyChronic/list',
    method: 'get',
    params: query
  })
}

// 查询老人慢病关联管理详细
export function getElderlyChronic(id) {
  return request({
    url: '/elderly/elderlyChronic/' + id,
    method: 'get'
  })
}

// 新增老人慢病关联管理
export function addElderlyChronic(data) {
  return request({
    url: '/elderly/elderlyChronic',
    method: 'post',
    data: data
  })
}

// 修改老人慢病关联管理
export function updateElderlyChronic(data) {
  return request({
    url: '/elderly/elderlyChronic',
    method: 'put',
    data: data
  })
}

// 删除老人慢病关联管理
export function delElderlyChronic(id) {
  return request({
    url: '/elderly/elderlyChronic/' + id,
    method: 'delete'
  })
}
