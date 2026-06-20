# 快递驿站出入库与取件管理系统

基于 Spring Boot + Vue 3 的快递驿站管理系统，支持包裹入库、出库、自助取件、货架管理等功能，包含管理员和普通用户两种角色。

## 技术栈

### 后端

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.0.2 | 应用框架 |
| MyBatis | 3.0.0 | ORM 框架 |
| MySQL | 8.0 | 数据库 |
| JWT (jjwt) | 0.11.5 | 身份认证 |
| Spring Security Crypto | - | 密码加密 (BCrypt) |
| Lombok | 1.18.30 | 简化代码 |
| Java | 17 | 编程语言 |

### 前端

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.5 | 前端框架 |
| TypeScript | 6.0 | 类型系统 |
| Vite | 8.0 | 构建工具 |
| Element Plus | 2.14 | UI 组件库 |
| Vue Router | 5.0 | 路由管理 |
| Axios | 1.18 | HTTP 请求 |

## 项目结构

```
kuaidi/
├── express-back/                    # 后端 Spring Boot 项目
│   └── src/main/java/com/example/kuaidi/
│       ├── config/                  # 配置类（CORS、JWT拦截器）
│       ├── controller/              # 控制器层
│       ├── dto/                     # 数据传输对象
│       ├── entity/                  # 实体类
│       ├── exception/               # 异常处理
│       ├── mapper/                  # MyBatis Mapper 接口
│       ├── service/                 # 业务逻辑层
│       ├── utils/                   # 工具类（JWT、取件码生成）
│       └── vo/                      # 视图对象
├── express-front/                   # 前端 Vue 项目
│   └── src/
│       ├── api/                     # API 请求封装
│       ├── components/              # 布局组件
│       ├── router/                  # 路由配置
│       ├── utils/                   # 工具函数
│       └── views/                   # 页面视图
└── sql/                             # 数据库脚本
    └── init.sql                     # 建表及初始数据
```

## 功能模块

### 管理员 (role=0)

- **工作台** — 数据统计概览
- **包裹入库** — 录入运单号、收件人信息，自动分配取件码和货架
- **包裹出库** — 工作人员操作出库确认
- **包裹管理** — 查询、筛选所有包裹，支持按运单号/取件码/手机号/状态过滤，分页展示
- **货架管理** — 管理货架信息及状态
- **快递公司管理** — 维护合作快递公司信息
- **通知管理** — 发布系统通知

### 普通用户 (role=1)

- **自助取件** — 输入取件码完成取件
- **我的包裹** — 查看关联所有手机号的在库/已取件包裹
- **系统通知** — 查看驿站公告
- **个人中心** — 管理个人信息及额外手机号

### 通用功能

- **用户登录 / 注册** — JWT 身份认证，密码 BCrypt 加密
- **角色权限控制** — 路由守卫 + 后端拦截器双重校验
- **跨域支持** — 全局 CORS 配置

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Node.js 20.19+ 或 22.12+

### 1. 初始化数据库

```bash
# 执行建表脚本（包含初始数据）
mysql -u root -p < sql/init.sql
```

初始账号：

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | 123456 | 管理员 |
| user1 | 123456 | 普通用户 |
| user2 | 123456 | 普通用户 |
| user3 | 123456 | 普通用户 |

### 2. 配置后端

```bash
cd express-back
cp src/main/resources/application.yml.example src/main/resources/application.yml
```

编辑 `application.yml`，填入数据库密码和 JWT 密钥：

```yaml
spring:
  datasource:
    password: YOUR_PASSWORD_HERE

jwt:
  secret: YOUR_JWT_SECRET_HERE
```

### 3. 启动后端

```bash
cd express-back
mvn spring-boot:run
```

后端默认运行在 `http://localhost:8080`。

### 4. 启动前端

```bash
cd express-front
npm install
npm run dev
```

前端默认运行在 `http://localhost:5173`。

## API 接口

所有接口以 `/api` 为前缀，需在请求头携带 JWT Token（登录/注册接口除外）。

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/login` | 用户登录 |
| POST | `/api/register` | 用户注册 |
| POST | `/api/packages/inbound` | 包裹入库 |
| POST | `/api/packages/outbound/self` | 用户自助取件 |
| POST | `/api/packages/outbound/staff` | 工作人员出库 |
| GET | `/api/packages` | 包裹列表（分页） |
| GET | `/api/packages/query` | 按运单号/手机号查询 |
| GET | `/api/packages/my` | 我的包裹 |
| GET | `/api/packages/my/status` | 按状态查我的包裹 |
| POST | `/api/packages/confirm` | 确认取件 |
| GET | `/api/shelves` | 货架列表 |
| GET | `/api/companies` | 快递公司列表 |
| GET/POST | `/api/notifications` | 通知管理 |
| GET | `/api/statistics` | 数据统计 |
