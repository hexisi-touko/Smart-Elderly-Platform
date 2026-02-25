import request from '@/utils/request'

// 查询服务商管理列表
export function listServiceProvider(query) {
  return request({
    url: '/service/serviceProvider/list',
    method: 'get',
    params: query
  })
}

// 查询服务商管理详细
export function getServiceProvider(providerId) {
  return request({
    url: '/service/serviceProvider/' + providerId,
    method: 'get'
  })
}

// 新增服务商管理
export function addServiceProvider(data) {
  return request({
    url: '/service/serviceProvider',
    method: 'post',
    data: data
  })
}

// 修改服务商管理
export function updateServiceProvider(data) {
  return request({
    url: '/service/serviceProvider',
    method: 'put',
    data: data
  })
}

// 删除服务商管理
export function delServiceProvider(providerId) {
  return request({
    url: '/service/serviceProvider/' + providerId,
    method: 'delete'
  })
}
