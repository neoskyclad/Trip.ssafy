# tripssafy 더미 데이터

drop schema if exists tripssafy;
create schema tripssafy;
use tripssafy;

create table `user`(
	id bigint primary key auto_increment not null,
	username varchar(100) unique not null,
	password varchar(100),
	nickname varchar(100) not null,
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
	writer_id bigint,
	created_at datetime not null,
	updated_at datetime,
	foreign key(`writer_id`) references user(`id`) on delete set null,
	foreign key(`room_id`) references trip_room(`id`) on delete cascade
);

alter table post add column view_count int not null default 0;

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
    content_id bigint not null,
    img varchar(255) not null,
    url varchar(255) not null,
    title text not null,
    content text not null,
    writer_id bigint not null,
    created_at datetime not null,
    foreign key(`writer_id`) references user(`id`)
);

-- DUMMY  DATA --
-- 1. admin 유저 생성
INSERT INTO user (username, password, nickname, role, profile_img, created_at)
VALUES ('admin', '{noop}admin', 'Trip.ssafy', 'ROLE_ADMIN', null, current_date());
-- 일반 유저 생성
INSERT INTO user (username, password, nickname, role, profile_img, created_at)
VALUES ('neoskyclad', '{bcrypt}$2a$10$qUtXDoGvKaoOYScIxUayguJHi5SA3eu38YR6yJ83RqmNIl76GzF82', '권대현', 'ROLE_USER', 'https://tripssafy-bucket.s3.amazonaws.com/74f7c7ae-b117-4be0-80e5-100242f039c7_sample-profile-image3.jpg', '2025-05-26 16:46:10'),
('myddaguri@hanmail.net', '{bcrypt}$2a$10$7aowOiVD.sWR1VQ0409W7e4dOLgUhcMPDnPBNCT4Zr6MM1OF5Pj2e', '김싸피', 'ROLE_USER', 'https://tripssafy-bucket.s3.amazonaws.com/57ac2731-3620-49e3-b16d-37a14356f63c_sample-profile-image1.jpg', '2025-05-26 17:05:19'),
('neoskyclad@naver.com', '{bcrypt}$2a$10$nRjW2m0RQgjnouee9dxTpe4Q4sJWlyQRWVWFgmtldeAvfjkWm.1DS', '조예진', 'ROLE_USER', 'https://tripssafy-bucket.s3.amazonaws.com/04c06cf4-96de-4f27-b72c-21fcdd5141b2_sample-profile-image2.jpg', '2025-05-26 17:05:47');
INSERT INTO user (username, password, nickname, profile_img, created_at) VALUES
('ssafyg0oe5i', 'test123', 'ssafy_0', NULL, '2025-05-26 16:07:20'),
('ssafy05kozj', 'test123', 'ssafy_1', NULL, '2025-05-26 16:07:20'),
('ssafybnimfo', 'test123', 'ssafy_2', NULL, '2025-05-26 16:07:20'),
('ssafy3h2i5q', 'test123', 'ssafy_3', NULL, '2025-05-26 16:07:20'),
('ssafyg8m951', 'test123', 'ssafy_4', NULL, '2025-05-26 16:07:20'),
('ssafy4ilmtb', 'test123', 'ssafy_5', NULL, '2025-05-26 16:07:20'),
('ssafy55db0m', 'test123', 'ssafy_6', NULL, '2025-05-26 16:07:20'),
('ssafyul472u', 'test123', 'ssafy_7', NULL, '2025-05-26 16:07:20'),
('ssafya23nvu', 'test123', 'ssafy_8', NULL, '2025-05-26 16:07:20'),
('ssafy1whj4g', 'test123', 'ssafy_9', NULL, '2025-05-26 16:07:20'),
('ssafy24vt7r', 'test123', 'ssafy_10', NULL, '2025-05-26 16:07:20'),
('ssafyfzhhpe', 'test123', 'ssafy_11', NULL, '2025-05-26 16:07:20'),
('ssafymkymog', 'test123', 'ssafy_12', NULL, '2025-05-26 16:07:20');

