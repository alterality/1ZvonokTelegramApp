create table auth_user(
    id serial primary key
);

create table telegram_user(
    id serial primary key
);

create table average_rating(
    id serial primary key,
    count int not null default 0,
    average real not null default 0,
    master_id int not null references auth_user(id),
    created_at TIMESTAMP DEFAULT NOW()
);


create table rating(
    id serial primary key,
    rate smallint not null check ( rate between 1 and 5 ),
    review varchar(500),
    master_id int not null references auth_user(id),
    client_id int references auth_user(id),
    telegram_user_id int not null references telegram_user(id),
    average_rating_id int not null references average_rating(id),
    created_at TIMESTAMP DEFAULT NOW()
);