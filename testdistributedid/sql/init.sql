-- 使用数据库来创建唯一主键
create table test.SEQUENCE_ID(
	id bigint(20) unsigned not null auto_increment,
	value char(1) not null default '',
	primary key (id)
) engine= innodb;

CREATE TABLE id_generator (
  id int(10) NOT NULL auto_increment,
  max_id bigint(20) NOT NULL COMMENT '当前最大id',
  step int(20) NOT NULL COMMENT '号段的步长',
  biz_type int(20) NOT NULL COMMENT '业务类型',
  version int(20) NOT NULL COMMENT '版本号',
  PRIMARY KEY (`id`)
) engine= innodb;

-- 初始化号段表
insert into id_generator(max_id,step,biz_type,version) values (0,1000,1000,0);

-- 取下一个号段

-- 查询当前业务的 max_id
select id,max_id,step,biz_type,version from id_generator where biz_type = ? ;

-- 乐观锁更新 max_id
update id_generator set max_id = max_id + step ,version = version + 1 where id = ? and max_id = ? and version = ?