-- 2. 방 생성
INSERT INTO trip_room
VALUES (1, 'test', '2025-05-25 00:00:00', '2025-05-28 00:00:00', '2025-05-26 16:09:21'),
('3', '권대현의 부산 뿌수기', '2025-05-25 00:00:00', '2025-05-27 00:00:00', '2025-05-26 17:14:26');

-- 2-1. 여행 장소 추가
INSERT INTO trip_room_place
VALUES ('1', '3', '126658', '태종대 (부산 국가지질공원)', '129.0798342807', '35.0597339815', NULL, '2025-05-25', '0'),
('2', '3', '229912', '씨라이프부산아쿠아리움', '129.1610328343', '35.159196575', NULL, '2025-05-25', '2'),
('3', '3', '127004', '해운대 관광특구', '129.1598690678', '35.1584383046', NULL, '2025-05-25', '1');

-- 2-2. 유저와 여행 방 매핑
INSERT INTO user_room_mapping
VALUES ('5', '2', '3', '2025-05-26 17:14:26'),
('6', '4', '3', '2025-05-26 17:14:26'),
('7', '3', '3', '2025-05-26 17:14:26');


-- 3. admin 유저의 id 조회
SELECT id FROM user WHERE username = 'admin';

-- 예를 들어 id가 1이라고 가정하면
-- 4. post 작성 (writer_id에 1 사용)
INSERT INTO post
VALUES (0, '❤️Trip.SSAFY  오픈 이벤트❤️', 'https://tripssafy-bucket.s3.amazonaws.com/f4f53e00-86fe-4c3f-becc-84cf7547f2a4_blob-heart.png',
'<h3>Trip.SSAFY가 정식 오픈 이벤트🧐</h3><p>5/28(수) Trip.SSAFY가 정식 오픈됩니다! 많은 관심 부탁드리며 이벤트에 많은 참가 부탁드립니다.</p><h3>오픈 이벤트</h3><ul><li><p>여행 방 생성 후 리뷰 게시글 1개 이상 작성 시 추첨 10분에게 치킨 기프티콘🐔</p></li><li><p>@tripssafy 태그 후 인스타 스토리 업로드 시 추첨 10분께 아메리카노 기프티콘☕</p></li></ul><p>을 드립니다</p><h5>기다려주셔서 감사합니다!💕</h5>',
0, 0, 1, 'notice', 1, current_date(), current_date(), 0),
('2', '혹시 여행 방에 사용자 초대는 어떻게 하나요?', NULL, '<p>이미 만든 여행방에 사용자 초대가 안됩니다..</p>', NULL, NULL, NULL, 'qna', '2', '2025-05-26 16:52:53', NULL, '3');
INSERT INTO post
VALUES 
('3', 'Q. 여행 방에 글을 올렸는데 수정은 못하나요?', NULL,
'<p>방금 리뷰 작성했는데 오타가 있어서요. 수정 기능이 있는지 궁금합니다!</p>',
NULL, NULL, NULL, 'qna', '3', '2025-05-26 17:15:00', NULL, 3),

('4', '🚨시스템 점검 안내🚨', NULL,
'<p>5/30(금) 00:00 ~ 02:00까지 시스템 점검이 예정되어 있습니다.<br>이용에 불편을 드려 죄송합니다. 더욱 안정적인 서비스 제공을 위해 노력하겠습니다.</p>',
0, 0, 1, 'notice', 1, '2025-05-26 17:20:00', NULL, 0);


# 프로모션
-- promotion 더미 숙소 5개
INSERT INTO promotion (id, content_id, img, url, title, content, writer_id, created_at)
VALUES 
(1, 1, 'https://cf.bstatic.com/xdata/images/hotel/max1024x768/60661791.jpg?k=fe8d4f9118e6f3eb6f38f42f5c79ad1930a0c544ded819d4820b5b57256dc3e1&o=&hp=1', 'https://cf.bstatic.com/xdata/images/hotel/max1024x768/60661791.jpg?k=fe8d4f9118e6f3eb6f38f42f5c79ad1930a0c544ded819d4820b5b57256dc3e1&o=&hp=1', '롯데시티호텔 명동', '서울 중심에서의 편안한 휴식, 롯데시티호텔에서 누려보세요.', 1, '2025-05-26 16:18:33'),

