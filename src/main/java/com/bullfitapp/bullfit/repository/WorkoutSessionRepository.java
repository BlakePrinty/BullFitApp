package com.bullfitapp.bullfit.repository;


import com.bullfitapp.bullfit.model.entity.User;
import com.bullfitapp.bullfit.model.entity.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    Page<WorkoutSession> findByUserOrderByDateDesc(User user, Pageable pageable);
}