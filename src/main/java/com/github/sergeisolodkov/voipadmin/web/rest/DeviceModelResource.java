package com.github.sergeisolodkov.voipadmin.web.rest;

import com.github.sergeisolodkov.voipadmin.repository.DeviceModelRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceModelService;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import com.github.sergeisolodkov.voipadmin.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.github.sergeisolodkov.voipadmin.domain.DeviceModel}.
 */
@RestController
@RequestMapping("/api/device-models")
public class DeviceModelResource {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceModelResource.class);

    private static final String ENTITY_NAME = "deviceModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceModelService deviceModelService;

    private final DeviceModelRepository deviceModelRepository;

    public DeviceModelResource(DeviceModelService deviceModelService, DeviceModelRepository deviceModelRepository) {
        this.deviceModelService = deviceModelService;
        this.deviceModelRepository = deviceModelRepository;
    }

    /**
     * {@code POST  /device-models} : Create a new deviceModel.
     *
     * @param deviceModelDTO the deviceModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceModelDTO, or with status {@code 400 (Bad Request)} if the deviceModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DeviceModelDTO> createDeviceModel(@Valid @RequestBody DeviceModelDTO deviceModelDTO) throws URISyntaxException {
        LOG.debug("REST request to save DeviceModel : {}", deviceModelDTO);
        if (deviceModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new deviceModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        deviceModelDTO = deviceModelService.save(deviceModelDTO);
        return ResponseEntity.created(new URI("/api/device-models/" + deviceModelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, deviceModelDTO.getId().toString()))
            .body(deviceModelDTO);
    }

    /**
     * {@code PUT  /device-models/:id} : Updates an existing deviceModel.
     *
     * @param id the id of the deviceModelDTO to save.
     * @param deviceModelDTO the deviceModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceModelDTO,
     * or with status {@code 400 (Bad Request)} if the deviceModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DeviceModelDTO> updateDeviceModel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeviceModelDTO deviceModelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DeviceModel : {}, {}", id, deviceModelDTO);
        if (deviceModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        deviceModelDTO = deviceModelService.update(deviceModelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceModelDTO.getId().toString()))
            .body(deviceModelDTO);
    }

    /**
     * {@code PATCH  /device-models/:id} : Partial updates given fields of an existing deviceModel, field will ignore if it is null
     *
     * @param id the id of the deviceModelDTO to save.
     * @param deviceModelDTO the deviceModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceModelDTO,
     * or with status {@code 400 (Bad Request)} if the deviceModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deviceModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deviceModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeviceModelDTO> partialUpdateDeviceModel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeviceModelDTO deviceModelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DeviceModel partially : {}, {}", id, deviceModelDTO);
        if (deviceModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deviceModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deviceModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeviceModelDTO> result = deviceModelService.partialUpdate(deviceModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /device-models} : get all the deviceModels.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceModels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DeviceModelDTO>> getAllDeviceModels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of DeviceModels");
        Page<DeviceModelDTO> page;
        if (eagerload) {
            page = deviceModelService.findAllWithEagerRelationships(pageable);
        } else {
            page = deviceModelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /device-models/:id} : get the "id" deviceModel.
     *
     * @param id the id of the deviceModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeviceModelDTO> getDeviceModel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DeviceModel : {}", id);
        Optional<DeviceModelDTO> deviceModelDTO = deviceModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deviceModelDTO);
    }

    /**
     * {@code DELETE  /device-models/:id} : delete the "id" deviceModel.
     *
     * @param id the id of the deviceModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeviceModel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DeviceModel : {}", id);
        deviceModelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * Download config template.
     * @param id device model ID.
     * @return {@link Resource}
     */
    @GetMapping("/{id}/config-template")
    public Resource getConfigTemplate(@PathVariable("id") Long id) {
        LOG.debug("REST request to download config template file for DeviceModel : {}", id);
        return deviceModelService.getConfigTemplate(id);
    }

    /**
     * Download firmware file.
     * @param id device model ID.
     * @return {@link Resource}
     */
    @GetMapping("/{id}/firmware-file")
    public Resource getFirmwareFile(@PathVariable("id") Long id) {
        LOG.debug("REST request to download firmware file for DeviceModel : {}", id);
        return deviceModelService.getFirmwareFile(id);
    }
}
