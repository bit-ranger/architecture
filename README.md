架构
====


# 开发工具

Intellij IDEA

# 模块说明

## core
无关业务的基础包

## common
服务层

## security
权限管理模块，整合spring-security，CAS

## rpc
远程调用模块，整合dubbo

## message
消息队列模块，整合activeMQ

## webapp-user
一个可放到web容器运行的应用
用springMVC暴露接口，数据格式为JSON

## webservice-user
一个可放到web容器运行的应用
通过CXF暴露接口，支持soap,json两种数据格式。

# 准备

1. 启动mysql, 创建数据库, 名称为architecture, 用户名为root, 无密码

2. 执行zero/tables.sql 创建所需的表

3. 解压zero/cas-tom.zip并启动

4. 如需使用message模块, 需启动ActiveMQ, 并使用默认配置

5. 如需使用rpc模块, 需启动zookeeper, 并使用默认配置


