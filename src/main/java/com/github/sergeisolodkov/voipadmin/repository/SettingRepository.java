package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.Setting;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Setting entity.
 *
 * When extending this class, extend SettingRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface SettingRepository extends SettingRepositoryWithBagRelationships, JpaRepository<Setting, Long> {
    default Optional<Setting> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Setting> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Setting> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select setting from Setting setting left join fetch setting.option",
        countQuery = "select count(setting) from Setting setting"
    )
    Page<Setting> findAllWithToOneRelationships(Pageable pageable);

    @Query("select setting from Setting setting left join fetch setting.option")
    List<Setting> findAllWithToOneRelationships();

    @Query("select setting from Setting setting left join fetch setting.option where setting.id =:id")
    Optional<Setting> findOneWithToOneRelationships(@Param("id") Long id);
}
