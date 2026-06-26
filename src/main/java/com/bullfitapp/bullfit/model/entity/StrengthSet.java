package com.bullfitapp.bullfit.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "strength_sets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrengthSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_log_id", nullable = false)
    private ExerciseLog exerciseLog;

    @Column(name = "set_number", nullable = false)
    private int setNumber;   // auto-assigned 1, 2, 3... in service

    @Column(nullable = false)
    private int reps;

    @Column(nullable = false)
    private double weight;

    @Column(name = "weight_unit", nullable = false, length = 3)
    private String weightUnit;

    @Builder.Default
    @Column(name = "is_warmup", nullable = false)
    private boolean warmup = false;
}