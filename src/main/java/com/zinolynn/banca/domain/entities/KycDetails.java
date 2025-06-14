package com.zinolynn.banca.domain.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "kyc_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class KycDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String documentType; // e.g., "passport", "driver_license"

    @Column(nullable = false)
    private String documentNumber;

    @Column(nullable = false)
    private String idImageUrl; // or store as byte[] if you prefer

    @Column
    private String address;

    @Column
    private String nationality;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
