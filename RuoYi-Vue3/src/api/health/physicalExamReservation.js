import request from '@/utils/request'

// 查询体检预约列表
export function listPhysicalExamReservation(query) {
  return request({
    url: '/health/physicalExamReservation/list',
    method: 'get',
    params: query
  })
}

// 查询体检预约详细
export function getPhysicalExamReservation(reservationId) {
  return request({
    url: '/health/physicalExamReservation/' + reservationId,
    method: 'get'
  })
}

// 新增体检预约
export function addPhysicalExamReservation(data) {
  return request({
    url: '/health/physicalExamReservation',
    method: 'post',
    data: data
  })
}

// 修改体检预约
export function updatePhysicalExamReservation(data) {
  return request({
    url: '/health/physicalExamReservation',
    method: 'put',
    data: data
  })
}

// 删除体检预约
export function delPhysicalExamReservation(reservationId) {
  return request({
    url: '/health/physicalExamReservation/' + reservationId,
    method: 'delete'
  })
}
