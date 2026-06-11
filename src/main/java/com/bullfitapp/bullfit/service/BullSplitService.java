package com.bullfitapp.bullfit.service;

import com.bullfitapp.bullfit.exception.ResourceNotFoundException;
import com.bullfitapp.bullfit.exception.UnauthorizedException;
import com.bullfitapp.bullfit.model.dto.request.CreateBullSplitRequest;
import com.bullfitapp.bullfit.model.dto.request.SplitDayExerciseRequest;
import com.bullfitapp.bullfit.model.dto.request.SplitDayRequest;
import com.bullfitapp.bullfit.model.dto.response.BullSplitResponse;
import com.bullfitapp.bullfit.model.dto.response.BullSplitSummaryResponse;
import com.bullfitapp.bullfit.model.dto.response.SplitDayExerciseResponse;
import com.bullfitapp.bullfit.model.dto.response.SplitDayResponse;
import com.bullfitapp.bullfit.model.entity.*;
import com.bullfitapp.bullfit.model.enums.SplitCategory;
import com.bullfitapp.bullfit.repository.BullSplitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BullSplitService {

    private final BullSplitRepository bullSplitRepository;
    private final ExerciseService exerciseService;
    private final UserService userService;

    public BullSplitResponse create(CreateBullSplitRequest request, String email) {
        User user = userService.findByEmailOrThrow(email);

        BullSplit split = BullSplit.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .tags(request.getTags())
                .createdBy(user)
                .build();

        buildDays(split, request);

        return toResponse(bullSplitRepository.save(split));
    }

    @Transactional(readOnly = true)
    public List<BullSplitSummaryResponse> getMySplits(String email) {
        User user = userService.findByEmailOrThrow(email);
        return bullSplitRepository.findByCreatedByOrderByCreatedAtDesc(user)
                .stream().map(this::toSummary).toList();
    }

    @Transactional(readOnly = true)
    public List<BullSplitSummaryResponse> getSavedSplits(String email) {
        User user = userService.findByEmailOrThrow(email);
        return bullSplitRepository.findSavedByUserId(user.getId())
                .stream().map(this::toSummary).toList();
    }

    @Transactional(readOnly = true)
    public BullSplitResponse getById(Long id) {
        return toResponse(findByIdOrThrow(id));
    }

    @Transactional(readOnly = true)
    public Page<BullSplitSummaryResponse> explore(SplitCategory category, Pageable pageable) {
        return bullSplitRepository.findExplore(category, pageable).map(this::toSummary);
    }

    public BullSplitResponse update(Long id, CreateBullSplitRequest request, String email) {
        BullSplit split = findByIdOrThrow(id);
        assertOwner(split, email);

        split.setName(request.getName());
        split.setDescription(request.getDescription());
        split.setCategory(request.getCategory());
        split.setTags(request.getTags());

        // Clearing the list triggers orphanRemoval — Hibernate deletes all old days + exercises.
        split.getDays().clear();
        buildDays(split, request);

        return toResponse(bullSplitRepository.save(split));
    }

    public void delete(Long id, String email) {
        BullSplit split = findByIdOrThrow(id);
        assertOwner(split, email);
        bullSplitRepository.delete(split);
    }

    public BullSplitResponse publish(Long id, String email) {
        BullSplit split = findByIdOrThrow(id);
        assertOwner(split, email);
        split.setPublished(true);
        split.setPublishedAt(LocalDateTime.now());
        return toResponse(bullSplitRepository.save(split));
    }

    public BullSplitResponse unpublish(Long id, String email) {
        BullSplit split = findByIdOrThrow(id);
        assertOwner(split, email);
        split.setPublished(false);
        split.setPublishedAt(null);
        return toResponse(bullSplitRepository.save(split));
    }

    public void saveSplit(Long splitId, String email) {
        BullSplit split = findByIdOrThrow(splitId);
        User user = userService.findByEmailOrThrow(email);

        if (!split.isPublished()) {
            throw new ResourceNotFoundException("Split not found");
        }
        if (bullSplitRepository.countSavedByUser(splitId, user.getId()) == 0) {
            split.getSavedByUsers().add(user);
            split.setSaveCount(split.getSaveCount() + 1);
            bullSplitRepository.save(split);
        }
    }

    public void unsaveSplit(Long splitId, String email) {
        BullSplit split = findByIdOrThrow(splitId);
        User user = userService.findByEmailOrThrow(email);

        int removed = bullSplitRepository.removeSavedByUser(splitId, user.getId());
        if (removed > 0) {
            split.setSaveCount(Math.max(0, split.getSaveCount() - 1));
            bullSplitRepository.save(split);
        }
    }

    // Shared — WorkoutSession will reference SplitDay later.
    public BullSplit findByIdOrThrow(Long id) {
        return bullSplitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Split not found: " + id));
    }

    // ── Private helpers ──────────────────────────────────────────────────

    private void assertOwner(BullSplit split, String email) {
        User user = userService.findByEmailOrThrow(email);
        if (!split.getCreatedBy().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not own this split");
        }
    }

    private void buildDays(BullSplit split, CreateBullSplitRequest request) {
        if (request.getDays() == null) return;
        for (SplitDayRequest dayReq : request.getDays()) {
            SplitDay day = SplitDay.builder()
                    .dayNumber(dayReq.getDayNumber())
                    .name(dayReq.getName())
                    .bullSplit(split)
                    .build();

            if (dayReq.getExercises() != null) {
                for (SplitDayExerciseRequest exReq : dayReq.getExercises()) {
                    Exercise exercise = exerciseService.findByIdOrThrow(exReq.getExerciseId());
                    SplitDayExercise sde = SplitDayExercise.builder()
                            .orderIndex(exReq.getOrderIndex())
                            .targetSets(exReq.getTargetSets())
                            .targetReps(exReq.getTargetReps())
                            .targetRepsMax(exReq.getTargetRepsMax())
                            .notes(exReq.getNotes())
                            .splitDay(day)
                            .exercise(exercise)
                            .build();
                    day.getExercises().add(sde);
                }
            }
            split.getDays().add(day);
        }
    }

    private BullSplitResponse toResponse(BullSplit split) {
        return BullSplitResponse.builder()
                .id(split.getId())
                .name(split.getName())
                .description(split.getDescription())
                .category(split.getCategory().name())
                .tags(split.getTags())
                .published(split.isPublished())
                .publishedAt(split.getPublishedAt())
                .saveCount(split.getSaveCount())
                .createdBy(split.getCreatedBy().getUsername())
                .createdAt(split.getCreatedAt())
                .days(split.getDays().stream().map(this::toDayResponse).toList())
                .build();
    }

    private BullSplitSummaryResponse toSummary(BullSplit split) {
        return BullSplitSummaryResponse.builder()
                .id(split.getId())
                .name(split.getName())
                .description(split.getDescription())
                .category(split.getCategory().name())
                .tags(split.getTags())
                .published(split.isPublished())
                .saveCount(split.getSaveCount())
                .dayCount(split.getDays().size())
                .createdBy(split.getCreatedBy().getUsername())
                .createdAt(split.getCreatedAt())
                .build();
    }

    private SplitDayResponse toDayResponse(SplitDay day) {
        return SplitDayResponse.builder()
                .id(day.getId())
                .dayNumber(day.getDayNumber())
                .name(day.getName())
                .exercises(day.getExercises().stream().map(this::toSdeResponse).toList())
                .build();
    }

    private SplitDayExerciseResponse toSdeResponse(SplitDayExercise sde) {
        return SplitDayExerciseResponse.builder()
                .id(sde.getId())
                .orderIndex(sde.getOrderIndex())
                .exerciseId(sde.getExercise().getId())
                .exerciseName(sde.getExercise().getName())
                .exerciseType(sde.getExercise().getType().name())
                .muscleGroup(sde.getExercise().getMuscleGroup().name())
                .targetSets(sde.getTargetSets())
                .targetReps(sde.getTargetReps())
                .targetRepsMax(sde.getTargetRepsMax())
                .notes(sde.getNotes())
                .build();
    }
}