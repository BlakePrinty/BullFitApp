package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class WorkoutSessionRequest {
    private Long splitDayId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private Integer durationMinutes;

    @Size(max = 500)
    private String notes;

    @Valid
    @NotEmpty(message = "A session must have at least one exercise")
    private List<ExerciseLogRequest> exercises;
}
