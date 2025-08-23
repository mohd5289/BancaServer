package com.zinolynn.banca.domain.controllers;


import com.zinolynn.banca.domain.dtos.ApiResponse;
import com.zinolynn.banca.domain.dtos.SetPinRequest;
import com.zinolynn.banca.domain.dtos.UserLoginRequest;
import com.zinolynn.banca.domain.dtos.UserRegisterRequest;
import com.zinolynn.banca.domain.entities.User;
import com.zinolynn.banca.domain.repositories.UserRepository;
import com.zinolynn.banca.domain.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<?>> register(@RequestBody UserRegisterRequest request) {
        Object registeredUser = userService.register(request);

        ApiResponse<Object> response = new ApiResponse<>(
                true,
                "Registration successful. Please verify your email.",
                registeredUser
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Map<String, String>>> login(@RequestBody UserLoginRequest request) {
        ResponseEntity<?> tokenResponse = userService.login(request);

        @SuppressWarnings("unchecked")
        Map<String, String> tokenData = (Map<String, String>) tokenResponse.getBody();

        ApiResponse<Map<String, String>> response = new ApiResponse<>(
                true,
                "Login successful",
                tokenData
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        Optional<User> userOpt = userRepository.findByVerificationToken(token);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        User user = userOpt.get();
        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token expired");
        }

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);

        return ResponseEntity.ok("Email verified successfully.");
    }
    @PostMapping("/set-pin")
    public ResponseEntity<?> setPin(@RequestBody SetPinRequest request) {
        System.out.println("DEBUG: setPin called with request: " + request);
        userService.setPin(request);
         return ResponseEntity.ok(new ApiResponse<>(
                true,
                "PIN set successfully",
                null
        ));
    }



}
