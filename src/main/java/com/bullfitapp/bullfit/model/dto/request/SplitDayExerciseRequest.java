package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SplitDayExerciseRequest {

    @NotNull(message = "Exercise ID is required")
    private Long exerciseId;

    @NotNull
    @Min(0)
    private Integer orderIndex;

    @Min(1)
    private Integer targetSets;

    @Min(1)
    private Integer targetReps;

    @Min(1)
    private Integer targetRepsMax;

    @Size(max = 500)
    private String notes;
}