(2, 1, 'https://m.shillastay.com/images/contents/accmo/ACCMO_INDEX/R0000001EGMS_KR.jpg', 'https://m.shillastay.com/images/contents/accmo/ACCMO_INDEX/R0000001EGMS_KR.jpg', '신라스테이 해운대', '부산 바다와 함께하는 하루, 신라스테이 해운대에서 즐겨보세요.', 1, '2025-05-26 16:18:33'),

(3, 1, 'https://yaimg.yanolja.com/v5/2022/10/27/10/640/635a5ef36f86e6.90566883.jpg', 'https://yaimg.yanolja.com/v5/2022/10/27/10/640/635a5ef36f86e6.90566883.jpg', '여수 베네치아 호텔', '오션뷰와 야경이 아름다운 여수의 인기 숙소.', 1, '2025-05-26 16:18:33'),

(4, 1, 'https://staynmore.com/photo/hotel/CJU/723/1/1594629684.jpg', 'https://staynmore.com/photo/hotel/CJU/723/1/1594629684.jpg', '제주 해비치 호텔', '제주의 자연과 함께하는 럭셔리 숙박 경험.', 1, '2025-05-26 16:18:33'),

(5, 1, 'https://cf.bstatic.com/xdata/images/hotel/max1024x768/129750773.jpg?k=d78c4b3776b28eca5159d89c4daa3ca4d307ee1327a5cf83bc4a27e4fd7cab66&o=&hp=1', 'https://cf.bstatic.com/xdata/images/hotel/max1024x768/129750773.jpg?k=d78c4b3776b28eca5159d89c4daa3ca4d307ee1327a5cf83bc4a27e4fd7cab66&o=&hp=1', '강릉 스카이베이 호텔', '탁 트인 동해 바다 전망, 강릉의 랜드마크 호텔.', 1, '2025-05-26 16:18:33');
-- promotion 더미 항공 5개
INSERT INTO promotion
VALUES 
(6, 2, 'https://cdns.tourvis.com/promotion/202410/58801/mo_01.jpg', 'https://ssafy.com', '대한 항공', '25.05.28 09:00 ~ 12:00 ICN -> NRT', 1, '2025-05-26 16:32:29'),

(7, 2, 'https://www.sisaon.co.kr/news/photo/202105/127080_138948_5353.jpg', 'https://airbusan.com', '에어부산', '25.06.01 14:20 ~ 16:00 PUS -> KIX', 1, '2025-05-26 16:32:29'),

(8, 2, 'https://cdns.tourvis.com/promotion/202410/58777/mo_v2_01.jpg', 'https://jejuair.net', '제주항공', '25.06.05 07:50 ~ 09:10 GMP -> CJU', 1, '2025-05-26 16:32:29'),

(9, 2, 'https://cdn.traveltimes.co.kr/news/photo/202205/401569_22080_1917.jpg', 'https://flyasiana.com', '아시아나 항공', '25.06.10 18:30 ~ 21:00 ICN -> TPE', 1, '2025-05-26 16:32:29'),

(10, 2, 'https://cdn.traveltimes.co.kr/news/photo/202201/400207_20257_5736.jpg', 'https://twayair.com', '티웨이 항공', '25.06.12 11:10 ~ 13:30 ICN -> BKK', 1, '2025-05-26 16:32:29');
-- promotion 더미 여행 상품 5개
INSERT INTO promotion (id, content_id, img, url, title, content, writer_id, created_at)
VALUES
(11, 3, 'https://cdn.onews.tv/news/photo/202501/230551_278690_418.jpg', 'https://modetour.com', '모두 투어 2025 하절기 여행 특가!', '여름 왔다 휴양 가자~', 1, '2025-05-26 16:39:06'),

(12, 3, 'https://cdn.finomy.com/news/photo/202010/84501_67740_5021.png', 'https://hanatour.com', '하나투어 일본 벚꽃 투어', '벚꽃 시즌에 맞춘 일본 소도시 여행 3박 4일', 1, '2025-05-26 16:39:06'),

(13, 3, 'https://file2.nocutnews.co.kr/newsroom/image/2016/06/24/20160624132608654143.jpg', 'https://tour2000.co.kr', '투어2000 동남아 자유여행 패키지', '방콕·다낭·세부 인기 여행지 묶음 할인 특가!', 1, '2025-05-26 16:39:06'),

