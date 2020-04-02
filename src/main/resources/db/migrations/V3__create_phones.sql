CREATE TABLE phones
(
  id serial PRIMARY KEY,
  phone VARCHAR (128) NOT NULL,
  user_id INT REFERENCES users(id),
  created_on TIMESTAMP NOT NULL
);