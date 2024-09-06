package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.OptionValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OptionValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OptionValueRepository extends JpaRepository<OptionValue, Long> {}
