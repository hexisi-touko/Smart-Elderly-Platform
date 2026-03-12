import request from '@/utils/request'

// 查询服务项目列表
export function listServiceItem(query) {
  return request({
    url: '/app/service/list',
    method: 'get',
    params: query
  })
}
