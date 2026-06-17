# 快递站管理系统

这是一个前后端分离的快递站管理系统。

## 项目结构

```
kuaidi/
├── express-front/          # 前端项目 (Vue 3 + Vite)
│   ├── src/
│   ├── package.json
│   └── ...
├── express-back/           # 后端项目 (Spring Boot)
│   ├── src/
│   ├── pom.xml
│   ├── sql/
│   └── ...
└── README.md
```

## 快速开始

### 后端启动

```bash
cd express-back
mvn spring-boot:run
```

后端将在 http://localhost:8080 启动

### 前端启动

```bash
cd express-front
npm install
npm run dev
```

前端将在 http://localhost:5173 启动

## 数据库配置

数据库配置在 `express-back/src/main/resources/application.yml` 中：

- 数据库：MySQL
- 数据库名：express_station
- 默认用户名：root
- 默认密码：123456

初始化 SQL 文件在 `express-back/sql/init.sql`

## 技术栈

### 后端
- Spring Boot 3.0.2
- MyBatis
- MySQL
- JWT 认证

### 前端
- Vue 3
- Vite
- Element Plus
- TypeScript
