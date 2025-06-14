package com.zinolynn.banca.domain.services;

public interface EmailService {
    void sendVerificationEmail(String toEmail, String token);
}
