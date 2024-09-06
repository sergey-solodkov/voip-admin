package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.OtherDeviceType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the OtherDeviceType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtherDeviceTypeRepository extends JpaRepository<OtherDeviceType, Long> {}
