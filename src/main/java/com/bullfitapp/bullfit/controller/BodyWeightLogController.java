package com.bullfitapp.bullfit.controller;

import com.bullfitapp.bullfit.exception.UnauthorizedException;
import com.bullfitapp.bullfit.model.dto.request.BodyWeightLogRequest;
import com.bullfitapp.bullfit.model.dto.response.BodyWeightLogResponse;
import com.bullfitapp.bullfit.service.BodyWeightLogService;
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
@RequestMapping("/api/bodyweight")
@RequiredArgsConstructor
public class BodyWeightLogController {

    private final BodyWeightLogService bodyWeightLogService;

    private String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) throw new UnauthorizedException("Not authenticated");
        return auth.getName();
    }

    @PostMapping
    public ResponseEntity<BodyWeightLogResponse> create(
            @Valid @RequestBody BodyWeightLogRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bodyWeightLogService.create(request, currentEmail()));
    }

    @GetMapping
    public ResponseEntity<Page<BodyWeightLogResponse>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("loggedAt").descending());
        return ResponseEntity.ok(bodyWeightLogService.getHistory(currentEmail(), pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bodyWeightLogService.delete(id, currentEmail());
        return ResponseEntity.noContent().build();
    }
}
