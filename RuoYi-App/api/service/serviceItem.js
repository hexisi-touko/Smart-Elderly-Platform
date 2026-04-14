import request from '@/utils/request'

/**
 * 查询服务项目列表（C端）
 */
export function listServiceItems(query) {
  return request({
    url: '/app/service/list',
    method: 'get',
    params: query
  })
}

/**
 * 获取服务项目详情
 */
export function getServiceItem(itemId) {
  return request({
    url: '/app/service/' + itemId,
    method: 'get'
  })
}
