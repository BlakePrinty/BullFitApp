CREATE TABLE workout_sessions (
                                  id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  user_id          BIGINT NOT NULL,
                                  split_day_id     BIGINT,
                                  date             DATE NOT NULL,
                                  duration_minutes INT,
                                  notes            VARCHAR(500),
                                  created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_session_user     FOREIGN KEY (user_id)      REFERENCES users(id)      ON DELETE CASCADE,
                                  CONSTRAINT fk_session_splitday FOREIGN KEY (split_day_id) REFERENCES split_days(id) ON DELETE SET NULL
);

CREATE TABLE exercise_logs (
                               id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
                               workout_session_id BIGINT NOT NULL,
                               exercise_id        BIGINT NOT NULL,
                               order_index        INT NOT NULL,
                               notes              VARCHAR(500),
                               CONSTRAINT fk_log_session  FOREIGN KEY (workout_session_id) REFERENCES workout_sessions(id) ON DELETE CASCADE,
                               CONSTRAINT fk_log_exercise FOREIGN KEY (exercise_id)        REFERENCES exercises(id)        ON DELETE CASCADE
);

CREATE TABLE strength_sets (
                               id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                               exercise_log_id BIGINT NOT NULL,
                               set_number      INT NOT NULL,
                               reps            INT NOT NULL,
                               weight          DOUBLE NOT NULL,
                               weight_unit     VARCHAR(3) NOT NULL DEFAULT 'KG',
                               is_warmup       TINYINT(1) NOT NULL DEFAULT 0,
                               CONSTRAINT fk_set_log FOREIGN KEY (exercise_log_id) REFERENCES exercise_logs(id) ON DELETE CASCADE
);

CREATE TABLE cardio_logs (
                             id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                             exercise_log_id  BIGINT NOT NULL,
                             duration_minutes INT NOT NULL,
                             distance         DOUBLE,
                             distance_unit    VARCHAR(2),
                             avg_heart_rate   INT,
                             notes            VARCHAR(500),
                             CONSTRAINT fk_cardio_log FOREIGN KEY (exercise_log_id) REFERENCES exercise_logs(id) ON DELETE CASCADE
);

CREATE INDEX idx_sessions_user ON workout_sessions(user_id);
CREATE INDEX idx_sessions_date ON workout_sessions(date);
CREATE INDEX idx_logs_session  ON exercise_logs(workout_session_id);
CREATE INDEX idx_sets_log      ON strength_sets(exercise_log_id);