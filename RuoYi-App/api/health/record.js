import request from '@/utils/request'

// 查询健康记录列表
export function listHealthRecords(query) {
  return request({
    url: '/app/health/record/list',
    method: 'get',
    params: query
  })
}
