import request from '@/utils/request'

// 查询活动报名管理列表
export function listActivityEnrollment(query) {
  return request({
    url: '/spirit/activityEnrollment/list',
    method: 'get',
    params: query
  })
}

// 查询活动报名管理详细
export function getActivityEnrollment(id) {
  return request({
    url: '/spirit/activityEnrollment/' + id,
    method: 'get'
  })
}

// 新增活动报名管理
export function addActivityEnrollment(data) {
  return request({
    url: '/spirit/activityEnrollment',
    method: 'post',
    data: data
  })
}

// 修改活动报名管理
export function updateActivityEnrollment(data) {
  return request({
    url: '/spirit/activityEnrollment',
    method: 'put',
    data: data
  })
}

// 删除活动报名管理
export function delActivityEnrollment(id) {
  return request({
    url: '/spirit/activityEnrollment/' + id,
    method: 'delete'
  })
}
