INSERT INTO user_info(id, clothing_size, country, date_of_birth, hobby, important, phone_number, shoe_size)
VALUES (1, 'XS', 'Kyrgyzstan', '1998-12-14', 'tennis', null, '0999234554', 36),
        (2, 'S', 'Kyrgyzstan', '2000-08-14', 'volleyball', null, '0709899876', 37),
        (3, 'XL', 'Kyrgyzstan', '2002-12-15', 'football', null, '0709465733', 38),
        (4, 'XXL', 'Kyrgyzstan', '1999-01-09', null, null, '0559234595', 35);

INSERT INTO users(id, first_name, last_name, is_block, email, password, photo, role, user_info_id)
VALUES (1, 'Admin', 'Admin', false, 'admin@gmail.com', '$2a$12$a/7JdTteE5.pmewQeybae.dumhUkp1ABxxESQN7c5zgmK9GFwHeIW',
        'image', 'ADMIN', 1),
       (2, 'User', 'User', false, 'user@gmail.com', '$2a$12$MXgVFpgW8uWMwTMjG/0I4ekXUiiPojhMXjBf6vefv3Ea.ZOx48fei',
        'image', 'USER', 2),
       (3, 'Aiza', 'Anarbekova', false, 'aiza@gmail.com',
        '$2a$12$yU5x4BST3FpXc0af1mwSfu3pGe./QBMU58VHinr9soYes/pf9jUca', 'image', 'USER', 3),
       (4, 'Aiperi', 'Mirlanova', false, 'mirlanova@gmail.com',
        '$2a$12$j1yNeUrGbQhG5HPNUkczjeFye7Y8Gawi2mS94afZKi4FC/s6BEoHS', 'image', 'USER', 4);

INSERT INTO charity(id, charity_status, condition, created_date,
                    description, image, name, reservoir_id, user_id)
VALUES (1, 'WAIT', 'Б/У', '12-12-2020', 'white', null, 'сумка', 3, 4),
       (2, 'WAIT', 'Б/У', '09-04-2021', null, null, 'ноутбук', 3, 4),
       (3, 'WAIT', 'Новый', '07-04-2021', null, null, 'платье', 3, 4);

INSERT INTO holidays(id, date_of_holiday, image, name, user_id)
VALUES (1, '12-12-2020', null, 'Нооруз', 4),
       (2, '08-08-2020', null, 'День рождения', 4),
       (3, '09-09-2020', null, '8-март', 3),
       (4, '06-05-2020', null, 'Курбан айт', 4);

INSERT INTO wishes(id, date_of_holiday, description, image, link_to_gift, wish_name, wish_status, holiday_id,
                   reservoir_id, user_id)
VALUES (1, '12-12-2020', 'роман', null, null, 'книга', 'RESERVED', 1, 4, 3),
       (2, '11-10-2020', 'телефон', null, null, 'iphone 14pro', 'RESERVED', 2, 3, 4),
       (3, '02-10-2020', 'шоколад', null, null, 'sneakers', 'RESERVED', 3, 3, 3),
       (4, '11-11-2020', 'ноутбук', null, null, 'macbook air pro', 'WAIT', 4, null, 4);

INSERT INTO gift(id, user_id, wish_id)
VALUES (1, 3, 1),
       (2, 4, 2),
       (3, 3, 3),
       (4, 3, 4);

INSERT INTO categories(id, name, charity_id)
VALUES (1, 'электроника', 1),
       (2, 'одежда', 2),
       (3, 'школа', 3),
       (4, 'дом и сад', 1),
       (5, 'обувь', 1),
       (6, 'транспорт', 1);

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

INSERT INTO users_requests(user_id, requests_id)
VALUES (3, 4);

INSERT INTO users_friends(user_id, friends_id)
VALUES (4, 3);

INSERT INTO complaints(id, is_seen, reason_text, complainer_id, wish_id)
VALUES (1, false, 'SPAM', 3, 1);
