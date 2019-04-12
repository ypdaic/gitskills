CREATE TABLE mysql_lock (
  id int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  lock_name varchar(64) NOT NULL COMMENT '锁名',
  update_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '保存数据时间，自动生成',
  PRIMARY KEY (id),
  UNIQUE KEY lock_name_UNIQUE (lock_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='mysql实现分布式锁';