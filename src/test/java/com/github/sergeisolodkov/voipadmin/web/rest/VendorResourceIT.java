package com.github.sergeisolodkov.voipadmin.web.rest;

import static com.github.sergeisolodkov.voipadmin.domain.VendorAsserts.*;
import static com.github.sergeisolodkov.voipadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sergeisolodkov.voipadmin.IntegrationTest;
import com.github.sergeisolodkov.voipadmin.domain.Vendor;
import com.github.sergeisolodkov.voipadmin.repository.VendorRepository;
import com.github.sergeisolodkov.voipadmin.service.dto.VendorDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.VendorMapper;
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
 * Integration tests for the {@link VendorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vendors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private VendorMapper vendorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendorMockMvc;

    private Vendor vendor;

    private Vendor insertedVendor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendor createEntity() {
        return new Vendor().name(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendor createUpdatedEntity() {
        return new Vendor().name(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        vendor = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVendor != null) {
            vendorRepository.delete(insertedVendor);
            insertedVendor = null;
        }
    }

    @Test
    @Transactional
    void createVendor() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);
        var returnedVendorDTO = om.readValue(
            restVendorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VendorDTO.class
        );

        // Validate the Vendor in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVendor = vendorMapper.toEntity(returnedVendorDTO);
        assertVendorUpdatableFieldsEquals(returnedVendor, getPersistedVendor(returnedVendor));

        insertedVendor = returnedVendor;
    }

    @Test
    @Transactional
    void createVendorWithExistingId() throws Exception {
        // Create the Vendor with an existing ID
        vendor.setId(1L);
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVendors() throws Exception {
        // Initialize the database
        insertedVendor = vendorRepository.saveAndFlush(vendor);

        // Get all the vendorList
        restVendorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getVendor() throws Exception {
        // Initialize the database
        insertedVendor = vendorRepository.saveAndFlush(vendor);

        // Get the vendor
        restVendorMockMvc
            .perform(get(ENTITY_API_URL_ID, vendor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingVendor() throws Exception {
        // Get the vendor
        restVendorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVendor() throws Exception {
        // Initialize the database
        insertedVendor = vendorRepository.saveAndFlush(vendor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendor
        Vendor updatedVendor = vendorRepository.findById(vendor.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVendor are not directly saved in db
        em.detach(updatedVendor);
        updatedVendor.name(UPDATED_NAME);
        VendorDTO vendorDTO = vendorMapper.toDto(updatedVendor);

        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVendorToMatchAllProperties(updatedVendor);
    }

    @Test
    @Transactional
    void putNonExistingVendor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendor.setId(longCount.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendorDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendor.setId(longCount.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(vendorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendor.setId(longCount.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(vendorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendorWithPatch() throws Exception {
        // Initialize the database
        insertedVendor = vendorRepository.saveAndFlush(vendor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendor using partial update
        Vendor partialUpdatedVendor = new Vendor();
        partialUpdatedVendor.setId(vendor.getId());

        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVendor))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVendorUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedVendor, vendor), getPersistedVendor(vendor));
    }

    @Test
    @Transactional
    void fullUpdateVendorWithPatch() throws Exception {
        // Initialize the database
        insertedVendor = vendorRepository.saveAndFlush(vendor);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the vendor using partial update
        Vendor partialUpdatedVendor = new Vendor();
        partialUpdatedVendor.setId(vendor.getId());

        partialUpdatedVendor.name(UPDATED_NAME);

        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendor.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVendor))
            )
            .andExpect(status().isOk());

        // Validate the Vendor in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVendorUpdatableFieldsEquals(partialUpdatedVendor, getPersistedVendor(partialUpdatedVendor));
    }

    @Test
    @Transactional
    void patchNonExistingVendor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendor.setId(longCount.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vendorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendor.setId(longCount.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(vendorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendor() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        vendor.setId(longCount.incrementAndGet());

        // Create the Vendor
        VendorDTO vendorDTO = vendorMapper.toDto(vendor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(vendorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendor in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendor() throws Exception {
        // Initialize the database
        insertedVendor = vendorRepository.saveAndFlush(vendor);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the vendor
        restVendorMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendor.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return vendorRepository.count();
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

    protected Vendor getPersistedVendor(Vendor vendor) {
        return vendorRepository.findById(vendor.getId()).orElseThrow();
    }

    protected void assertPersistedVendorToMatchAllProperties(Vendor expectedVendor) {
        assertVendorAllPropertiesEquals(expectedVendor, getPersistedVendor(expectedVendor));
    }

    protected void assertPersistedVendorToMatchUpdatableProperties(Vendor expectedVendor) {
        assertVendorAllUpdatablePropertiesEquals(expectedVendor, getPersistedVendor(expectedVendor));
    }
}
