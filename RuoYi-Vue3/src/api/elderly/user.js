import request from '@/utils/request'

// 查询C端用户账号列表
export function listUser(query) {
  return request({
    url: '/elderly/user/list',
    method: 'get',
    params: query
  })
}

// 查询C端用户账号详细
export function getUser(userId) {
  return request({
    url: '/elderly/user/' + userId,
    method: 'get'
  })
}

// 新增C端用户账号
export function addUser(data) {
  return request({
    url: '/elderly/user',
    method: 'post',
    data: data
  })
}

// 修改C端用户账号
export function updateUser(data) {
  return request({
    url: '/elderly/user',
    method: 'put',
    data: data
  })
}

// 删除C端用户账号
export function delUser(userId) {
  return request({
    url: '/elderly/user/' + userId,
    method: 'delete'
  })
}
