package com.zinolynn.banca.domain.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        String subject = "Verify your email";
        String verifyUrl = "https://bancaserver-2.onrender.com/verify-email?token=" + token;
        String body = """
                <p>Welcome!</p>
                <p>Please verify your email by clicking the link below:</p>
                <a href="%s">Verify Email</a>
                <p>This link expires in 24 hours.</p>
                """.formatted(verifyUrl);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true); // true = isHtml

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
}