package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ExerciseLogRequest {
    @NotNull(message = "Exercise ID is required")
    private Long exerciseId;

    @NotNull
    @Min(0)
    private Integer orderIndex;

    @Size(max = 500)
    private String notes;

    @Valid
    private List<StrengthSetRequest> sets = new ArrayList<>();

    @Valid
    private CardioLogRequest cardio;
}
