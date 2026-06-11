package com.bullfitapp.bullfit.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SplitDayExerciseResponse {
    private Long id;
    private int orderIndex;
    private Long exerciseId;
    private String exerciseName;
    private String exerciseType;
    private String muscleGroup;
    private Integer targetSets;
    private Integer targetReps;
    private Integer targetRepsMax;      // null = exact reps, set = range
    private Double targetWeight;
    private String notes;
}
