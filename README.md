site
====

这是一个J2EE的基础架构，不包含具体业务，不解决具体问题。

## 开发工具

Intellij IDEA

## 开源组件

spring

spring-webmvc

spring-security

spring-test

mybatis

hibernate-validator

cxf

activeMQ

spring-jms

mongodb

ehcache

markdownpapers

## 补充说明

项目为标准maven结构

web容器需要支持JTA事务, 例如 `weblogic`,且需要单独部署一台ActiveMQ以实现异步通信。

## 使用方式

1. 新建一个mysql数据库，库名为 `sllxsite`。
2. 使用 `src/doc/tables.sql` 建表。
3. 在本机启动ActiveMQ，端口号默认。
4. 将本项目部署到 `weblogic`。

## 内容

打开 `http://localhost:7001/service` 能够看到导出的 `webservice` 接口。

