CREATE TABLE body_weight_logs (
                                  id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  user_id     BIGINT NOT NULL,
                                  weight      DOUBLE NOT NULL,
                                  unit        VARCHAR(3) NOT NULL DEFAULT 'KG',
                                  logged_at   TIMESTAMP NOT NULL,
                                  notes       VARCHAR(500),
                                  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_bwl_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_bwl_user      ON body_weight_logs(user_id);
CREATE INDEX idx_bwl_logged_at ON body_weight_logs(logged_at);