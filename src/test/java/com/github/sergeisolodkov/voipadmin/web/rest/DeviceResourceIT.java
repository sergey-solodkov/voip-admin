package com.github.sergeisolodkov.voipadmin.web.rest;

import static com.github.sergeisolodkov.voipadmin.domain.DeviceAsserts.*;
import static com.github.sergeisolodkov.voipadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sergeisolodkov.voipadmin.IntegrationTest;
import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.domain.enumeration.ProvisioningMode;
import com.github.sergeisolodkov.voipadmin.repository.DeviceRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceService;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.DeviceMapper;
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
 * Integration tests for the {@link DeviceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DeviceResourceIT {

    private static final String DEFAULT_MAC = "AAAAAAAAAA";
    private static final String UPDATED_MAC = "BBBBBBBBBB";

    private static final String DEFAULT_INVENTORY_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVENTORY_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String DEFAULT_HOSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_HOSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_ACCESS_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_WEB_ACCESS_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_WEB_ACCESS_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_WEB_ACCESS_PASSWORD_HASH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DHCP_ENABLED = false;
    private static final Boolean UPDATED_DHCP_ENABLED = true;

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SUBNET_MASK = "AAAAAAAAAA";
    private static final String UPDATED_SUBNET_MASK = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_GW = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_GW = "BBBBBBBBBB";

    private static final String DEFAULT_DNS_1 = "AAAAAAAAAA";
    private static final String UPDATED_DNS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_DNS_2 = "AAAAAAAAAA";
    private static final String UPDATED_DNS_2 = "BBBBBBBBBB";

    private static final ProvisioningMode DEFAULT_PROVISIONING_MODE = ProvisioningMode.FTP;
    private static final ProvisioningMode UPDATED_PROVISIONING_MODE = ProvisioningMode.TFTP;

    private static final String DEFAULT_PROVISIONING_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROVISIONING_URL = "BBBBBBBBBB";

    private static final String DEFAULT_NTP = "AAAAAAAAAA";
    private static final String UPDATED_NTP = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIG_PATH = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DeviceRepository deviceRepository;

    @Mock
    private DeviceRepository deviceRepositoryMock;

    @Autowired
    private DeviceMapper deviceMapper;

    @Mock
    private DeviceService deviceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceMockMvc;

    private Device device;

    private Device insertedDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createEntity() {
        return new Device()
            .mac(DEFAULT_MAC)
            .inventoryId(DEFAULT_INVENTORY_ID)
            .location(DEFAULT_LOCATION)
            .hostname(DEFAULT_HOSTNAME)
            .webAccessLogin(DEFAULT_WEB_ACCESS_LOGIN)
            .webAccessPasswordHash(DEFAULT_WEB_ACCESS_PASSWORD_HASH)
            .dhcpEnabled(DEFAULT_DHCP_ENABLED)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .subnetMask(DEFAULT_SUBNET_MASK)
            .defaultGw(DEFAULT_DEFAULT_GW)
            .dns1(DEFAULT_DNS_1)
            .dns2(DEFAULT_DNS_2)
            .provisioningMode(DEFAULT_PROVISIONING_MODE)
            .provisioningUrl(DEFAULT_PROVISIONING_URL)
            .ntp(DEFAULT_NTP)
            .configPath(DEFAULT_CONFIG_PATH)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Device createUpdatedEntity() {
        return new Device()
            .mac(UPDATED_MAC)
            .inventoryId(UPDATED_INVENTORY_ID)
            .location(UPDATED_LOCATION)
            .hostname(UPDATED_HOSTNAME)
            .webAccessLogin(UPDATED_WEB_ACCESS_LOGIN)
            .webAccessPasswordHash(UPDATED_WEB_ACCESS_PASSWORD_HASH)
            .dhcpEnabled(UPDATED_DHCP_ENABLED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .subnetMask(UPDATED_SUBNET_MASK)
            .defaultGw(UPDATED_DEFAULT_GW)
            .dns1(UPDATED_DNS_1)
            .dns2(UPDATED_DNS_2)
            .provisioningMode(UPDATED_PROVISIONING_MODE)
            .provisioningUrl(UPDATED_PROVISIONING_URL)
            .ntp(UPDATED_NTP)
            .configPath(UPDATED_CONFIG_PATH)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    public void initTest() {
        device = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDevice != null) {
            deviceRepository.delete(insertedDevice);
            insertedDevice = null;
        }
    }

    @Test
    @Transactional
    void createDevice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);
        var returnedDeviceDTO = om.readValue(
            restDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DeviceDTO.class
        );

        // Validate the Device in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDevice = deviceMapper.toEntity(returnedDeviceDTO);
        assertDeviceUpdatableFieldsEquals(returnedDevice, getPersistedDevice(returnedDevice));

        insertedDevice = returnedDevice;
    }

    @Test
    @Transactional
    void createDeviceWithExistingId() throws Exception {
        // Create the Device with an existing ID
        device.setId(1L);
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMacIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        device.setMac(null);

        // Create the Device, which fails.
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        restDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDevices() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        // Get all the deviceList
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(device.getId().intValue())))
            .andExpect(jsonPath("$.[*].mac").value(hasItem(DEFAULT_MAC)))
            .andExpect(jsonPath("$.[*].inventoryId").value(hasItem(DEFAULT_INVENTORY_ID)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].hostname").value(hasItem(DEFAULT_HOSTNAME)))
            .andExpect(jsonPath("$.[*].webAccessLogin").value(hasItem(DEFAULT_WEB_ACCESS_LOGIN)))
            .andExpect(jsonPath("$.[*].webAccessPasswordHash").value(hasItem(DEFAULT_WEB_ACCESS_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].dhcpEnabled").value(hasItem(DEFAULT_DHCP_ENABLED.booleanValue())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].subnetMask").value(hasItem(DEFAULT_SUBNET_MASK)))
            .andExpect(jsonPath("$.[*].defaultGw").value(hasItem(DEFAULT_DEFAULT_GW)))
            .andExpect(jsonPath("$.[*].dns1").value(hasItem(DEFAULT_DNS_1)))
            .andExpect(jsonPath("$.[*].dns2").value(hasItem(DEFAULT_DNS_2)))
            .andExpect(jsonPath("$.[*].provisioningMode").value(hasItem(DEFAULT_PROVISIONING_MODE.toString())))
            .andExpect(jsonPath("$.[*].provisioningUrl").value(hasItem(DEFAULT_PROVISIONING_URL)))
            .andExpect(jsonPath("$.[*].ntp").value(hasItem(DEFAULT_NTP)))
            .andExpect(jsonPath("$.[*].configPath").value(hasItem(DEFAULT_CONFIG_PATH)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDevicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(deviceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(deviceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDevicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deviceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(deviceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDevice() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        // Get the device
        restDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, device.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(device.getId().intValue()))
            .andExpect(jsonPath("$.mac").value(DEFAULT_MAC))
            .andExpect(jsonPath("$.inventoryId").value(DEFAULT_INVENTORY_ID))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.hostname").value(DEFAULT_HOSTNAME))
            .andExpect(jsonPath("$.webAccessLogin").value(DEFAULT_WEB_ACCESS_LOGIN))
            .andExpect(jsonPath("$.webAccessPasswordHash").value(DEFAULT_WEB_ACCESS_PASSWORD_HASH))
            .andExpect(jsonPath("$.dhcpEnabled").value(DEFAULT_DHCP_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.subnetMask").value(DEFAULT_SUBNET_MASK))
            .andExpect(jsonPath("$.defaultGw").value(DEFAULT_DEFAULT_GW))
            .andExpect(jsonPath("$.dns1").value(DEFAULT_DNS_1))
            .andExpect(jsonPath("$.dns2").value(DEFAULT_DNS_2))
            .andExpect(jsonPath("$.provisioningMode").value(DEFAULT_PROVISIONING_MODE.toString()))
            .andExpect(jsonPath("$.provisioningUrl").value(DEFAULT_PROVISIONING_URL))
            .andExpect(jsonPath("$.ntp").value(DEFAULT_NTP))
            .andExpect(jsonPath("$.configPath").value(DEFAULT_CONFIG_PATH))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getNonExistingDevice() throws Exception {
        // Get the device
        restDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDevice() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device
        Device updatedDevice = deviceRepository.findById(device.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDevice are not directly saved in db
        em.detach(updatedDevice);
        updatedDevice
            .mac(UPDATED_MAC)
            .inventoryId(UPDATED_INVENTORY_ID)
            .location(UPDATED_LOCATION)
            .hostname(UPDATED_HOSTNAME)
            .webAccessLogin(UPDATED_WEB_ACCESS_LOGIN)
            .webAccessPasswordHash(UPDATED_WEB_ACCESS_PASSWORD_HASH)
            .dhcpEnabled(UPDATED_DHCP_ENABLED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .subnetMask(UPDATED_SUBNET_MASK)
            .defaultGw(UPDATED_DEFAULT_GW)
            .dns1(UPDATED_DNS_1)
            .dns2(UPDATED_DNS_2)
            .provisioningMode(UPDATED_PROVISIONING_MODE)
            .provisioningUrl(UPDATED_PROVISIONING_URL)
            .ntp(UPDATED_NTP)
            .configPath(UPDATED_CONFIG_PATH)
            .notes(UPDATED_NOTES);
        DeviceDTO deviceDTO = deviceMapper.toDto(updatedDevice);

        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDeviceToMatchAllProperties(updatedDevice);
    }

    @Test
    @Transactional
    void putNonExistingDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, deviceDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .inventoryId(UPDATED_INVENTORY_ID)
            .hostname(UPDATED_HOSTNAME)
            .dhcpEnabled(UPDATED_DHCP_ENABLED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .defaultGw(UPDATED_DEFAULT_GW)
            .dns2(UPDATED_DNS_2)
            .provisioningMode(UPDATED_PROVISIONING_MODE)
            .provisioningUrl(UPDATED_PROVISIONING_URL);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDevice, device), getPersistedDevice(device));
    }

    @Test
    @Transactional
    void fullUpdateDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the device using partial update
        Device partialUpdatedDevice = new Device();
        partialUpdatedDevice.setId(device.getId());

        partialUpdatedDevice
            .mac(UPDATED_MAC)
            .inventoryId(UPDATED_INVENTORY_ID)
            .location(UPDATED_LOCATION)
            .hostname(UPDATED_HOSTNAME)
            .webAccessLogin(UPDATED_WEB_ACCESS_LOGIN)
            .webAccessPasswordHash(UPDATED_WEB_ACCESS_PASSWORD_HASH)
            .dhcpEnabled(UPDATED_DHCP_ENABLED)
            .ipAddress(UPDATED_IP_ADDRESS)
            .subnetMask(UPDATED_SUBNET_MASK)
            .defaultGw(UPDATED_DEFAULT_GW)
            .dns1(UPDATED_DNS_1)
            .dns2(UPDATED_DNS_2)
            .provisioningMode(UPDATED_PROVISIONING_MODE)
            .provisioningUrl(UPDATED_PROVISIONING_URL)
            .ntp(UPDATED_NTP)
            .configPath(UPDATED_CONFIG_PATH)
            .notes(UPDATED_NOTES);

        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDevice))
            )
            .andExpect(status().isOk());

        // Validate the Device in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDeviceUpdatableFieldsEquals(partialUpdatedDevice, getPersistedDevice(partialUpdatedDevice));
    }

    @Test
    @Transactional
    void patchNonExistingDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, deviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(deviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        device.setId(longCount.incrementAndGet());

        // Create the Device
        DeviceDTO deviceDTO = deviceMapper.toDto(device);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeviceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(deviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Device in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDevice() throws Exception {
        // Initialize the database
        insertedDevice = deviceRepository.saveAndFlush(device);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the device
        restDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, device.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return deviceRepository.count();
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

    protected Device getPersistedDevice(Device device) {
        return deviceRepository.findById(device.getId()).orElseThrow();
    }

    protected void assertPersistedDeviceToMatchAllProperties(Device expectedDevice) {
        assertDeviceAllPropertiesEquals(expectedDevice, getPersistedDevice(expectedDevice));
    }

    protected void assertPersistedDeviceToMatchUpdatableProperties(Device expectedDevice) {
        assertDeviceAllUpdatablePropertiesEquals(expectedDevice, getPersistedDevice(expectedDevice));
    }
}
