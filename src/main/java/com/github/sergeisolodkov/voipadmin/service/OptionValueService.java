package com.github.sergeisolodkov.voipadmin.service;

import com.github.sergeisolodkov.voipadmin.service.dto.OptionValueDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.sergeisolodkov.voipadmin.domain.OptionValue}.
 */
public interface OptionValueService {
    /**
     * Save a optionValue.
     *
     * @param optionValueDTO the entity to save.
     * @return the persisted entity.
     */
    OptionValueDTO save(OptionValueDTO optionValueDTO);

    /**
     * Updates a optionValue.
     *
     * @param optionValueDTO the entity to update.
     * @return the persisted entity.
     */
    OptionValueDTO update(OptionValueDTO optionValueDTO);

    /**
     * Partially updates a optionValue.
     *
     * @param optionValueDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OptionValueDTO> partialUpdate(OptionValueDTO optionValueDTO);

    /**
     * Get all the optionValues.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OptionValueDTO> findAll(Pageable pageable);

    /**
     * Get the "id" optionValue.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OptionValueDTO> findOne(Long id);

    /**
     * Delete the "id" optionValue.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
