drop table if exists article;

drop table if exists SecurityMetadata;

drop table if exists colony_role;

drop table if exists user_colony;

drop table if exists user_role;

drop table if exists articleclass;

drop table if exists colony;

drop table if exists role;

drop table if exists user;

drop table if exists webResource;

drop table if exists file;




/*==============================================================*/
/* Table: SecurityMetadata                                      */
/*==============================================================*/
create table SecurityMetadata
(
   id                   int not null auto_increment comment 'ID',
   webResource_id       int not null comment '资源ID',
   role_id              int not null comment '角色ID',
   primary key (id),
   unique key UK_SecurityMetadata (webResource_id, role_id)
);

/*==============================================================*/
/* Table: article                                               */
/*==============================================================*/
create table article
(
   articleid            int not null auto_increment comment '文章id',
   classid              int not null comment '分类id',
   userid               int not null comment '发表用户id',
   title                varchar(128) not null comment '标题',
   content              mediumtext not null comment '内容',
   releasetime          datetime not null comment '发布时间',
   primary key (articleid)
);

alter table article comment '文章';

/*==============================================================*/
/* Table: articleclass                                          */
/*==============================================================*/
create table articleclass
(
   classid              int not null auto_increment comment '分类id',
   userid               int not null comment '所属用户id',
   name                 varchar(16) not null comment '分类名称',
   primary key (classid)
);

alter table articleclass comment '文章分类';

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
);

alter table colony comment '群体';

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
);

alter table colony_role comment '群体角色';

/*==============================================================*/
/* Table: file                                                  */
/*==============================================================*/
create table file
(
   id                   varchar(36) not null comment '文件id',
   name                 varchar(50) not null comment '文件名',
   body                 mediumblob not null comment '二进制文件',
   primary key (id)
);

alter table file comment '文件';

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
);

alter table role comment '角色';

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
);

alter table user comment '用户';

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
);

alter table user_colony comment '用户群体';

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
);

alter table user_role comment '用户角色';

/*==============================================================*/
/* Table: webResource                                           */
/*==============================================================*/
create table webResource
(
   id                   int not null auto_increment comment 'ID',
   pattern              varchar(100) not null comment 'URI模式',
   sequence             int not null comment '排序号',
   primary key (id),
   unique key UK_pattern (pattern),
   unique key UK_sequence (sequence)
);

alter table webResource comment '系统资源';

alter table SecurityMetadata add constraint FK_SecurityMetadata_Reference_role foreign key (role_id)
      references role (id) on delete restrict on update restrict;

alter table SecurityMetadata add constraint FK_SecurityMetadata_Reference_webResource foreign key (webResource_id)
      references webResource (id) on delete restrict on update restrict;

alter table article add constraint FK_article_Reference_articleclass foreign key (classid)
      references articleclass (classid) on delete restrict on update restrict;

alter table article add constraint FK_article_Reference_user foreign key (userid)
      references user (id) on delete restrict on update restrict;

alter table articleclass add constraint FK_articleclass_Reference_user foreign key (userid)
      references user (id) on delete restrict on update restrict;

alter table colony_role add constraint FK_colony_role_Reference_colony foreign key (colony_id)
      references colony (id) on delete restrict on update restrict;

alter table colony_role add constraint FK_colony_role_Reference_role foreign key (role_id)
      references role (id) on delete restrict on update restrict;

alter table user_colony add constraint FK_user_colony_Reference_colony foreign key (colony_id)
      references colony (id) on delete restrict on update restrict;

alter table user_colony add constraint FK_user_colony_Reference_user foreign key (user_id)
      references user (id) on delete restrict on update restrict;

alter table user_role add constraint FK_user_role_Reference_role foreign key (role_id)
      references role (id) on delete restrict on update restrict;

alter table user_role add constraint FK_user_role_Reference_user foreign key (user_id)
      references user (id) on delete restrict on update restrict;
