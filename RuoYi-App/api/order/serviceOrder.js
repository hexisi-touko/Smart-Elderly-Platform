import request from '@/utils/request'

/**
 * C端新增服务订单
 */
export function addServiceOrder(data) {
  return request({
    url: '/app/order',
    method: 'post',
    data: data
  })
}

/**
 * C端查询我的订单列表
 */
export function listMyOrders(query) {
  return request({
    url: '/app/order/list',
    method: 'get',
    params: query
  })
}

/**
 * C端取消服务订单
 */
export function cancelServiceOrder(orderId) {
  return request({
    url: '/app/order/cancel/' + orderId,
    method: 'put'
  })
}

/**
 * 获取订单详情
 */
export function getOrderDetail(orderId) {
  return request({
    url: '/app/order/' + orderId,
    method: 'get'
  })
}
