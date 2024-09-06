package com.github.sergeisolodkov.voipadmin.web.rest;

import static com.github.sergeisolodkov.voipadmin.domain.OtherDeviceTypeAsserts.*;
import static com.github.sergeisolodkov.voipadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sergeisolodkov.voipadmin.IntegrationTest;
import com.github.sergeisolodkov.voipadmin.domain.OtherDeviceType;
import com.github.sergeisolodkov.voipadmin.repository.OtherDeviceTypeRepository;
import com.github.sergeisolodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.OtherDeviceTypeMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OtherDeviceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OtherDeviceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/other-device-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OtherDeviceTypeRepository otherDeviceTypeRepository;

    @Autowired
    private OtherDeviceTypeMapper otherDeviceTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOtherDeviceTypeMockMvc;

    private OtherDeviceType otherDeviceType;

    private OtherDeviceType insertedOtherDeviceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtherDeviceType createEntity() {
        return new OtherDeviceType().name(DEFAULT_NAME).description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OtherDeviceType createUpdatedEntity() {
        return new OtherDeviceType().name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        otherDeviceType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOtherDeviceType != null) {
            otherDeviceTypeRepository.delete(insertedOtherDeviceType);
            insertedOtherDeviceType = null;
        }
    }

    @Test
    @Transactional
    void createOtherDeviceType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);
        var returnedOtherDeviceTypeDTO = om.readValue(
            restOtherDeviceTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otherDeviceTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OtherDeviceTypeDTO.class
        );

        // Validate the OtherDeviceType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOtherDeviceType = otherDeviceTypeMapper.toEntity(returnedOtherDeviceTypeDTO);
        assertOtherDeviceTypeUpdatableFieldsEquals(returnedOtherDeviceType, getPersistedOtherDeviceType(returnedOtherDeviceType));

        insertedOtherDeviceType = returnedOtherDeviceType;
    }

    @Test
    @Transactional
    void createOtherDeviceTypeWithExistingId() throws Exception {
        // Create the OtherDeviceType with an existing ID
        otherDeviceType.setId(1L);
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOtherDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otherDeviceTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOtherDeviceTypes() throws Exception {
        // Initialize the database
        insertedOtherDeviceType = otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        // Get all the otherDeviceTypeList
        restOtherDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(otherDeviceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getOtherDeviceType() throws Exception {
        // Initialize the database
        insertedOtherDeviceType = otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        // Get the otherDeviceType
        restOtherDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, otherDeviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(otherDeviceType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingOtherDeviceType() throws Exception {
        // Get the otherDeviceType
        restOtherDeviceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOtherDeviceType() throws Exception {
        // Initialize the database
        insertedOtherDeviceType = otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otherDeviceType
        OtherDeviceType updatedOtherDeviceType = otherDeviceTypeRepository.findById(otherDeviceType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOtherDeviceType are not directly saved in db
        em.detach(updatedOtherDeviceType);
        updatedOtherDeviceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(updatedOtherDeviceType);

        restOtherDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otherDeviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the OtherDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOtherDeviceTypeToMatchAllProperties(updatedOtherDeviceType);
    }

    @Test
    @Transactional
    void putNonExistingOtherDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherDeviceType.setId(longCount.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, otherDeviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOtherDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherDeviceType.setId(longCount.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOtherDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherDeviceType.setId(longCount.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(otherDeviceTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtherDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOtherDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedOtherDeviceType = otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otherDeviceType using partial update
        OtherDeviceType partialUpdatedOtherDeviceType = new OtherDeviceType();
        partialUpdatedOtherDeviceType.setId(otherDeviceType.getId());

        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtherDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOtherDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the OtherDeviceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOtherDeviceTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOtherDeviceType, otherDeviceType),
            getPersistedOtherDeviceType(otherDeviceType)
        );
    }

    @Test
    @Transactional
    void fullUpdateOtherDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedOtherDeviceType = otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the otherDeviceType using partial update
        OtherDeviceType partialUpdatedOtherDeviceType = new OtherDeviceType();
        partialUpdatedOtherDeviceType.setId(otherDeviceType.getId());

        partialUpdatedOtherDeviceType.name(UPDATED_NAME).description(UPDATED_DESCRIPTION);

        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOtherDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOtherDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the OtherDeviceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOtherDeviceTypeUpdatableFieldsEquals(
            partialUpdatedOtherDeviceType,
            getPersistedOtherDeviceType(partialUpdatedOtherDeviceType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOtherDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherDeviceType.setId(longCount.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, otherDeviceTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOtherDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherDeviceType.setId(longCount.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(otherDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OtherDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOtherDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        otherDeviceType.setId(longCount.incrementAndGet());

        // Create the OtherDeviceType
        OtherDeviceTypeDTO otherDeviceTypeDTO = otherDeviceTypeMapper.toDto(otherDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOtherDeviceTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(otherDeviceTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OtherDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOtherDeviceType() throws Exception {
        // Initialize the database
        insertedOtherDeviceType = otherDeviceTypeRepository.saveAndFlush(otherDeviceType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the otherDeviceType
        restOtherDeviceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, otherDeviceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return otherDeviceTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected OtherDeviceType getPersistedOtherDeviceType(OtherDeviceType otherDeviceType) {
        return otherDeviceTypeRepository.findById(otherDeviceType.getId()).orElseThrow();
    }

    protected void assertPersistedOtherDeviceTypeToMatchAllProperties(OtherDeviceType expectedOtherDeviceType) {
        assertOtherDeviceTypeAllPropertiesEquals(expectedOtherDeviceType, getPersistedOtherDeviceType(expectedOtherDeviceType));
    }

    protected void assertPersistedOtherDeviceTypeToMatchUpdatableProperties(OtherDeviceType expectedOtherDeviceType) {
        assertOtherDeviceTypeAllUpdatablePropertiesEquals(expectedOtherDeviceType, getPersistedOtherDeviceType(expectedOtherDeviceType));
    }
}
