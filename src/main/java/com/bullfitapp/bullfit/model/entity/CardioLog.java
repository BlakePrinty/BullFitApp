package com.bullfitapp.bullfit.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cardio_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardioLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_log_id", nullable = false)
    private ExerciseLog exerciseLog;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    private Double distance;

    @Column(name = "distance_unit", length = 2)
    private String distanceUnit;   // "KM" or "MI"

    @Column(name = "avg_heart_rate")
    private Integer avgHeartRate;

    @Column(length = 500)
    private String notes;
}