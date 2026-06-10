CREATE TABLE exercises (
                           id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
                           name                VARCHAR(255) NOT NULL,
                           description         VARCHAR(500),
                           type                VARCHAR(20)  NOT NULL,
                           muscle_group        VARCHAR(30)  NOT NULL,
                           equipment           VARCHAR(100),
                           is_custom           TINYINT(1)   NOT NULL DEFAULT 0,
                           created_by_user_id  BIGINT,
                           CONSTRAINT fk_exercise_user
                               FOREIGN KEY (created_by_user_id)
                                   REFERENCES users(id)
                                   ON DELETE SET NULL
);

CREATE INDEX idx_exercises_type        ON exercises(type);
CREATE INDEX idx_exercises_muscle      ON exercises(muscle_group);
CREATE INDEX idx_exercises_custom_user ON exercises(created_by_user_id);