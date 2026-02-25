import request from '@/utils/request'

// 查询服务商结算管理列表
export function listSettlement(query) {
  return request({
    url: '/order/settlement/list',
    method: 'get',
    params: query
  })
}

// 查询服务商结算管理详细
export function getSettlement(settlementId) {
  return request({
    url: '/order/settlement/' + settlementId,
    method: 'get'
  })
}

// 新增服务商结算管理
export function addSettlement(data) {
  return request({
    url: '/order/settlement',
    method: 'post',
    data: data
  })
}

// 修改服务商结算管理
export function updateSettlement(data) {
  return request({
    url: '/order/settlement',
    method: 'put',
    data: data
  })
}

// 删除服务商结算管理
export function delSettlement(settlementId) {
  return request({
    url: '/order/settlement/' + settlementId,
    method: 'delete'
  })
}
