import request from '@/utils/request'

// 新增监护人信息
export function addGuardian(data) {
  return request({
    url: '/app/profile/guardian',
    method: 'post',
    data: data
  })
}
