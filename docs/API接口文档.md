# 智慧居家养老服务平台 API 接口文档

> **版本**: v1.0.0  
> **更新时间**: 2026-02-02  
> **基础路径**: `/api/v1`

---

## 📋 目录

1. [通用说明](#1-通用说明)
2. [认证授权模块](#2-认证授权模块)
3. [用户管理模块](#3-用户管理模块)
4. [健康管理模块](#4-健康管理模块)
5. [安全应急模块](#5-安全应急模块)
6. [生活照料模块](#6-生活照料模块)
7. [精神慰藉模块](#7-精神慰藉模块)
8. [支付结算模块](#8-支付结算模块)
9. [系统管理模块](#9-系统管理模块)
10. [数据统计模块](#10-数据统计模块)

---

## 🔧 RuoYi 框架适配说明

> 本文档基于 **RuoYi-Vue** 框架进行开发，以下为框架适配要点：

### 认证路径对齐

| 本文档路径 | RuoYi 默认路径 | 适配方案 |
|------------|----------------|----------|
| `/auth/register` | 无（需新增） | 在 `ruoyi-admin` 新增注册接口 |
| `/login` | `/login` | ✅ 已对齐 |
| `/auth/logout` | `/logout` | 建议改为 `/logout` 对齐 |
| `/auth/sms/send` | 无（需新增） | 新增短信验证码接口 |

**权限白名单配置**（`SecurityConfig.java`）：

```java
// 允许匿名访问的路径
.antMatchers(
    "/login",
    "/register",
    "/captchaImage",
    "/auth/sms/send",
    "/auth/password/reset"
).permitAll()
```

### 文件上传规范

RuoYi 自带 `/common/upload` 接口，建议在 `CommonController` 中增加 `bizType` 参数逻辑：

```java
@PostMapping("/common/upload")
public AjaxResult uploadFile(
    @RequestParam("file") MultipartFile file,
    @RequestParam(value = "bizType", defaultValue = "default") String bizType
) {
    // 根据 bizType 确定 OSS 存储路径
    String ossPath = switch (bizType) {
        case "avatar" -> "avatar/";
        case "evaluation" -> "evaluation/";
        case "license" -> "license/";
        case "service_record" -> "service_record/";
        case "report" -> "report/";
        default -> "default/";
    };
    // ... 上传逻辑
}
```

**bizType 业务类型映射**：

| bizType | 说明 | OSS 存储路径 |
|---------|------|--------------|
| `avatar` | 用户头像 | `/avatar/{yyyy}/{MM}/{dd}/` |
| `evaluation` | 服务评价照片 | `/evaluation/{yyyy}/{MM}/{dd}/` |
| `license` | 资质证照 | `/license/{providerId}/` |
| `service_record` | 服务记录照片 | `/service_record/{orderId}/` |
| `report` | 健康/体检报告 | `/report/{elderlyId}/` |

---

## 1. 通用说明

### 1.1 请求格式

| 项目 | 说明 |
|------|------|
| **协议** | HTTPS |
| **请求方式** | RESTful（GET/POST/PUT/DELETE） |
| **数据格式** | JSON |
| **编码方式** | UTF-8 |
| **认证方式** | JWT Bearer Token |

### 1.2 请求头

```http
Content-Type: application/json
Authorization: Bearer {token}
X-Request-Id: {uuid}  # 请求追踪ID
```

### 1.3 统一响应格式

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {},
  "timestamp": 1706860800000
}
```

### 1.4 状态码说明

| 状态码 | 说明 | 备注 |
|--------|------|------|
| 200 | 操作成功 | - |
| 400 | 请求参数错误 | 参数校验失败 |
| 401 | 未授权 | Token无效或过期 |
| 403 | 权限不足 | 无操作权限 |
| 404 | 资源不存在 | - |
| 429 | 请求过于频繁 | 触发限流 |
| 500 | 服务器内部错误 | - |

### 1.5 分页参数

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页数量，默认10，最大100 |

### 1.6 分页响应

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "list": [],
    "total": 100,
    "pageNum": 1,
    "pageSize": 10,
    "pages": 10
  }
}
```

### 1.7 通用文件上传接口

**POST** `/common/upload`

> 用于上传图片、文件至阿里云 OSS，支持服务评价照片、资质证照等场景

**请求头**

```http
Content-Type: multipart/form-data
Authorization: Bearer {token}
```

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| file | file | 是 | 上传的文件 |
| bizType | string | 是 | 业务类型：`avatar`/`evaluation`/`license`/`service_record`/`report` |

**响应示例**

```json
{
  "code": 200,
  "msg": "上传成功",
  "data": {
    "fileId": "550e8400-e29b-41d4-a716-446655440000",
    "fileName": "photo_20260202.jpg",
    "fileSize": 102400,
    "fileType": "image/jpeg",
    "url": "https://smart-elderly.oss-cn-beijing.aliyuncs.com/evaluation/2026/02/02/xxx.jpg",
    "relativePath": "evaluation/2026/02/02/xxx.jpg"
  }
}
```

> **说明**
> - 返回的 `url` 为 OSS **绝对路径**，可直接用于前端展示
> - `relativePath` 为相对路径，用于数据库存储（节省空间）
> - 图片文件限制：最大 5MB，支持 jpg/jpeg/png/gif
> - 其他文件限制：最大 20MB，支持 pdf/doc/docx/xls/xlsx

### 1.8 限流与降级说明

为保障系统在 **1000条/秒** 设备数据采集的高并发场景下稳定运行，平台采用以下限流策略：

| 接口类型 | 限流阈值 | 说明 |
|----------|----------|------|
| 健康数据上报 | 1000 QPS | 设备数据采集核心接口 |
| 普通查询接口 | 500 QPS/用户 | 防止单用户恶意请求 |
| 应急呼叫 | 不限流 | 优先保障生命安全 |

**客户端处理建议**：

当收到 `429 Too Many Requests` 错误时，前端应采用 **指数退避重试** 策略：

```javascript
// 指数退避重试示例
async function retryWithBackoff(fn, maxRetries = 5) {
  for (let i = 0; i < maxRetries; i++) {
    try {
      return await fn();
    } catch (error) {
      if (error.status === 429 && i < maxRetries - 1) {
        const delay = Math.pow(2, i) * 1000 + Math.random() * 1000;
        await new Promise(resolve => setTimeout(resolve, delay));
      } else {
        throw error;
      }
    }
  }
}
```

---

## 2. 认证授权模块

### 2.1 用户注册

**POST** `/auth/register`

> 🔓 无需认证

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| phone | string | 是 | 手机号 |
| password | string | 是 | 密码（6-20位） |
| smsCode | string | 是 | 短信验证码 |
| userType | string | 是 | 用户类型：`elderly`/`guardian`/`provider` |
| realName | string | 是 | 真实姓名 |
| idCard | string | 是 | 身份证号 |

**响应示例**

```json
{
  "code": 200,
  "msg": "注册成功",
  "data": {
    "userId": "550e8400-e29b-41d4-a716-446655440000"
  }
}
```

---

### 2.2 发送短信验证码

**POST** `/auth/sms/send`

> 🔓 无需认证

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| phone | string | 是 | 手机号 |
| type | string | 是 | 类型：`register`/`login`/`reset` |

---

### 2.3 用户登录

**POST** `/login`

> 🔓 无需认证

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| phone | string | 是 | 手机号 |
| password | string | 否 | 密码（密码登录时必填） |
| smsCode | string | 否 | 短信验证码（验证码登录时必填） |
| loginType | string | 是 | 登录方式：`password`/`sms` |

**响应示例**

```json
{
  "code": 200,
  "msg": "登录成功",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 7200,
    "userInfo": {
      "userId": "550e8400-e29b-41d4-a716-446655440000",
      "phone": "138****8888",
      "realName": "张三",
      "userType": "elderly",
      "avatar": "https://oss.example.com/avatar/xxx.jpg"
    }
  }
}
```

---

### 2.4 刷新Token

**POST** `/auth/token/refresh`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| refreshToken | string | 是 | 刷新令牌 |

---

### 2.5 退出登录

**POST** `/auth/logout`

**请求参数**: 无

---

### 2.6 修改密码

**PUT** `/auth/password`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| oldPassword | string | 是 | 旧密码 |
| newPassword | string | 是 | 新密码 |

---

### 2.7 重置密码

**POST** `/auth/password/reset`

> 🔓 无需认证

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| phone | string | 是 | 手机号 |
| smsCode | string | 是 | 短信验证码 |
| newPassword | string | 是 | 新密码 |

---

## 3. 用户管理模块

### 3.1 老人信息管理

#### 3.1.1 获取老人信息

**GET** `/elderly/{elderlyId}`

**响应示例**

```json
{
  "code": 200,
  "data": {
    "elderlyId": "550e8400-e29b-41d4-a716-446655440000",
    "name": "张三",
    "phone": "138****8888",
    "gender": 1,
    "birthDate": "1950-01-15",
    "healthStatus": 1,
    "address": "北京市朝阳区xxx街道",
    "emergencyContactName": "张小明",
    "emergencyContactPhone": "139****9999",
    "chronicList": ["高血压", "糖尿病"],
    "boundDevices": 3,
    "boundGuardians": 2
  }
}
```

---

#### 3.1.2 更新老人信息

**PUT** `/elderly/{elderlyId}`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| name | string | 否 | 姓名 |
| phone | string | 否 | 手机号 |
| address | string | 否 | 居住地址 |
| healthStatus | int | 否 | 健康状态（0-健康、1-慢病、2-失能） |
| emergencyContactName | string | 否 | 紧急联系人姓名 |
| emergencyContactPhone | string | 否 | 紧急联系人电话 |

---

#### 3.1.3 添加慢病记录

**POST** `/elderly/{elderlyId}/chronic`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| chronicType | string | 是 | 慢病类型 |
| diagnosisDate | date | 否 | 确诊日期 |

---

#### 3.1.4 删除慢病记录

**DELETE** `/elderly/{elderlyId}/chronic/{chronicId}`

---

### 3.2 监护人管理

#### 3.2.1 获取老人的监护人列表

**GET** `/elderly/{elderlyId}/guardians`

**响应示例**

```json
{
  "code": 200,
  "data": [
    {
      "guardianId": "xxx",
      "name": "张小明",
      "phone": "139****9999",
      "relationship": "子女",
      "isPrimary": true,
      "authorizationStatus": 1
    }
  ]
}
```

---

#### 3.2.2 绑定监护人

**POST** `/elderly/{elderlyId}/guardians`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| guardianPhone | string | 是 | 监护人手机号 |
| relationship | string | 是 | 关系（子女/配偶/亲属等） |
| isPrimary | boolean | 否 | 是否主要监护人 |

---

#### 3.2.3 解绑监护人

**DELETE** `/elderly/{elderlyId}/guardians/{guardianId}`

---

#### 3.2.4 监护人获取绑定的老人列表

**GET** `/guardian/elderly-list`

---

### 3.3 服务商管理

#### 3.3.1 服务商入驻申请

**POST** `/provider/apply`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| providerName | string | 是 | 服务商名称 |
| licenseCode | string | 是 | 资质许可证号 |
| contactPerson | string | 是 | 联系人姓名 |
| contactPhone | string | 是 | 联系人电话 |
| address | string | 是 | 办公地址 |
| businessLicense | string | 否 | 营业执照编号 |
| serviceScope | string | 是 | 服务范围 |
| licenseImages | array | 是 | 资质证照图片URL列表 |

---

#### 3.3.2 获取服务商信息

**GET** `/provider/{providerId}`

---

#### 3.3.3 更新服务商信息

**PUT** `/provider/{providerId}`

---

#### 3.3.4 获取服务商列表（分页）

**GET** `/providers`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| keyword | string | 否 | 搜索关键词 |
| auditStatus | int | 否 | 审核状态 |
| serviceScope | string | 否 | 服务范围筛选 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

### 3.4 服务人员管理

#### 3.4.1 添加服务人员

**POST** `/provider/{providerId}/staff`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| staffName | string | 是 | 姓名 |
| phone | string | 是 | 手机号 |
| idCard | string | 是 | 身份证号 |
| staffType | string | 是 | 人员类型（护理员/社工/医护） |
| certificate | string | 否 | 职业资格证书编号 |

---

#### 3.4.2 获取服务人员列表

**GET** `/provider/{providerId}/staff`

---

#### 3.4.3 更新服务人员信息

**PUT** `/staff/{staffId}`

---

#### 3.4.4 更新服务人员状态

**PUT** `/staff/{staffId}/status`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | int | 是 | 状态（0-休息、1-在岗、2-请假） |

---

## 4. 健康管理模块

### 4.1 智能设备管理

#### 4.1.1 绑定设备

**POST** `/device/bind`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| deviceCode | string | 是 | 设备编码（扫码获取） |
| elderlyId | string | 是 | 老人ID |

**响应示例**

```json
{
  "code": 200,
  "msg": "绑定成功",
  "data": {
    "deviceId": "xxx",
    "deviceType": "血压计",
    "deviceBrand": "欧姆龙",
    "bindTime": "2026-02-02 14:30:00"
  }
}
```

---

#### 4.1.2 解绑设备

**POST** `/device/unbind`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| deviceId | string | 是 | 设备ID |

---

#### 4.1.3 获取老人绑定的设备列表

**GET** `/elderly/{elderlyId}/devices`

---

#### 4.1.4 更新设备推送设置

**PUT** `/device/{deviceId}/push-setting`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| pushStatus | int | 是 | 推送状态（0-关闭、1-开启） |

---

### 4.2 健康数据管理

#### 4.2.1 上报健康数据（设备/手动）

**POST** `/health/record`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 是 | 老人ID |
| deviceId | string | 否 | 设备ID（手动录入时为空） |
| recordType | string | 是 | 记录类型（血压/血糖/心率/体温/血氧） |
| collectMethod | int | 是 | 采集方式（0-设备、1-手动） |
| collectTime | datetime | 是 | 采集时间 |
| systolicBp | int | 否 | 收缩压（mmHg） |
| diastolicBp | int | 否 | 舒张压（mmHg） |
| bloodSugar | decimal | 否 | 血糖值（mmol/L） |
| heartRate | int | 否 | 心率（次/分钟） |
| temperature | decimal | 否 | 体温（℃） |
| bloodOxygen | int | 否 | 血氧饱和度（%） |

> ⚠️ **高并发接口限流说明**
> - 本接口支持 **1000 QPS** 并发写入，适配智能设备批量数据上报场景
> - 若触发 `429 Too Many Requests` 错误，**前端应采用指数退避重试策略**（参见 1.8 节）
> - 数据会先写入 Redis 缓存队列，异步批量入库，确保高吞吐量
> - 异常数据（如收缩压 > 200 或 < 50）会被标记为 `data_status=2`（异常待处理）并触发预警

---

#### 4.2.2 获取健康数据列表

**GET** `/elderly/{elderlyId}/health/records`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| recordType | string | 否 | 记录类型 |
| startTime | datetime | 否 | 开始时间 |
| endTime | datetime | 否 | 结束时间 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

#### 4.2.3 获取实时健康数据

**GET** `/elderly/{elderlyId}/health/realtime`

**响应示例**

```json
{
  "code": 200,
  "data": {
    "bloodPressure": {
      "systolicBp": 125,
      "diastolicBp": 80,
      "collectTime": "2026-02-02 14:00:00",
      "status": "normal"
    },
    "heartRate": {
      "value": 75,
      "collectTime": "2026-02-02 14:00:00",
      "status": "normal"
    },
    "bloodSugar": {
      "value": 5.6,
      "collectTime": "2026-02-02 08:00:00",
      "status": "normal"
    }
  }
}
```

---

#### 4.2.4 获取健康趋势分析

**GET** `/elderly/{elderlyId}/health/trend`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| recordType | string | 是 | 记录类型 |
| period | string | 是 | 周期（day/week/month） |

---

#### 4.2.5 导出健康报告

**GET** `/elderly/{elderlyId}/health/report/export`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| startTime | datetime | 是 | 开始时间 |
| endTime | datetime | 是 | 结束时间 |
| format | string | 否 | 导出格式（pdf/excel），默认pdf |

**响应**: 返回文件下载链接

---

### 4.3 健康阈值管理

#### 4.3.1 获取老人健康阈值配置

**GET** `/elderly/{elderlyId}/health/threshold`

---

#### 4.3.2 更新健康阈值配置

**PUT** `/elderly/{elderlyId}/health/threshold`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| sysBpMax | int | 否 | 收缩压上限（默认140） |
| sysBpMin | int | 否 | 收缩压下限（默认90） |
| diaBpMax | int | 否 | 舒张压上限（默认90） |
| diaBpMin | int | 否 | 舒张压下限（默认60） |
| heartRateMax | int | 否 | 心率上限（默认100） |
| heartRateMin | int | 否 | 心率下限（默认60） |
| bloodSugarMax | decimal | 否 | 血糖上限（默认7.0） |
| bloodSugarMin | decimal | 否 | 血糖下限（默认3.9） |

---

### 4.4 用药提醒

#### 4.4.1 添加用药提醒

**POST** `/elderly/{elderlyId}/medication`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| medicationName | string | 是 | 药品名称 |
| medicationType | string | 否 | 药品类型 |
| dosage | string | 是 | 用药剂量 |
| frequency | string | 是 | 用药频率 |
| reminderTime | time | 是 | 提醒时间 |
| startDate | date | 是 | 开始日期 |
| endDate | date | 否 | 结束日期 |

---

#### 4.4.2 获取用药提醒列表

**GET** `/elderly/{elderlyId}/medications`

---

#### 4.4.3 更新用药提醒

**PUT** `/medication/{reminderId}`

---

#### 4.4.4 删除用药提醒

**DELETE** `/medication/{reminderId}`

---

#### 4.4.5 确认服药

**POST** `/medication/{reminderId}/confirm`

---

### 4.5 体检预约

#### 4.5.1 创建体检预约

**POST** `/elderly/{elderlyId}/exam/reservation`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| examType | string | 是 | 体检类型 |
| examInstitution | string | 是 | 体检机构名称 |
| reservationTime | datetime | 是 | 预约时间 |
| examAddress | string | 是 | 体检地址 |
| examItems | array | 否 | 体检项目列表 |

---

#### 4.5.2 获取体检预约列表

**GET** `/elderly/{elderlyId}/exam/reservations`

---

#### 4.5.3 取消体检预约

**PUT** `/exam/reservation/{reservationId}/cancel`

---

#### 4.5.4 获取体检报告

**GET** `/exam/reservation/{reservationId}/report`

---

## 5. 安全应急模块

### 5.1 应急呼叫

#### 5.1.1 发起应急呼叫

**POST** `/emergency/call`

> ⚠️ **高优先级接口**，响应时间 ≤ 3秒

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 是 | 老人ID |
| callType | string | 是 | 呼叫类型（manual/fall/gas/smoke） |
| location | object | 是 | 位置信息 |
| location.lng | decimal | 是 | 经度 |
| location.lat | decimal | 是 | 纬度 |
| location.address | string | 是 | 地址描述 |
| deviceId | string | 否 | 触发设备ID |

**响应示例**

```json
{
  "code": 200,
  "msg": "呼叫已发出",
  "data": {
    "alertId": "xxx",
    "alertTime": "2026-02-02 14:30:00",
    "notifiedContacts": ["张小明", "李四"],
    "assignedProvider": "XX养老服务中心"
  }
}
```

---

### 5.2 安全预警

#### 5.2.1 获取预警列表

**GET** `/alerts`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 否 | 老人ID |
| alertStatus | int | 否 | 预警状态（0-未处理、1-处理中、2-已完成） |
| alertType | string | 否 | 预警类型 |
| startTime | datetime | 否 | 开始时间 |
| endTime | datetime | 否 | 结束时间 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

#### 5.2.2 获取预警详情

**GET** `/alert/{alertId}`

**响应示例**

```json
{
  "code": 200,
  "data": {
    "alertId": "xxx",
    "elderlyId": "xxx",
    "elderlyName": "张三",
    "alertType": "跌倒",
    "alertTime": "2026-02-02 14:30:00",
    "alertStatus": 1,
    "responseTime": "2026-02-02 14:35:00",
    "handlerId": "xxx",
    "handlerName": "李四",
    "handlerRole": "服务商",
    "location": {
      "lng": 116.404,
      "lat": 39.915,
      "address": "北京市朝阳区xxx"
    },
    "urgeCount": 0,
    "handleResult": null
  }
}
```

---

#### 5.2.3 服务商接单处理预警

**PUT** `/alert/{alertId}/accept`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| handlerId | string | 是 | 处理人ID |

---

#### 5.2.4 更新预警处置状态

**PUT** `/alert/{alertId}/status`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| alertStatus | int | 是 | 状态（1-处理中、2-已完成、3-误报） |
| handleResult | string | 否 | 处置结果描述（完成时必填） |
| photos | array | 否 | 处置照片URL列表 |

---

#### 5.2.5 子女一键催促

**POST** `/alert/{alertId}/urge`

---

#### 5.2.6 获取老人预警历史

**GET** `/elderly/{elderlyId}/alerts`

---

## 6. 生活照料模块

### 6.1 服务项目

#### 6.1.1 获取服务项目列表

**GET** `/service-items`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| category | string | 否 | 服务类别 |
| providerId | string | 否 | 服务商ID |
| keyword | string | 否 | 搜索关键词 |
| minPrice | decimal | 否 | 最低价格 |
| maxPrice | decimal | 否 | 最高价格 |
| sortBy | string | 否 | 排序字段（price/rating） |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

#### 6.1.2 获取服务项目详情

**GET** `/service-item/{itemId}`

---

#### 6.1.3 创建服务项目（服务商）

**POST** `/provider/{providerId}/service-items`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| itemName | string | 是 | 项目名称 |
| itemCategory | string | 是 | 服务类别 |
| itemDescription | string | 否 | 项目描述 |
| standardPrice | decimal | 是 | 标准价格 |
| serviceDuration | int | 否 | 服务时长（分钟） |
| serviceUnit | string | 否 | 服务单位 |

---

#### 6.1.4 更新服务项目

**PUT** `/service-item/{itemId}`

---

#### 6.1.5 上下架服务项目

**PUT** `/service-item/{itemId}/status`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | int | 是 | 状态（0-下架、1-上架） |

---

### 6.2 服务订单

#### 6.2.1 创建服务订单

**POST** `/orders`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 是 | 老人ID |
| serviceItemId | string | 是 | 服务项目ID |
| serviceTime | datetime | 是 | 约定服务时间 |
| serviceAddress | string | 是 | 服务地址 |
| serviceRequirements | string | 否 | 服务要求说明 |

**响应示例**

```json
{
  "code": 200,
  "msg": "订单创建成功",
  "data": {
    "orderId": "xxx",
    "orderNo": "ORD202602021430001",
    "orderAmount": 150.00,
    "orderStatus": 0,
    "createTime": "2026-02-02 14:30:00"
  }
}
```

---

#### 6.2.2 获取订单列表

**GET** `/orders`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 否 | 老人ID |
| providerId | string | 否 | 服务商ID |
| orderStatus | int | 否 | 订单状态 |
| startTime | datetime | 否 | 开始时间 |
| endTime | datetime | 否 | 结束时间 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

#### 6.2.3 获取订单详情

**GET** `/order/{orderId}`

**响应示例**

```json
{
  "code": 200,
  "data": {
    "orderId": "xxx",
    "orderNo": "ORD202602021430001",
    "elderly": {
      "elderlyId": "xxx",
      "name": "张三",
      "phone": "138****8888"
    },
    "provider": {
      "providerId": "xxx",
      "providerName": "XX养老服务中心"
    },
    "serviceItem": {
      "itemId": "xxx",
      "itemName": "日常保洁",
      "itemCategory": "家政服务"
    },
    "orderAmount": 150.00,
    "serviceTime": "2026-02-03 09:00:00",
    "serviceAddress": "北京市朝阳区xxx",
    "orderStatus": 2,
    "staffList": [
      {
        "staffId": "xxx",
        "staffName": "李四",
        "phone": "139****9999",
        "isPrimary": true
      }
    ],
    "acceptTime": "2026-02-02 15:00:00",
    "startTime": "2026-02-03 09:00:00",
    "createTime": "2026-02-02 14:30:00"
  }
}
```

---

#### 6.2.4 服务商接单

**PUT** `/order/{orderId}/accept`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| staffIds | array | 是 | 指派的服务人员ID列表 |
| primaryStaffId | string | 是 | 主要服务人员ID |

---

#### 6.2.5 开始服务

**PUT** `/order/{orderId}/start`

---

#### 6.2.6 完成服务

**PUT** `/order/{orderId}/complete`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| serviceRecord | string | 是 | 服务记录 |
| photos | array | 否 | 服务照片URL列表 |

---

#### 6.2.7 取消订单

**PUT** `/order/{orderId}/cancel`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| cancelReason | string | 是 | 取消原因 |

---

#### 6.2.8 获取服务人员实时位置

**GET** `/order/{orderId}/staff-location`

---

### 6.3 服务评价

#### 6.3.1 提交服务评价

**POST** `/order/{orderId}/evaluation`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| starLevel | int | 是 | 星级评分（1-5） |
| evaluationContent | string | 否 | 评价内容 |
| proofPhotos | array | 否 | 照片凭证URL列表 |

---

#### 6.3.2 获取订单评价

**GET** `/order/{orderId}/evaluation`

---

#### 6.3.3 服务商回复评价

**PUT** `/evaluation/{evaluationId}/reply`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| providerReply | string | 是 | 回复内容 |

---

#### 6.3.4 获取服务商评价列表

**GET** `/provider/{providerId}/evaluations`

---

## 7. 精神慰藉模块

### 7.1 线上课程

#### 7.1.1 获取课程列表

**GET** `/courses`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| courseType | string | 否 | 课程类型 |
| status | int | 否 | 课程状态 |
| keyword | string | 否 | 搜索关键词 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

#### 7.1.2 获取课程详情

**GET** `/course/{courseId}`

---

#### 7.1.3 报名课程

**POST** `/course/{courseId}/enroll`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 是 | 老人ID |

---

#### 7.1.4 取消报名

**DELETE** `/course/{courseId}/enrollment/{elderlyId}`

---

#### 7.1.5 获取我的课程列表

**GET** `/elderly/{elderlyId}/courses`

---

#### 7.1.6 更新学习进度

**PUT** `/course/{courseId}/progress`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 是 | 老人ID |
| learningProgress | int | 是 | 学习进度（0-100） |

---

### 7.2 线下活动

#### 7.2.1 获取活动列表

**GET** `/activities`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| activityType | string | 否 | 活动类型 |
| status | int | 否 | 活动状态 |
| startTime | datetime | 否 | 开始时间 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

#### 7.2.2 获取活动详情

**GET** `/activity/{activityId}`

---

#### 7.2.3 报名活动

**POST** `/activity/{activityId}/enroll`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 是 | 老人ID |

---

#### 7.2.4 取消报名

**DELETE** `/activity/{activityId}/enrollment/{elderlyId}`

---

#### 7.2.5 活动签到

**POST** `/activity/{activityId}/checkin`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| elderlyId | string | 是 | 老人ID |

---

#### 7.2.6 获取我的活动列表

**GET** `/elderly/{elderlyId}/activities`

---

### 7.3 活动管理（运营端）

#### 7.3.1 创建线下活动

**POST** `/admin/activities`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| activityName | string | 是 | 活动名称 |
| activityType | string | 是 | 活动类型 |
| activityDescription | string | 否 | 活动描述 |
| organizer | string | 否 | 主办方 |
| activityCover | string | 否 | 活动封面URL |
| activityLocation | string | 是 | 活动地点 |
| activityLng | decimal | 否 | 活动地点经度 |
| activityLat | decimal | 否 | 活动地点纬度 |
| maxParticipants | int | 否 | 最大参与人数 |
| startTime | datetime | 是 | 开始时间 |
| endTime | datetime | 是 | 结束时间 |

---

#### 7.3.2 更新活动信息

**PUT** `/admin/activity/{activityId}`

---

#### 7.3.3 取消活动

**PUT** `/admin/activity/{activityId}/cancel`

---

#### 7.3.4 获取活动报名列表

**GET** `/admin/activity/{activityId}/enrollments`

---

## 8. 支付结算模块

### 8.1 订单支付

#### 8.1.1 发起支付

**POST** `/payment/create`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| orderId | string | 是 | 订单ID |
| paymentMethod | string | 是 | 支付方式（wechat/alipay） |

**响应示例**

```json
{
  "code": 200,
  "data": {
    "paymentId": "xxx",
    "paymentAmount": 150.00,
    "payParams": {
      "appId": "xxx",
      "timeStamp": "1706860800",
      "nonceStr": "xxx",
      "package": "prepay_id=xxx",
      "signType": "RSA",
      "paySign": "xxx"
    }
  }
}
```

---

#### 8.1.2 支付回调通知

**POST** `/payment/notify/{channel}`

> 🔓 第三方回调接口，无需认证

---

#### 8.1.3 查询支付状态

**GET** `/payment/{paymentId}/status`

---

### 8.2 退款

#### 8.2.1 申请退款

**POST** `/order/{orderId}/refund`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| refundReason | string | 是 | 退款原因 |
| refundAmount | decimal | 否 | 退款金额（默认全额） |

---

#### 8.2.2 审核退款（运营端）

**PUT** `/admin/refund/{paymentId}/audit`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| approved | boolean | 是 | 是否同意 |
| remark | string | 否 | 审核备注 |

---

### 8.3 服务商结算

#### 8.3.1 获取结算单列表

**GET** `/provider/{providerId}/settlements`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| settlementPeriod | string | 否 | 结算周期（如2026-01） |
| settlementStatus | int | 否 | 结算状态 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

#### 8.3.2 获取结算单详情

**GET** `/settlement/{settlementId}`

---

#### 8.3.3 申请提现

**POST** `/settlement/{settlementId}/withdraw`

> ⚠️ 需遵循 T+7 结算周期

---

#### 8.3.4 审核提现（运营端）

**PUT** `/admin/settlement/{settlementId}/audit`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| approved | boolean | 是 | 是否同意 |
| remark | string | 否 | 审核备注 |

---

#### 8.3.5 确认转账完成（运营端）

**PUT** `/admin/settlement/{settlementId}/transfer`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| transferVoucher | string | 是 | 转账凭证URL |

---

## 9. 系统管理模块

### 9.1 角色权限管理

#### 9.1.1 获取角色列表

**GET** `/admin/roles`

---

#### 9.1.2 创建角色

**POST** `/admin/roles`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| roleName | string | 是 | 角色名称 |
| roleCode | string | 是 | 角色编码 |
| roleDesc | string | 否 | 角色描述 |
| permissionCodes | array | 是 | 权限编码列表 |

---

#### 9.1.3 更新角色

**PUT** `/admin/role/{roleId}`

---

#### 9.1.4 删除角色

**DELETE** `/admin/role/{roleId}`

---

#### 9.1.5 获取权限列表

**GET** `/admin/permissions`

---

### 9.2 系统用户管理

#### 9.2.1 获取系统用户列表

**GET** `/admin/users`

---

#### 9.2.2 创建系统用户

**POST** `/admin/users`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |
| realName | string | 是 | 真实姓名 |
| phone | string | 否 | 手机号 |
| email | string | 否 | 邮箱 |
| roleId | string | 是 | 角色ID |
| userType | string | 是 | 用户类型 |

---

#### 9.2.3 更新系统用户

**PUT** `/admin/user/{userId}`

---

#### 9.2.4 禁用/启用用户

**PUT** `/admin/user/{userId}/status`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| status | int | 是 | 状态（0-禁用、1-正常） |

---

#### 9.2.5 重置用户密码

**PUT** `/admin/user/{userId}/password/reset`

---

### 9.3 服务商审核

#### 9.3.1 获取待审核服务商列表

**GET** `/admin/providers/pending`

---

#### 9.3.2 审核服务商

**PUT** `/admin/provider/{providerId}/audit`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| auditStatus | int | 是 | 审核状态（1-通过、2-驳回） |
| auditRemark | string | 否 | 审核备注 |

---

### 9.4 系统日志

#### 9.4.1 获取操作日志列表

**GET** `/admin/logs`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| operatorId | string | 否 | 操作人ID |
| operateModule | string | 否 | 操作模块 |
| operateType | string | 否 | 操作类型 |
| startTime | datetime | 否 | 开始时间 |
| endTime | datetime | 否 | 结束时间 |
| pageNum | int | 否 | 页码 |
| pageSize | int | 否 | 每页数量 |

---

#### 9.4.2 获取日志详情

**GET** `/admin/log/{logId}`

---

#### 9.4.3 导出操作日志

**GET** `/admin/logs/export`

---

## 10. 数据统计模块

### 10.1 运营数据看板

#### 10.1.1 获取核心指标概览

**GET** `/admin/dashboard/overview`

**响应示例**

```json
{
  "code": 200,
  "data": {
    "totalElderly": 10000,
    "activeElderly": 8500,
    "totalProviders": 150,
    "totalOrders": 50000,
    "todayOrders": 120,
    "todayAlerts": 15,
    "alertResponseRate": 0.95,
    "avgSatisfaction": 4.5
  }
}
```

---

#### 10.1.2 获取订单统计

**GET** `/admin/dashboard/orders`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| period | string | 是 | 统计周期（day/week/month/year） |
| startTime | datetime | 否 | 开始时间 |
| endTime | datetime | 否 | 结束时间 |

---

#### 10.1.3 获取预警统计

**GET** `/admin/dashboard/alerts`

---

#### 10.1.4 获取健康数据分布

**GET** `/admin/dashboard/health`

---

#### 10.1.5 获取服务满意度分析

**GET** `/admin/dashboard/satisfaction`

---

### 10.2 监管数据

#### 10.2.1 获取合规达标率

**GET** `/supervisor/compliance`

---

#### 10.2.2 获取服务商响应率排名

**GET** `/supervisor/provider-ranking`

---

#### 10.2.3 获取区域服务需求分布

**GET** `/supervisor/service-distribution`

---

#### 10.2.4 获取老龄化趋势统计

**GET** `/supervisor/aging-trend`

---

#### 10.2.5 导出统计报告

**POST** `/supervisor/report/export`

**请求参数**

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| reportType | string | 是 | 报告类型 |
| startTime | datetime | 是 | 开始时间 |
| endTime | datetime | 是 | 结束时间 |
| format | string | 否 | 导出格式（pdf/excel） |

---

## 📎 附录

### A. 订单状态流转

```
待支付(0) → 待接单(1) → 服务中(2) → 已完成(3)
    ↓           ↓           ↓
 已取消(4)   已取消(4)   退款中(5) → 已退款(6)
```

### B. 预警状态流转

```
未处理(0) → 处理中(1) → 已完成(2)
                ↓
              误报(3)
```

### C. 服务商审核状态

| 状态码 | 说明 |
|--------|------|
| 0 | 待审核 |
| 1 | 已通过 |
| 2 | 已驳回 |

### D. 健康状态

| 状态码 | 说明 |
|--------|------|
| 0 | 健康 |
| 1 | 慢病 |
| 2 | 失能 |

### E. WebSocket 推送事件

#### E.1 连接地址

```
wss://api.example.com/ws?token={accessToken}
```

#### E.2 心跳机制

- 客户端每 **30秒** 发送一次心跳包：`{"type": "ping"}`
- 服务端响应：`{"type": "pong"}`
- 超过 **90秒** 未收到心跳，服务端主动断开连接

#### E.3 推送事件类型

| 事件类型 | 说明 | 推送对象 |
|----------|------|----------|
| `alert.new` | 新预警通知 | 监护人、服务商 |
| `alert.update` | 预警状态更新 | 监护人 |
| `order.status` | 订单状态变更 | 老人、监护人 |
| `health.abnormal` | 健康数据异常 | 监护人 |
| `medication.remind` | 用药提醒 | 老人 |
| `activity.remind` | 活动提醒 | 老人 |

#### E.4 推送报文格式

**通用报文结构**

```json
{
  "type": "事件类型",
  "timestamp": 1706860800000,
  "data": {}
}
```

**1. 新预警通知 (`alert.new`)**

```json
{
  "type": "alert.new",
  "timestamp": 1706860800000,
  "data": {
    "alertId": "550e8400-e29b-41d4-a716-446655440000",
    "elderlyId": "xxx",
    "elderlyName": "张三",
    "alertType": "跌倒",
    "alertTime": "2026-02-02 14:30:00",
    "alertStatus": 0,
    "location": {
      "lng": 116.404,
      "lat": 39.915,
      "address": "北京市朝阳区xxx街道"
    },
    "priority": "high",
    "message": "张三发生跌倒预警，请立即处理！"
  }
}
```

**2. 预警状态更新 (`alert.update`)**

```json
{
  "type": "alert.update",
  "timestamp": 1706860800000,
  "data": {
    "alertId": "xxx",
    "alertStatus": 1,
    "statusText": "处理中",
    "handlerName": "李四",
    "handlerRole": "服务商",
    "responseTime": "2026-02-02 14:35:00",
    "message": "服务商 李四 已接单，正在赶往现场"
  }
}
```

**3. 订单状态变更 (`order.status`)**

```json
{
  "type": "order.status",
  "timestamp": 1706860800000,
  "data": {
    "orderId": "xxx",
    "orderNo": "ORD202602021430001",
    "orderStatus": 2,
    "statusText": "服务中",
    "staffName": "王五",
    "staffPhone": "139****9999",
    "message": "服务人员 王五 已开始服务"
  }
}
```

**4. 健康数据异常 (`health.abnormal`)**

```json
{
  "type": "health.abnormal",
  "timestamp": 1706860800000,
  "data": {
    "elderlyId": "xxx",
    "elderlyName": "张三",
    "recordType": "血压",
    "abnormalType": "偏高",
    "currentValue": {
      "systolicBp": 165,
      "diastolicBp": 95
    },
    "threshold": {
      "sysBpMax": 140,
      "diaBpMax": 90
    },
    "collectTime": "2026-02-02 14:30:00",
    "message": "张三 血压偏高（165/95mmHg），请关注",
    "suggestion": "建议老人休息，必要时联系医生"
  }
}
```

**5. 用药提醒 (`medication.remind`)**

```json
{
  "type": "medication.remind",
  "timestamp": 1706860800000,
  "data": {
    "reminderId": "xxx",
    "elderlyId": "xxx",
    "medicationName": "降压药（氨氯地平）",
    "dosage": "1片",
    "reminderTime": "08:00",
    "message": "该吃药啦！降压药（氨氯地平）1片",
    "voiceUrl": "https://oss.example.com/voice/medication_remind.mp3"
  }
}
```

**6. 活动提醒 (`activity.remind`)**

```json
{
  "type": "activity.remind",
  "timestamp": 1706860800000,
  "data": {
    "activityId": "xxx",
    "activityName": "社区书法班",
    "activityLocation": "社区活动中心3楼",
    "startTime": "2026-02-03 09:00:00",
    "remindBefore": 24,
    "message": "您报名的"社区书法班"将于明天 09:00 开始，地点：社区活动中心3楼"
  }
}
```

#### E.5 前端监听示例

```javascript
const ws = new WebSocket(`wss://api.example.com/ws?token=${accessToken}`);

ws.onmessage = (event) => {
  const message = JSON.parse(event.data);
  
  switch (message.type) {
    case 'alert.new':
      // 显示紧急预警弹窗
      showAlertNotification(message.data);
      break;
    case 'health.abnormal':
      // 显示健康异常提醒
      showHealthWarning(message.data);
      break;
    case 'medication.remind':
      // 播放语音提醒
      playVoiceRemind(message.data.voiceUrl);
      break;
    // ... 其他事件处理
  }
};

// 心跳保活
setInterval(() => {
  if (ws.readyState === WebSocket.OPEN) {
    ws.send(JSON.stringify({ type: 'ping' }));
  }
}, 30000);
```

---

> 📌 **注意事项**
> 1. 所有涉及敏感数据（身份证、手机号）的接口在 Service 层需进行 AES 加解密处理
> 2. 应急呼叫接口（`/emergency/call`）响应时间必须 ≤ 3 秒
> 3. 服务商提现需遵循 T+7 结算周期
> 4. 健康数据采集需进行逻辑范围校验（如收缩压 90-140mmHg）
> 5. 文件上传返回 OSS 绝对路径，数据库存储相对路径
> 6. 高并发场景（设备数据采集）触发 429 错误时，前端需实现指数退避重试
