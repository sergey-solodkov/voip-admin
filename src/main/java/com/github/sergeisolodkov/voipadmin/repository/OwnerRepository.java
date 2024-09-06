package com.github.sergeisolodkov.voipadmin.repository;

import com.github.sergeisolodkov.voipadmin.domain.Owner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Owner entity.
 */
@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {
    default Optional<Owner> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Owner> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Owner> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select owner from Owner owner left join fetch owner.department", countQuery = "select count(owner) from Owner owner")
    Page<Owner> findAllWithToOneRelationships(Pageable pageable);

    @Query("select owner from Owner owner left join fetch owner.department")
    List<Owner> findAllWithToOneRelationships();

    @Query("select owner from Owner owner left join fetch owner.department where owner.id =:id")
    Optional<Owner> findOneWithToOneRelationships(@Param("id") Long id);
}
