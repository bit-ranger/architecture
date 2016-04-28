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
权限管理模块

## rpc
远程调用模块

## message
消息队列模块

## webapp-user
一个可放到web容器运行的应用
暴露user相关的服务

## webservice-user
一个可放到web容器运行的应用
以webservice形式暴露user相关的服务

# 准备

1. 启动mysql, 创建数据库, 名称为architecture

2. 执行common/doc/tables.sql 创建所需的表

3. 如需使用message模块, 需启动ActiveMQ, ActiveMQ使用默认配置

4. 如需使用rpc模块, 需启动zookeeper, zookeeper使用默认配置


