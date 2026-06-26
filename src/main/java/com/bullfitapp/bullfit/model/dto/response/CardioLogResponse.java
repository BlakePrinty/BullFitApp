package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardioLogResponse {
    private Long id;
    private int durationMinutes;
    private Double distance;
    private String distanceUnit;
    private Integer avgHeartRate;
    private String notes;
}
