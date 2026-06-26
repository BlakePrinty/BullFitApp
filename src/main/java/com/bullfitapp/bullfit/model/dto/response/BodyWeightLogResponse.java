package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BodyWeightLogResponse {
    private Long id;
    private double weight;
    private String unit;
    private LocalDateTime loggedAt;
    private String notes;
    private LocalDateTime createdAt;
}
