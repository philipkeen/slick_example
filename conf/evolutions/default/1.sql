-- !Ups

CREATE TABLE "person" (
    "id" serial PRIMARY KEY,
    "name" varchar(255) NOT NULL,
    "age" integer NOT NULL
);

-- !Downs

DROP TABLE "person" if exists;