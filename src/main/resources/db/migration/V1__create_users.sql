CREATE TABLE users (
                       id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email      VARCHAR(255) NOT NULL UNIQUE,
                       password   VARCHAR(255) NOT NULL,
                       username   VARCHAR(50)  NOT NULL UNIQUE,
                       first_name VARCHAR(100) NOT NULL,
                       last_name  VARCHAR(100) NOT NULL,
                       age        INT,
                       weight_unit VARCHAR(3)  DEFAULT 'KG',
                       streak_count INT        DEFAULT 0,
                       last_workout_date DATE,
                       role       VARCHAR(20)  DEFAULT 'USER',
                       created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);