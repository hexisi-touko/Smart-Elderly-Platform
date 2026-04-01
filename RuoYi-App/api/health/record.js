import request from '@/utils/request'

// 查询健康记录列表
export function listHealthRecords(query) {
  return request({
    url: '/app/health/record/list',
    method: 'get',
    params: query
  })
}

// 上报健康数据（设备/手动）
export function reportHealthData(data) {
  return request({
    url: '/app/health/report',
    method: 'post',
    data: data
  })
}
