import request from '@/utils/request'

// 发起 SOS 一键报警
export function triggerSOS() {
  return request({
    url: '/app/alert/sos',
    method: 'post'
  })
}
