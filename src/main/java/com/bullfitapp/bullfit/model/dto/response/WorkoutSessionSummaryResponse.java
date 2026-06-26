package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

// Lightweight — for list views. Full detail only returned from GET /api/sessions/{id}.
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSessionSummaryResponse {
    private Long id;
    private LocalDate date;
    private Integer durationMinutes;
    private String splitDayName;   // null for freestyle sessions
    private String splitName;
    private int exerciseCount;
    private LocalDateTime createdAt;
}
