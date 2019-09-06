DELETE FROM dishes;
DELETE FROM vote;
DELETE FROM lunch_menu;
DELETE FROM restaurants;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES  ('User', 'user@mail.ru','{noop}password'),
        ('Second User', 'seconduser@mail.ru', '{noop}password'),
        ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES  ('ROLE_USER', 100000),
        ('ROLE_ADMIN', 100002),
        ('ROLE_USER', 100001);

INSERT INTO restaurants (NAME)
VALUES  ('Горыныч'),
        ('Северяне'),
        ('White Rabbit'),
        ('Remy Kitchen Bakery');

INSERT INTO lunch_menu (DATE, RESTAURANT_ID)
VALUES  ('2015-05-30', 100003),
        ('2015-05-30', 100004),
        ('2015-05-30', 100005),
        ('2015-05-30', 100006),
        ('2016-02-03', 100003);

INSERT INTO dishes (NAME, PRICE, LUNCH_MENU_ID)
VALUES
        ('Утиная ножка конфи с морковью и мочеными яблоками', 760.00, 100007),
        ('Утиная грудка с пармантье из сельдерея и вишней', 680.00, 100007),
        ('Томленый говяжий язык в соусе из сладкой горчицы с малосольным огурцом', 760.00, 100008),
        ('Филе-миньон с лжекартофелем, белыми грибами и трюфельным соусом', 950.00, 100008),
        ('Жареный осьминог с картофелем и вешенками', 780.00, 100009),
        ('Подкопченный лосось с зеленым горошком и луковым муссом', 980.00, 100009),
        ('Сливочный суп с лососем', 480.00, 100010),
        ('Горячий борщ с ростбифом и салом', 350.00, 100010),
        ('Пирожок с картошкой', 50.0, 100011),
        ('Пирожок с капустой', 30.0, 100011);

INSERT INTO vote (DATE, USER_ID, RESTAURANT_NAME)
VALUES
       ('2019-07-19', 100000, 'Горыныч'),
       ('2019-07-19', 100001, 'Горыныч'),
       ('2019-07-19', 100002, 'White Rabbit'),
       ('2019-07-20', 100000, 'Remy Kitchen Bakery'),
       ('2019-07-20', 100001, 'Remy Kitchen Bakery'),
       ('2019-07-20', 100002, 'Remy Kitchen Bakery'),
       ('2019-07-21', 100000, 'Горыныч'),
       ('2019-07-21', 100001, 'Северяне'),
       ('2019-07-21', 100002, 'White Rabbit'),
       ('2019-07-22', 100001, 'Горыныч');
