package com.bullfitapp.bullfit.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "split_day_exercises")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SplitDayExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    @Column(name = "target_sets")
    private Integer targetSets;

    @Column(name = "target_reps")
    private Integer targetReps;         // minimum reps, or exact if no max

    @Column(name = "target_reps_max")
    private Integer targetRepsMax;      // if set, forms a range (e.g. 8–12)

    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "split_day_id", nullable = false)
    private SplitDay splitDay;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;
}
