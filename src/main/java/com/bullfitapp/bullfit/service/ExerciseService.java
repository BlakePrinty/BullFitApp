package com.bullfitapp.bullfit.service;

import com.bullfitapp.bullfit.exception.ResourceNotFoundException;
import com.bullfitapp.bullfit.model.dto.request.CreateExerciseRequest;
import com.bullfitapp.bullfit.model.dto.response.ExerciseResponse;
import com.bullfitapp.bullfit.model.entity.Exercise;
import com.bullfitapp.bullfit.model.enums.ExerciseType;
import com.bullfitapp.bullfit.model.enums.MuscleGroup;
import com.bullfitapp.bullfit.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;

    public Page<ExerciseResponse> browse(
            ExerciseType type, MuscleGroup muscleGroup, String name, Pageable pageable) {
        return exerciseRepository
                .findGlobalExercises(type, muscleGroup, name, pageable)
                .map(this::toResponse);
    }

    public ExerciseResponse getById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    public ExerciseResponse createCustom(CreateExerciseRequest request, Long userId) {
        Exercise exercise = Exercise.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .muscleGroup(request.getMuscleGroup())
                .equipment(request.getEquipment())
                .custom(true)
                .createdByUserId(userId)
                .build();
        return toResponse(exerciseRepository.save(exercise));
    }

    public ExerciseResponse createGlobal(CreateExerciseRequest request) {
        Exercise exercise = Exercise.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType())
                .muscleGroup(request.getMuscleGroup())
                .equipment(request.getEquipment())
                .build();
        return toResponse(exerciseRepository.save(exercise));
    }

    public List<ExerciseResponse> getMyCustomExercises(Long userId) {
        return exerciseRepository.findByCreatedByUserIdAndCustomTrue(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Exercise findByIdOrThrow(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found :" + id));
    }

    public ExerciseResponse toResponse(Exercise exercise) {
        return ExerciseResponse.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                .type(exercise.getType().name())
                .muscleGroup(exercise.getMuscleGroup().name())
                .equipment(exercise.getEquipment() != null ? exercise.getEquipment().name() : null)
                .custom(exercise.isCustom())
                .build();
    }
}
