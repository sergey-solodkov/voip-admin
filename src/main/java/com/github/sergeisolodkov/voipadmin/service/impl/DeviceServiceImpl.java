package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.repository.DeviceRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceService;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.DeviceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.Device}.
 */
@Service
@Transactional
public class DeviceServiceImpl implements DeviceService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceServiceImpl.class);

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    public DeviceServiceImpl(DeviceRepository deviceRepository, DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }

    @Override
    public DeviceDTO save(DeviceDTO deviceDTO) {
        LOG.debug("Request to save Device : {}", deviceDTO);
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    @Override
    public DeviceDTO update(DeviceDTO deviceDTO) {
        LOG.debug("Request to update Device : {}", deviceDTO);
        Device device = deviceMapper.toEntity(deviceDTO);
        device = deviceRepository.save(device);
        return deviceMapper.toDto(device);
    }

    @Override
    public Optional<DeviceDTO> partialUpdate(DeviceDTO deviceDTO) {
        LOG.debug("Request to partially update Device : {}", deviceDTO);

        return deviceRepository
            .findById(deviceDTO.getId())
            .map(existingDevice -> {
                deviceMapper.partialUpdate(existingDevice, deviceDTO);

                return existingDevice;
            })
            .map(deviceRepository::save)
            .map(deviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DeviceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Devices");
        return deviceRepository.findAll(pageable).map(deviceMapper::toDto);
    }

    public Page<DeviceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return deviceRepository.findAllWithEagerRelationships(pageable).map(deviceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceDTO> findOne(Long id) {
        LOG.debug("Request to get Device : {}", id);
        return deviceRepository.findOneWithEagerRelationships(id).map(deviceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Device : {}", id);
        deviceRepository.deleteById(id);
    }
}
