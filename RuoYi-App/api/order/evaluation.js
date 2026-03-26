import request from '@/utils/request'

// 提交服务评价
export function addEvaluation(data) {
  return request({
    url: '/app/order/evaluation',
    method: 'post',
    data: data
  })
}
