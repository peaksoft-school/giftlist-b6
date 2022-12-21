INSERT INTO user_info(id, clothing_size, country, date_of_birth, hobby, important, phone_number, shoe_size)
VALUES (1, 'XS', 'Kyrgyzstan', '2000-12-12', 'tennis', null, '0999234554', 36),
       (2, 'M', 'Kyrgyzstan', '2004-11-11', 'volleyball', null, '0709899876', 37),
       (3, 'S', 'Kyrgyzstan', '1998-10-09', 'football', null, '0709465733', 38),
       (4, 'XS', 'Kyrgyzstan', '2000-10-05', null, null, '0559234595', 35),
       (5, 'S', 'Kyrgyzstan', '1997-12-03', null, null, '0559234595', 36),
       (6, 'XS', 'Kyrgyzstan', '1998-12-12', null, null, '0559290595', 37),
       (7, 'S', 'Kyrgyzstan', '2002-10-12', null, null, '0959234845', 38),
       (8, 'M', 'Kyrgyzstan', '1999-09-01', null, null, '0999234595', 35),
       (9, 'M', 'Kyrgyzstan', '2003-12-09', null, null, '0999234595', 35);

INSERT INTO users(id, first_name, last_name, is_block, email, password, image, role, user_info_id)
VALUES (1, 'Admin', 'Admin', false, 'admin@gmail.com', '$2a$12$a/7JdTteE5.pmewQeybae.dumhUkp1ABxxESQN7c5zgmK9GFwHeIW',
        'image', 'ADMIN', 1),
       (2, 'Bektur', 'Kanybekov', false, 'bektur@gmail.com', '$2a$12$MXgVFpgW8uWMwTMjG/0I4ekXUiiPojhMXjBf6vefv3Ea.ZOx48fei',
        'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671654162438photo_2022-12-22_02-21-49.jpg', 'USER', 2),
       (3, 'Aiza', 'Anarbekova', false, 'aiza@gmail.com',
        '$2a$12$yU5x4BST3FpXc0af1mwSfu3pGe./QBMU58VHinr9soYes/pf9jUca', 'image', 'USER', 3),
       (4, 'Aiperi', 'Mirlanova', false, 'mirlanova@gmail.com',
        '$2a$12$j1yNeUrGbQhG5HPNUkczjeFye7Y8Gawi2mS94afZKi4FC/s6BEoHS', 'image', 'USER', 4),
       (5, 'Nurisa', 'Mamiraimova', false, 'nurisa@gmail.com',
        '$2a$12$fUGH/gruCqYHD7MTy0Io8edOa0x3MsRtNlHJGUzyDPosGDUyU7.sa', 'image', 'USER', 5),
       (6, 'Sunat', 'Januzakov', false, 'sunat@gmail.com',
        '$2a$12$DzNum5bGvbAy67AR0aHZ6u48jJGHdGL14z3JZhQDsSwKSKiR8C.Ze', 'image', 'USER', 6),
       (7, 'Klara', 'Azimova', false, 'klara@gmail.com', '$2a$12$VAo1JkaqTlw9PYwC5hPIs.7u.h3uCXOFqNlLM8LFKVOj3y3ZnFGBi',
        'image', 'USER', 7),
       (8, 'Maksat', 'Bekmurza uulu', false, 'maksat@gmail.com',
        '$2a$12$FluRPUh71oKI/UqjxdLz8e9ltAO0vqGvxMsWzSF2X4zemBfKfJGWu', 'image', 'USER', 8),
       (9, 'Nurgazy', 'Nurmamatov', false, 'nurgazy@gmail.com',
        '$2a$12$HdYBNIIcIMx1gJ90DKwmt.wnAEEAmXgSlB2q6SP1xkgtoye1e6UjK', 'image', 'USER', 9);

INSERT INTO categories(id, name)
VALUES (1, 'Электроника'),
       (2, 'Одежда'),
       (3, 'Школа'),
       (4, 'Дом и сад'),
       (5, 'Обувь'),
       (6, 'Транспорт');

INSERT INTO sub_category(id, name, category_id)
VALUES (1, 'Телефон', 1),
       (2,'Техника для кухни',1),
       (3, 'Аудиотехника', 1),
       (4, 'Фото и видеокамеры', 1),
       (5, 'Бытовая техника', 1),
       (6, 'Тв и видео', 1),
       (7, 'Компьютеры, ноутбуки и планшеты', 1),
       (8, 'Автоэлектроника', 1),
       (9, 'Свитера', 2),
       (10, 'Сумка', 2),
       (11, 'Брюки', 2),
       (12, 'Юбки', 2),
       (13, 'Шубы', 2),
       (14, 'Пальто', 2),
       (15, 'Головные уборы',2),
       (16, 'Платья', 2),
       (17, 'Тетради', 3),
       (19,'Маркеры',3),
       (20,'Ручка',3),
       (21,'Фломастеры',3),
       (22,'Парта',3),
       (23,'Доска',3),
       (24,'Рюкзак',3),
       (25,'Школьная форма',3),
       (26,'Мебель',4),
       (27,'Продукты питания',4),
       (28,'Декор для дома',4),
       (29,'Комнатные растения',4),
       (30,'Гладильные доски',4),
       (31,'Ремонт и строительство',4),
       (32,'Кухонные принадлежности',4),
       (33,'Ботильоны',5),
       (34,'Угги',5),
       (35,'Домашние тапочки',5),
       (36,'Сапоги',5),
       (37,'Сандали',5),
       (38,'Туфли',5),
       (39,'Автомобили',6),
       (40,'Автозапчасти',6),
       (41,'Мотоциклы',6),
       (42,'Водный транспорт',6),
       (43,'Аксессуары, шины',6),
       (44,'Другой транспорт',6);

