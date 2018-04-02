package com.rainyalley.architecture.dao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages="com.rainyalley.architecture.dao.mapper")
public class DaoConfig {}
