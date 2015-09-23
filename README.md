site
====

这是一个J2EE的基础架构实验，不包含具体业务，不解决具体问题。

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

打开 `http://localhost:7001` 可以看到一个普通页面，这没什么好说的。

打开 `http://localhost:7001/service` 能够看到由 `CXF` 导出的 `webservice` 接口。

打开项目文件 `src/test/java/site/jms/MessageConsumerTest.java` 运行此 Test Case，查看 WebLogic 控制台，
可以看到web项目接收了一条消息，并且存入了数据库，然后消息与数据库一起回滚，接着再次接受消息并入库，这就是JTA事务。


