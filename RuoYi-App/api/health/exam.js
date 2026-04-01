import request from '@/utils/request'

/**
 * 查询体检预约列表
 */
export function listExam(query) {
  return request({
    url: '/app/exam/list',
    method: 'get',
    params: query
  })
}

/**
 * 获取体检预约详情
 */
export function getExam(reservationId) {
  return request({
    url: '/app/exam/' + reservationId,
    method: 'get'
  })
}

/**
 * 新增体检预约
 */
export function addExam(data) {
  return request({
    url: '/app/exam',
    method: 'post',
    data: data
  })
}

/**
 * 取消体检预约
 */
export function cancelExam(reservationId) {
  return request({
    url: '/app/exam/cancel/' + reservationId,
    method: 'put'
  })
}
