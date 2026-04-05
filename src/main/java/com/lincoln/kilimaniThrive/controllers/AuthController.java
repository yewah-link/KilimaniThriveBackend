package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.mappers.UserMapper;
import com.lincoln.kilimaniThrive.models.dtos.auth.AuthResponse;
import com.lincoln.kilimaniThrive.models.dtos.auth.LoginRequest;
import com.lincoln.kilimaniThrive.models.dtos.auth.RegisterRequest;
import com.lincoln.kilimaniThrive.models.entity.User;
import com.lincoln.kilimaniThrive.models.entity.RoleEntity;
import com.lincoln.kilimaniThrive.repositories.RoleEntityRepository;
import com.lincoln.kilimaniThrive.repositories.UserRepository;
import com.lincoln.kilimaniThrive.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleEntityRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<GenericResponseV2<AuthResponse>> login(@RequestBody LoginRequest loginRequest) {
        try {
            log.info("Attempting login for email: {}", loginRequest.getEmail());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            log.info("Authentication successful for email: {}", loginRequest.getEmail());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String token = jwtTokenProvider.generateToken(loginRequest.getEmail());
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User was authenticated but not found in repository"));

            log.info("JWT token generated and user data fetched for email: {}", loginRequest.getEmail());

            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .user(userMapper.userToUserDto(user))
                    .build();

            return ResponseEntity.ok(GenericResponseV2.<AuthResponse>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Login successful")
                    ._embedded(authResponse)
                    .build());
        } catch (Exception e) {
            log.error("Login failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(GenericResponseV2.<AuthResponse>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Invalid email or password")
                    ._embedded(null)
                    .build());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponseV2<AuthResponse>> register(@RequestBody RegisterRequest registerRequest) {
        try {
            Optional<User> existingUser = userRepository.findByEmail(registerRequest.getEmail());
            if (existingUser.isPresent()) {
                return ResponseEntity.badRequest().body(GenericResponseV2.<AuthResponse>builder()
                        .status(ResponseStatusEnum.ERROR)
                        .message("Email is already taken!")
                        ._embedded(null)
                        .build());
            }

            // Fetch default role
            RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(RoleEntity.builder().name("ROLE_USER").build()));

            // Create new user's account
            User user = User.builder()
                    .firstName(registerRequest.getFirstName())
                    .lastName(registerRequest.getLastName())
                    .email(registerRequest.getEmail())
                    .phoneNumber(registerRequest.getPhoneNumber())
                    .password(passwordEncoder.encode(registerRequest.getPassword()))
                    .active(true)
                    .roles(new java.util.HashSet<>(java.util.List.of(userRole)))
                    .build();

            userRepository.save(user);

            // Generate Token
            String token = jwtTokenProvider.generateToken(user.getEmail());

            AuthResponse authResponse = AuthResponse.builder()
                    .token(token)
                    .user(userMapper.userToUserDto(user))
                    .build();

            return ResponseEntity.ok(GenericResponseV2.<AuthResponse>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("User registered successfully")
                    ._embedded(authResponse)
                    .build());
        } catch (Exception e) {
            log.error("Registration fatal error for email {}: {}", registerRequest.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().body(GenericResponseV2.<AuthResponse>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Unable to register user: " + e.getMessage())
                    ._embedded(null)
                    .build());
        }
    }
}
