架构
====


# 开发工具

Intellij IDEA

# 模块说明

## core
无关业务的基础包

## common
服务层

## jedis-ss
扩展的jedis客户端，当redis使用分片，每个分片使用sentinel监控时，
此客户端可以自动定位到分片，并从sentinel中获取有效的连接进行读写

## security
权限管理模块，整合spring-security，通过CAS进行单点登录

## rpc
远程调用模块，整合dubbo

## message
消息队列模块，整合activeMQ

## webapp-user
一个可放到web容器运行的应用
通过springMVC暴露接口，数据格式为json

## webservice-user
一个可放到web容器运行的应用
通过CXF暴露接口，支持soap,rest两种接口。

## search
简易的搜索引擎，使用ansj_seg分词，用redis的Zset存储数据

# 准备

1. 启动mysql, 创建数据库, 名称为architecture, 用户名为root, 无密码

2. 执行zero/tables.sql 创建所需的表

3. 解压zero/cas-tom.zip并启动

4. 如需使用message模块, 需启动ActiveMQ, 并使用默认配置

5. 如需使用rpc模块, 需启动zookeeper, 并使用默认配置

6. 如需使用search模块，需启动redis-server, 并使用默认配置

7. 进入根目录，执行 `clean install -DskipTests=true`

8. 进入 webapp-user 或 webservice-user 执行 `tomcat7:run`


