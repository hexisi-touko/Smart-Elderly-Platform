import request from '@/utils/request'

// 查询服务项目管理列表
export function listServiceItem(query) {
  return request({
    url: '/service/serviceItem/list',
    method: 'get',
    params: query
  })
}

// 查询服务项目管理详细
export function getServiceItem(itemId) {
  return request({
    url: '/service/serviceItem/' + itemId,
    method: 'get'
  })
}

// 新增服务项目管理
export function addServiceItem(data) {
  return request({
    url: '/service/serviceItem',
    method: 'post',
    data: data
  })
}

// 修改服务项目管理
export function updateServiceItem(data) {
  return request({
    url: '/service/serviceItem',
    method: 'put',
    data: data
  })
}

// 删除服务项目管理
export function delServiceItem(itemId) {
  return request({
    url: '/service/serviceItem/' + itemId,
    method: 'delete'
  })
}
