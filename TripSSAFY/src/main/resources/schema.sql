create schema tripssafy;
use tripssafy;

create table `user`(
	id bigint primary key auto_increment not null,
	username varchar(100) unique not null,
	password varchar(100),
	nickname varchar(100) unique not null,
	role enum('ROLE_USER', 'ROLE_ADMIN', 'ROLE_TRAVEL_AGENCY') default 'ROLE_USER',
	profile_img varchar(255),
	created_at datetime not null
);

create table `trip_room`(
	id bigint primary key auto_increment not null,
	title varchar(100) not null,
	start_date datetime not null,
	end_date datetime not null,
	created_at datetime not null
);

create table `user_room_mapping`(
	id bigint primary key auto_increment not null,
	user_id bigint not null,
	room_id bigint not null,
	created_at datetime not null,
	foreign key(`user_id`) references user(`id`) on delete cascade,
	foreign key(`room_id`) references trip_room(`id`) on delete cascade
);

create table `post`(
	id bigint primary key auto_increment not null,
	title varchar(100) not null,
	img varchar(255),
	content text not null,
	rating int,
	place_id bigint,
	room_id bigint,
	post_type enum('review', 'notice', 'room', 'qna'),
	view_count int not null default 0,
	writer_id bigint,
	created_at datetime not null,
	updated_at datetime,
	foreign key(`writer_id`) references user(`id`) on delete set null,
	foreign key(`room_id`) references trip_room(`id`) on delete cascade
);

create table `comment`(
	id bigint primary key auto_increment not null,
	content text not null,
	writer_id bigint,
	post_id bigint not null,
	parent_id bigint,
	created_at datetime not null,
	updated_at datetime,
	foreign key(`writer_id`) references user(`id`) on delete set null,
	foreign key(`post_id`) references post(`id`) on delete cascade,
	foreign key(`parent_id`) references comment(`id`) on delete set null
);

create table `trip_room_place`(
	id bigint primary key auto_increment not null,
	room_id bigint not null,
	place_id bigint not null,
	place_name varchar(100) not null,
	place_mapx double,
	place_mapy double,
	memo text,
	visit_date date not null,
	visit_order int not null,
	foreign key(`room_id`) references trip_room(`id`) on delete cascade
);

create table `expenses`(
	id bigint primary key auto_increment not null,
	amount int not null,
	user_id bigint not null,
	trip_place_id bigint not null,
	foreign key(`user_id`) references user(`id`),
	foreign key(`trip_place_id`) references trip_room_place(`id`)
);

create table `game_result`(
	id bigint primary key auto_increment not null,
    result_url text,
    trip_place_id bigint not null,
    created_at datetime not null,
    foreign key(`trip_place_id`) references trip_room_place(`id`)
);

create table `promotion`(
	id bigint primary key auto_increment not null,
    img varchar(255) not null,
    url varchar(255) not null,
    title text not null,
    content text not null,
    writer_id bigint not null,
    created_at datetime not null,
    foreign key(`writer_id`) references user(`id`)
);