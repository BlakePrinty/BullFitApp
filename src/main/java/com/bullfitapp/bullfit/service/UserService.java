package com.bullfitapp.bullfit.service;

import com.bullfitapp.bullfit.exception.ResourceNotFoundException;
import com.bullfitapp.bullfit.model.dto.request.UpdateProfileRequest;
import com.bullfitapp.bullfit.model.dto.response.UserResponse;
import com.bullfitapp.bullfit.model.entity.User;
import com.bullfitapp.bullfit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse getByEmail(String email) {
        return toResponse(findByEmailOrThrow(email));
    }

    public UserResponse getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
        return toResponse(user);
    }

    public UserResponse update(String email, UpdateProfileRequest request) {
        User user = findByEmailOrThrow(email);

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName()  != null) user.setLastName(request.getLastName());
        if (request.getAge()       != null) user.setAge(request.getAge());
        if (request.getWeightUnit()!= null) user.setWeightUnit(request.getWeightUnit());

        return toResponse(userRepository.save(user));
    }

    public User findByEmailOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .age(user.getAge())
                .weightUnit(user.getWeightUnit())
                .streakCount(user.getStreakCount())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
