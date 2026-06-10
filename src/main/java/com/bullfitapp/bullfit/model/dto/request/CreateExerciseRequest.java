package com.bullfitapp.bullfit.model.dto.request;

import com.bullfitapp.bullfit.model.enums.Equipment;
import com.bullfitapp.bullfit.model.enums.ExerciseType;
import com.bullfitapp.bullfit.model.enums.MuscleGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateExerciseRequest {

    @NotBlank(message = "Exercise name is required")
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Exercise type is required")
    private ExerciseType type;

    @NotNull(message = "Muscle group is required")
    private MuscleGroup muscleGroup;

    @NotNull(message = "Exercise equipment is required")
    private Equipment equipment;
}
