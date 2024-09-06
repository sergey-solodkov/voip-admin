package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.OtherDeviceType;
import com.github.sergeisolodkov.voipadmin.repository.OtherDeviceTypeRepository;
import com.github.sergeisolodkov.voipadmin.service.OtherDeviceTypeService;
import com.github.sergeisolodkov.voipadmin.service.dto.OtherDeviceTypeDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.OtherDeviceTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.OtherDeviceType}.
 */
@Service
@Transactional
public class OtherDeviceTypeServiceImpl implements OtherDeviceTypeService {

    private static final Logger LOG = LoggerFactory.getLogger(OtherDeviceTypeServiceImpl.class);

    private final OtherDeviceTypeRepository otherDeviceTypeRepository;

    private final OtherDeviceTypeMapper otherDeviceTypeMapper;

    public OtherDeviceTypeServiceImpl(OtherDeviceTypeRepository otherDeviceTypeRepository, OtherDeviceTypeMapper otherDeviceTypeMapper) {
        this.otherDeviceTypeRepository = otherDeviceTypeRepository;
        this.otherDeviceTypeMapper = otherDeviceTypeMapper;
    }

    @Override
    public OtherDeviceTypeDTO save(OtherDeviceTypeDTO otherDeviceTypeDTO) {
        LOG.debug("Request to save OtherDeviceType : {}", otherDeviceTypeDTO);
        OtherDeviceType otherDeviceType = otherDeviceTypeMapper.toEntity(otherDeviceTypeDTO);
        otherDeviceType = otherDeviceTypeRepository.save(otherDeviceType);
        return otherDeviceTypeMapper.toDto(otherDeviceType);
    }

    @Override
    public OtherDeviceTypeDTO update(OtherDeviceTypeDTO otherDeviceTypeDTO) {
        LOG.debug("Request to update OtherDeviceType : {}", otherDeviceTypeDTO);
        OtherDeviceType otherDeviceType = otherDeviceTypeMapper.toEntity(otherDeviceTypeDTO);
        otherDeviceType = otherDeviceTypeRepository.save(otherDeviceType);
        return otherDeviceTypeMapper.toDto(otherDeviceType);
    }

    @Override
    public Optional<OtherDeviceTypeDTO> partialUpdate(OtherDeviceTypeDTO otherDeviceTypeDTO) {
        LOG.debug("Request to partially update OtherDeviceType : {}", otherDeviceTypeDTO);

        return otherDeviceTypeRepository
            .findById(otherDeviceTypeDTO.getId())
            .map(existingOtherDeviceType -> {
                otherDeviceTypeMapper.partialUpdate(existingOtherDeviceType, otherDeviceTypeDTO);

                return existingOtherDeviceType;
            })
            .map(otherDeviceTypeRepository::save)
            .map(otherDeviceTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OtherDeviceTypeDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all OtherDeviceTypes");
        return otherDeviceTypeRepository.findAll(pageable).map(otherDeviceTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OtherDeviceTypeDTO> findOne(Long id) {
        LOG.debug("Request to get OtherDeviceType : {}", id);
        return otherDeviceTypeRepository.findById(id).map(otherDeviceTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete OtherDeviceType : {}", id);
        otherDeviceTypeRepository.deleteById(id);
    }
}
