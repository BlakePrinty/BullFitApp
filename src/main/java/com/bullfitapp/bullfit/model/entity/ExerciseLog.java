package com.bullfitapp.bullfit.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercise_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @Column(length = 500)
    private String notes;

    @Builder.Default
    @OneToMany(mappedBy = "exerciseLog", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("setNumber ASC")
    private List<StrengthSet> sets = new ArrayList<>();

    @OneToOne(mappedBy = "exerciseLog", cascade = CascadeType.ALL, orphanRemoval = true)
    private CardioLog cardioLog;
}