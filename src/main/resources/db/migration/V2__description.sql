INSERT INTO user_info(id, clothing_size, country, date_of_birth, hobby, important, phone_number, shoe_size)
VALUES (1, 'XS', 'Kyrgyzstan', '2000-12-12', 'tennis', null, '0999234554', 36),
       (2, 'S', 'Kyrgyzstan', '1999-11-11', 'volleyball', null, '0709899876', 37),
       (3, 'XL', 'Kyrgyzstan', '1998-10-09', 'football', null, '0709465733', 38),
       (4, 'XS', 'Kyrgyzstan', '2000-10-05', null, null, '0559234595', 35),
       (5, 'XL', 'Kyrgyzstan', '1997-12-03', null, null, '0559234595', 36),
       (6, 'XS', 'Kyrgyzstan', '1998-12-12', null, null, '0559290595', 37),
       (7, 'S', 'Kyrgyzstan', '2002-10-12', null, null, '0959234845', 38),
       (8, 'M', 'Kyrgyzstan', '1999-09-01', null, null, '0999234595', 35),
       (9, 'M', 'Kyrgyzstan', '2003-12-09', null, null, '0999234595', 35);

INSERT INTO users(id, first_name, last_name, is_block, email, password, image, role, user_info_id)
VALUES (1, 'Admin', 'Admin', false, 'admin@gmail.com', '$2a$12$a/7JdTteE5.pmewQeybae.dumhUkp1ABxxESQN7c5zgmK9GFwHeIW',
        'image', 'ADMIN', 1),
       (2, 'User', 'User', false, 'user@gmail.com', '$2a$12$MXgVFpgW8uWMwTMjG/0I4ekXUiiPojhMXjBf6vefv3Ea.ZOx48fei',
        'image', 'USER', 2),
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
VALUES (1, 'электроника'),
       (2, 'одежда'),
       (3, 'школа'),
       (4, 'дом и сад'),
       (5, 'обувь'),
       (6, 'транспорт');

INSERT INTO sub_category(id, name, category_id)
VALUES (1, 'телефон', 1),
       (3, 'аудиотехника', 1),
       (4, 'фото и видеокамеры', 1),
       (5, 'свитер', 2),
       (6, 'сумка', 2),
       (7, 'школьная сумка', 3),
       (8, 'тетрадь', 3),
       (9, 'диван', 4),
       (10, 'плитка', 4),
       (11, 'кроссовки', 5),
       (12, 'велосипед', 6);

INSERT INTO charity(id, charity_status, condition, created_date,
                    description, image, name, reservoir_id, user_id, category_id, sub_category_id)
VALUES (1, 'RESERVED', 'Б/У', '2021-12-12', 'white', null, 'сумка', 3, 4, 2, 6),
       (2, 'WAIT', 'Б/У', '2022-09-08', null, null, 'ноутбук', null, 4, 1, 3),
       (3, 'WAIT', 'Новый', '2020-12-01', null, null, 'платье', null, 4, 2, 5);

INSERT INTO holidays(id, date_of_holiday, image, name, user_id)
VALUES (1, '2022-05-09', null, 'Нооруз', 4),
       (2, '2021-09-02', null, 'День рождения', 4),
       (3, '2022-03-08', null, '8-март', 3),
       (4, '2022-09-08', null, 'Курбан айт', 4);

INSERT INTO wishes(id, date_of_holiday, description, image, link_to_gift, wish_name, wish_status, is_block, holiday_id,
                   reservoir_id, user_id)
VALUES (1, '2020-12-03', 'роман', null, null, 'книга', 'RESERVED', false, 1, 4, 3),
       (2, '2022-07-05', 'телефон', null, null, 'iphone 14pro', 'RESERVED', false, 2, 3, 4),
       (3, '2022-01-09', 'шоколад', null, null, 'sneakers', 'RESERVED', false, 3, 3, 4),
       (4, '2020-02-09', 'ноутбук', null, null, 'macbook air pro', 'WAIT', false, 4, null, 4);

INSERT INTO gift(id, user_id, wish_id)
VALUES (1, 4, 1),
       (2, 3, 2),
       (3, 3, 3);

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
