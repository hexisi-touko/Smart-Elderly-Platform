import request from '@/utils/request'

// 查询服务评价管理列表
export function listEvaluation(query) {
  return request({
    url: '/order/evaluation/list',
    method: 'get',
    params: query
  })
}

// 查询服务评价管理详细
export function getEvaluation(evaluationId) {
  return request({
    url: '/order/evaluation/' + evaluationId,
    method: 'get'
  })
}

// 新增服务评价管理
export function addEvaluation(data) {
  return request({
    url: '/order/evaluation',
    method: 'post',
    data: data
  })
}

// 修改服务评价管理
export function updateEvaluation(data) {
  return request({
    url: '/order/evaluation',
    method: 'put',
    data: data
  })
}

// 删除服务评价管理
export function delEvaluation(evaluationId) {
  return request({
    url: '/order/evaluation/' + evaluationId,
    method: 'delete'
  })
}
