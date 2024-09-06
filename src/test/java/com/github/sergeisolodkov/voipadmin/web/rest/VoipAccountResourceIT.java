package com.github.sergeisolodkov.voipadmin.web.rest;

import static com.github.sergeisolodkov.voipadmin.domain.VoipAccountAsserts.*;
import static com.github.sergeisolodkov.voipadmin.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sergeisolodkov.voipadmin.IntegrationTest;
import com.github.sergeisolodkov.voipadmin.domain.VoipAccount;
import com.github.sergeisolodkov.voipadmin.repository.VoipAccountRepository;
import com.github.sergeisolodkov.voipadmin.service.dto.VoipAccountDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.VoipAccountMapper;
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
 * Integration tests for the {@link VoipAccountResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoipAccountResourceIT {

    private static final Boolean DEFAULT_MANUAL = false;
    private static final Boolean UPDATED_MANUAL = true;

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_HASH = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_SIP_SERVER = "AAAAAAAAAA";
    private static final String UPDATED_SIP_SERVER = "BBBBBBBBBB";

    private static final String DEFAULT_SIP_PORT = "AAAAAAAAAA";
    private static final String UPDATED_SIP_PORT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_LINE_ENABLE = false;
    private static final Boolean UPDATED_LINE_ENABLE = true;

    private static final Integer DEFAULT_LINE_NUMBER = 1;
    private static final Integer UPDATED_LINE_NUMBER = 2;

    private static final String ENTITY_API_URL = "/api/voip-accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private VoipAccountRepository voipAccountRepository;

    @Autowired
    private VoipAccountMapper voipAccountMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoipAccountMockMvc;

    private VoipAccount voipAccount;

    private VoipAccount insertedVoipAccount;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoipAccount createEntity() {
        return new VoipAccount()
            .manual(DEFAULT_MANUAL)
            .username(DEFAULT_USERNAME)
            .passwordHash(DEFAULT_PASSWORD_HASH)
            .sipServer(DEFAULT_SIP_SERVER)
            .sipPort(DEFAULT_SIP_PORT)
            .lineEnable(DEFAULT_LINE_ENABLE)
            .lineNumber(DEFAULT_LINE_NUMBER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VoipAccount createUpdatedEntity() {
        return new VoipAccount()
            .manual(UPDATED_MANUAL)
            .username(UPDATED_USERNAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .sipServer(UPDATED_SIP_SERVER)
            .sipPort(UPDATED_SIP_PORT)
            .lineEnable(UPDATED_LINE_ENABLE)
            .lineNumber(UPDATED_LINE_NUMBER);
    }

    @BeforeEach
    public void initTest() {
        voipAccount = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedVoipAccount != null) {
            voipAccountRepository.delete(insertedVoipAccount);
            insertedVoipAccount = null;
        }
    }

    @Test
    @Transactional
    void createVoipAccount() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the VoipAccount
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(voipAccount);
        var returnedVoipAccountDTO = om.readValue(
            restVoipAccountMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voipAccountDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            VoipAccountDTO.class
        );

        // Validate the VoipAccount in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedVoipAccount = voipAccountMapper.toEntity(returnedVoipAccountDTO);
        assertVoipAccountUpdatableFieldsEquals(returnedVoipAccount, getPersistedVoipAccount(returnedVoipAccount));

        insertedVoipAccount = returnedVoipAccount;
    }

    @Test
    @Transactional
    void createVoipAccountWithExistingId() throws Exception {
        // Create the VoipAccount with an existing ID
        voipAccount.setId(1L);
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(voipAccount);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoipAccountMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voipAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the VoipAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVoipAccounts() throws Exception {
        // Initialize the database
        insertedVoipAccount = voipAccountRepository.saveAndFlush(voipAccount);

        // Get all the voipAccountList
        restVoipAccountMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voipAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].manual").value(hasItem(DEFAULT_MANUAL.booleanValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].passwordHash").value(hasItem(DEFAULT_PASSWORD_HASH)))
            .andExpect(jsonPath("$.[*].sipServer").value(hasItem(DEFAULT_SIP_SERVER)))
            .andExpect(jsonPath("$.[*].sipPort").value(hasItem(DEFAULT_SIP_PORT)))
            .andExpect(jsonPath("$.[*].lineEnable").value(hasItem(DEFAULT_LINE_ENABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].lineNumber").value(hasItem(DEFAULT_LINE_NUMBER)));
    }

    @Test
    @Transactional
    void getVoipAccount() throws Exception {
        // Initialize the database
        insertedVoipAccount = voipAccountRepository.saveAndFlush(voipAccount);

        // Get the voipAccount
        restVoipAccountMockMvc
            .perform(get(ENTITY_API_URL_ID, voipAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voipAccount.getId().intValue()))
            .andExpect(jsonPath("$.manual").value(DEFAULT_MANUAL.booleanValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.passwordHash").value(DEFAULT_PASSWORD_HASH))
            .andExpect(jsonPath("$.sipServer").value(DEFAULT_SIP_SERVER))
            .andExpect(jsonPath("$.sipPort").value(DEFAULT_SIP_PORT))
            .andExpect(jsonPath("$.lineEnable").value(DEFAULT_LINE_ENABLE.booleanValue()))
            .andExpect(jsonPath("$.lineNumber").value(DEFAULT_LINE_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingVoipAccount() throws Exception {
        // Get the voipAccount
        restVoipAccountMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoipAccount() throws Exception {
        // Initialize the database
        insertedVoipAccount = voipAccountRepository.saveAndFlush(voipAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the voipAccount
        VoipAccount updatedVoipAccount = voipAccountRepository.findById(voipAccount.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVoipAccount are not directly saved in db
        em.detach(updatedVoipAccount);
        updatedVoipAccount
            .manual(UPDATED_MANUAL)
            .username(UPDATED_USERNAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .sipServer(UPDATED_SIP_SERVER)
            .sipPort(UPDATED_SIP_PORT)
            .lineEnable(UPDATED_LINE_ENABLE)
            .lineNumber(UPDATED_LINE_NUMBER);
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(updatedVoipAccount);

        restVoipAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voipAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voipAccountDTO))
            )
            .andExpect(status().isOk());

        // Validate the VoipAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedVoipAccountToMatchAllProperties(updatedVoipAccount);
    }

    @Test
    @Transactional
    void putNonExistingVoipAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voipAccount.setId(longCount.incrementAndGet());

        // Create the VoipAccount
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(voipAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoipAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voipAccountDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voipAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoipAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoipAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voipAccount.setId(longCount.incrementAndGet());

        // Create the VoipAccount
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(voipAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoipAccountMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(voipAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoipAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoipAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voipAccount.setId(longCount.incrementAndGet());

        // Create the VoipAccount
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(voipAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoipAccountMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(voipAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoipAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoipAccountWithPatch() throws Exception {
        // Initialize the database
        insertedVoipAccount = voipAccountRepository.saveAndFlush(voipAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the voipAccount using partial update
        VoipAccount partialUpdatedVoipAccount = new VoipAccount();
        partialUpdatedVoipAccount.setId(voipAccount.getId());

        partialUpdatedVoipAccount
            .username(UPDATED_USERNAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .sipPort(UPDATED_SIP_PORT)
            .lineNumber(UPDATED_LINE_NUMBER);

        restVoipAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoipAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVoipAccount))
            )
            .andExpect(status().isOk());

        // Validate the VoipAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoipAccountUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedVoipAccount, voipAccount),
            getPersistedVoipAccount(voipAccount)
        );
    }

    @Test
    @Transactional
    void fullUpdateVoipAccountWithPatch() throws Exception {
        // Initialize the database
        insertedVoipAccount = voipAccountRepository.saveAndFlush(voipAccount);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the voipAccount using partial update
        VoipAccount partialUpdatedVoipAccount = new VoipAccount();
        partialUpdatedVoipAccount.setId(voipAccount.getId());

        partialUpdatedVoipAccount
            .manual(UPDATED_MANUAL)
            .username(UPDATED_USERNAME)
            .passwordHash(UPDATED_PASSWORD_HASH)
            .sipServer(UPDATED_SIP_SERVER)
            .sipPort(UPDATED_SIP_PORT)
            .lineEnable(UPDATED_LINE_ENABLE)
            .lineNumber(UPDATED_LINE_NUMBER);

        restVoipAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoipAccount.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedVoipAccount))
            )
            .andExpect(status().isOk());

        // Validate the VoipAccount in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertVoipAccountUpdatableFieldsEquals(partialUpdatedVoipAccount, getPersistedVoipAccount(partialUpdatedVoipAccount));
    }

    @Test
    @Transactional
    void patchNonExistingVoipAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voipAccount.setId(longCount.incrementAndGet());

        // Create the VoipAccount
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(voipAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoipAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voipAccountDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(voipAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoipAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoipAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voipAccount.setId(longCount.incrementAndGet());

        // Create the VoipAccount
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(voipAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoipAccountMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(voipAccountDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the VoipAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoipAccount() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        voipAccount.setId(longCount.incrementAndGet());

        // Create the VoipAccount
        VoipAccountDTO voipAccountDTO = voipAccountMapper.toDto(voipAccount);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoipAccountMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(voipAccountDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the VoipAccount in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoipAccount() throws Exception {
        // Initialize the database
        insertedVoipAccount = voipAccountRepository.saveAndFlush(voipAccount);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the voipAccount
        restVoipAccountMockMvc
            .perform(delete(ENTITY_API_URL_ID, voipAccount.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return voipAccountRepository.count();
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

    protected VoipAccount getPersistedVoipAccount(VoipAccount voipAccount) {
        return voipAccountRepository.findById(voipAccount.getId()).orElseThrow();
    }

    protected void assertPersistedVoipAccountToMatchAllProperties(VoipAccount expectedVoipAccount) {
        assertVoipAccountAllPropertiesEquals(expectedVoipAccount, getPersistedVoipAccount(expectedVoipAccount));
    }

    protected void assertPersistedVoipAccountToMatchUpdatableProperties(VoipAccount expectedVoipAccount) {
        assertVoipAccountAllUpdatablePropertiesEquals(expectedVoipAccount, getPersistedVoipAccount(expectedVoipAccount));
    }
}
