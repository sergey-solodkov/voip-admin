package com.github.sergeisolodkov.voipadmin.web.rest;

import static com.github.sergeisolodkov.voipadmin.domain.OptionValueAsserts.*;
import static com.github.sergeisolodkov.voipadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sergeisolodkov.voipadmin.IntegrationTest;
import com.github.sergeisolodkov.voipadmin.domain.OptionValue;
import com.github.sergeisolodkov.voipadmin.repository.OptionValueRepository;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionValueDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.OptionValueMapper;
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
 * Integration tests for the {@link OptionValueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OptionValueResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/option-values";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OptionValueRepository optionValueRepository;

    @Autowired
    private OptionValueMapper optionValueMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionValueMockMvc;

    private OptionValue optionValue;

    private OptionValue insertedOptionValue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionValue createEntity() {
        return new OptionValue().value(DEFAULT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OptionValue createUpdatedEntity() {
        return new OptionValue().value(UPDATED_VALUE);
    }

    @BeforeEach
    public void initTest() {
        optionValue = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOptionValue != null) {
            optionValueRepository.delete(insertedOptionValue);
            insertedOptionValue = null;
        }
    }

    @Test
    @Transactional
    void createOptionValue() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);
        var returnedOptionValueDTO = om.readValue(
            restOptionValueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionValueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OptionValueDTO.class
        );

        // Validate the OptionValue in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOptionValue = optionValueMapper.toEntity(returnedOptionValueDTO);
        assertOptionValueUpdatableFieldsEquals(returnedOptionValue, getPersistedOptionValue(returnedOptionValue));

        insertedOptionValue = returnedOptionValue;
    }

    @Test
    @Transactional
    void createOptionValueWithExistingId() throws Exception {
        // Create the OptionValue with an existing ID
        optionValue.setId(1L);
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionValueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionValueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOptionValues() throws Exception {
        // Initialize the database
        insertedOptionValue = optionValueRepository.saveAndFlush(optionValue);

        // Get all the optionValueList
        restOptionValueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(optionValue.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getOptionValue() throws Exception {
        // Initialize the database
        insertedOptionValue = optionValueRepository.saveAndFlush(optionValue);

        // Get the optionValue
        restOptionValueMockMvc
            .perform(get(ENTITY_API_URL_ID, optionValue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(optionValue.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingOptionValue() throws Exception {
        // Get the optionValue
        restOptionValueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOptionValue() throws Exception {
        // Initialize the database
        insertedOptionValue = optionValueRepository.saveAndFlush(optionValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the optionValue
        OptionValue updatedOptionValue = optionValueRepository.findById(optionValue.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOptionValue are not directly saved in db
        em.detach(updatedOptionValue);
        updatedOptionValue.value(UPDATED_VALUE);
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(updatedOptionValue);

        restOptionValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionValueDTO))
            )
            .andExpect(status().isOk());

        // Validate the OptionValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOptionValueToMatchAllProperties(updatedOptionValue);
    }

    @Test
    @Transactional
    void putNonExistingOptionValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionValue.setId(longCount.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionValueDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOptionValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionValue.setId(longCount.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOptionValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionValue.setId(longCount.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OptionValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionValueWithPatch() throws Exception {
        // Initialize the database
        insertedOptionValue = optionValueRepository.saveAndFlush(optionValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the optionValue using partial update
        OptionValue partialUpdatedOptionValue = new OptionValue();
        partialUpdatedOptionValue.setId(optionValue.getId());

        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptionValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOptionValue))
            )
            .andExpect(status().isOk());

        // Validate the OptionValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionValueUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOptionValue, optionValue),
            getPersistedOptionValue(optionValue)
        );
    }

    @Test
    @Transactional
    void fullUpdateOptionValueWithPatch() throws Exception {
        // Initialize the database
        insertedOptionValue = optionValueRepository.saveAndFlush(optionValue);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the optionValue using partial update
        OptionValue partialUpdatedOptionValue = new OptionValue();
        partialUpdatedOptionValue.setId(optionValue.getId());

        partialUpdatedOptionValue.value(UPDATED_VALUE);

        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOptionValue.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOptionValue))
            )
            .andExpect(status().isOk());

        // Validate the OptionValue in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionValueUpdatableFieldsEquals(partialUpdatedOptionValue, getPersistedOptionValue(partialUpdatedOptionValue));
    }

    @Test
    @Transactional
    void patchNonExistingOptionValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionValue.setId(longCount.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, optionValueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOptionValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionValue.setId(longCount.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionValueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OptionValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOptionValue() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        optionValue.setId(longCount.incrementAndGet());

        // Create the OptionValue
        OptionValueDTO optionValueDTO = optionValueMapper.toDto(optionValue);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionValueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(optionValueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OptionValue in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOptionValue() throws Exception {
        // Initialize the database
        insertedOptionValue = optionValueRepository.saveAndFlush(optionValue);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the optionValue
        restOptionValueMockMvc
            .perform(delete(ENTITY_API_URL_ID, optionValue.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return optionValueRepository.count();
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

    protected OptionValue getPersistedOptionValue(OptionValue optionValue) {
        return optionValueRepository.findById(optionValue.getId()).orElseThrow();
    }

    protected void assertPersistedOptionValueToMatchAllProperties(OptionValue expectedOptionValue) {
        assertOptionValueAllPropertiesEquals(expectedOptionValue, getPersistedOptionValue(expectedOptionValue));
    }

    protected void assertPersistedOptionValueToMatchUpdatableProperties(OptionValue expectedOptionValue) {
        assertOptionValueAllUpdatablePropertiesEquals(expectedOptionValue, getPersistedOptionValue(expectedOptionValue));
    }
}
