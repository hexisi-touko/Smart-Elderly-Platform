-- ============================================================================
-- 智慧居家养老服务平台 - 数据库初始化脚本
-- 数据库版本: MySQL 8.0
-- 生成时间: 2026-02-01
-- 说明: 
--   1. 所有主键使用 VARCHAR(36) 存储 UUID
--   2. 敏感字段（身份证号、手机号、银行卡号）需使用 AES 加密
--   3. 所有业务表包含逻辑删除字段 is_deleted
--   4. 所有表包含审计字段：create_time, update_time
-- ============================================================================

-- 创建数据库
DROP DATABASE IF EXISTS smart_elderly_platform;
CREATE DATABASE smart_elderly_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smart_elderly_platform;

-- ============================================================================
-- 一、用户维度核心表
-- ============================================================================

-- 1. 老人表
DROP TABLE IF EXISTS t_elderly;
CREATE TABLE t_elderly (
    elderly_id VARCHAR(36) PRIMARY KEY COMMENT '老人唯一标识（UUID）',
    name VARCHAR(50) NOT NULL COMMENT '老人姓名',
    id_card VARCHAR(100) NOT NULL COMMENT '身份证号（需 AES 加密）',
    phone VARCHAR(50) COMMENT '老人手机号（需 AES 加密）',
    gender TINYINT COMMENT '性别（0-女、1-男）',
    birth_date DATE COMMENT '出生日期',
    health_status TINYINT NOT NULL DEFAULT 0 COMMENT '健康状态（0-健康、1-慢病、2-失能）',
    address VARCHAR(200) COMMENT '居住地址',
    emergency_contact_name VARCHAR(50) COMMENT '紧急联系人姓名',
    emergency_contact_phone VARCHAR(50) COMMENT '紧急联系人电话（需 AES 加密）',
    backup_flag TINYINT DEFAULT 0 COMMENT '备份标记（0-未备份、1-已备份）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_phone (phone),
    INDEX idx_health_status (health_status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='老人基础信息表';

-- 2. 老人慢病关联表
DROP TABLE IF EXISTS t_elderly_chronic;
CREATE TABLE t_elderly_chronic (
    id VARCHAR(36) PRIMARY KEY COMMENT '唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    chronic_type VARCHAR(50) NOT NULL COMMENT '慢病类型（如高血压、糖尿病、冠心病等）',
    diagnosis_date DATE COMMENT '确诊日期',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_chronic_type (chronic_type),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='老人慢病关联表';

-- 3. 监护人表
DROP TABLE IF EXISTS t_guardian;
CREATE TABLE t_guardian (
    guardian_id VARCHAR(36) PRIMARY KEY COMMENT '监护人唯一标识（UUID）',
    name VARCHAR(50) NOT NULL COMMENT '监护人姓名',
    id_card VARCHAR(100) NOT NULL COMMENT '身份证号（需 AES 加密）',
    phone VARCHAR(50) NOT NULL COMMENT '监护人手机号（需 AES 加密）',
    relationship VARCHAR(20) COMMENT '与老人关系（子女、配偶、亲属等）',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主要监护人（0-否、1-是）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_phone (phone),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='监护人信息表';

-- 4. 老人-监护人关联表（N:M中间表）
DROP TABLE IF EXISTS t_elderly_guardian;
CREATE TABLE t_elderly_guardian (
    id VARCHAR(36) PRIMARY KEY COMMENT '唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    guardian_id VARCHAR(36) NOT NULL COMMENT '关联监护人ID',
    authorization_status TINYINT DEFAULT 1 COMMENT '授权状态（0-已撤销、1-已授权）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_guardian_id (guardian_id),
    INDEX idx_is_deleted (is_deleted),
    UNIQUE KEY uk_elderly_guardian (elderly_id, guardian_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='老人-监护人关联表';

-- 5. 服务人员表
DROP TABLE IF EXISTS t_service_staff;
CREATE TABLE t_service_staff (
    staff_id VARCHAR(36) PRIMARY KEY COMMENT '服务人员唯一标识（UUID）',
    staff_name VARCHAR(50) NOT NULL COMMENT '服务人员姓名',
    provider_id VARCHAR(36) NOT NULL COMMENT '关联所属服务商ID',
    phone VARCHAR(50) NOT NULL COMMENT '服务人员手机号（需 AES 加密）',
    id_card VARCHAR(100) COMMENT '身份证号（需 AES 加密）',
    certificate VARCHAR(100) COMMENT '职业资格证书编号（如护理证、养老护理员证）',
    staff_type VARCHAR(20) COMMENT '人员类型（护理员、社工、医护等）',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '工作状态（0-休息、1-在岗、2-请假、3-离职）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_provider_id (provider_id),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务人员信息表';

-- 6. 系统用户表（运营、监管人员）
DROP TABLE IF EXISTS t_system_user;
CREATE TABLE t_system_user (
    user_id VARCHAR(36) PRIMARY KEY COMMENT '用户唯一标识（UUID）',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名（登录账号）',
    password VARCHAR(100) NOT NULL COMMENT '密码（SHA-256哈希）',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(50) COMMENT '手机号（需 AES 加密）',
    email VARCHAR(100) COMMENT '电子邮箱',
    role_id VARCHAR(36) NOT NULL COMMENT '关联角色ID',
    user_type VARCHAR(20) NOT NULL COMMENT '用户类型（运营人员、监管人员、系统管理员）',
    status TINYINT DEFAULT 1 COMMENT '账号状态（0-禁用、1-正常）',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_role_id (role_id),
    INDEX idx_phone (phone),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- ============================================================================
-- 二、服务维度核心表
-- ============================================================================

-- 7. 服务商表
DROP TABLE IF EXISTS t_service_provider;
CREATE TABLE t_service_provider (
    provider_id VARCHAR(36) PRIMARY KEY COMMENT '服务商唯一标识（UUID）',
    provider_name VARCHAR(100) NOT NULL UNIQUE COMMENT '服务商名称',
    license_code VARCHAR(100) NOT NULL UNIQUE COMMENT '资质许可证号',
    contact_person VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    contact_phone VARCHAR(50) NOT NULL COMMENT '联系人手机号（需 AES 加密）',
    address VARCHAR(200) NOT NULL COMMENT '服务商办公地址',
    business_license VARCHAR(100) COMMENT '营业执照编号',
    audit_status TINYINT NOT NULL DEFAULT 0 COMMENT '审核状态（0-待审核、1-已通过、2-已驳回）',
    service_scope VARCHAR(200) NOT NULL COMMENT '服务范围（如生活照料、应急救援、医疗护理）',
    score DECIMAL(3,2) DEFAULT 5.00 COMMENT '服务商综合评分（1.00-5.00）',
    bank_account VARCHAR(100) COMMENT '结算银行账号（需 AES 加密）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_provider_name (provider_name),
    INDEX idx_audit_status (audit_status),
    INDEX idx_score (score),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务商信息表';

-- 8. 服务项目表
DROP TABLE IF EXISTS t_service_item;
CREATE TABLE t_service_item (
    item_id VARCHAR(36) PRIMARY KEY COMMENT '服务项目唯一标识（UUID）',
    provider_id VARCHAR(36) NOT NULL COMMENT '关联服务商ID',
    item_name VARCHAR(100) NOT NULL COMMENT '服务项目名称',
    item_category VARCHAR(50) NOT NULL COMMENT '服务类别（生活照料、医疗护理、康复理疗、家政服务等）',
    item_description TEXT COMMENT '服务项目描述',
    standard_price DECIMAL(10,2) NOT NULL COMMENT '标准价格（元）',
    service_duration INT COMMENT '服务时长（分钟）',
    service_unit VARCHAR(20) DEFAULT '次' COMMENT '服务单位（次、小时、天等）',
    status TINYINT DEFAULT 1 COMMENT '上架状态（0-下架、1-上架）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_provider_id (provider_id),
    INDEX idx_item_category (item_category),
    INDEX idx_status (status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务项目表';

-- 9. 线上课程表
DROP TABLE IF EXISTS t_online_course;
CREATE TABLE t_online_course (
    course_id VARCHAR(36) PRIMARY KEY COMMENT '课程唯一标识（UUID）',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_type VARCHAR(50) NOT NULL COMMENT '课程类型（健康讲座、兴趣培训、文化娱乐等）',
    course_description TEXT COMMENT '课程描述',
    instructor VARCHAR(50) COMMENT '讲师姓名',
    course_cover VARCHAR(200) COMMENT '课程封面图片URL',
    video_url VARCHAR(500) COMMENT '课程视频URL',
    duration INT COMMENT '课程时长（分钟）',
    max_enrollment INT DEFAULT 0 COMMENT '最大报名人数（0表示不限）',
    current_enrollment INT DEFAULT 0 COMMENT '当前报名人数',
    start_time DATETIME COMMENT '开课时间',
    end_time DATETIME COMMENT '结课时间',
    status TINYINT DEFAULT 1 COMMENT '课程状态（0-下架、1-报名中、2-进行中、3-已结束）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_course_type (course_type),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='线上课程表';

-- 10. 老人-课程报名表（N:M中间表）
DROP TABLE IF EXISTS t_elderly_course;
CREATE TABLE t_elderly_course (
    id VARCHAR(36) PRIMARY KEY COMMENT '唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    course_id VARCHAR(36) NOT NULL COMMENT '关联课程ID',
    enrollment_status TINYINT DEFAULT 1 COMMENT '报名状态（0-已取消、1-已报名、2-学习中、3-已完成）',
    learning_progress INT DEFAULT 0 COMMENT '学习进度（百分比：0-100）',
    enrollment_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
    completion_time DATETIME COMMENT '完成时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_course_id (course_id),
    INDEX idx_is_deleted (is_deleted),
    UNIQUE KEY uk_elderly_course (elderly_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='老人-课程报名表';

-- 11. 线下活动表
DROP TABLE IF EXISTS t_offline_activity;
CREATE TABLE t_offline_activity (
    activity_id VARCHAR(36) PRIMARY KEY COMMENT '活动唯一标识（UUID）',
    activity_name VARCHAR(100) NOT NULL COMMENT '活动名称',
    activity_type VARCHAR(50) NOT NULL COMMENT '活动类型（文娱活动、社交聚会、健康讲座等）',
    activity_description TEXT COMMENT '活动描述',
    organizer VARCHAR(100) COMMENT '主办方',
    activity_cover VARCHAR(200) COMMENT '活动封面图片URL',
    activity_location VARCHAR(200) NOT NULL COMMENT '活动地点',
    activity_lng DECIMAL(10,6) COMMENT '活动地点经度',
    activity_lat DECIMAL(10,6) COMMENT '活动地点纬度',
    max_participants INT DEFAULT 0 COMMENT '最大参与人数（0表示不限）',
    current_participants INT DEFAULT 0 COMMENT '当前报名人数',
    start_time DATETIME NOT NULL COMMENT '活动开始时间',
    end_time DATETIME NOT NULL COMMENT '活动结束时间',
    status TINYINT DEFAULT 1 COMMENT '活动状态（0-已取消、1-报名中、2-进行中、3-已结束）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_activity_type (activity_type),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='线下活动表';

-- 12. 活动报名表（N:M中间表）
DROP TABLE IF EXISTS t_activity_enrollment;
CREATE TABLE t_activity_enrollment (
    id VARCHAR(36) PRIMARY KEY COMMENT '唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    activity_id VARCHAR(36) NOT NULL COMMENT '关联活动ID',
    enrollment_status TINYINT DEFAULT 1 COMMENT '报名状态（0-已取消、1-已报名、2-已签到、3-已完成）',
    check_in_time DATETIME COMMENT '签到时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_activity_id (activity_id),
    INDEX idx_enrollment_status (enrollment_status),
    INDEX idx_is_deleted (is_deleted),
    UNIQUE KEY uk_elderly_activity (elderly_id, activity_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动报名表';

-- ============================================================================
-- 三、设备与健康数据核心表
-- ============================================================================

-- 13. 智能设备表
DROP TABLE IF EXISTS t_smart_device;
CREATE TABLE t_smart_device (
    device_id VARCHAR(36) PRIMARY KEY COMMENT '设备唯一标识（UUID）',
    device_code VARCHAR(50) NOT NULL UNIQUE COMMENT '设备硬件编码',
    device_type VARCHAR(50) NOT NULL COMMENT '设备类型（血压计、血糖仪、红外探测器、智能手环等）',
    elderly_id VARCHAR(36) COMMENT '关联老人ID（支持设备换绑）',
    device_brand VARCHAR(50) COMMENT '设备品牌',
    device_model VARCHAR(50) COMMENT '设备型号',
    bind_time DATETIME COMMENT '绑定时间',
    maintenance_time DATETIME COMMENT '上次维护时间',
    next_maintenance_time DATETIME COMMENT '下次维护时间',
    push_status TINYINT DEFAULT 1 COMMENT '推送状态（0-关闭、1-开启）',
    device_status TINYINT DEFAULT 1 COMMENT '设备状态（0-离线、1-在线、2-故障）',
    third_party_device_id VARCHAR(100) COMMENT '第三方设备平台ID',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_device_code (device_code),
    INDEX idx_device_type (device_type),
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_device_status (device_status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='智能设备表';

-- 14. 健康记录表（建议按年度分表，如 t_health_record_2026）
DROP TABLE IF EXISTS t_health_record;
CREATE TABLE t_health_record (
    record_id VARCHAR(36) PRIMARY KEY COMMENT '记录唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    device_id VARCHAR(36) COMMENT '关联采集设备ID（手动录入时为空）',
    collect_time DATETIME NOT NULL COMMENT '数据采集时间',
    record_type VARCHAR(50) NOT NULL COMMENT '记录类型（血压、血糖、心率、体温、血氧等）',
    collect_method TINYINT NOT NULL COMMENT '采集方式（0-智能设备、1-手动录入、2-医院同步）',
    data_source VARCHAR(20) NOT NULL COMMENT '数据来源（设备、手动、医院）',
    systolic_bp INT COMMENT '收缩压（mmHg），仅血压类型记录填写',
    diastolic_bp INT COMMENT '舒张压（mmHg），仅血压类型记录填写',
    blood_sugar DECIMAL(5,2) COMMENT '血糖值（mmol/L）',
    heart_rate INT COMMENT '心率（次/分钟）',
    temperature DECIMAL(4,2) COMMENT '体温（℃）',
    blood_oxygen INT COMMENT '血氧饱和度（%）',
    data_status TINYINT DEFAULT 1 COMMENT '数据状态（0-异常已剔除、1-正常、2-异常待处理）',
    third_party_sync_id VARCHAR(100) COMMENT '医院同步标识',
    backup_flag TINYINT DEFAULT 0 COMMENT '备份标记（0-未备份、1-已备份）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_collect_type (elderly_id, collect_time, record_type),
    INDEX idx_data_source (data_source),
    INDEX idx_collect_time (collect_time),
    INDEX idx_data_status (data_status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康记录表';

-- 15. 健康阈值表
DROP TABLE IF EXISTS t_health_threshold;
CREATE TABLE t_health_threshold (
    threshold_id VARCHAR(36) PRIMARY KEY COMMENT '阈值唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL UNIQUE COMMENT '关联老人ID（一人一套个性化阈值）',
    sys_bp_max INT NOT NULL DEFAULT 140 COMMENT '收缩压上限阈值（mmHg）',
    sys_bp_min INT NOT NULL DEFAULT 90 COMMENT '收缩压下限阈值（mmHg）',
    dia_bp_max INT NOT NULL DEFAULT 90 COMMENT '舒张压上限阈值（mmHg）',
    dia_bp_min INT NOT NULL DEFAULT 60 COMMENT '舒张压下限阈值（mmHg）',
    heart_rate_max INT NOT NULL DEFAULT 100 COMMENT '心率上限阈值（次/分钟）',
    heart_rate_min INT NOT NULL DEFAULT 60 COMMENT '心率下限阈值（次/分钟）',
    blood_sugar_max DECIMAL(5,2) NOT NULL DEFAULT 7.0 COMMENT '血糖上限阈值（mmol/L）',
    blood_sugar_min DECIMAL(5,2) NOT NULL DEFAULT 3.9 COMMENT '血糖下限阈值（mmol/L）',
    temperature_max DECIMAL(4,2) NOT NULL DEFAULT 37.3 COMMENT '体温上限阈值（℃）',
    temperature_min DECIMAL(4,2) NOT NULL DEFAULT 36.0 COMMENT '体温下限阈值（℃）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康阈值表';

-- 16. 安全预警记录表（建议按年度分表，如 t_safety_alert_2026）
DROP TABLE IF EXISTS t_safety_alert;
CREATE TABLE t_safety_alert (
    alert_id VARCHAR(36) PRIMARY KEY COMMENT '预警唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    device_id VARCHAR(36) NOT NULL COMMENT '关联触发预警的设备ID',
    alert_type VARCHAR(50) NOT NULL COMMENT '预警类型（跌倒、血压异常、久坐不动、离开安全区域等）',
    alert_time DATETIME NOT NULL COMMENT '预警触发时间',
    response_time DATETIME COMMENT '服务商接单时间（用于计算15分钟响应率）',
    complete_time DATETIME COMMENT '处置完成时间',
    urge_count TINYINT DEFAULT 0 COMMENT '子女一键催促次数',
    handler_id VARCHAR(36) COMMENT '处理人ID（服务商/运营人员账号）',
    handler_role VARCHAR(20) NOT NULL COMMENT '处理人角色（服务商、运营）',
    alert_lng DECIMAL(10,6) NOT NULL COMMENT '预警地点经度',
    alert_lat DECIMAL(10,6) NOT NULL COMMENT '预警地点纬度',
    alert_address VARCHAR(100) NOT NULL COMMENT '预警地点文字描述',
    alert_status TINYINT NOT NULL DEFAULT 0 COMMENT '预警状态（0-未处理、1-处理中、2-已完成、3-误报）',
    handle_result TEXT COMMENT '处置结果描述',
    emergency_order_id VARCHAR(50) COMMENT '急救系统订单号（对接第三方急救接口）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_alert_time_status (alert_time, alert_status),
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_alert_status (alert_status),
    INDEX idx_emergency_order_id (emergency_order_id),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='安全预警记录表';

-- 17. 用药提醒表
DROP TABLE IF EXISTS t_medication_reminder;
CREATE TABLE t_medication_reminder (
    reminder_id VARCHAR(36) PRIMARY KEY COMMENT '提醒唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    medication_name VARCHAR(100) NOT NULL COMMENT '药品名称',
    medication_type VARCHAR(50) COMMENT '药品类型（降压药、降糖药等）',
    dosage VARCHAR(50) COMMENT '用药剂量（如1片、10ml）',
    frequency VARCHAR(50) NOT NULL COMMENT '用药频率（每日一次、每日三次等）',
    reminder_time TIME NOT NULL COMMENT '提醒时间',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE COMMENT '结束日期',
    is_taken TINYINT DEFAULT 0 COMMENT '今日是否已服药（0-未服药、1-已服药）',
    last_taken_time DATETIME COMMENT '最后服药确认时间',
    note TEXT COMMENT '备注说明',
    status TINYINT DEFAULT 1 COMMENT '提醒状态（0-已停用、1-正常）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_reminder_time (reminder_time),
    INDEX idx_status (status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用药提醒表';

-- 18. 体检预约表
DROP TABLE IF EXISTS t_physical_exam_reservation;
CREATE TABLE t_physical_exam_reservation (
    reservation_id VARCHAR(36) PRIMARY KEY COMMENT '预约唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    exam_type VARCHAR(50) NOT NULL COMMENT '体检类型（常规体检、专项检查、上门体检等）',
    exam_institution VARCHAR(100) NOT NULL COMMENT '体检机构名称',
    third_party_org_id VARCHAR(100) COMMENT '第三方体检机构ID',
    reservation_time DATETIME NOT NULL COMMENT '预约时间',
    exam_address VARCHAR(200) NOT NULL COMMENT '体检地址',
    exam_items TEXT COMMENT '体检项目清单',
    reservation_status TINYINT DEFAULT 0 COMMENT '预约状态（0-待确认、1-已确认、2-已完成、3-已取消）',
    report_status TINYINT DEFAULT 0 COMMENT '报告状态（0-未生成、1-已生成、2-已解读）',
    report_url VARCHAR(500) COMMENT '体检报告PDF链接',
    report_interpret TEXT COMMENT '报告解读内容',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_elderly_reservation (elderly_id, reservation_time),
    INDEX idx_reservation_status (reservation_status),
    INDEX idx_report_status (report_status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='体检预约表';

-- ============================================================================
-- 四、服务与订单核心表
-- ============================================================================

-- 19. 服务订单表
DROP TABLE IF EXISTS t_service_order;
CREATE TABLE t_service_order (
    order_id VARCHAR(36) PRIMARY KEY COMMENT '订单唯一标识（UUID）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '关联老人ID',
    provider_id VARCHAR(36) NOT NULL COMMENT '关联服务商ID',
    service_item_id VARCHAR(36) NOT NULL COMMENT '关联服务项目ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号（业务流水号）',
    order_amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    service_time DATETIME NOT NULL COMMENT '约定服务时间',
    service_address VARCHAR(200) COMMENT '服务地址',
    service_requirements TEXT COMMENT '服务要求说明',
    order_status TINYINT NOT NULL DEFAULT 0 COMMENT '订单状态（0-待支付、1-待接单、2-服务中、3-已完成、4-已取消、5-退款中、6-已退款）',
    accept_time DATETIME COMMENT '接单时间',
    start_time DATETIME COMMENT '服务开始时间',
    complete_time DATETIME COMMENT '服务完成时间',
    cancel_reason VARCHAR(200) COMMENT '取消原因',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_provider_status_time (provider_id, order_status, service_time),
    INDEX idx_elderly_status (elderly_id, order_status),
    INDEX idx_order_no (order_no),
    INDEX idx_order_status (order_status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务订单表';

-- 20. 订单-服务人员关联表（N:M中间表，支持多人协作服务）
DROP TABLE IF EXISTS t_order_staff;
CREATE TABLE t_order_staff (
    id VARCHAR(36) PRIMARY KEY COMMENT '唯一标识（UUID）',
    order_id VARCHAR(36) NOT NULL COMMENT '关联订单ID',
    staff_id VARCHAR(36) NOT NULL COMMENT '关联服务人员ID',
    is_primary TINYINT DEFAULT 0 COMMENT '是否主要服务人员（0-否、1-是）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_id (order_id),
    INDEX idx_staff_id (staff_id),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单-服务人员关联表';

-- 21. 支付记录表
DROP TABLE IF EXISTS t_payment_record;
CREATE TABLE t_payment_record (
    payment_id VARCHAR(36) PRIMARY KEY COMMENT '支付记录唯一标识（UUID）',
    order_id VARCHAR(36) NOT NULL UNIQUE COMMENT '关联服务订单ID（1:1对应）',
    payment_amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    payment_time DATETIME NOT NULL COMMENT '支付时间',
    payment_method VARCHAR(20) COMMENT '支付方式（微信、支付宝、余额等）',
    payment_status TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态（0-未支付、1-已支付、2-支付失败）',
    transaction_id VARCHAR(100) COMMENT '第三方支付流水号',
    refund_flag TINYINT DEFAULT 0 COMMENT '退款标记（0-未退款、1-已退款）',
    refund_amount DECIMAL(10,2) COMMENT '退款金额',
    refund_time DATETIME COMMENT '退款时间',
    refund_reason VARCHAR(200) COMMENT '退款原因',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_id (order_id),
    INDEX idx_transaction_id (transaction_id),
    INDEX idx_payment_status (payment_status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- 22. 服务评价表
DROP TABLE IF EXISTS t_service_evaluation;
CREATE TABLE t_service_evaluation (
    evaluation_id VARCHAR(36) PRIMARY KEY COMMENT '评价唯一标识（UUID）',
    order_id VARCHAR(36) NOT NULL UNIQUE COMMENT '关联服务订单ID（1:1对应）',
    elderly_id VARCHAR(36) NOT NULL COMMENT '评价人ID（老人/监护人）',
    star_level TINYINT NOT NULL COMMENT '星级评分（1-5分）',
    evaluation_content TEXT COMMENT '评价内容',
    proof_photos TEXT COMMENT '照片凭证（阿里云OSS链接，多链接用逗号分隔）',
    evaluation_time DATETIME NOT NULL COMMENT '评价提交时间',
    provider_reply TEXT COMMENT '服务商回复',
    reply_time DATETIME COMMENT '回复时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_id (order_id),
    INDEX idx_elderly_id (elderly_id),
    INDEX idx_star_level (star_level),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务评价表';

-- ============================================================================
-- 五、权限与审计核心表
-- ============================================================================

-- 23. 角色表
DROP TABLE IF EXISTS t_role;
CREATE TABLE t_role (
    role_id VARCHAR(36) PRIMARY KEY COMMENT '角色唯一标识（UUID）',
    role_name VARCHAR(50) NOT NULL UNIQUE COMMENT '角色名称（如运营人员、监管人员、服务商管理员）',
    role_code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色编码（如ROLE_OPERATOR、ROLE_SUPERVISOR）',
    role_desc VARCHAR(200) COMMENT '角色权限描述',
    sort_order INT DEFAULT 0 COMMENT '显示排序',
    status TINYINT DEFAULT 1 COMMENT '角色状态（0-禁用、1-正常）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_role_code (role_code),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 24. 角色权限关联表
DROP TABLE IF EXISTS t_role_permission;
CREATE TABLE t_role_permission (
    id VARCHAR(36) PRIMARY KEY COMMENT '唯一标识（UUID）',
    role_id VARCHAR(36) NOT NULL COMMENT '关联角色ID',
    permission_code VARCHAR(50) NOT NULL COMMENT '权限编码（如order:query、elderly:edit、system:manage）',
    permission_name VARCHAR(100) COMMENT '权限名称',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_role_id (role_id),
    INDEX idx_permission_code (permission_code),
    INDEX idx_is_deleted (is_deleted),
    UNIQUE KEY uk_role_permission (role_id, permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 25. 系统日志表
DROP TABLE IF EXISTS t_system_log;
CREATE TABLE t_system_log (
    log_id VARCHAR(36) PRIMARY KEY COMMENT '日志唯一标识（UUID）',
    operator_id VARCHAR(36) NOT NULL COMMENT '操作人员ID',
    operator_name VARCHAR(50) NOT NULL COMMENT '操作人员姓名',
    operator_role VARCHAR(50) COMMENT '操作人员角色',
    operate_type VARCHAR(50) NOT NULL COMMENT '操作类型（登录、新增、修改、删除、查询等）',
    operate_module VARCHAR(50) NOT NULL COMMENT '操作模块（订单管理、用户管理、系统管理等）',
    operate_content TEXT COMMENT '操作内容描述',
    operate_result TINYINT DEFAULT 1 COMMENT '操作结果（0-失败、1-成功）',
    error_msg TEXT COMMENT '错误信息（失败时记录）',
    operate_ip VARCHAR(50) COMMENT '操作IP地址',
    operate_location VARCHAR(100) COMMENT 'IP归属地',
    user_agent VARCHAR(500) COMMENT '浏览器UA',
    operate_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    cost_time INT COMMENT '耗时（毫秒）',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_operate_time_operator (operate_time, operator_id),
    INDEX idx_operator_id (operator_id),
    INDEX idx_operate_module (operate_module),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统日志表';

-- ============================================================================
-- 六、结算相关表（补充）
-- ============================================================================

-- 26. 服务商结算表
DROP TABLE IF EXISTS t_provider_settlement;
CREATE TABLE t_provider_settlement (
    settlement_id VARCHAR(36) PRIMARY KEY COMMENT '结算唯一标识（UUID）',
    provider_id VARCHAR(36) NOT NULL COMMENT '关联服务商ID',
    settlement_period VARCHAR(20) NOT NULL COMMENT '结算周期（如2026-01）',
    order_count INT DEFAULT 0 COMMENT '订单数量',
    total_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '结算总金额',
    platform_fee DECIMAL(10,2) DEFAULT 0.00 COMMENT '平台服务费',
    actual_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '实际结算金额',
    settlement_status TINYINT DEFAULT 0 COMMENT '结算状态（0-待结算、1-待审核、2-已完成、3-已驳回）',
    apply_time DATETIME COMMENT '提现申请时间',
    approve_time DATETIME COMMENT '审核时间',
    transfer_time DATETIME COMMENT '转账时间',
    transfer_voucher VARCHAR(200) COMMENT '转账凭证URL',
    is_deleted TINYINT DEFAULT 0 COMMENT '逻辑删除（0-未删除、1-已删除）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_provider_id (provider_id),
    INDEX idx_settlement_period (settlement_period),
    INDEX idx_settlement_status (settlement_status),
    INDEX idx_is_deleted (is_deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='服务商结算表';

-- ============================================================================
-- 七、数据初始化（示例）
-- ============================================================================

-- 插入默认角色
INSERT INTO t_role (role_id, role_name, role_code, role_desc) VALUES
(UUID(), '系统管理员', 'ROLE_ADMIN', '拥有系统所有权限'),
(UUID(), '运营人员', 'ROLE_OPERATOR', '负责平台日常运营管理'),
(UUID(), '监管人员', 'ROLE_SUPERVISOR', '负责平台监管和审计'),
(UUID(), '服务商管理员', 'ROLE_PROVIDER_ADMIN', '服务商后台管理员');

-- ============================================================================
-- 八、分表提示
-- ============================================================================
-- 注意：以下表在生产环境中需要按年度分表，以优化查询性能和存储效率
-- 1. t_health_record -> t_health_record_2026, t_health_record_2027 等
-- 2. t_safety_alert -> t_safety_alert_2026, t_safety_alert_2027 等
-- 分表路由可通过 ShardingSphere 等中间件实现
-- ============================================================================

-- 脚本执行完成
SELECT '数据库初始化完成！' AS message;