INSERT INTO charity(id, charity_status, condition, created_date,is_block,
                    description, image, name, reservoir_id, user_id, category_id, sub_category_id)
VALUES (1, 'RESERVED', 'Б/У', '2021-12-12',false, 'white', null, 'сумка', 3, 4, 2, 6),
    (2, 'WAIT', 'Б/У', '2022-09-08',false, null, null, 'ноутбук', null, 4, 1, 3),
    (3, 'WAIT', 'Новый', '2020-12-01',false, null, null, 'платье', null, 4, 2, 5);

INSERT INTO holidays(id, date_of_holiday, image,is_block, name, user_id)
VALUES (1, '2023-03-21', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671643919783252950.jpg',false, 'Нооруз', 4),
       (2, '2023-11-15','https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671644063231den-rozhdeniya.jpg',false, 'День рождения', 4),
       (3, '2023-03-08','https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671644327119-01-08-2020-193112.png',false, '8-март', 3),
       (4, '2023-08-08', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/16716444864580170dd058e449c971751808fe2829d15.jpg',false, 'Курбан айт', 4),
       (5, '2023-02-24', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/167164512606907-12-18_2.jpg',false, 'День рождения',9),
       (6, '2022-12-31', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671645212294ng.jpg',false, 'Новый год', 9),
       (7, '2022-12-31', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671645212294ng.jpg',false, 'Новый год', 3),
       (8, '2022-12-31', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671645212294ng.jpg',false, 'Новый год', 4),
       (9, '2022-12-31', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671645212294ng.jpg',false, 'Новый год', 5),
       (10, '2022-12-31', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671645212294ng.jpg',false, 'Новый год', 6),
       (11, '2022-12-31', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671645212294ng.jpg',false, 'Новый год', 7),
       (12, '2022-12-31', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671645212294ng.jpg',false, 'Новый год', 8),
       (13, '2022-12-31', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671645212294ng.jpg',false, 'Новый год', 2),
       (14, '2023-01-08', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/167164536675278a63cefd569bbe7b9adada2d36d60d7.jpeg',false, 'Halloween', 9),
       (15, '2023-09-02','https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671644063231den-rozhdeniya.jpg',false, 'День рождения', 3),
       (16, '2023-03-05','https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671644063231den-rozhdeniya.jpg',false, 'День рождения', 5),
       (17, '2023-11-09','https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671644063231den-rozhdeniya.jpg',false, 'День рождения', 6),
       (18, '2023-06-15','https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671644063231den-rozhdeniya.jpg',false, 'День рождения', 7),
       (19, '2023-01-01','https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671644063231den-rozhdeniya.jpg',false, 'День рождения', 8);


INSERT INTO wishes(id, date_of_holiday, description, image, link_to_gift, wish_name, wish_status, is_block, holiday_id,
                   reservoir_id, user_id)
VALUES (1, '2023-03-21', 'Книга, покорившая мир, эталон литературы, синоним успеха. Книга, ставшая культовой уже для нескольких поколений.', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671655269614307ee2b078788afb3a64d903b389bf54.webp', 'https://topkaup.ee/garri-potter-i-uznik-azkabana-s-cvetnymi-illyustraciyami-kupit-knigu', 'книга', 'RESERVED', false, 1, 4, 3),
       (2, '2023-09-02', 'Телефон', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671655467740CFE1B928-708E-4DB5-B801-85BA8BF1ABFF.jpeg', 'https://asiastore.kg/apple-iphone/iphone-14-pro/iphone-14-pro-128-gb-temno-fioletovyj-2166', 'iphone 14pro', 'RESERVED', false, 2, 3, 4),
       (3, '2023-03-08', 'Велосипед', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671655775275r140.jpg', 'https://bikes.kg/c/velosipedy-dlya-malchikov-9-13-let/podrostkovyj-velosiped-stels-mustang-v-24-v030-2018/', 'sneakers', 'RESERVED', false, 3, 3, 4),
       (4, '2023-08-08', 'Ноутбук', 'https://giftlist-b6.s3.eu-central-1.amazonaws.com/1671656155376273178363.jpg', 'https://bikes.kg/c/velosipedy-dlya-malchikov-9-13-let/podrostkovyj-ve', 'macbook air pro', 'WAIT', false, 4, null, 4),
       (5, '2023-08-04', 'Сумка', null, null, 'сумка', 'RESERVED', false, 4, 5, 4),
       (6, '2023-07-04', 'телефон', null, null, 'iphone 13', 'RESERVED', false, 4, 5, 2);

INSERT INTO gift(id, user_id, wish_id)
VALUES (1, 4, 1),
       (2, 3, 2),
       (3, 3, 3),
       (4, 5, 5),
       (5, 5, 6);

INSERT INTO users_requests(user_id, requests_id)
VALUES (3, 4),
       (3, 5),
       (3, 9);

INSERT INTO users_friends(user_id, friends_id)
VALUES (5, 6),
       (5, 7),
       (5, 8),
       (5, 4);

INSERT INTO complaints(id, is_seen, reason_text, complainer_id, created_at, wish_id)
VALUES (1, false, 'SPAM', 3, '2022-10-12', 1);
