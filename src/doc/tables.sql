drop table if exists article;

drop table if exists articleclass;

drop table if exists authority;

drop table if exists file;

drop table if exists group;

drop table if exists group_authority;

drop table if exists group_user;

drop table if exists user;

drop table if exists user_authority;

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
/* Table: authority                                             */
/*==============================================================*/
create table authority
(
   authorityid          int not null auto_increment comment '权限ID',
   authorityname        varchar(50) not null comment '权限名称',
   primary key (authorityid),
   unique key UK_authorityname (authorityname)
);

alter table authority comment '权限';

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
/* Table: group                                                 */
/*==============================================================*/
create table group
(
   groupid              int not null auto_increment comment '用户组ID',
   groupname            varchar(50) not null comment '用户组名称',
   primary key (groupid),
   unique key UK_groupname (groupname)
);

alter table group comment '用户组';

/*==============================================================*/
/* Table: group_authority                                       */
/*==============================================================*/
create table group_authority
(
   group_authority_id   int not null auto_increment comment '用户组权限ID',
   groupid              int not null comment '用户组ID',
   authorityid          int not null comment '权限ID',
   primary key (group_authority_id),
   unique key UK_groupid_authorityid (groupid, authorityid)
);

alter table group_authority comment '用户组权限';

/*==============================================================*/
/* Table: group_user                                            */
/*==============================================================*/
create table group_user
(
   group_user_id        int not null auto_increment comment '用户组成员ID',
   userid               int not null comment '用户ID',
   groupid              int not null comment '用户组ID',
   primary key (group_user_id),
   unique key UK_userid_groupid (userid, groupid)
);

alter table group_user comment '用户组成员';

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   userid               int not null auto_increment comment '用户ID',
   name                 varchar(16) not null comment '用户名',
   password             varchar(16) not null comment '密码',
   enabled              boolean not null comment 'true : 可用,  false : 不可用',
   primary key (userid),
   unique key UK_name (name)
);

alter table user comment '用户';

/*==============================================================*/
/* Table: user_authority                                        */
/*==============================================================*/
create table user_authority
(
   user_authority_id    int not null auto_increment comment '用户权限ID',
   userid               int not null comment '用户ID',
   authorityid          int not null comment '权限ID',
   primary key (user_authority_id),
   unique key UK_userid_authorityid (userid, authorityid)
);

alter table user_authority comment '用户权限';

alter table article add constraint FK_Reference_3 foreign key (classid)
      references articleclass (classid) on delete restrict on update restrict;

alter table article add constraint FK_Reference_4 foreign key (userid)
      references user (userid) on delete restrict on update restrict;

alter table articleclass add constraint FK_Reference_1 foreign key (userid)
      references user (userid) on delete restrict on update restrict;

alter table group_authority add constraint FK_Reference_7 foreign key (groupid)
      references group (groupid) on delete restrict on update restrict;

alter table group_authority add constraint FK_Reference_8 foreign key (authorityid)
      references authority (authorityid) on delete restrict on update restrict;

alter table group_user add constraint FK_Reference_10 foreign key (groupid)
      references group (groupid) on delete restrict on update restrict;

alter table group_user add constraint FK_Reference_9 foreign key (userid)
      references user (userid) on delete restrict on update restrict;

alter table user_authority add constraint FK_Reference_5 foreign key (userid)
      references user (userid) on delete restrict on update restrict;

alter table user_authority add constraint FK_Reference_6 foreign key (authorityid)
      references authority (authorityid) on delete restrict on update restrict;
