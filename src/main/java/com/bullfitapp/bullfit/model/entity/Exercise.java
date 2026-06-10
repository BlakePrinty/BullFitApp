package com.bullfitapp.bullfit.model.entity;

import com.bullfitapp.bullfit.model.enums.Equipment;
import com.bullfitapp.bullfit.model.enums.ExerciseType;
import com.bullfitapp.bullfit.model.enums.MuscleGroup;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExerciseType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "muscle_group", nullable = false)
    private MuscleGroup muscleGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "equipment")
    private Equipment equipment;

    @Builder.Default
    @Column(name = "is_custom", nullable = false)
    private boolean custom = false;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;
}
