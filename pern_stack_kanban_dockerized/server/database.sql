CREATE TABLE todo(
    todo_id SERIAL PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE inprogress(
    todo_id SERIAL PRIMARY KEY,
    description VARCHAR(255)
);

CREATE TABLE finished(
    todo_id SERIAL PRIMARY KEY,
    description VARCHAR(255)
);