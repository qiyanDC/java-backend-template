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

- [x] Spring Boot 多模块启动工程
- [x] 分层目录结构（common / service 模块拆分）
- [x] Jackson 统一序列化配置（Long 转 String、LocalDateTime 格式化、枚举按 code 序列化）
- [x] 自定义 JsonUtils 工具类（支持属性过滤、动态包含/排除字段）
- [x] 全局日期处理（DateUtils / DatePattern，支持多种日期格式解析与转换）
- [x] 自定义 StringUtils / ObjectUtils / EnumUtils / ReflectUtils 工具类
- [x] 参数校验依赖（spring-boot-starter-validation）
- [x] Logback 日志规范（控制台 + 文件 + 错误日志分离 + 异步输出）
- [x] Maven 多环境配置（dev / test / prod profile）
- [x] Swagger / Knife4j API 文档集成
- [x] 示例启动类（含系统环境变量日志打印）

后续计划逐步增加：

- [ ] 统一响应体封装
- [ ] 全局异常处理
- [ ] 数据库访问示例
- [ ] Redis 示例
- [ ] 接口幂等示例
- [ ] 统一错误码设计
- [ ] 链路追踪接入
- [ ] 审计日志
- [ ] Docker 部署支持
- [ ] GitHub Actions 基础 CI

---

## 工程结构

```text
java-backend-template
├── pom.xml                          # 父 POM，依赖管理与多模块聚合
├── jbt-common                       # 通用模块（工具类、序列化器、常量）
│   ├── pom.xml
│   └── src/main/java/com/example/jbt/common
│       ├── aop
│       │   ├── deserializer         # Jackson 反序列化器
│       │   │   └── LocalDateTimeDeserializer.java
│       │   └── serializer           # Jackson 序列化器
│       │       ├── LocalDateTimeSerializer.java
│       │       └── LongToStringSerializer.java
│       ├── constants                # 常量定义
│       │   ├── CommonConstants.java
│       │   └── DatePattern.java     # 日期格式常量（含 Formatter）
│       └── utils                    # 通用工具类
│           ├── DateUtils.java       # 日期工具（解析/格式化/范围计算）
│           ├── EnumUtils.java       # 枚举工具（按 code/ordinal 互转）
│           ├── JsonUtils.java       # JSON 工具（序列化/反序列化/属性过滤）
│           ├── ObjectUtils.java     # 对象判空/类型判断
│           ├── ReflectUtils.java    # 反射工具（字段获取/值读写）
│           └── StringUtils.java     # 字符串工具（驼峰/下划线/随机数等）
├── jbt-service                      # 启动模块（Spring Boot 应用入口）
│   ├── pom.xml
│   └── src/main
│       ├── java/com/example/jbt/service
│       │   └── Application.java     # Spring Boot 启动类
│       └── resource
│           ├── application.xml      # 服务配置（端口等）
│           └── logback.xml          # Logback 日志配置
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

# 启动服务（默认 dev 环境，端口 8080）
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
| `jbt-common` | 通用基础模块，包含工具类、序列化器、常量等，不包含启动类 |
| `jbt-service` | 服务启动模块，依赖 `jbt-common`，包含 Spring Boot 启动入口与资源文件 |

---

## 日志说明

日志配置位于 `jbt-service/src/main/resource/logback.xml`，包含以下 Appender：

- **CONSOLE**：控制台输出，带高亮，`INFO` 级别
- **APP_ASYNC**：异步文件日志，按天 + 大小（500MB）滚动，保留 30 天
- **APP_ASYNC_ERROR**：异步错误日志，仅输出 `ERROR` 级别

可通过 JVM 参数覆盖关键路径：

```bash
-DLOG_HOME=/your/log/path          # 日志存储目录，默认 ./nfslocal/app-logs
-Dapp.name=your-app-name           # 应用名称，默认 app-beibeiyu
```

---

## JSON 序列化约定

- `Long` 类型默认序列化为字符串，避免前端精度丢失
- `LocalDateTime` 默认按 `yyyy-MM-dd HH:mm:ss` 序列化与反序列化
- 枚举默认按 `getCode()` 方法返回值序列化，无 `getCode` 时回退到 `ordinal`
- 支持通过 `JsonUtils.toJson(entity, includes, excludes)` 动态过滤字段
