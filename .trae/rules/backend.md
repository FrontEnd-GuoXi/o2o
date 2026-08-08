# 后端开发规范 (Backend Rules)

## 技术栈

- **语言**: Java 11
- **框架**: Spring 5.1.8 (传统 Spring MVC，非 Spring Boot)
- **ORM**: MyBatis 3.5.1
- **数据库**: MySQL 8.0.16 + c3p0 连接池
- **缓存**: Redis (Jedis 2.9.1)
- **消息队列**: RabbitMQ (spring-rabbit 2.4.12)
- **安全**: Spring Security 5.1.8 + JWT (java-jwt 4.5.0)
- **JSON**: Jackson 2.10.0
- **日志**: Logback 1.2.3
- **构建**: Maven，WAR 包部署
- **工具**: Lombok 1.18.26, Thumbnailator 0.4.8

## 项目结构规范

```
src/main/java/com/o2o/
├── config/          # 配置类 (Jackson, Redis 等)
├── dao/             # MyBatis Mapper 接口
├── dto/             # 数据传输对象 (请求/响应)
├── entity/          # 数据库实体类
├── enums/           # 枚举类
├── exceptions/      # 自定义异常
├── interceptor/     # 全局异常处理器
├── listener/        # 消息监听器
├── security/        # 安全相关 (JWT Filter, UserContext)
├── service/         # 业务接口
│   └── impl/        # 业务实现
├── util/            # 工具类
├── vo/              # 视图对象
└── web/             # Controller 层
    ├── product/
    ├── shop/
    ├── shopadmin/
    └── user/
```

## 代码规范

### 1. 分层架构
- **Controller** 只负责参数接收、校验、调用 Service、返回结果，不写业务逻辑
- **Service** 负责业务逻辑，通过接口+实现类的方式组织
- **DAO** 只负责数据访问，MyBatis Mapper 接口 + XML 映射文件
- 禁止跨层调用（Controller 不能直接调 DAO）

### 2. 命名规范
- 类名: `UpperCamelCase`
- 方法名/变量名: `lowerCamelCase`
- 常量: `UPPER_SNAKE_CASE`
- 包名: 全小写
- DAO 接口: `XxxDao` (非 Mapper 后缀)
- Service 接口: `XxxService`，实现类: `XxxServiceImpl`
- Controller: `XxxController`
- DTO: `XxxDTO`，VO: `XxxVO`
- 实体类: 与数据库表名对应，如 `Shop`, `PersonInfo`

### 3. Lombok 使用
- 实体类使用 `@Data` 或 `@Getter/@Setter`
- 日志使用 `@Slf4j` 或 `LoggerFactory.getLogger()`
- 避免使用 `@Builder` 除非确有必要

### 4. 异常处理
- 业务异常使用自定义 `BusinessException` 或 `ShopOperationException`
- 全局异常由 `GlobalExceptionHandler` 统一处理
- 不要在 Controller 中 try-catch 业务异常

### 5. 返回值规范
- 统一使用 `ResponseResultWrap` 包装返回结果
- 枚举 `HttpApiCode` 定义状态码

### 6. Spring 配置
- 使用 XML 配置方式 (`spring-context.xml`, `spring-dao.xml`, `spring-service.xml`, `spring-web.xml`)
- 不使用注解驱动的组件扫描配置（如 `@ComponentScan`），但 Bean 可用 `@Service`、`@Repository` 等注解
- 多环境通过 Maven profile (`home` / `company`) + `application.properties` 切换

### 7. 数据库规范
- MyBatis XML 映射文件放在 `src/main/resources/mapper/` 下
- 命名: `XxxDaoMapper.xml`
- 参数使用 `#{param}` 占位符防 SQL 注入
- 连表查询使用 MyBatis association/collection 或手动映射

### 8. 安全规范
- 接口鉴权通过 `JwtFilter` 实现
- 用户上下文从 `UserContextHolder` 获取
- 密码使用 `BCryptPasswordEncoder` 加密
- 敏感信息不在日志中打印
- 禁止在代码中硬编码密钥、密码等敏感信息

### 9. 日志规范
- 使用 Logback + SLF4J
- 关键操作记录 info 日志（登录、下单、支付等）
- 异常记录 error 日志并附上完整堆栈
- 日志中不要输出 emoji 等特殊字符（生产环境可能乱码）

### 10. 文件上传
- 图片处理使用 `ImageUtil` + Thumbnailator
- 上传路径使用 `ImgDir` 管理
- 注意文件大小限制和类型校验

### 11. 禁止事项
- **禁止使用 Spring Boot 的注解和自动配置**（如 `@SpringBootApplication`, `@Autowired` 可用）
- **禁止修改 Java 版本**（保持 Java 11）
- **禁止引入新的依赖框架**，除非与现有技术栈一致
- **禁止在 DAO 层写业务逻辑**
- **禁止直接返回实体类给前端**，使用 DTO/VO 转换