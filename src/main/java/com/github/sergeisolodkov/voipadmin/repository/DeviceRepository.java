package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.Device;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Device entity.
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    default Optional<Device> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Device> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Device> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select device from Device device left join fetch device.model left join fetch device.owner",
        countQuery = "select count(device) from Device device"
    )
    Page<Device> findAllWithToOneRelationships(Pageable pageable);

    @Query("select device from Device device left join fetch device.model left join fetch device.owner")
    List<Device> findAllWithToOneRelationships();

    @Query("select device from Device device left join fetch device.model left join fetch device.owner where device.id =:id")
    Optional<Device> findOneWithToOneRelationships(@Param("id") Long id);
}
