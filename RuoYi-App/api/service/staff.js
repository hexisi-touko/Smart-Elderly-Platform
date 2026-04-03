import request from '@/utils/request'

// 服务人员完善资料
export function setupStaff(data) {
  return request({
    url: '/app/staff/setup',
    method: 'post',
    data: data
  })
}
