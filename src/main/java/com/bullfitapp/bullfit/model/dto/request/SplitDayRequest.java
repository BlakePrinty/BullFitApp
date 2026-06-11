package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SplitDayRequest {

    @NotNull
    @Min(1)
    private Integer dayNumber;

    @NotBlank(message = "Day name is required")
    private String name;

    @Valid
    private List<SplitDayExerciseRequest> exercises = new ArrayList<>();
}
