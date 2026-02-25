import request from '@/utils/request'

// 查询服务订单列表
export function listServiceOrder(query) {
  return request({
    url: '/order/serviceOrder/list',
    method: 'get',
    params: query
  })
}

// 查询服务订单详细
export function getServiceOrder(orderId) {
  return request({
    url: '/order/serviceOrder/' + orderId,
    method: 'get'
  })
}

// 新增服务订单
export function addServiceOrder(data) {
  return request({
    url: '/order/serviceOrder',
    method: 'post',
    data: data
  })
}

// 修改服务订单
export function updateServiceOrder(data) {
  return request({
    url: '/order/serviceOrder',
    method: 'put',
    data: data
  })
}

// 删除服务订单
export function delServiceOrder(orderId) {
  return request({
    url: '/order/serviceOrder/' + orderId,
    method: 'delete'
  })
}
