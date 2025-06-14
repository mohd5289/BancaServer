package com.zinolynn.banca.domain.repositories;

import com.zinolynn.banca.domain.entities.KycDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface KYCDetailsRepository extends JpaRepository<KycDetails, UUID> {

}
