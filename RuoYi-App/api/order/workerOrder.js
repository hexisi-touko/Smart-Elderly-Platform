import request from '@/utils/request'

// 查询员工关联的订单列表
export function listWorkerOrder(query) {
  return request({
    url: '/app/worker/order/list',
    method: 'get',
    params: query
  })
}

// 员工接单
export function acceptOrder(orderId) {
  return request({
    url: '/app/worker/order/accept/' + orderId,
    method: 'put'
  })
}

// 开始服务
export function startService(orderId) {
  return request({
    url: '/app/worker/order/start/' + orderId,
    method: 'put'
  })
}

// 完成服务
export function completeService(orderId) {
  return request({
    url: '/app/worker/order/complete/' + orderId,
    method: 'put'
  })
}
