package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.Setting;
import com.github.sergeisolodkov.voipadmin.repository.SettingRepository;
import com.github.sergeisolodkov.voipadmin.service.SettingService;
import com.github.sergeisolodkov.voipadmin.service.dto.SettingDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.SettingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.Setting}.
 */
@Service
@Transactional
public class SettingServiceImpl implements SettingService {

    private static final Logger LOG = LoggerFactory.getLogger(SettingServiceImpl.class);

    private final SettingRepository settingRepository;

    private final SettingMapper settingMapper;

    public SettingServiceImpl(SettingRepository settingRepository, SettingMapper settingMapper) {
        this.settingRepository = settingRepository;
        this.settingMapper = settingMapper;
    }

    @Override
    public SettingDTO save(SettingDTO settingDTO) {
        LOG.debug("Request to save Setting : {}", settingDTO);
        Setting setting = settingMapper.toEntity(settingDTO);
        setting = settingRepository.save(setting);
        return settingMapper.toDto(setting);
    }

    @Override
    public SettingDTO update(SettingDTO settingDTO) {
        LOG.debug("Request to update Setting : {}", settingDTO);
        Setting setting = settingMapper.toEntity(settingDTO);
        setting = settingRepository.save(setting);
        return settingMapper.toDto(setting);
    }

    @Override
    public Optional<SettingDTO> partialUpdate(SettingDTO settingDTO) {
        LOG.debug("Request to partially update Setting : {}", settingDTO);

        return settingRepository
            .findById(settingDTO.getId())
            .map(existingSetting -> {
                settingMapper.partialUpdate(existingSetting, settingDTO);

                return existingSetting;
            })
            .map(settingRepository::save)
            .map(settingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SettingDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Settings");
        return settingRepository.findAll(pageable).map(settingMapper::toDto);
    }

    public Page<SettingDTO> findAllWithEagerRelationships(Pageable pageable) {
        return settingRepository.findAllWithEagerRelationships(pageable).map(settingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SettingDTO> findOne(Long id) {
        LOG.debug("Request to get Setting : {}", id);
        return settingRepository.findOneWithEagerRelationships(id).map(settingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Setting : {}", id);
        settingRepository.deleteById(id);
    }
}
