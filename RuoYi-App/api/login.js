import request from '@/utils/request'

// 登录方法
export function login(phone, password) {
  const data = {
    phone,
    password
  }
  return request({
    'url': '/app/login',
    headers: {
      isToken: false
    },
    'method': 'post',
    'data': data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/app/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    'url': '/app/getInfo',
    'method': 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    'url': '/app/logout',
    'method': 'post'
  })
}
