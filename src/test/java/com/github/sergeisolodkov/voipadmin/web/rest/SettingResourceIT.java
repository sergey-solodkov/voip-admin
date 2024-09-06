package com.github.sergeisolodkov.voipadmin.web.rest;

import static com.github.sergeisolodkov.voipadmin.domain.SettingAsserts.*;
import static com.github.sergeisolodkov.voipadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sergeisolodkov.voipadmin.IntegrationTest;
import com.github.sergeisolodkov.voipadmin.domain.Setting;
import com.github.sergeisolodkov.voipadmin.repository.SettingRepository;
import com.github.sergeisolodkov.voipadmin.service.SettingService;
import com.github.sergeisolodkov.voipadmin.service.dto.SettingDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.SettingMapper;
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
 * Integration tests for the {@link SettingResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SettingResourceIT {

    private static final String DEFAULT_TEXT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SettingRepository settingRepository;

    @Mock
    private SettingRepository settingRepositoryMock;

    @Autowired
    private SettingMapper settingMapper;

    @Mock
    private SettingService settingServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSettingMockMvc;

    private Setting setting;

    private Setting insertedSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Setting createEntity() {
        return new Setting().textValue(DEFAULT_TEXT_VALUE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Setting createUpdatedEntity() {
        return new Setting().textValue(UPDATED_TEXT_VALUE);
    }

    @BeforeEach
    public void initTest() {
        setting = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSetting != null) {
            settingRepository.delete(insertedSetting);
            insertedSetting = null;
        }
    }

    @Test
    @Transactional
    void createSetting() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Setting
        SettingDTO settingDTO = settingMapper.toDto(setting);
        var returnedSettingDTO = om.readValue(
            restSettingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(settingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SettingDTO.class
        );

        // Validate the Setting in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSetting = settingMapper.toEntity(returnedSettingDTO);
        assertSettingUpdatableFieldsEquals(returnedSetting, getPersistedSetting(returnedSetting));

        insertedSetting = returnedSetting;
    }

    @Test
    @Transactional
    void createSettingWithExistingId() throws Exception {
        // Create the Setting with an existing ID
        setting.setId(1L);
        SettingDTO settingDTO = settingMapper.toDto(setting);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(settingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSettings() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.saveAndFlush(setting);

        // Get all the settingList
        restSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(setting.getId().intValue())))
            .andExpect(jsonPath("$.[*].textValue").value(hasItem(DEFAULT_TEXT_VALUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSettingsWithEagerRelationshipsIsEnabled() throws Exception {
        when(settingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSettingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(settingServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSettingsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(settingServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSettingMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(settingRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSetting() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.saveAndFlush(setting);

        // Get the setting
        restSettingMockMvc
            .perform(get(ENTITY_API_URL_ID, setting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(setting.getId().intValue()))
            .andExpect(jsonPath("$.textValue").value(DEFAULT_TEXT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingSetting() throws Exception {
        // Get the setting
        restSettingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSetting() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.saveAndFlush(setting);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the setting
        Setting updatedSetting = settingRepository.findById(setting.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSetting are not directly saved in db
        em.detach(updatedSetting);
        updatedSetting.textValue(UPDATED_TEXT_VALUE);
        SettingDTO settingDTO = settingMapper.toDto(updatedSetting);

        restSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(settingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSettingToMatchAllProperties(updatedSetting);
    }

    @Test
    @Transactional
    void putNonExistingSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(longCount.incrementAndGet());

        // Create the Setting
        SettingDTO settingDTO = settingMapper.toDto(setting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, settingDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(settingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(longCount.incrementAndGet());

        // Create the Setting
        SettingDTO settingDTO = settingMapper.toDto(setting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(settingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(longCount.incrementAndGet());

        // Create the Setting
        SettingDTO settingDTO = settingMapper.toDto(setting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(settingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSettingWithPatch() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.saveAndFlush(setting);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the setting using partial update
        Setting partialUpdatedSetting = new Setting();
        partialUpdatedSetting.setId(setting.getId());

        partialUpdatedSetting.textValue(UPDATED_TEXT_VALUE);

        restSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSetting))
            )
            .andExpect(status().isOk());

        // Validate the Setting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSettingUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSetting, setting), getPersistedSetting(setting));
    }

    @Test
    @Transactional
    void fullUpdateSettingWithPatch() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.saveAndFlush(setting);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the setting using partial update
        Setting partialUpdatedSetting = new Setting();
        partialUpdatedSetting.setId(setting.getId());

        partialUpdatedSetting.textValue(UPDATED_TEXT_VALUE);

        restSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSetting))
            )
            .andExpect(status().isOk());

        // Validate the Setting in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSettingUpdatableFieldsEquals(partialUpdatedSetting, getPersistedSetting(partialUpdatedSetting));
    }

    @Test
    @Transactional
    void patchNonExistingSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(longCount.incrementAndGet());

        // Create the Setting
        SettingDTO settingDTO = settingMapper.toDto(setting);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, settingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(settingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(longCount.incrementAndGet());

        // Create the Setting
        SettingDTO settingDTO = settingMapper.toDto(setting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(settingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSetting() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        setting.setId(longCount.incrementAndGet());

        // Create the Setting
        SettingDTO settingDTO = settingMapper.toDto(setting);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSettingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(settingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Setting in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSetting() throws Exception {
        // Initialize the database
        insertedSetting = settingRepository.saveAndFlush(setting);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the setting
        restSettingMockMvc
            .perform(delete(ENTITY_API_URL_ID, setting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return settingRepository.count();
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

    protected Setting getPersistedSetting(Setting setting) {
        return settingRepository.findById(setting.getId()).orElseThrow();
    }

    protected void assertPersistedSettingToMatchAllProperties(Setting expectedSetting) {
        assertSettingAllPropertiesEquals(expectedSetting, getPersistedSetting(expectedSetting));
    }

    protected void assertPersistedSettingToMatchUpdatableProperties(Setting expectedSetting) {
        assertSettingAllUpdatablePropertiesEquals(expectedSetting, getPersistedSetting(expectedSetting));
    }
}
