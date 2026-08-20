# Java Backend Template

<div align="center">

**面向企业项目的 Java 后端工程模板与最佳实践示例**

一套结构清晰、职责明确、易于扩展、便于维护的 Spring Boot 多模块骨架

![Java](https://img.shields.io/badge/Java-25-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-green)
![MyBatis Plus](https://img.shields.io/badge/MyBatis%20Plus-3.5.17-blue)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

</div>

---

## ✨ 特性

| 能力 | 说明 |
|------|------|
| 🏗️ 多模块架构 | `common` / `data` / `business` / `service` 四层清晰分层 |
| 📦 统一响应体 | `R<T>` 封装 code / msg / traceId / data |
| ⚠️ 全局异常处理 | `@RestControllerAdvice` 集中拦截业务、校验、未知异常 |
| 🔗 链路追踪 | 基于 MDC，自动为响应体与日志注入 traceId |
| 🗄️ 数据库访问 | PostgreSQL + MyBatis Plus，含分页与防全表更新删除 |
| ⚡ Redis | Lettuce 连接 + Jackson 序列化 + 健康检查 |
| ✅ 参数校验 | spring-boot-starter-validation |
| 📄 API 文档 | SpringDoc OpenAPI + Knife4j |
| 📊 监控 | Actuator + Micrometer Prometheus |
| 🔧 多环境配置 | dev / test / prod profile 一键切换 |

## 🚀 快速开始

### 环境要求

- JDK 25+
- Maven 3.6+
- PostgreSQL / Redis（可选）

### 启动

```bash
git clone <repo-url>
cd java-backend-template

# 编译（跳过测试）
mvn clean package -DskipTests

# 启动（业务端口 8080，管理端口 8091）
mvn spring-boot:run -pl jbt-service
```

切换环境：

```bash
mvn spring-boot:run -pl jbt-service -Ptest   # 测试
mvn spring-boot:run -pl jbt-service -Pprod   # 生产
```

## 🧱 工程结构

```text
java-backend-template
├── jbt-common     # 通用模块：工具类、配置、异常、统一响应体、拦截器
├── jbt-data       # 数据访问模块：Mapper 接口、实体类
├── jbt-business   # 业务模块：Controller、Service
└── jbt-service    # 启动模块：Application 入口、资源配置
```

## 🎯 核心设计

### 统一响应格式

所有接口返回 `R<T>`：

```json
{
  "code": 200,
  "msg": "操作成功",
  "traceId": "a1b2c3d4...",
  "data": {}
}
```

### 链路追踪

请求头携带 `Trace_Id` 或自动生成，MDC 贯穿日志与响应体：

```text
2026-08-06 10:00:00 INFO [a1b2c3d4] 请求处理完成
```

### Redis 序列化

- Key：`StringRedisSerializer`
- Value：`Jackson2JsonRedisSerializer`（复用统一 `ObjectMapper`）

### 数据库

`MybatisPlusConfig` 内置：
- 分页插件（自动识别数据库类型）
- 防全表更新删除插件（`BlockAttackInnerInterceptor`）

---

## 📝 License

MIT License
