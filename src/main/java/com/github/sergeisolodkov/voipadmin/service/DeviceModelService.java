package com.github.sergeisolodkov.voipadmin.service;

import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.sergeisolodkov.voipadmin.domain.DeviceModel}.
 */
public interface DeviceModelService {
    /**
     * Save a deviceModel.
     *
     * @param deviceModelDTO the entity to save.
     * @return the persisted entity.
     */
    DeviceModelDTO save(DeviceModelDTO deviceModelDTO);

    /**
     * Updates a deviceModel.
     *
     * @param deviceModelDTO the entity to update.
     * @return the persisted entity.
     */
    DeviceModelDTO update(DeviceModelDTO deviceModelDTO);

    /**
     * Partially updates a deviceModel.
     *
     * @param deviceModelDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DeviceModelDTO> partialUpdate(DeviceModelDTO deviceModelDTO);

    /**
     * Get all the deviceModels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceModelDTO> findAll(Pageable pageable);

    /**
     * Get all the deviceModels with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DeviceModelDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" deviceModel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DeviceModelDTO> findOne(Long id);

    /**
     * Delete the "id" deviceModel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get config template from storage.
     * @param id device model ID.
     * @return {@link Resource}
     */
    Resource getConfigTemplate(Long id);

    /**
     * Get firmware file from storage.
     * @param id device model ID.
     * @return {@link Resource}
     */
    Resource getFirmwareFile(Long id);
}
