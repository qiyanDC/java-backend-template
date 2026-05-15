# java-backend-template

> 面向企业项目的 Java 后端工程模板与最佳实践示例。  
> A practical Java backend starter template for enterprise projects.

## 项目简介

`java-backend-template` 是一个用于企业级后端项目冷启动的基础工程模板，目标是提供一套 **结构清晰、职责明确、易于扩展、便于维护** 的 Java 后端项目骨架。

这个仓库不追求"大而全"，而是优先解决实际项目初期最常见的工程问题，例如：

- 项目结构如何组织更清晰
- 接口返回如何统一
- 异常如何集中处理
- 参数校验如何规范接入
- 日志如何更利于排查问题
- 基础工程能力如何提前收敛

适合希望快速搭建 Java / Spring Boot 项目的开发者，也适合作为团队内部项目初始化模板参考。

---

## 设计目标

本项目希望实现以下目标：

- 提供一个可直接启动的 Java 后端基础骨架
- 建立统一的工程结构与开发约定
- 降低项目早期无序扩展带来的维护成本
- 沉淀企业项目中常见的基础实践
- 为后续扩展 Redis、MQ、鉴权、幂等、审计等能力预留空间

---

## 适用场景

适用于以下场景：

- 新项目初始化
- 中后台系统骨架搭建
- 团队统一工程结构
- 个人学习企业级项目工程化实践
- 作为 Spring Boot 项目模板进行二次开发

---

## 技术栈

当前版本基于以下技术栈：

- Java 25
- Spring Boot 3.5.14
- Maven（多模块）
- Undertow（Web 容器，替代 Tomcat）
- Jackson（JSON 序列化，含自定义 Long/String、日期序列化器）
- Hibernate Validator（参数校验）
- SLF4J + Logback（日志）
- SpringDoc OpenAPI + Knife4j（API 文档）
- Spring Boot Actuator + Micrometer Prometheus（监控）
- commons-lang3（通用工具）
- JUnit + Mockito（测试）

后续可按版本逐步扩展：

- MyBatis / JPA
- Redis
- RabbitMQ / Kafka
- Docker
- Spring Security
- CI/CD

---

## 当前包含的能力

- [x] Spring Boot 多模块启动工程（jbt-common + jbt-service）
- [x] 统一响应体封装（`R<T>`，含 code / msg / traceId / data）
- [x] 统一错误码枚举（`HttpStatusEnums`，200 / 400 / 401 / 403 / 404 / 500 等）
- [x] 全局异常处理（`@RestControllerAdvice`，覆盖未知异常、业务异常、参数校验异常）
- [x] 业务异常类（`BusinessException`，支持自定义 code + message）
- [x] Jackson 统一序列化配置（Long 转 String、LocalDateTime 格式化、枚举按 code 序列化）
- [x] 自定义工具类（`JsonUtils` / `DateUtils` / `StringUtils` / `ObjectUtils` / `EnumUtils` / `ReflectUtils`）
- [x] 日期常量定义（`DatePattern`，覆盖常用格式 + `DateTimeFormatter` + `FastDateFormat`）
- [x] 参数校验支持（spring-boot-starter-validation）
- [x] Logback 日志规范（控制台高亮 + 文件滚动 + 错误日志分离 + 异步 Appender）
- [x] Maven 多环境配置（dev / test / prod profile，dev 为默认）
- [x] Swagger / Knife4j API 文档集成
- [x] Prometheus 指标暴露（Actuator + Micrometer，management 端口 8091）
- [x] 启动时打印系统环境变量

后续计划逐步增加：

- [ ] 数据库访问示例
- [ ] Redis 示例
- [ ] 接口幂等示例
- [ ] 链路追踪接入
- [ ] 审计日志
- [ ] Docker 部署支持
- [ ] GitHub Actions 基础 CI

---

## 工程结构

