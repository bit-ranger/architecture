site
====

这是一个J2EE的架构实验，不包含具体业务，不解决具体问题。

## 开发工具

Intellij IDEA

## 开源组件

spring

spring-webmvc

mybatis

hibernate-validator

spring-security

cxf

spring-jms

ehcache

spring-test


## 补充说明

项目为 `maven` `web` 结构。

应用服务器需要支持 `JNDI` 数据源, 例如 `weblogic`, 且需要单独部署一台 `ActiveMQ` 以实现异步通信。

## 使用准备

1. 启动mysql，新建一个mysql数据库，库名为 `sllxsite`。

2. 使用 `src/doc/tables.sql` 建表，使用 `src/doc/data.sql` 导入数据。

3. 在本机启动ActiveMQ，端口号默认。

4. 打开weblogic配置文件 `domains/DOMAIN_NAME/config/config.xml` 关闭自带的 `Basic Authentication`：

    ```xml
    <security-configuration>
    ...
    <enforce-valid-basic-auth-credentials>false</enforce-valid-basic-auth-credentials>
    </security-configuration>
    ```

5. 将本项目部署到 `weblogic`。

## 内容

打开 `http://localhost:7001` 可以看到一个普通页面，这没什么好说的。

打开 `http://localhost:7001/user` 可以看到权限控制。

打开 `http://localhost:7001/service` 能够看到由 `CXF` 导出的 `webservice` 接口。

打开项目文件 `src/test/java/site/jms/MessageConsumerTest.java` 运行此 Test Case，查看 WebLogic 控制台，
可以看到web项目接收了一条消息，并且存入了数据库，然后消息与数据库一起回滚，接着再次接受消息并入库，这就是JTA事务。


