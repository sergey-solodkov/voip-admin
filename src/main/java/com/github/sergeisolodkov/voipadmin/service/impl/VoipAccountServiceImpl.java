package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.VoipAccount;
import com.github.sergeisolodkov.voipadmin.repository.VoipAccountRepository;
import com.github.sergeisolodkov.voipadmin.service.VoipAccountService;
import com.github.sergeisolodkov.voipadmin.service.dto.VoipAccountDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.VoipAccountMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.VoipAccount}.
 */
@Service
@Transactional
public class VoipAccountServiceImpl implements VoipAccountService {

    private static final Logger LOG = LoggerFactory.getLogger(VoipAccountServiceImpl.class);

    private final VoipAccountRepository voipAccountRepository;

    private final VoipAccountMapper voipAccountMapper;

    public VoipAccountServiceImpl(VoipAccountRepository voipAccountRepository, VoipAccountMapper voipAccountMapper) {
        this.voipAccountRepository = voipAccountRepository;
        this.voipAccountMapper = voipAccountMapper;
    }

    @Override
    public VoipAccountDTO save(VoipAccountDTO voipAccountDTO) {
        LOG.debug("Request to save VoipAccount : {}", voipAccountDTO);
        VoipAccount voipAccount = voipAccountMapper.toEntity(voipAccountDTO);
        voipAccount = voipAccountRepository.save(voipAccount);
        return voipAccountMapper.toDto(voipAccount);
    }

    @Override
    public VoipAccountDTO update(VoipAccountDTO voipAccountDTO) {
        LOG.debug("Request to update VoipAccount : {}", voipAccountDTO);
        VoipAccount voipAccount = voipAccountMapper.toEntity(voipAccountDTO);
        voipAccount = voipAccountRepository.save(voipAccount);
        return voipAccountMapper.toDto(voipAccount);
    }

    @Override
    public Optional<VoipAccountDTO> partialUpdate(VoipAccountDTO voipAccountDTO) {
        LOG.debug("Request to partially update VoipAccount : {}", voipAccountDTO);

        return voipAccountRepository
            .findById(voipAccountDTO.getId())
            .map(existingVoipAccount -> {
                voipAccountMapper.partialUpdate(existingVoipAccount, voipAccountDTO);

                return existingVoipAccount;
            })
            .map(voipAccountRepository::save)
            .map(voipAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VoipAccountDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all VoipAccounts");
        return voipAccountRepository.findAll(pageable).map(voipAccountMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VoipAccountDTO> findOne(Long id) {
        LOG.debug("Request to get VoipAccount : {}", id);
        return voipAccountRepository.findById(id).map(voipAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete VoipAccount : {}", id);
        voipAccountRepository.deleteById(id);
    }
}
