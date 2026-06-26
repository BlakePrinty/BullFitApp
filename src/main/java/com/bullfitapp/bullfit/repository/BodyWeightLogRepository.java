package com.bullfitapp.bullfit.repository;

import com.bullfitapp.bullfit.model.entity.BodyWeightLog;
import com.bullfitapp.bullfit.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyWeightLogRepository extends JpaRepository<BodyWeightLog, Long> {
    Page<BodyWeightLog> findByUserOrderByLoggedAtDesc(User user, Pageable pageable);

    BodyWeightLog findFirstByUserOrderByLoggedAtDesc(User user);
}