-- 快递驿站出入库与取件管理系统 建表脚本
CREATE DATABASE IF NOT EXISTS express_station DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE express_station;

-- 1. 工作人员/用户表
CREATE TABLE IF NOT EXISTS staff (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '登录账号',
    password VARCHAR(255) NOT NULL COMMENT '加密密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '联系电话',
    role TINYINT DEFAULT 1 COMMENT '0-工作人员/管理员 1-普通用户',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2. 快递公司表
CREATE TABLE IF NOT EXISTS express_company (
    id INT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(100) NOT NULL COMMENT '公司名称',
    code VARCHAR(50) UNIQUE COMMENT '快递编码',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3. 货架表
CREATE TABLE IF NOT EXISTS shelf (
    id INT PRIMARY KEY AUTO_INCREMENT,
    shelf_code VARCHAR(20) NOT NULL UNIQUE COMMENT '货架编号',
    capacity INT DEFAULT 0 COMMENT '容量',
    used_count INT DEFAULT 0 COMMENT '已存放数量',
    status TINYINT DEFAULT 1 COMMENT '1-启用 0-停用'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4. 快递包裹表
CREATE TABLE IF NOT EXISTS package (
    id INT PRIMARY KEY AUTO_INCREMENT,
    tracking_number VARCHAR(50) NOT NULL UNIQUE COMMENT '运单号',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收件人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收件人手机号',
    express_company_id INT COMMENT '快递公司ID',
    weight DECIMAL(5,2) COMMENT '重量(KG)',
    pickup_code VARCHAR(20) NOT NULL UNIQUE COMMENT '取件码',
    shelf_id INT COMMENT '货架ID',
    status TINYINT DEFAULT 0 COMMENT '0-在库 1-已取件 2-异常',
    in_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
    out_time DATETIME COMMENT '出库时间',
    remark VARCHAR(255) COMMENT '备注',
    INDEX idx_tracking_number (tracking_number),
    INDEX idx_pickup_code (pickup_code),
    INDEX idx_receiver_phone (receiver_phone),
    INDEX idx_status (status),
    FOREIGN KEY (express_company_id) REFERENCES express_company(id),
    FOREIGN KEY (shelf_id) REFERENCES shelf(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5. 取件记录表
CREATE TABLE IF NOT EXISTS pickup_record (
    id INT PRIMARY KEY AUTO_INCREMENT,
    package_id INT NOT NULL COMMENT '包裹ID',
    pickup_type TINYINT COMMENT '1-用户自助 2-工作人员操作',
    pickup_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    operator_id INT COMMENT '操作人员ID',
    FOREIGN KEY (package_id) REFERENCES package(id),
    FOREIGN KEY (operator_id) REFERENCES staff(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始数据（密码均为 123456，BCrypt 加密）
INSERT INTO staff (username, password, real_name, phone, role) VALUES
('admin', '$2a$10$1q16D1tctIl2N/m/w6734ui5nOx0jCgU3peL4MICOExF8sV2glFYO', '系统管理员', '13800000000', 0),
('user1', '$2a$10$1q16D1tctIl2N/m/w6734ui5nOx0jCgU3peL4MICOExF8sV2glFYO', '测试用户1', '13800000001', 1),
('user2', '$2a$10$1q16D1tctIl2N/m/w6734ui5nOx0jCgU3peL4MICOExF8sV2glFYO', '测试用户2', '13800000002', 1),
('user3', '$2a$10$1q16D1tctIl2N/m/w6734ui5nOx0jCgU3peL4MICOExF8sV2glFYO', '测试用户3', '13800000003', 1);

INSERT INTO express_company (company_name, code) VALUES
('顺丰速运', 'SF'),
('圆通速递', 'YTO'),
('中通快递', 'ZTO'),
('韵达快递', 'YD'),
('申通快递', 'STO'),
('百世快递', 'HTKY'),
('极兔速递', 'JT');

INSERT INTO shelf (shelf_code, capacity, used_count, status) VALUES
('A-01', 50, 0, 1),
('A-02', 50, 0, 1),
('A-03', 50, 0, 1),
('B-01', 50, 0, 1),
('B-02', 50, 0, 1),
('B-03', 50, 0, 1),
('C-01', 30, 0, 1),
('C-02', 30, 0, 1);

-- ============================================================
-- 以下为【已有数据库】的增量更新语句（请手动执行）
-- ============================================================
-- 1. staff 表新增 role 字段
ALTER TABLE staff ADD COLUMN role TINYINT DEFAULT 1 COMMENT '0-工作人员/管理员 1-普通用户' AFTER phone;

-- 2. 更新已有 admin 账号的密码为 BCrypt 加密，并设置 role=0
UPDATE staff SET password = '$2a$10$1q16D1tctIl2N/m/w6734ui5nOx0jCgU3peL4MICOExF8sV2glFYO', role = 0 WHERE username = 'admin';

-- 3. 插入测试用户（密码均为 123456）
INSERT INTO staff (username, password, real_name, phone, role) VALUES
('user1', '$2a$10$1q16D1tctIl2N/m/w6734ui5nOx0jCgU3peL4MICOExF8sV2glFYO', '测试用户1', '13800000001', 1),
('user2', '$2a$10$1q16D1tctIl2N/m/w6734ui5nOx0jCgU3peL4MICOExF8sV2glFYO', '测试用户2', '13800000002', 1),
('user3', '$2a$10$1q16D1tctIl2N/m/w6734ui5nOx0jCgU3peL4MICOExF8sV2glFYO', '测试用户3', '13800000003', 1);
