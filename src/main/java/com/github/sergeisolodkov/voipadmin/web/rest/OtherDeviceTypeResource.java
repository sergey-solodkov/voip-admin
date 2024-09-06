package com.github.sergeisolodkov.voipadmin.web.rest;

import com.github.sergeisolodkov.voipadmin.repository.OtherDeviceTypeRepository;
import com.github.sergeisolodkov.voipadmin.service.OtherDeviceTypeService;
import com.github.sergeisolodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
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
 * REST controller for managing {@link com.github.sergeisolodkov.voipadmin.domain.OtherDeviceType}.
 */
@RestController
@RequestMapping("/api/other-device-types")
public class OtherDeviceTypeResource {

    private static final Logger LOG = LoggerFactory.getLogger(OtherDeviceTypeResource.class);

    private static final String ENTITY_NAME = "otherDeviceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OtherDeviceTypeService otherDeviceTypeService;

    private final OtherDeviceTypeRepository otherDeviceTypeRepository;

    public OtherDeviceTypeResource(OtherDeviceTypeService otherDeviceTypeService, OtherDeviceTypeRepository otherDeviceTypeRepository) {
        this.otherDeviceTypeService = otherDeviceTypeService;
        this.otherDeviceTypeRepository = otherDeviceTypeRepository;
    }

    /**
     * {@code POST  /other-device-types} : Create a new otherDeviceType.
     *
     * @param otherDeviceTypeDTO the otherDeviceTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new otherDeviceTypeDTO, or with status {@code 400 (Bad Request)} if the otherDeviceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OtherDeviceTypeDTO> createOtherDeviceType(@RequestBody OtherDeviceTypeDTO otherDeviceTypeDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save OtherDeviceType : {}", otherDeviceTypeDTO);
        if (otherDeviceTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new otherDeviceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        otherDeviceTypeDTO = otherDeviceTypeService.save(otherDeviceTypeDTO);
        return ResponseEntity.created(new URI("/api/other-device-types/" + otherDeviceTypeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, otherDeviceTypeDTO.getId().toString()))
            .body(otherDeviceTypeDTO);
    }

    /**
     * {@code PUT  /other-device-types/:id} : Updates an existing otherDeviceType.
     *
     * @param id the id of the otherDeviceTypeDTO to save.
     * @param otherDeviceTypeDTO the otherDeviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otherDeviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the otherDeviceTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the otherDeviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OtherDeviceTypeDTO> updateOtherDeviceType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OtherDeviceTypeDTO otherDeviceTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OtherDeviceType : {}, {}", id, otherDeviceTypeDTO);
        if (otherDeviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otherDeviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otherDeviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        otherDeviceTypeDTO = otherDeviceTypeService.update(otherDeviceTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otherDeviceTypeDTO.getId().toString()))
            .body(otherDeviceTypeDTO);
    }

    /**
     * {@code PATCH  /other-device-types/:id} : Partial updates given fields of an existing otherDeviceType, field will ignore if it is null
     *
     * @param id the id of the otherDeviceTypeDTO to save.
     * @param otherDeviceTypeDTO the otherDeviceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated otherDeviceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the otherDeviceTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the otherDeviceTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the otherDeviceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OtherDeviceTypeDTO> partialUpdateOtherDeviceType(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody OtherDeviceTypeDTO otherDeviceTypeDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OtherDeviceType partially : {}, {}", id, otherDeviceTypeDTO);
        if (otherDeviceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, otherDeviceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!otherDeviceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OtherDeviceTypeDTO> result = otherDeviceTypeService.partialUpdate(otherDeviceTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, otherDeviceTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /other-device-types} : get all the otherDeviceTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of otherDeviceTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OtherDeviceTypeDTO>> getAllOtherDeviceTypes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of OtherDeviceTypes");
        Page<OtherDeviceTypeDTO> page = otherDeviceTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /other-device-types/:id} : get the "id" otherDeviceType.
     *
     * @param id the id of the otherDeviceTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the otherDeviceTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OtherDeviceTypeDTO> getOtherDeviceType(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OtherDeviceType : {}", id);
        Optional<OtherDeviceTypeDTO> otherDeviceTypeDTO = otherDeviceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(otherDeviceTypeDTO);
    }

    /**
     * {@code DELETE  /other-device-types/:id} : delete the "id" otherDeviceType.
     *
     * @param id the id of the otherDeviceTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOtherDeviceType(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OtherDeviceType : {}", id);
        otherDeviceTypeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
