package com.bullfitapp.bullfit.controller;

import com.bullfitapp.bullfit.model.dto.request.CreateExerciseRequest;
import com.bullfitapp.bullfit.model.dto.response.ExerciseResponse;
import com.bullfitapp.bullfit.model.entity.User;
import com.bullfitapp.bullfit.model.enums.ExerciseType;
import com.bullfitapp.bullfit.model.enums.MuscleGroup;
import com.bullfitapp.bullfit.service.ExerciseService;
import com.bullfitapp.bullfit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final UserService userService;

    private String currentEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @GetMapping
    public ResponseEntity<Page<ExerciseResponse>> browse (
            @RequestParam(required = false) ExerciseType type,
            @RequestParam(required = false) MuscleGroup muscleGroup,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return ResponseEntity.ok(exerciseService.browse(type, muscleGroup, name, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(exerciseService.getById(id));
    }

    @GetMapping("/my")
    public ResponseEntity<List<ExerciseResponse>> getMine() {
        User user = userService.findByEmailOrThrow(currentEmail());
        return ResponseEntity.ok(exerciseService.getMyCustomExercises(user.getId()));
    }

    @PostMapping("/custom")
    public ResponseEntity<ExerciseResponse> createCustom(
            @Valid @RequestBody CreateExerciseRequest request) {
        User user = userService.findByEmailOrThrow(currentEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exerciseService.createCustom(request, user.getId()));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExerciseResponse> createGlobal (
            @Valid @RequestBody CreateExerciseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(exerciseService.createGlobal(request));
    }
}
