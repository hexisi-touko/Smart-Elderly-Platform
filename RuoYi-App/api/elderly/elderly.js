import request from '@/utils/request'

// 新增老人基础信息
export function addElderly(data) {
  return request({
    url: '/app/profile/elderly',
    method: 'post',
    data: data
  })
}
