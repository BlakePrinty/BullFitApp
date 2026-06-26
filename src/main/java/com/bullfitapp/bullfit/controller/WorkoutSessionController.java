package com.bullfitapp.bullfit.controller;

import com.bullfitapp.bullfit.exception.UnauthorizedException;
import com.bullfitapp.bullfit.model.dto.request.WorkoutSessionRequest;
import com.bullfitapp.bullfit.model.dto.response.WorkoutSessionResponse;
import com.bullfitapp.bullfit.model.dto.response.WorkoutSessionSummaryResponse;
import com.bullfitapp.bullfit.service.WorkoutSessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;

    private String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new UnauthorizedException("Not authenticated");
        return auth.getName();
    }

    @PostMapping
    public ResponseEntity<WorkoutSessionResponse> create(
            @Valid @RequestBody WorkoutSessionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(workoutSessionService.create(request, currentEmail()));
    }

    @GetMapping
    public ResponseEntity<Page<WorkoutSessionSummaryResponse>> getMySessions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("date").descending());
        return ResponseEntity.ok(workoutSessionService.getMySessions(currentEmail(), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSessionResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(workoutSessionService.getById(id, currentEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workoutSessionService.delete(id, currentEmail());
        return ResponseEntity.noContent().build();
    }
}