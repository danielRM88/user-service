CREATE TABLE user_emails
(
  id serial PRIMARY KEY,
  email VARCHAR (128) NOT NULL,
  created_on TIMESTAMP NOT NULL
);