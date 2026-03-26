import request from '@/utils/request'

// 发起 SOS 一键报警
export function triggerSOS(data) {
  return request({
    url: '/app/alert/sos',
    method: 'post',
    data: data
  })
}

// 查询预警列表
export function listMyAlerts(query) {
  return request({
    url: '/app/alert/list',
    method: 'get',
    params: query
  })
}
