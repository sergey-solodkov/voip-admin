package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.repository.DeviceModelRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceModelService;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.DeviceModelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.DeviceModel}.
 */
@Service
@Transactional
public class DeviceModelServiceImpl implements DeviceModelService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceModelServiceImpl.class);

    private final DeviceModelRepository deviceModelRepository;

    private final DeviceModelMapper deviceModelMapper;

    public DeviceModelServiceImpl(DeviceModelRepository deviceModelRepository, DeviceModelMapper deviceModelMapper) {
        this.deviceModelRepository = deviceModelRepository;
        this.deviceModelMapper = deviceModelMapper;
    }

    @Override
    public DeviceModelDTO save(DeviceModelDTO deviceModelDTO) {
        LOG.debug("Request to save DeviceModel : {}", deviceModelDTO);
        DeviceModel deviceModel = deviceModelMapper.toEntity(deviceModelDTO);
        deviceModel = deviceModelRepository.save(deviceModel);
        return deviceModelMapper.toDto(deviceModel);
    }

    @Override
    public DeviceModelDTO update(DeviceModelDTO deviceModelDTO) {
        LOG.debug("Request to update DeviceModel : {}", deviceModelDTO);
        DeviceModel deviceModel = deviceModelMapper.toEntity(deviceModelDTO);
        deviceModel = deviceModelRepository.save(deviceModel);
        return deviceModelMapper.toDto(deviceModel);
    }

    @Override
    public Optional<DeviceModelDTO> partialUpdate(DeviceModelDTO deviceModelDTO) {
        LOG.debug("Request to partially update DeviceModel : {}", deviceModelDTO);

        return deviceModelRepository
            .findById(deviceModelDTO.getId())
            .map(existingDeviceModel -> {
                deviceModelMapper.partialUpdate(existingDeviceModel, deviceModelDTO);

                return existingDeviceModel;
            })
            .map(deviceModelRepository::save)
            .map(deviceModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceModelDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DeviceModels");
        return deviceModelRepository.findAll(pageable).map(deviceModelMapper::toDto);
    }

    public Page<DeviceModelDTO> findAllWithEagerRelationships(Pageable pageable) {
        return deviceModelRepository.findAllWithEagerRelationships(pageable).map(deviceModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceModelDTO> findOne(Long id) {
        LOG.debug("Request to get DeviceModel : {}", id);
        return deviceModelRepository.findOneWithEagerRelationships(id).map(deviceModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete DeviceModel : {}", id);
        deviceModelRepository.deleteById(id);
    }
}
