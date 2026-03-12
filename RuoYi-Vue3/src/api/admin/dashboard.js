import request from '@/utils/request'

// 获取核心指标概览
export function getOverview() {
  return request({
    url: '/admin/dashboard/overview',
    method: 'get'
  })
}

// 获取订单趋势
export function getOrderTrend(days) {
  return request({
    url: '/admin/dashboard/orderTrend',
    method: 'get',
    params: { days }
  })
}

// 获取预警类型统计
export function getAlertStats() {
  return request({
    url: '/admin/dashboard/alertStats',
    method: 'get'
  })
}

// 获取订单状态分布
export function getOrderStatusDistribution() {
  return request({
    url: '/admin/dashboard/orderStatusDistribution',
    method: 'get'
  })
}

// 获取服务满意度分布
export function getSatisfactionDistribution() {
  return request({
    url: '/admin/dashboard/satisfaction',
    method: 'get'
  })
}
