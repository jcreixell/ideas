# Ideas schema

# --- !Ups

CREATE SEQUENCE idea_id_seq;
CREATE TABLE ideas(
  id integer NOT NULL DEFAULT nextval('idea_id_seq'),
  title varchar(255),
  content text
);

# --- !Downs

DROP TABLE ideas;
DROP SEQUENCE idea_id_seq;
