import request from '@/utils/request'

// 查询支付记录管理列表
export function listPaymentRecord(query) {
  return request({
    url: '/order/paymentRecord/list',
    method: 'get',
    params: query
  })
}

// 查询支付记录管理详细
export function getPaymentRecord(paymentId) {
  return request({
    url: '/order/paymentRecord/' + paymentId,
    method: 'get'
  })
}

// 新增支付记录管理
export function addPaymentRecord(data) {
  return request({
    url: '/order/paymentRecord',
    method: 'post',
    data: data
  })
}

// 修改支付记录管理
export function updatePaymentRecord(data) {
  return request({
    url: '/order/paymentRecord',
    method: 'put',
    data: data
  })
}

// 删除支付记录管理
export function delPaymentRecord(paymentId) {
  return request({
    url: '/order/paymentRecord/' + paymentId,
    method: 'delete'
  })
}