(14, 3, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSK5ivzCj2DKlXGYBmI4oK6xXTch265W56gbg&s', 'https://kkday.com', 'KKday 유럽 투어 세미패키지', '파리·로마·프라하 자유일정 포함 9일 상품', 1, '2025-05-26 16:39:06'),

(15, 3, 'https://news.nateimg.co.kr/orgImg/fr/2024/08/16/art_651057_1723781929.jpg', 'https://interpark.com', '인터파크 괌 가족여행 전세기 상품', '괌 직항 전세기 + 리조트 포함 가족맞춤 패키지', 1, '2025-05-26 16:39:06');

# comment
INSERT INTO comment (id, content_id, img, url, title, content, writer_id, created_at)
VALUES ('1', '와!! 너무 기다렸어요~', '2', '1', NULL, '2025-05-26 16:46:28', NULL),
('2', '권대현님! 이용에 불편을 드려서 죄송합니다. 현재 사용자 초대는 방 생성 시에 가능하며, 추후에 이미 생성된 방에 사용자 초대 기능을 넣을 예정입니다. 😊', '1', '2', NULL, '2025-05-26 16:54:40', NULL),
-- 추가 댓글들
('3', '정말 기대되는 서비스에요! 친구들이랑 같이 여행 계획 짜기 너무 좋을 것 같아요.', '3', '1', NULL, '2025-05-26 17:00:10', NULL),

('4', '공지사항 잘 봤습니다. 혹시 모바일 앱도 출시 예정인가요?', '2', '2', NULL, '2025-05-26 17:05:22', NULL),

-- 대댓글 (부모 댓글 4번에 대한 답글)
('5', '앱은 현재 계획 중이며, 우선 웹에서 모든 기능을 제공할 예정입니다. 😊', '1', '2', '4', '2025-05-26 17:07:45', NULL),

-- 리뷰 게시글에 댓글
('6', '리뷰 보고 따라갔다 왔는데 진짜 너무 좋았어요! 감사합니다 ㅎㅎ', '4', '3', NULL, '2025-05-26 18:12:00', NULL),

-- 리뷰 게시글에 질문
('7', '여기 숙소 예약은 어디서 하셨어요?', '3', '3', NULL, '2025-05-26 18:14:20', NULL),

-- 대댓글 (7번에 대한 답글)
('8', '저는 야놀자에서 예약했어요~ 할인도 있어서 저렴하게 다녀왔답니다!', '4', '3', '7', '2025-05-26 18:15:10', NULL);

# post (장소 리뷰)
INSERT INTO post
VALUES ('5', '대박이에요', NULL, '뷰가.. 어우', '5', '126658', NULL, 'review', '2', '2025-05-26 17:17:37', NULL, '0'),
('6', '열차가 재밌어요', NULL, '11', '3', '252052', NULL, 'review', '2', '2025-05-26 17:18:40', NULL, '0'),
('7', '전망대 뷰가 좋아요!', NULL, '완전 짱~', '4', '1608611', NULL, 'review', '2', '2025-05-26 17:18:52', NULL, '0'),
('8', '물고기가 너무 아름다워요~~', NULL, '부산 아쿠아리움 최고!', '4', '229912', NULL, 'review', '4', '2025-05-26 17:21:35', NULL, '0'),
('9', '몰빵 룰렛에서 졌어요..', NULL, '기분 안좋아서 1점 드립니다.. 해운대는 좋아요', '1', '127004', NULL, 'review', '3', '2025-05-26 17:22:19', NULL, '0');

# game_result
INSERT INTO game_result
VALUES ('1', 'data:image/png;base64,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', '3', '2025-05-26 17:20:40');

# expenses
INSERT INTO expenses
VALUES ('1', '0', '2', '1'),
('2', '0', '4', '1'),
('3', '0', '3', '1'),
('4', '0', '2', '3'),
('5', '0', '4', '3'),
('6', '100000', '3', '3'),
('7', '26666', '2', '2'),
('8', '26666', '4', '2'),
('9', '26666', '3', '2');
