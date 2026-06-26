package com.bullfitapp.bullfit.service;

import com.bullfitapp.bullfit.exception.ResourceNotFoundException;
import com.bullfitapp.bullfit.exception.UnauthorizedException;
import com.bullfitapp.bullfit.model.dto.request.CardioLogRequest;
import com.bullfitapp.bullfit.model.dto.request.ExerciseLogRequest;
import com.bullfitapp.bullfit.model.dto.request.StrengthSetRequest;
import com.bullfitapp.bullfit.model.dto.request.WorkoutSessionRequest;
import com.bullfitapp.bullfit.model.dto.response.*;
import com.bullfitapp.bullfit.model.entity.*;
import com.bullfitapp.bullfit.model.enums.ExerciseType;
import com.bullfitapp.bullfit.repository.SplitDayRepository;
import com.bullfitapp.bullfit.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final SplitDayRepository splitDayRepository;
    private final ExerciseService exerciseService;
    private final UserService userService;

    public WorkoutSessionResponse create(WorkoutSessionRequest request, String email) {
        User user = userService.findByEmailOrThrow(email);

        WorkoutSession session = WorkoutSession.builder()
                .user(user)
                .date(request.getDate())
                .durationMinutes(request.getDurationMinutes())
                .notes(request.getNotes())
                .build();

        if (request.getSplitDayId() != null) {
            SplitDay splitDay = splitDayRepository.findById(request.getSplitDayId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "SplitDay not found: " + request.getSplitDayId()));
            session.setSplitDay(splitDay);
        }

        for (ExerciseLogRequest exReq : request.getExercises()) {
            Exercise exercise = exerciseService.findByIdOrThrow(exReq.getExerciseId());

            ExerciseLog log = ExerciseLog.builder()
                    .workoutSession(session)
                    .exercise(exercise)
                    .orderIndex(exReq.getOrderIndex())
                    .notes(exReq.getNotes())
                    .build();

            if (exercise.getType() == ExerciseType.STRENGTH && exReq.getSets() != null) {
                for (int i = 0; i < exReq.getSets().size(); i++) {
                    StrengthSetRequest setReq = exReq.getSets().get(i);
                    StrengthSet set = StrengthSet.builder()
                            .exerciseLog(log)
                            .setNumber(i + 1)
                            .reps(setReq.getReps())
                            .weight(setReq.getWeight())
                            .weightUnit(user.getWeightUnit())
                            .warmup(setReq.isWarmup())
                            .build();
                    log.getSets().add(set);
                }
            }

            if (exercise.getType() == ExerciseType.CARDIO && exReq.getCardio() != null) {
                CardioLogRequest cardioReq = exReq.getCardio();
                CardioLog cardio = CardioLog.builder()
                        .exerciseLog(log)
                        .durationMinutes(cardioReq.getDurationMinutes())
                        .distance(cardioReq.getDistance())
                        .distanceUnit(cardioReq.getDistanceUnit())
                        .avgHeartRate(cardioReq.getAvgHeartRate())
                        .notes(cardioReq.getNotes())
                        .build();
                log.setCardioLog(cardio);
            }

            session.getExerciseLogs().add(log);
        }

        WorkoutSession saved = workoutSessionRepository.save(session);

        updateStreak(user);

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<WorkoutSessionSummaryResponse> getMySessions(String email, Pageable pageable) {
        User user = userService.findByEmailOrThrow(email);
        return workoutSessionRepository.findByUserOrderByDateDesc(user, pageable)
                .map(this::toSummary);
    }

    @Transactional(readOnly = true)
    public WorkoutSessionResponse getById(Long id, String email) {
        WorkoutSession session = findByIdOrThrow(id);
        User user = userService.findByEmailOrThrow(email);
        if (!session.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not own this session");
        }
        return toResponse(session);
    }

    public void delete(Long id, String email) {
        WorkoutSession session = findByIdOrThrow(id);
        User user = userService.findByEmailOrThrow(email);
        if (!session.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not own this session");
        }
        workoutSessionRepository.delete(session);
    }

    public WorkoutSession findByIdOrThrow(Long id) {
        return workoutSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found: " + id));
    }

    // ── Private helpers ──────────────────────────────────────────────────

    private void updateStreak(User user) {
        LocalDate today = LocalDate.now();
        LocalDate last = user.getLastWorkoutDate();

        if (last == null || last.isBefore(today.minusDays(1))) {
            user.setStreakCount(1);
        } else if (last.equals(today.minusDays(1))) {
            user.setStreakCount(user.getStreakCount() + 1);
        }

        user.setLastWorkoutDate(today);
    }

    private WorkoutSessionResponse toResponse(WorkoutSession session) {
        String splitDayName = session.getSplitDay() != null
                ? session.getSplitDay().getName() : null;
        String splitName = session.getSplitDay() != null
                ? session.getSplitDay().getBullSplit().getName() : null;

        return WorkoutSessionResponse.builder()
                .id(session.getId())
                .date(session.getDate())
                .durationMinutes(session.getDurationMinutes())
                .splitDayId(session.getSplitDay() != null ? session.getSplitDay().getId() : null)
                .splitDayName(splitDayName)
                .splitName(splitName)
                .notes(session.getNotes())
                .createdAt(session.getCreatedAt())
                .exercises(session.getExerciseLogs().stream().map(this::toLogResponse).toList())
                .build();
    }

    private WorkoutSessionSummaryResponse toSummary(WorkoutSession session) {
        return WorkoutSessionSummaryResponse.builder()
                .id(session.getId())
                .date(session.getDate())
                .durationMinutes(session.getDurationMinutes())
                .splitDayName(session.getSplitDay() != null
                        ? session.getSplitDay().getName() : null)
                .splitName(session.getSplitDay() != null
                        ? session.getSplitDay().getBullSplit().getName() : null)
                .exerciseCount(session.getExerciseLogs().size())
                .createdAt(session.getCreatedAt())
                .build();
    }

    private ExerciseLogResponse toLogResponse(ExerciseLog log) {
        return ExerciseLogResponse.builder()
                .id(log.getId())
                .exerciseId(log.getExercise().getId())
                .exerciseName(log.getExercise().getName())
                .exerciseType(log.getExercise().getType().name())
                .muscleGroup(log.getExercise().getMuscleGroup().name())
                .orderIndex(log.getOrderIndex())
                .notes(log.getNotes())
                .totalSets(log.getSets().size())
                .sets(log.getSets().stream().map(this::toSetResponse).toList())
                .cardio(log.getCardioLog() != null ? toCardioResponse(log.getCardioLog()) : null)
                .build();
    }

    private StrengthSetResponse toSetResponse(StrengthSet set) {
        return StrengthSetResponse.builder()
                .id(set.getId())
                .setNumber(set.getSetNumber())
                .reps(set.getReps())
                .weight(set.getWeight())
                .weightUnit(set.getWeightUnit())
                .warmup(set.isWarmup())
                .build();
    }

    private CardioLogResponse toCardioResponse(CardioLog cardio) {
        return CardioLogResponse.builder()
                .id(cardio.getId())
                .durationMinutes(cardio.getDurationMinutes())
                .distance(cardio.getDistance())
                .distanceUnit(cardio.getDistanceUnit())
                .avgHeartRate(cardio.getAvgHeartRate())
                .notes(cardio.getNotes())
                .build();
    }
}