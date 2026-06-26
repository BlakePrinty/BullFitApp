package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSessionResponse {
    private Long id;
    private LocalDate date;
    private Integer durationMinutes;
    private Long splitDayId;
    private String splitDayName;
    private String splitName;
    private String notes;
    private LocalDateTime createdAt;
    private List<ExerciseLogResponse> exercises;
}
