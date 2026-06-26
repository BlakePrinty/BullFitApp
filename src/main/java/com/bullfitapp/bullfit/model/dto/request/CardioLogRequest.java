package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardioLogRequest {
    @NotNull
    @Min(1)
    private Integer durationMinutes;

    @Min(0)
    private Double distance;

    @Pattern(regexp = "KM|MI", message = "Distance unit must be KM or MI")
    private String distanceUnit;

    @Min(0)
    private Integer avgHeartRate;

    @Size(max = 500)
    private String notes;
}
