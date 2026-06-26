package com.bullfitapp.bullfit.service;

import com.bullfitapp.bullfit.exception.ResourceNotFoundException;
import com.bullfitapp.bullfit.exception.UnauthorizedException;
import com.bullfitapp.bullfit.model.dto.request.BodyWeightLogRequest;
import com.bullfitapp.bullfit.model.dto.response.BodyWeightLogResponse;
import com.bullfitapp.bullfit.model.entity.BodyWeightLog;
import com.bullfitapp.bullfit.model.entity.User;
import com.bullfitapp.bullfit.repository.BodyWeightLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class BodyWeightLogService {

    private final BodyWeightLogRepository bodyWeightLogRepository;
    private final UserService userService;

    public BodyWeightLogResponse create(BodyWeightLogRequest request, String email) {
        User user = userService.findByEmailOrThrow(email);

        BodyWeightLog log = BodyWeightLog.builder()
                .user(user)
                .weight(request.getWeight())
                .unit(request.getUnit() != null ? request.getUnit() : user.getWeightUnit())
                .loggedAt(request.getLoggedAt() != null ? request.getLoggedAt() : LocalDateTime.now())
                .notes(request.getNotes())
                .build();

        return toResponse(bodyWeightLogRepository.save(log));
    }

    @Transactional(readOnly = true)
    public Page<BodyWeightLogResponse> getHistory(String email, Pageable pageable) {
        User user = userService.findByEmailOrThrow(email);
        return bodyWeightLogRepository.findByUserOrderByLoggedAtDesc(user, pageable)
                .map(this::toResponse);
    }

    public void delete(Long id, String email) {
        BodyWeightLog log = findByIdOrThrow(id);
        User user = userService.findByEmailOrThrow(email);
        if (!log.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not own this entry");
        }
        bodyWeightLogRepository.delete(log);
    }

    public BodyWeightLog findByIdOrThrow(Long id) {
        return bodyWeightLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Body weight entry not found: " + id));
    }

    private BodyWeightLogResponse toResponse(BodyWeightLog log) {
        return BodyWeightLogResponse.builder()
                .id(log.getId())
                .weight(log.getWeight())
                .unit(log.getUnit())
                .loggedAt(log.getLoggedAt())
                .notes(log.getNotes())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
