package com.github.sergeisolodkov.voipadmin.web.rest;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceModelAsserts.*;
import static com.github.sergeisolodkov.voipadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sergeisolodkov.voipadmin.IntegrationTest;
import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.domain.enumeration.DeviceType;
import com.github.sergeisolodkov.voipadmin.repository.DeviceModelRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceModelService;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.DeviceModelMapper;
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
 * Integration tests for the {@link DeviceModelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DeviceModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONFIGURABLE = false;
    private static final Boolean UPDATED_CONFIGURABLE = true;

    private static final Integer DEFAULT_LINES_AMOUNT = 1;
    private static final Integer UPDATED_LINES_AMOUNT = 2;

    private static final String DEFAULT_CONFIG_TEMPLATE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_TEMPLATE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_FIRMWARE_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FIRMWARE_FILE_PATH = "BBBBBBBBBB";

    private static final DeviceType DEFAULT_DEVICE_TYPE = DeviceType.IPPHONE;
    private static final DeviceType UPDATED_DEVICE_TYPE = DeviceType.IPGATEWAY;

    private static final String ENTITY_API_URL = "/api/device-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeviceModelRepository deviceModelRepository;

    @Mock
    private DeviceModelRepository deviceModelRepositoryMock;

    @Autowired
    private DeviceModelMapper deviceModelMapper;

    @Mock
    private DeviceModelService deviceModelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceModelMockMvc;

    private DeviceModel deviceModel;

    private DeviceModel insertedDeviceModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceModel createEntity() {
        return new DeviceModel()
            .name(DEFAULT_NAME)
            .configurable(DEFAULT_CONFIGURABLE)
            .linesAmount(DEFAULT_LINES_AMOUNT)
            .configTemplatePath(DEFAULT_CONFIG_TEMPLATE_PATH)
            .firmwareFilePath(DEFAULT_FIRMWARE_FILE_PATH)
            .deviceType(DEFAULT_DEVICE_TYPE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceModel createUpdatedEntity() {
        return new DeviceModel()
            .name(UPDATED_NAME)
            .configurable(UPDATED_CONFIGURABLE)
            .linesAmount(UPDATED_LINES_AMOUNT)
            .configTemplatePath(UPDATED_CONFIG_TEMPLATE_PATH)
            .firmwareFilePath(UPDATED_FIRMWARE_FILE_PATH)
            .deviceType(UPDATED_DEVICE_TYPE);
    }

    @BeforeEach
    public void initTest() {
        deviceModel = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDeviceModel != null) {
            deviceModelRepository.delete(insertedDeviceModel);
            insertedDeviceModel = null;
        }
    }

    @Test
    @Transactional
    void createDeviceModel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);
        var returnedDeviceModelDTO = om.readValue(
            restDeviceModelMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceModelDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DeviceModelDTO.class
        );

        // Validate the DeviceModel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDeviceModel = deviceModelMapper.toEntity(returnedDeviceModelDTO);
        assertDeviceModelUpdatableFieldsEquals(returnedDeviceModel, getPersistedDeviceModel(returnedDeviceModel));

        insertedDeviceModel = returnedDeviceModel;
    }

    @Test
    @Transactional
    void createDeviceModelWithExistingId() throws Exception {
        // Create the DeviceModel with an existing ID
        deviceModel.setId(1L);
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceModelDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deviceModel.setName(null);

        // Create the DeviceModel, which fails.
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        restDeviceModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceModelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfigurableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deviceModel.setConfigurable(null);

        // Create the DeviceModel, which fails.
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        restDeviceModelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceModelDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeviceModels() throws Exception {
        // Initialize the database
        insertedDeviceModel = deviceModelRepository.saveAndFlush(deviceModel);

        // Get all the deviceModelList
        restDeviceModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].configurable").value(hasItem(DEFAULT_CONFIGURABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].linesAmount").value(hasItem(DEFAULT_LINES_AMOUNT)))
            .andExpect(jsonPath("$.[*].configTemplatePath").value(hasItem(DEFAULT_CONFIG_TEMPLATE_PATH)))
            .andExpect(jsonPath("$.[*].firmwareFilePath").value(hasItem(DEFAULT_FIRMWARE_FILE_PATH)))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeviceModelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(deviceModelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deviceModelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDeviceModelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deviceModelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceModelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(deviceModelRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDeviceModel() throws Exception {
        // Initialize the database
        insertedDeviceModel = deviceModelRepository.saveAndFlush(deviceModel);

        // Get the deviceModel
        restDeviceModelMockMvc
            .perform(get(ENTITY_API_URL_ID, deviceModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.configurable").value(DEFAULT_CONFIGURABLE.booleanValue()))
            .andExpect(jsonPath("$.linesAmount").value(DEFAULT_LINES_AMOUNT))
            .andExpect(jsonPath("$.configTemplatePath").value(DEFAULT_CONFIG_TEMPLATE_PATH))
            .andExpect(jsonPath("$.firmwareFilePath").value(DEFAULT_FIRMWARE_FILE_PATH))
            .andExpect(jsonPath("$.deviceType").value(DEFAULT_DEVICE_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDeviceModel() throws Exception {
        // Get the deviceModel
        restDeviceModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeviceModel() throws Exception {
        // Initialize the database
        insertedDeviceModel = deviceModelRepository.saveAndFlush(deviceModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceModel
        DeviceModel updatedDeviceModel = deviceModelRepository.findById(deviceModel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeviceModel are not directly saved in db
        em.detach(updatedDeviceModel);
        updatedDeviceModel
            .name(UPDATED_NAME)
            .configurable(UPDATED_CONFIGURABLE)
            .linesAmount(UPDATED_LINES_AMOUNT)
            .configTemplatePath(UPDATED_CONFIG_TEMPLATE_PATH)
            .firmwareFilePath(UPDATED_FIRMWARE_FILE_PATH)
            .deviceType(UPDATED_DEVICE_TYPE);
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(updatedDeviceModel);

        restDeviceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the DeviceModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeviceModelToMatchAllProperties(updatedDeviceModel);
    }

    @Test
    @Transactional
    void putNonExistingDeviceModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceModel.setId(longCount.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceModelDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeviceModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceModel.setId(longCount.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeviceModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceModel.setId(longCount.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceModelWithPatch() throws Exception {
        // Initialize the database
        insertedDeviceModel = deviceModelRepository.saveAndFlush(deviceModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceModel using partial update
        DeviceModel partialUpdatedDeviceModel = new DeviceModel();
        partialUpdatedDeviceModel.setId(deviceModel.getId());

        partialUpdatedDeviceModel
            .configurable(UPDATED_CONFIGURABLE)
            .configTemplatePath(UPDATED_CONFIG_TEMPLATE_PATH)
            .firmwareFilePath(UPDATED_FIRMWARE_FILE_PATH)
            .deviceType(UPDATED_DEVICE_TYPE);

        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeviceModel))
            )
            .andExpect(status().isOk());

        // Validate the DeviceModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceModelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDeviceModel, deviceModel),
            getPersistedDeviceModel(deviceModel)
        );
    }

    @Test
    @Transactional
    void fullUpdateDeviceModelWithPatch() throws Exception {
        // Initialize the database
        insertedDeviceModel = deviceModelRepository.saveAndFlush(deviceModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deviceModel using partial update
        DeviceModel partialUpdatedDeviceModel = new DeviceModel();
        partialUpdatedDeviceModel.setId(deviceModel.getId());

        partialUpdatedDeviceModel
            .name(UPDATED_NAME)
            .configurable(UPDATED_CONFIGURABLE)
            .linesAmount(UPDATED_LINES_AMOUNT)
            .configTemplatePath(UPDATED_CONFIG_TEMPLATE_PATH)
            .firmwareFilePath(UPDATED_FIRMWARE_FILE_PATH)
            .deviceType(UPDATED_DEVICE_TYPE);

        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeviceModel.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeviceModel))
            )
            .andExpect(status().isOk());

        // Validate the DeviceModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceModelUpdatableFieldsEquals(partialUpdatedDeviceModel, getPersistedDeviceModel(partialUpdatedDeviceModel));
    }

    @Test
    @Transactional
    void patchNonExistingDeviceModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceModel.setId(longCount.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceModelDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeviceModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceModel.setId(longCount.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DeviceModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeviceModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deviceModel.setId(longCount.incrementAndGet());

        // Create the DeviceModel
        DeviceModelDTO deviceModelDTO = deviceModelMapper.toDto(deviceModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceModelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(deviceModelDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DeviceModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeviceModel() throws Exception {
        // Initialize the database
        insertedDeviceModel = deviceModelRepository.saveAndFlush(deviceModel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the deviceModel
        restDeviceModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, deviceModel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return deviceModelRepository.count();
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

    protected DeviceModel getPersistedDeviceModel(DeviceModel deviceModel) {
        return deviceModelRepository.findById(deviceModel.getId()).orElseThrow();
    }

    protected void assertPersistedDeviceModelToMatchAllProperties(DeviceModel expectedDeviceModel) {
        assertDeviceModelAllPropertiesEquals(expectedDeviceModel, getPersistedDeviceModel(expectedDeviceModel));
    }

    protected void assertPersistedDeviceModelToMatchUpdatableProperties(DeviceModel expectedDeviceModel) {
        assertDeviceModelAllUpdatablePropertiesEquals(expectedDeviceModel, getPersistedDeviceModel(expectedDeviceModel));
    }
}
