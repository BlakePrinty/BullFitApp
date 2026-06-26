package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseLogResponse {
    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private String exerciseType;
    private String muscleGroup;
    private int orderIndex;
    private String notes;
    private int totalSets;
    private List<StrengthSetResponse> sets;
    private CardioLogResponse cardio;
}
