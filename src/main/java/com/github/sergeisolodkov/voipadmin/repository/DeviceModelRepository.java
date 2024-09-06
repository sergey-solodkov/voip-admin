package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DeviceModel entity.
 *
 * When extending this class, extend DeviceModelRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DeviceModelRepository extends DeviceModelRepositoryWithBagRelationships, JpaRepository<DeviceModel, Long> {
    default Optional<DeviceModel> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<DeviceModel> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<DeviceModel> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select deviceModel from DeviceModel deviceModel left join fetch deviceModel.vendor",
        countQuery = "select count(deviceModel) from DeviceModel deviceModel"
    )
    Page<DeviceModel> findAllWithToOneRelationships(Pageable pageable);

    @Query("select deviceModel from DeviceModel deviceModel left join fetch deviceModel.vendor")
    List<DeviceModel> findAllWithToOneRelationships();

    @Query("select deviceModel from DeviceModel deviceModel left join fetch deviceModel.vendor where deviceModel.id =:id")
    Optional<DeviceModel> findOneWithToOneRelationships(@Param("id") Long id);
}
