package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrengthSetResponse {
    private Long id;
    private int setNumber;
    private int reps;
    private double weight;
    private String weightUnit;
    private boolean warmup;
}
