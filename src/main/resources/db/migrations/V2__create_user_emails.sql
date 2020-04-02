CREATE TABLE user_emails
(
  id serial PRIMARY KEY,
  email VARCHAR (128) NOT NULL,
  user_id INT REFERENCES users(id),
  created_on TIMESTAMP NOT NULL
);