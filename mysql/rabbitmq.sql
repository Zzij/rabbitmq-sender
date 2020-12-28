create table if not exists t_order(
 id varchar(50) primary key not null,
 name varchar(50) not null,
 message_id varchar(50) not null
) ENGINE=InnoDB default CHARSET=utf8;

create table if not exists t_broke_message_log(
  message_id varchar(50) not null,
  message varchar(50) default null;
  try_count int(4) default 0,
  status int default '',
  next_retry timestamp not null default '0000-00-00 00:00:00',
  create_time timestamp not null default '0000-00-00 00:00:00',
  update_time timestamp not null default '0000-00-00 00:00:00',
  primary key(message_id)
) ENGINE=InnoDB default CHARSET=utf8;