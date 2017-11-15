CREATE DATABASE IF NOT EXISTS architecture;

USE architecture;

drop table if exists security_metadata;

drop table if exists colony_role;

drop table if exists user_colony;

drop table if exists user_role;

drop table if exists colony;

drop table if exists role;

drop table if exists user;

drop table if exists resource;




/*==============================================================*/
/* Table: SecurityMetadata                                      */
/*==============================================================*/
create table security_metadata
(
   id                   int not null auto_increment comment 'ID',
   resource_id       int not null comment '资源ID',
   role_id              int not null comment '角色ID',
   primary key (id),
   unique key UK_SecurityMetadata (resource_id, role_id)
) COMMENT '权限元数据';


/*==============================================================*/
/* Table: colony                                                */
/*==============================================================*/
create table colony
(
   id                   int not null auto_increment comment 'ID',
   name                 varchar(50) not null comment '名称',
   description          varchar(100) not null comment '描述',
   primary key (id),
   unique key UK_colony (name)
) COMMENT '群体';


/*==============================================================*/
/* Table: colony_role                                           */
/*==============================================================*/
create table colony_role
(
   id                   int not null auto_increment comment 'ID',
   role_id              int not null comment '角色ID',
   colony_id            int not null comment '群体ID',
   primary key (id),
   unique key UK_colony_role (role_id, colony_id)
) COMMENT '群体角色';


/*==============================================================*/
/* Table: role                                                  */
/*==============================================================*/
create table role
(
   id                   int not null auto_increment comment 'ID',
   name                 varchar(50) not null comment '名称',
   description          varchar(100) not null comment '描述',
   primary key (id),
   unique key UK_role (name)
) COMMENT '角色';


/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   int not null auto_increment comment 'ID',
   name                 varchar(50) not null comment '用户名',
   password             varchar(50) not null comment '密码',
   enabled              boolean not null comment 'true : 可用,  false : 不可用',
   primary key (id),
   unique key UK_name (name)
) COMMENT '用户';


/*==============================================================*/
/* Table: user_colony                                           */
/*==============================================================*/
create table user_colony
(
   id                   int not null auto_increment comment 'ID',
   user_id              int not null comment '用户ID',
   colony_id            int not null comment '群体ID',
   primary key (id),
   unique key UK_user_colony (user_id, colony_id)
) COMMENT '用户群体';


/*==============================================================*/
/* Table: user_role                                             */
/*==============================================================*/
create table user_role
(
   id                   int not null auto_increment comment 'ID',
   user_id              int not null comment '用户ID',
   role_id              int not null comment '角色ID',
   primary key (id),
   unique key UK_user_role (user_id, role_id)
) COMMENT '用户角色';


/*==============================================================*/
/* Table: webResource                                           */
/*==============================================================*/
create table resource
(
   id                   int not null auto_increment comment 'ID',
   content              varchar(100) not null comment '内容',
   sequence             int not null comment '排序号',
   primary key (id),
   unique key UK_pattern (content),
   unique key UK_sequence (sequence)
) COMMENT '系统资源';
