CREATE TABLE users
(
  id serial PRIMARY KEY,
  first_name VARCHAR (128) NOT NULL,
  last_name VARCHAR (128) NOT NULL,
  created_on TIMESTAMP NOT NULL
);