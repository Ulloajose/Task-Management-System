INSERT INTO user(EMAIL, USERNAME, PASSWORD) VALUE('jose@mail.com', 'jose', '$2a$10$nU6WDJvvfpLZSFEJZ9WND.ZDiOOMMhjRXabRRwimrR7JxlSxuInf2');
INSERT INTO user(EMAIL, USERNAME, PASSWORD) VALUE('ulloa@mail.com', 'ulloa', '$2a$10$dZrQHa/U.g9bnDhb6pOrNeb0tKl2CBeSOQJdFG.MS1sWM.SNuFhI6');

INSERT INTO role(name) VALUES ('ADMIN'), ('USER');
INSERT INTO user_role(role_id, user_id) VALUES (1,1),(1,2),(2,2);
INSERT INTO task_status(name) VALUES ('PENDING'), ('IN_PROGRESS'), ('COMPLETED'), ('CANCELLED');