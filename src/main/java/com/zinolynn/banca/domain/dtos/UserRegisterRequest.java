package com.zinolynn.banca.domain.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String country;
    private String phoneNumber;
//    private boolean hasSetPin = false;
    private String referralCode; // optional

}
