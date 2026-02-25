import request from '@/utils/request'

// 查询订单-服务人员关联列表
export function listOrderStaff(query) {
  return request({
    url: '/order/orderStaff/list',
    method: 'get',
    params: query
  })
}

// 查询订单-服务人员关联详细
export function getOrderStaff(id) {
  return request({
    url: '/order/orderStaff/' + id,
    method: 'get'
  })
}

// 新增订单-服务人员关联
export function addOrderStaff(data) {
  return request({
    url: '/order/orderStaff',
    method: 'post',
    data: data
  })
}

// 修改订单-服务人员关联
export function updateOrderStaff(data) {
  return request({
    url: '/order/orderStaff',
    method: 'put',
    data: data
  })
}

// 删除订单-服务人员关联
export function delOrderStaff(id) {
  return request({
    url: '/order/orderStaff/' + id,
    method: 'delete'
  })
}
