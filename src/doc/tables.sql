drop table if exists article;

drop table if exists articleclass;

drop table if exists file;

drop table if exists user;

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
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   userid               int not null auto_increment comment '用户ID',
   name                 varchar(16) not null comment '用户名',
   password             varchar(16) not null comment '密码',
   primary key (userid),
   unique key UK_name (name)
);

alter table user comment '用户';

alter table article add constraint FK_Reference_3 foreign key (classid)
      references articleclass (classid) on delete restrict on update restrict;

alter table article add constraint FK_Reference_4 foreign key (userid)
      references user (userid) on delete restrict on update restrict;

alter table articleclass add constraint FK_Reference_1 foreign key (userid)
      references user (userid) on delete restrict on update restrict;
