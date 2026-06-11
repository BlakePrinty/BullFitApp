package com.bullfitapp.bullfit.controller;

import com.bullfitapp.bullfit.exception.UnauthorizedException;
import com.bullfitapp.bullfit.model.dto.request.CreateBullSplitRequest;
import com.bullfitapp.bullfit.model.dto.response.BullSplitResponse;
import com.bullfitapp.bullfit.model.dto.response.BullSplitSummaryResponse;
import com.bullfitapp.bullfit.model.enums.SplitCategory;
import com.bullfitapp.bullfit.service.BullSplitService;
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

import java.util.List;

@RestController
@RequestMapping("/api/splits")
@RequiredArgsConstructor
public class BullSplitController {

    private final BullSplitService bullSplitService;

    private String currentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new UnauthorizedException("Not authenticated");
        }
        return auth.getName();
    }

    @PostMapping
    public ResponseEntity<BullSplitResponse> create(
            @Valid @RequestBody CreateBullSplitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bullSplitService.create(request, currentEmail()));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<BullSplitSummaryResponse>> getMySplits() {
        return ResponseEntity.ok(bullSplitService.getMySplits(currentEmail()));
    }

    @GetMapping("/saved")
    public ResponseEntity<List<BullSplitSummaryResponse>> getSaved() {
        return ResponseEntity.ok(bullSplitService.getSavedSplits(currentEmail()));
    }

    @GetMapping("/explore")
    public ResponseEntity<Page<BullSplitSummaryResponse>> explore(
            @RequestParam(required = false) SplitCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("saveCount").descending());
        return ResponseEntity.ok(bullSplitService.explore(category, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BullSplitResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bullSplitService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BullSplitResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateBullSplitRequest request) {
        return ResponseEntity.ok(bullSplitService.update(id, request, currentEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bullSplitService.delete(id, currentEmail());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<BullSplitResponse> publish(@PathVariable Long id) {
        return ResponseEntity.ok(bullSplitService.publish(id, currentEmail()));
    }

    @PostMapping("/{id}/unpublish")
    public ResponseEntity<BullSplitResponse> unpublish(@PathVariable Long id) {
        return ResponseEntity.ok(bullSplitService.unpublish(id, currentEmail()));
    }

    @PostMapping("/{id}/save")
    public ResponseEntity<Void> saveSplit(@PathVariable Long id) {
        bullSplitService.saveSplit(id, currentEmail());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/save")
    public ResponseEntity<Void> unsaveSplit(@PathVariable Long id) {
        bullSplitService.unsaveSplit(id, currentEmail());
        return ResponseEntity.noContent().build();
    }

}
