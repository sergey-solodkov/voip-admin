package com.github.sergeisolodkov.voipadmin.service;

import com.github.sergeisolodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.sergeisolodkov.voipadmin.domain.OtherDeviceType}.
 */
public interface OtherDeviceTypeService {
    /**
     * Save a otherDeviceType.
     *
     * @param otherDeviceTypeDTO the entity to save.
     * @return the persisted entity.
     */
    OtherDeviceTypeDTO save(OtherDeviceTypeDTO otherDeviceTypeDTO);

    /**
     * Updates a otherDeviceType.
     *
     * @param otherDeviceTypeDTO the entity to update.
     * @return the persisted entity.
     */
    OtherDeviceTypeDTO update(OtherDeviceTypeDTO otherDeviceTypeDTO);

    /**
     * Partially updates a otherDeviceType.
     *
     * @param otherDeviceTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<OtherDeviceTypeDTO> partialUpdate(OtherDeviceTypeDTO otherDeviceTypeDTO);

    /**
     * Get all the otherDeviceTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OtherDeviceTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" otherDeviceType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OtherDeviceTypeDTO> findOne(Long id);

    /**
     * Delete the "id" otherDeviceType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
