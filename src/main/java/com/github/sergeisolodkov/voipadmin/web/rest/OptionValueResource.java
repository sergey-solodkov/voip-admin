package com.github.sergeisolodkov.voipadmin.web.rest;

import com.github.sergeisolodkov.voipadmin.repository.OptionValueRepository;
import com.github.sergeisolodkov.voipadmin.service.OptionValueService;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionValueDTO;
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
 * REST controller for managing {@link com.github.sergeisolodkov.voipadmin.domain.OptionValue}.
 */
@RestController
@RequestMapping("/api/option-values")
public class OptionValueResource {

    private static final Logger LOG = LoggerFactory.getLogger(OptionValueResource.class);

    private static final String ENTITY_NAME = "optionValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OptionValueService optionValueService;

    private final OptionValueRepository optionValueRepository;

    public OptionValueResource(OptionValueService optionValueService, OptionValueRepository optionValueRepository) {
        this.optionValueService = optionValueService;
        this.optionValueRepository = optionValueRepository;
    }

    /**
     * {@code POST  /option-values} : Create a new optionValue.
     *
     * @param optionValueDTO the optionValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new optionValueDTO, or with status {@code 400 (Bad Request)} if the optionValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OptionValueDTO> createOptionValue(@RequestBody OptionValueDTO optionValueDTO) throws URISyntaxException {
        LOG.debug("REST request to save OptionValue : {}", optionValueDTO);
        if (optionValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new optionValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        optionValueDTO = optionValueService.save(optionValueDTO);
        return ResponseEntity.created(new URI("/api/option-values/" + optionValueDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, optionValueDTO.getId().toString()))
            .body(optionValueDTO);
    }

    /**
     * {@code PUT  /option-values/:id} : Updates an existing optionValue.
     *
     * @param id the id of the optionValueDTO to save.
     * @param optionValueDTO the optionValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated optionValueDTO,
     * or with status {@code 400 (Bad Request)} if the optionValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the optionValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OptionValueDTO> updateOptionValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OptionValueDTO optionValueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OptionValue : {}, {}", id, optionValueDTO);
        if (optionValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, optionValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        optionValueDTO = optionValueService.update(optionValueDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, optionValueDTO.getId().toString()))
            .body(optionValueDTO);
    }

    /**
     * {@code PATCH  /option-values/:id} : Partial updates given fields of an existing optionValue, field will ignore if it is null
     *
     * @param id the id of the optionValueDTO to save.
     * @param optionValueDTO the optionValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated optionValueDTO,
     * or with status {@code 400 (Bad Request)} if the optionValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the optionValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the optionValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OptionValueDTO> partialUpdateOptionValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OptionValueDTO optionValueDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OptionValue partially : {}, {}", id, optionValueDTO);
        if (optionValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, optionValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!optionValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OptionValueDTO> result = optionValueService.partialUpdate(optionValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, optionValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /option-values} : get all the optionValues.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of optionValues in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OptionValueDTO>> getAllOptionValues(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of OptionValues");
        Page<OptionValueDTO> page = optionValueService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /option-values/:id} : get the "id" optionValue.
     *
     * @param id the id of the optionValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the optionValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OptionValueDTO> getOptionValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OptionValue : {}", id);
        Optional<OptionValueDTO> optionValueDTO = optionValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(optionValueDTO);
    }

    /**
     * {@code DELETE  /option-values/:id} : delete the "id" optionValue.
     *
     * @param id the id of the optionValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOptionValue(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OptionValue : {}", id);
        optionValueService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
