package com.github.sergeisolodkov.voipadmin.service;

import com.github.sergeisolodkov.voipadmin.service.dto.OptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.sergeisolodkov.voipadmin.domain.Option}.
 */
public interface OptionService {
    /**
     * Save a option.
     *
     * @param optionDTO the entity to save.
     * @return the persisted entity.
     */
    OptionDTO save(OptionDTO optionDTO);

    /**
     * Updates a option.
     *
     * @param optionDTO the entity to update.
     * @return the persisted entity.
     */
    OptionDTO update(OptionDTO optionDTO);

    /**
     * Partially updates a option.
     *
     * @param optionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OptionDTO> partialUpdate(OptionDTO optionDTO);

    /**
     * Get all the options with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OptionDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" option.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OptionDTO> findOne(Long id);

    /**
     * Delete the "id" option.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
