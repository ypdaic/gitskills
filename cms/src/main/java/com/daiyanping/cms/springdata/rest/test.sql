create table user (
  id int auto_increment primary key,
  name varchar(50) null,
  email varchar(200) null
);

create table user_address(
  id int auto_increment primary key,
  user_id int null,
  city varchar(50) null,
  constraint user_address_user_id_fk foreign key (user_id) references user (id)
);

create index user_address_user_id_fk on user_address (user_id);