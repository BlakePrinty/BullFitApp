package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BodyWeightLogRequest {

    @NotNull
    @Min(1)
    private Double weight;

    @Pattern(regexp = "KG|LBS", message = "Unit must be KG or LBS")
    private String unit;

    private LocalDateTime loggedAt;

    @Size(max = 500)
    private String notes;
}
