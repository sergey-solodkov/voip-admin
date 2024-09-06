package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.VoipAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the VoipAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VoipAccountRepository extends JpaRepository<VoipAccount, Long> {}
