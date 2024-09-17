package com.github.sergeisolodkov.voipadmin.web.rest;

import static com.github.sergeisolodkov.voipadmin.domain.OptionAsserts.*;
import static com.github.sergeisolodkov.voipadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sergeisolodkov.voipadmin.IntegrationTest;
import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.domain.Option;
import com.github.sergeisolodkov.voipadmin.domain.Vendor;
import com.github.sergeisolodkov.voipadmin.domain.enumeration.OptionValueType;
import com.github.sergeisolodkov.voipadmin.repository.OptionRepository;
import com.github.sergeisolodkov.voipadmin.service.OptionService;
import com.github.sergeisolodkov.voipadmin.service.dto.OptionDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.OptionMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OptionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OptionResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCR = "AAAAAAAAAA";
    private static final String UPDATED_DESCR = "BBBBBBBBBB";

    private static final OptionValueType DEFAULT_VALUE_TYPE = OptionValueType.TEXT;
    private static final OptionValueType UPDATED_VALUE_TYPE = OptionValueType.SELECT;

    private static final Boolean DEFAULT_MULTIPLE = false;
    private static final Boolean UPDATED_MULTIPLE = true;

    private static final String ENTITY_API_URL = "/api/options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OptionRepository optionRepository;

    @Mock
    private OptionRepository optionRepositoryMock;

    @Autowired
    private OptionMapper optionMapper;

    @Mock
    private OptionService optionServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOptionMockMvc;

    private Option option;

    private Option insertedOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createEntity() {
        return new Option().code(DEFAULT_CODE).descr(DEFAULT_DESCR).valueType(DEFAULT_VALUE_TYPE).multiple(DEFAULT_MULTIPLE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Option createUpdatedEntity() {
        return new Option().code(UPDATED_CODE).descr(UPDATED_DESCR).valueType(UPDATED_VALUE_TYPE).multiple(UPDATED_MULTIPLE);
    }

    @BeforeEach
    public void initTest() {
        option = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedOption != null) {
            optionRepository.delete(insertedOption);
            insertedOption = null;
        }
    }

    @Test
    @Transactional
    void createOption() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);
        var returnedOptionDTO = om.readValue(
            restOptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OptionDTO.class
        );

        // Validate the Option in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOption = optionMapper.toEntity(returnedOptionDTO);
        assertOptionUpdatableFieldsEquals(returnedOption, getPersistedOption(returnedOption));

        insertedOption = returnedOption;
    }

    @Test
    @Transactional
    void createOptionWithExistingId() throws Exception {
        // Create the Option with an existing ID
        option.setId(1L);
        OptionDTO optionDTO = optionMapper.toDto(option);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOptions() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].descr").value(hasItem(DEFAULT_DESCR)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].multiple").value(hasItem(DEFAULT_MULTIPLE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOptionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(optionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(optionServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOptionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(optionServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOptionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(optionRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getOption() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get the option
        restOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, option.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(option.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.descr").value(DEFAULT_DESCR))
            .andExpect(jsonPath("$.valueType").value(DEFAULT_VALUE_TYPE.toString()))
            .andExpect(jsonPath("$.multiple").value(DEFAULT_MULTIPLE.booleanValue()));
    }

    @Test
    @Transactional
    void getOptionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        Long id = option.getId();

        defaultOptionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOptionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOptionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where code equals to
        defaultOptionFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where code in
        defaultOptionFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where code is not null
        defaultOptionFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where code contains
        defaultOptionFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where code does not contain
        defaultOptionFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where descr equals to
        defaultOptionFiltering("descr.equals=" + DEFAULT_DESCR, "descr.equals=" + UPDATED_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where descr in
        defaultOptionFiltering("descr.in=" + DEFAULT_DESCR + "," + UPDATED_DESCR, "descr.in=" + UPDATED_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where descr is not null
        defaultOptionFiltering("descr.specified=true", "descr.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByDescrContainsSomething() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where descr contains
        defaultOptionFiltering("descr.contains=" + DEFAULT_DESCR, "descr.contains=" + UPDATED_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByDescrNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where descr does not contain
        defaultOptionFiltering("descr.doesNotContain=" + UPDATED_DESCR, "descr.doesNotContain=" + DEFAULT_DESCR);
    }

    @Test
    @Transactional
    void getAllOptionsByValueTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where valueType equals to
        defaultOptionFiltering("valueType.equals=" + DEFAULT_VALUE_TYPE, "valueType.equals=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllOptionsByValueTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where valueType in
        defaultOptionFiltering("valueType.in=" + DEFAULT_VALUE_TYPE + "," + UPDATED_VALUE_TYPE, "valueType.in=" + UPDATED_VALUE_TYPE);
    }

    @Test
    @Transactional
    void getAllOptionsByValueTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where valueType is not null
        defaultOptionFiltering("valueType.specified=true", "valueType.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByMultipleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where multiple equals to
        defaultOptionFiltering("multiple.equals=" + DEFAULT_MULTIPLE, "multiple.equals=" + UPDATED_MULTIPLE);
    }

    @Test
    @Transactional
    void getAllOptionsByMultipleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where multiple in
        defaultOptionFiltering("multiple.in=" + DEFAULT_MULTIPLE + "," + UPDATED_MULTIPLE, "multiple.in=" + UPDATED_MULTIPLE);
    }

    @Test
    @Transactional
    void getAllOptionsByMultipleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        // Get all the optionList where multiple is not null
        defaultOptionFiltering("multiple.specified=true", "multiple.specified=false");
    }

    @Test
    @Transactional
    void getAllOptionsByVendorsIsEqualToSomething() throws Exception {
        Vendor vendors;
        if (TestUtil.findAll(em, Vendor.class).isEmpty()) {
            optionRepository.saveAndFlush(option);
            vendors = VendorResourceIT.createEntity();
        } else {
            vendors = TestUtil.findAll(em, Vendor.class).get(0);
        }
        em.persist(vendors);
        em.flush();
        option.addVendors(vendors);
        optionRepository.saveAndFlush(option);
        Long vendorsId = vendors.getId();
        // Get all the optionList where vendors equals to vendorsId
        defaultOptionShouldBeFound("vendorsId.equals=" + vendorsId);

        // Get all the optionList where vendors equals to (vendorsId + 1)
        defaultOptionShouldNotBeFound("vendorsId.equals=" + (vendorsId + 1));
    }

    @Test
    @Transactional
    void getAllOptionsByModelsIsEqualToSomething() throws Exception {
        DeviceModel models;
        if (TestUtil.findAll(em, DeviceModel.class).isEmpty()) {
            optionRepository.saveAndFlush(option);
            models = DeviceModelResourceIT.createEntity();
        } else {
            models = TestUtil.findAll(em, DeviceModel.class).get(0);
        }
        em.persist(models);
        em.flush();
        option.addModels(models);
        optionRepository.saveAndFlush(option);
        Long modelsId = models.getId();
        // Get all the optionList where models equals to modelsId
        defaultOptionShouldBeFound("modelsId.equals=" + modelsId);

        // Get all the optionList where models equals to (modelsId + 1)
        defaultOptionShouldNotBeFound("modelsId.equals=" + (modelsId + 1));
    }

    private void defaultOptionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOptionShouldBeFound(shouldBeFound);
        defaultOptionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOptionShouldBeFound(String filter) throws Exception {
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(option.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].descr").value(hasItem(DEFAULT_DESCR)))
            .andExpect(jsonPath("$.[*].valueType").value(hasItem(DEFAULT_VALUE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].multiple").value(hasItem(DEFAULT_MULTIPLE.booleanValue())));

        // Check, that the count call also returns 1
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOptionShouldNotBeFound(String filter) throws Exception {
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOption() throws Exception {
        // Get the option
        restOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOption() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option
        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOption are not directly saved in db
        em.detach(updatedOption);
        updatedOption.code(UPDATED_CODE).descr(UPDATED_DESCR).valueType(UPDATED_VALUE_TYPE).multiple(UPDATED_MULTIPLE);
        OptionDTO optionDTO = optionMapper.toDto(updatedOption);

        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOptionToMatchAllProperties(updatedOption);
    }

    @Test
    @Transactional
    void putNonExistingOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, optionDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(optionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.code(UPDATED_CODE);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOption, option), getPersistedOption(option));
    }

    @Test
    @Transactional
    void fullUpdateOptionWithPatch() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the option using partial update
        Option partialUpdatedOption = new Option();
        partialUpdatedOption.setId(option.getId());

        partialUpdatedOption.code(UPDATED_CODE).descr(UPDATED_DESCR).valueType(UPDATED_VALUE_TYPE).multiple(UPDATED_MULTIPLE);

        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOption))
            )
            .andExpect(status().isOk());

        // Validate the Option in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOptionUpdatableFieldsEquals(partialUpdatedOption, getPersistedOption(partialUpdatedOption));
    }

    @Test
    @Transactional
    void patchNonExistingOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, optionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(optionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        option.setId(longCount.incrementAndGet());

        // Create the Option
        OptionDTO optionDTO = optionMapper.toDto(option);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(optionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Option in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOption() throws Exception {
        // Initialize the database
        insertedOption = optionRepository.saveAndFlush(option);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the option
        restOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, option.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return optionRepository.count();
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

    protected Option getPersistedOption(Option option) {
        return optionRepository.findById(option.getId()).orElseThrow();
    }

    protected void assertPersistedOptionToMatchAllProperties(Option expectedOption) {
        assertOptionAllPropertiesEquals(expectedOption, getPersistedOption(expectedOption));
    }

    protected void assertPersistedOptionToMatchUpdatableProperties(Option expectedOption) {
        assertOptionAllUpdatablePropertiesEquals(expectedOption, getPersistedOption(expectedOption));
    }
}
