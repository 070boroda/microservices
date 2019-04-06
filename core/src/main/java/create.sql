CREATE TABLE IF NOT EXISTS application_users(
id int PRIMARY KEY,
user_name varchar(255),
password varchar(255)),
role varchar(255);

CREATE TABLE IF NOT EXISTS teachers(
id int PRIMARY KEY,
first_name varchar(255),
second_name varchar(255));
