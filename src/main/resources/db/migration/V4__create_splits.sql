CREATE TABLE bull_splits (
                             id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name                VARCHAR(255) NOT NULL,
                             description         VARCHAR(1000),
                             category            VARCHAR(20) NOT NULL,
                             tags                VARCHAR(500),
                             is_published        TINYINT(1) NOT NULL DEFAULT 0,
                             published_at        TIMESTAMP NULL,
                             save_count          INT NOT NULL DEFAULT 0,
                             created_by_user_id  BIGINT NOT NULL,
                             created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_split_user FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE split_days (
                            id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                            day_number      INT NOT NULL,
                            name            VARCHAR(255) NOT NULL,
                            bull_split_id   BIGINT NOT NULL,
                            CONSTRAINT fk_day_split FOREIGN KEY (bull_split_id) REFERENCES bull_splits(id) ON DELETE CASCADE
);

CREATE TABLE split_day_exercises (
                                     id              BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     order_index     INT NOT NULL,
                                     target_sets     INT,
                                     target_reps     INT,
                                     target_reps_max INT,
                                     notes           VARCHAR(500),
                                     split_day_id    BIGINT NOT NULL,
                                     exercise_id     BIGINT NOT NULL,
                                     CONSTRAINT fk_sde_day      FOREIGN KEY (split_day_id) REFERENCES split_days(id) ON DELETE CASCADE,
                                     CONSTRAINT fk_sde_exercise FOREIGN KEY (exercise_id)  REFERENCES exercises(id) ON DELETE CASCADE
);

CREATE TABLE user_saved_splits (
                                   user_id     BIGINT NOT NULL,
                                   split_id    BIGINT NOT NULL,
                                   saved_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   PRIMARY KEY (user_id, split_id),
                                   CONSTRAINT fk_uss_user  FOREIGN KEY (user_id)  REFERENCES users(id)       ON DELETE CASCADE,
                                   CONSTRAINT fk_uss_split FOREIGN KEY (split_id) REFERENCES bull_splits(id) ON DELETE CASCADE
);

CREATE INDEX idx_splits_published ON bull_splits(is_published);
CREATE INDEX idx_splits_category  ON bull_splits(category);
CREATE INDEX idx_splits_creator   ON bull_splits(created_by_user_id);