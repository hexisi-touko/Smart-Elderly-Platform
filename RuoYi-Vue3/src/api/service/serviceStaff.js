import request from '@/utils/request'

// 查询服务人员管理列表
export function listServiceStaff(query) {
  return request({
    url: '/service/serviceStaff/list',
    method: 'get',
    params: query
  })
}

// 查询服务人员管理详细
export function getServiceStaff(staffId) {
  return request({
    url: '/service/serviceStaff/' + staffId,
    method: 'get'
  })
}

// 新增服务人员管理
export function addServiceStaff(data) {
  return request({
    url: '/service/serviceStaff',
    method: 'post',
    data: data
  })
}

// 修改服务人员管理
export function updateServiceStaff(data) {
  return request({
    url: '/service/serviceStaff',
    method: 'put',
    data: data
  })
}

// 删除服务人员管理
export function delServiceStaff(staffId) {
  return request({
    url: '/service/serviceStaff/' + staffId,
    method: 'delete'
  })
}
