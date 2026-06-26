package com.bullfitapp.bullfit.repository;

import com.bullfitapp.bullfit.model.entity.Exercise;
import com.bullfitapp.bullfit.model.entity.ExerciseLog;
import com.bullfitapp.bullfit.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Long> {
    @Query("SELECT el FROM ExerciseLog el " +
            "WHERE el.workoutSession.user = :user " +
            "AND el.exercise = :exercise")
    List<ExerciseLog> findByUserAndExercise(
            @Param("user") User user,
            @Param("exercise") Exercise exercise);
}
