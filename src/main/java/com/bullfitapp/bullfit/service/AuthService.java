package com.bullfitapp.bullfit.service;

import com.bullfitapp.bullfit.exception.DuplicateResourceException;
import com.bullfitapp.bullfit.model.dto.request.LoginRequest;
import com.bullfitapp.bullfit.model.dto.request.RegisterRequest;
import com.bullfitapp.bullfit.model.dto.response.AuthResponse;
import com.bullfitapp.bullfit.model.dto.response.UserResponse;
import com.bullfitapp.bullfit.model.entity.User;
import com.bullfitapp.bullfit.model.enums.Role;
import com.bullfitapp.bullfit.repository.UserRepository;
import com.bullfitapp.bullfit.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username unavailable");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .age(request.getAge())
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(token)
                .user(toUserResponse(saved))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found after auth"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(token)
                .user(toUserResponse(user))
                .build();
    }

    private UserResponse toUserResponse(User user) {
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
