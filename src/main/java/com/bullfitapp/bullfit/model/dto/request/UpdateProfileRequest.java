package com.bullfitapp.bullfit.model.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Min(13) @Max(110)
    private Integer age;

    @Pattern(regexp = "KG|Lbs", message = "Weight unit must be KG or LBS")
    private String weightUnit;
}
