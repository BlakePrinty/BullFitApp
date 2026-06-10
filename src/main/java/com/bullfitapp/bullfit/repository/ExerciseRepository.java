package com.bullfitapp.bullfit.repository;

import com.bullfitapp.bullfit.model.entity.Exercise;
import com.bullfitapp.bullfit.model.enums.ExerciseType;
import com.bullfitapp.bullfit.model.enums.MuscleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    @Query("SELECT e FROM Exercise e WHERE e.custom = false " +
            "AND (:type IS NULL OR e.type = :type) " +
            "AND (:muscleGroup IS NULL OR e.muscleGroup = :muscleGroup) " +
            "AND (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))")
    Page<Exercise> findGlobalExercises(
            @Param("type") ExerciseType type,
            @Param("muscleGroup") MuscleGroup muscleGroup,
            @Param("name") String name,
            Pageable pageable);

    List<Exercise> findByCreatedByUserIdAndCustomTrue(Long userId);
}
