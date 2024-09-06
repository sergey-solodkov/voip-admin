package com.github.sergeisolodkov.voipadmin.service;

import com.github.sergeisolodkov.voipadmin.service.dto.VoipAccountDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.github.sergeisolodkov.voipadmin.domain.VoipAccount}.
 */
public interface VoipAccountService {
    /**
     * Save a voipAccount.
     *
     * @param voipAccountDTO the entity to save.
     * @return the persisted entity.
     */
    VoipAccountDTO save(VoipAccountDTO voipAccountDTO);

    /**
     * Updates a voipAccount.
     *
     * @param voipAccountDTO the entity to update.
     * @return the persisted entity.
     */
    VoipAccountDTO update(VoipAccountDTO voipAccountDTO);

    /**
     * Partially updates a voipAccount.
     *
     * @param voipAccountDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<VoipAccountDTO> partialUpdate(VoipAccountDTO voipAccountDTO);

    /**
     * Get all the voipAccounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VoipAccountDTO> findAll(Pageable pageable);

    /**
     * Get the "id" voipAccount.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VoipAccountDTO> findOne(Long id);

    /**
     * Delete the "id" voipAccount.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
