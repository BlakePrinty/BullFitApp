package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StrengthSetRequest {
    @NotNull
    @Min(1)
    private Integer reps;

    @NotNull
    @Min(0)
    private Double weight;

    private boolean warmup;
}
