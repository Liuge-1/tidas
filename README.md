# TIDAS培训管理系统
基于 SpringBoote3.4 + MySQL8.0 + MyBatis  + WebSocket 的员工培训管理后台系统

## 在线访问地址
http://tidas.pengdev.online

http://www.tidas.pengdev.online

## 技术栈
- 后端：Spring Boot 4.0、MyBatis、MyBatis-Plus、JWT、WebSocket
- 工具：Jasypt 配置加密、Excel导入导出、阿里云OSS文件存储
- 数据库：MySQL8.0
- 前端：原生HTML+JS
- 构建工具：Maven

## 项目功能
1. 用户登录、Token登录拦截、接口限流
2. 部门、班级、员工信息管理，员工履历维护
3. 员工离职申请流程管理
4. 公告发布、操作日志记录查询
5. Excel批量导入导出员工数据
6. 阿里云OSS图片上传下载
7. 邮件通知、在线聊天WebSocket
8. 数据统计报表

## 环境配置说明
### 1. 配置文件规则
仓库仅提供脱敏模板配置：`src/main/resources/application-template.yml`
本地私有配置 `application.yml` 已通过.gitignore屏蔽，不会上传代码仓库，保护数据库、OSS、邮箱密钥安全。

### 2. 本地启动步骤
1. 克隆项目到本地
```powershell
git clone https://github.com/Liuge-1/tidas.git
