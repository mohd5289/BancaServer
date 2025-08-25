package com.zinolynn.banca.domain.dtos;


import com.zinolynn.banca.domain.entities.KycStatus;
import com.zinolynn.banca.domain.entities.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String country;
    private String phoneNumber;
    private boolean hasSetPin;
    private boolean emailVerified;
    private KycStatus kycStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .country(user.getCountry())
                .phoneNumber(user.getPhoneNumber())
                .hasSetPin(user.isHasSetPin())
                .emailVerified(user.isEmailVerified())
                .kycStatus(user.getKycStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }





}
