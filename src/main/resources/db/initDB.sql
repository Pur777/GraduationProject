DROP TABLE dishes IF EXISTS;
DROP TABLE vote IF EXISTS;
DROP TABLE lunch_menu IF EXISTS;
DROP TABLE user_roles IF EXISTS;
DROP TABLE restaurants IF EXISTS;
DROP TABLE users IF EXISTS;
DROP SEQUENCE global_seq IF EXISTS;

CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;

CREATE TABLE users
(
    id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name             VARCHAR(255)            NOT NULL,
    email            VARCHAR(255)            NOT NULL,
    password         VARCHAR(255)            NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx
    ON USERS (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
    id          INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name        VARCHAR(255)        NOT NULL
);
CREATE UNIQUE INDEX restaurants_unique_name
    ON RESTAURANTS (name);

CREATE TABLE vote
(
    id                  INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    date                DATE            NOT NULL,
    user_id             INTEGER         NOT NULL,
    restaurant_name     VARCHAR(255)    NOT NULL
);
CREATE UNIQUE INDEX vote_unique
    ON VOTE (date, user_id);

CREATE TABLE lunch_menu
(
  id              INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  date            DATE         NOT NULL,
  restaurant_id   INTEGER      NOT NULL,
  FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX lunch_menu_unique
    ON LUNCH_MENU (date, restaurant_id);

CREATE TABLE dishes
(
    id              INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    name            VARCHAR(255)    NOT NULL,
    price           DOUBLE          NOT NULL,
    lunch_menu_id   integer         NOT NULL,
    FOREIGN KEY (lunch_menu_id) REFERENCES lunch_menu (id) ON DELETE CASCADE
);
