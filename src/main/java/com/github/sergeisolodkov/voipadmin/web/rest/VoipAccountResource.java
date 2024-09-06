package com.github.sergeisolodkov.voipadmin.web.rest;

import com.github.sergeisolodkov.voipadmin.repository.VoipAccountRepository;
import com.github.sergeisolodkov.voipadmin.service.VoipAccountService;
import com.github.sergeisolodkov.voipadmin.service.dto.VoipAccountDTO;
import com.github.sergeisolodkov.voipadmin.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.github.sergeisolodkov.voipadmin.domain.VoipAccount}.
 */
@RestController
@RequestMapping("/api/voip-accounts")
public class VoipAccountResource {

    private static final Logger LOG = LoggerFactory.getLogger(VoipAccountResource.class);

    private static final String ENTITY_NAME = "voipAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VoipAccountService voipAccountService;

    private final VoipAccountRepository voipAccountRepository;

    public VoipAccountResource(VoipAccountService voipAccountService, VoipAccountRepository voipAccountRepository) {
        this.voipAccountService = voipAccountService;
        this.voipAccountRepository = voipAccountRepository;
    }

    /**
     * {@code POST  /voip-accounts} : Create a new voipAccount.
     *
     * @param voipAccountDTO the voipAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voipAccountDTO, or with status {@code 400 (Bad Request)} if the voipAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<VoipAccountDTO> createVoipAccount(@RequestBody VoipAccountDTO voipAccountDTO) throws URISyntaxException {
        LOG.debug("REST request to save VoipAccount : {}", voipAccountDTO);
        if (voipAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new voipAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        voipAccountDTO = voipAccountService.save(voipAccountDTO);
        return ResponseEntity.created(new URI("/api/voip-accounts/" + voipAccountDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, voipAccountDTO.getId().toString()))
            .body(voipAccountDTO);
    }

    /**
     * {@code PUT  /voip-accounts/:id} : Updates an existing voipAccount.
     *
     * @param id the id of the voipAccountDTO to save.
     * @param voipAccountDTO the voipAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voipAccountDTO,
     * or with status {@code 400 (Bad Request)} if the voipAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voipAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<VoipAccountDTO> updateVoipAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoipAccountDTO voipAccountDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update VoipAccount : {}, {}", id, voipAccountDTO);
        if (voipAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voipAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voipAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        voipAccountDTO = voipAccountService.update(voipAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voipAccountDTO.getId().toString()))
            .body(voipAccountDTO);
    }

    /**
     * {@code PATCH  /voip-accounts/:id} : Partial updates given fields of an existing voipAccount, field will ignore if it is null
     *
     * @param id the id of the voipAccountDTO to save.
     * @param voipAccountDTO the voipAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voipAccountDTO,
     * or with status {@code 400 (Bad Request)} if the voipAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the voipAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the voipAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<VoipAccountDTO> partialUpdateVoipAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody VoipAccountDTO voipAccountDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update VoipAccount partially : {}, {}", id, voipAccountDTO);
        if (voipAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voipAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voipAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<VoipAccountDTO> result = voipAccountService.partialUpdate(voipAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, voipAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /voip-accounts} : get all the voipAccounts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voipAccounts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<VoipAccountDTO>> getAllVoipAccounts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of VoipAccounts");
        Page<VoipAccountDTO> page = voipAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /voip-accounts/:id} : get the "id" voipAccount.
     *
     * @param id the id of the voipAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voipAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<VoipAccountDTO> getVoipAccount(@PathVariable("id") Long id) {
        LOG.debug("REST request to get VoipAccount : {}", id);
        Optional<VoipAccountDTO> voipAccountDTO = voipAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(voipAccountDTO);
    }

    /**
     * {@code DELETE  /voip-accounts/:id} : delete the "id" voipAccount.
     *
     * @param id the id of the voipAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVoipAccount(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete VoipAccount : {}", id);
        voipAccountService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
