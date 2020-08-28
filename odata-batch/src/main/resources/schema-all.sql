DROP TABLE people IF EXISTS;
CREATE TABLE people  (
    person_id BIGINT IDENTITY NOT NULL PRIMARY KEY,
    rabobank_id VARCHAR(40),
    first_name VARCHAR(40),
    last_name VARCHAR(40)
);
