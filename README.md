# 文件管理模拟系统后端 - File Manager Backend

`file-manager-backend` 是一个基于 Spring Boot 的后端项目，为操作系统课程设计中的文件管理模拟系统提供服务。该系统支持用户管理（注册、登录）和文件管理（创建、删除、修改、查看文件属性等功能），采用前后端分离架构，数据库使用 MySQL。后端运行在 `http://localhost:8080`，提供 RESTful API 接口。

此项目旨在通过编程实践加深对操作系统文件管理原理的理解，并锻炼学生的开发能力。

## 功能特性

- **用户管理**
  - 注册新用户
  - 用户登录验证
- **文件管理**
  - 创建文件（支持文件名、内容和权限设置）
  - 删除文件
  - 获取文件列表和文件详情
  - 修改文件内容、文件名和权限
  - 查看文件属性（类型、大小、创建时间、修改时间、权限）
- **其他**
  - 文件大小自动计算（基于内容字节数）
  - 修改操作自动更新时间戳

## 技术栈

- **语言**: Java 17
- **框架**: Spring Boot 3.2.0
- **构建工具**: Maven
- **数据库**: MySQL (数据库名: `springos`, 用户名: `root`, 密码: `5201314`)
- **依赖**:
  - Spring Boot Starter Web
  - Spring Data JPA
  - MySQL Connector
  - Lombok

## 项目结构

```
file-manager-backend/
├── src/
│   ├── main/
│   │   ├── java/com/xai/
│   │   │   ├── FileManagerApplication.java  # 主应用类
│   │   │   ├── entity/                     # 实体类
│   │   │   ├── repository/                 # 数据访问层
│   │   │   ├── service/                    # 业务逻辑层
│   │   │   └── controller/                 # 接口层
│   │   └── resources/
│   │       └── application.yml             # 配置文件
└── pom.xml                                 # Maven 配置文件
```

## 安装与运行

### 前置条件

- JDK 17
- Maven 3.6+
- MySQL 8.0+
- Git

### 步骤

1. **克隆仓库**

   ```bash
   git clone https://github.com/2423560192/file-manager-backend.git
   cd file-manager-backend
   ```

2. **配置数据库** 创建 MySQL 数据库：

   ```sql
   CREATE DATABASE springos;
   ```

   确保 MySQL 用户名为 root，密码为 5201314，或修改 `src/main/resources/application.yml` 中的配置。

3. **安装依赖**

   ```bash
   mvn clean install
   ```

4. **运行项目**

   ```bash
   mvn spring-boot:run
   ```

   项目启动后，访问 <http://localhost:8080/api>。

5. **验证** 使用 Postman 或 Apifox 测试接口（见下方 API 文档）。

## API 文档

后端提供 RESTful API，所有接口前缀为 `/api`。详细接口说明请参考：

- OpenAPI 3.0 文件: [openapi.yaml](https://kimi.moonshot.cn/chat/openapi.yaml)（可导入 Apifox 查看）
- 主要接口:
  - `POST /api/user/register` - 注册用户
  - `POST /api/user/login` - 用户登录
  - `POST /api/file/create` - 创建文件
  - `GET /api/file/list/{userId}` - 获取文件列表
  - `GET /api/file/{fileId}` - 获取文件详情
  - `DELETE /api/file/{fileId}` - 删除文件
  - `PUT /api/file/content/{fileId}` - 更新文件内容
  - `PUT /api/file/name/{fileId}` - 更新文件名
  - `PUT /api/file/permission/{fileId}` - 更新权限

示例请求：

```json
// 注册用户
POST /api/user/register
{
  "username": "testuser",
  "password": "123456"
}
```