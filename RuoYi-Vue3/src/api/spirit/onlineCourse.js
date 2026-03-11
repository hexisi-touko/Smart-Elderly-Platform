import request from '@/utils/request'

// 查询线上课程管理列表
export function listOnlineCourse(query) {
  return request({
    url: '/spirit/onlineCourse/list',
    method: 'get',
    params: query
  })
}

// 查询线上课程管理详细
export function getOnlineCourse(courseId) {
  return request({
    url: '/spirit/onlineCourse/' + courseId,
    method: 'get'
  })
}

// 新增线上课程管理
export function addOnlineCourse(data) {
  return request({
    url: '/spirit/onlineCourse',
    method: 'post',
    data: data
  })
}

// 修改线上课程管理
export function updateOnlineCourse(data) {
  return request({
    url: '/spirit/onlineCourse',
    method: 'put',
    data: data
  })
}

// 删除线上课程管理
export function delOnlineCourse(courseId) {
  return request({
    url: '/spirit/onlineCourse/' + courseId,
    method: 'delete'
  })
}