```text
java-backend-template
├── pom.xml                          # 父 POM，依赖管理与多模块聚合
├── jbt-common                       # 通用模块（工具类、序列化器、异常、响应体）
│   ├── pom.xml
│   └── src/main/java/com/example/jbt/common
│       ├── aop/advice
│       │   └── GlobalExceptionHandler.java   # 全局异常处理
│       ├── codec
│       │   ├── deserializer
│       │   │   └── LocalDateTimeDeserializer.java
│       │   └── serializer
│       │       ├── LocalDateTimeSerializer.java
│       │       └── LongToStringSerializer.java
│       ├── constants
│       │   ├── CommonConstants.java
│       │   └── DatePattern.java             # 日期格式常量
│       ├── enums
│       │   └── HttpStatusEnums.java         # 统一错误码枚举
│       ├── exception
│       │   └── BusinessException.java       # 业务异常
│       ├── result
│       │   └── R.java                       # 统一响应体
│       └── utils
│           ├── DateUtils.java
│           ├── EnumUtils.java
│           ├── JsonUtils.java
│           ├── ObjectUtils.java
│           ├── ReflectUtils.java
│           └── StringUtils.java
├── jbt-service                      # 启动模块（Spring Boot 应用入口）
│   ├── pom.xml
│   └── src/main
│       ├── java/com/example/jbt/service
│       │   └── Application.java             # Spring Boot 启动类
│       └── resources
│           ├── application.yml              # 服务配置与管理端点配置
│           └── logback.xml                  # Logback 日志配置
└── README.md
```

---

## 快速开始

### 环境要求

- JDK 25+
- Maven 3.6+

### 本地启动

```bash
# 克隆项目
git clone <repo-url>
cd java-backend-template

# 编译打包（跳过测试）
mvn clean package -DskipTests

# 启动服务（默认 dev 环境，业务端口 8080）
mvn spring-boot:run -pl jbt-service
```

### Profile 切换

```bash
# 测试环境
mvn spring-boot:run -pl jbt-service -Ptest

# 生产环境
mvn spring-boot:run -pl jbt-service -Pprod
```

---

## 模块说明

| 模块 | 说明 |
|------|------|
| `jbt-common` | 通用基础模块，包含工具类、序列化器、枚举、异常、统一响应体、全局异常处理等，不包含启动类 |
| `jbt-service` | 服务启动模块，依赖 `jbt-common`，包含 Spring Boot 启动入口与资源文件 |

---

## 端口说明

| 端口 | 用途 |
|------|------|
| 8080 | 业务端口（server.port） |
| 8091 | 管理端口（management.server.port），暴露 Actuator 端点 |

### Actuator 端点

启动后可通过管理端口访问：

- `http://localhost:8091/actuator/health` — 健康检查
- `http://localhost:8091/actuator/prometheus` — Prometheus 指标
- `http://localhost:8091/actuator/metrics` — 所有指标列表

---

## 统一响应格式

所有接口返回统一封装为 `R<T>`，结构如下：

```json
{
  "code": 200,
  "msg": "操作成功",
  "traceId": "abc123...",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 状态码，对应 `HttpStatusEnums` |
| msg | string | 提示信息 |
| traceId | string | 链路追踪 ID |
| data | T | 业务数据 |

### 错误码

| code | 含义 |
|------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 访问受限 |
| 404 | 资源未找到 |
| 500 | 系统内部错误 |

完整定义见 `HttpStatusEnums.java`。

### 异常处理

全局异常处理器（`GlobalExceptionHandler`）统一拦截三类异常：

| 异常类型 | 处理方式 |
|---------|---------|
| `Exception` | 返回 500，记录完整栈 |
| `BusinessException` | 返回自定义 code + message |
| `MethodArgumentNotValidException` | 返回 400 + 第一条校验错误 |

---

## 日志说明

日志配置位于 `jbt-service/src/main/resources/logback.xml`，包含以下 Appender：

- **CONSOLE**：控制台输出，带高亮，`INFO` 级别
- **APP_ASYNC**：异步文件日志，按天 + 大小（500MB）滚动，保留 30 天
- **APP_ASYNC_ERROR**：异步错误日志，仅输出 `ERROR` 级别

可通过 JVM 参数覆盖关键路径：

```bash
-DLOG_HOME=/your/log/path          # 日志存储目录，默认 ./nfslocal/app-logs
-Dapp.name=your-app-name           # 应用名称
```

---

## JSON 序列化约定

- `Long` 类型默认序列化为字符串，避免前端精度丢失
- `LocalDateTime` 默认按 `yyyy-MM-dd HH:mm:ss` 序列化与反序列化
- 枚举默认按 `getCode()` 方法返回值序列化，无 `getCode` 时回退到 `ordinal`
- 支持通过 `JsonUtils.toJson(entity, includes, excludes)` 动态过滤字段
