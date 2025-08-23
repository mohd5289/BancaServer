package com.zinolynn.banca.domain.services;

import com.zinolynn.banca.domain.dtos.SetPinRequest;
import com.zinolynn.banca.domain.dtos.UserLoginRequest;
import com.zinolynn.banca.domain.dtos.UserRegisterRequest;
import com.zinolynn.banca.domain.entities.KycStatus;

import com.zinolynn.banca.domain.entities.User;
import com.zinolynn.banca.domain.repositories.UserRepository;
import com.zinolynn.banca.domain.security.CustomUserDetailsService;
import com.zinolynn.banca.domain.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;


    public User register(UserRegisterRequest req) {
        // Check if email exists
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already taken");
        }

        User user = User.builder()
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .country(req.getCountry())
                .phoneNumber(req.getPhoneNumber())
                .verificationToken(UUID.randomUUID().toString())
                .tokenExpiry(LocalDateTime.now().plusSeconds(60))
                .referralCode(req.getReferralCode())
                .kycStatus(KycStatus.PENDING)
                .hasSetPin(false)
                .build();


        try {
            emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
        } catch (Exception e) {
            // rollback if email sending is critical
            throw new RuntimeException("Failed to send verification email. Please try again.", e);
        }

        return userRepository.save(user);

    }


    public ResponseEntity<?> login(@RequestBody UserLoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEmailVerified()) {
            throw new RuntimeException("Email not verified");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(Map.of("token", token,"userId", user.getId()));
    }


    public void setPin(SetPinRequest req) {
        User user = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isHasSetPin()) {
            throw new RuntimeException("PIN already set");
        }

        user.setPin(passwordEncoder.encode(req.getPin()));
        user.setHasSetPin(true);
        userRepository.save(user);
        System.out.println("PIN has been set successfully for userId: " + user.getId());
    }
}
