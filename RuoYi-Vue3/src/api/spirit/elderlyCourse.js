import request from '@/utils/request'

// 查询老人-课程报名列表
export function listElderlyCourse(query) {
  return request({
    url: '/spirit/elderlyCourse/list',
    method: 'get',
    params: query
  })
}

// 查询老人-课程报名详细
export function getElderlyCourse(id) {
  return request({
    url: '/spirit/elderlyCourse/' + id,
    method: 'get'
  })
}

// 新增老人-课程报名
export function addElderlyCourse(data) {
  return request({
    url: '/spirit/elderlyCourse',
    method: 'post',
    data: data
  })
}

// 修改老人-课程报名
export function updateElderlyCourse(data) {
  return request({
    url: '/spirit/elderlyCourse',
    method: 'put',
    data: data
  })
}

// 删除老人-课程报名
export function delElderlyCourse(id) {
  return request({
    url: '/spirit/elderlyCourse/' + id,
    method: 'delete'
  })
}
