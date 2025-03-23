create table auth_user(
                          id serial primary key
);

create table telegram_user(
                              id serial primary key
);


create table deals(
                      id serial primary key,
                      address varchar not null,
                      description varchar(2000),
                      cost int,
                      photo varchar,
                      master_id int references auth_user(id),
                      client_id int references auth_user(id),
                      telegram_user_id int not null references telegram_user(id),
                      created_at TIMESTAMP DEFAULT NOW()
);