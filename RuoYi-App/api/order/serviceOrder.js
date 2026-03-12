import request from '@/utils/request'

// C端新增服务订单
export function addServiceOrder(data) {
  return request({
    url: '/app/order',
    method: 'post',
    data: data
  })
}
