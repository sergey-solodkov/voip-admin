package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.repository.DeviceModelRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceModelService;
import com.github.sergeisolodkov.voipadmin.service.FileStorageService;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.DeviceModelMapper;
import com.nimbusds.jose.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.DeviceModel}.
 */
@Service
@Transactional
public class DeviceModelServiceImpl implements DeviceModelService {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceModelServiceImpl.class);

    private final DeviceModelRepository deviceModelRepository;

    private final DeviceModelMapper deviceModelMapper;

    private final FileStorageService fileStorageService;

    public DeviceModelServiceImpl(DeviceModelRepository deviceModelRepository,
                                  DeviceModelMapper deviceModelMapper,
                                  FileStorageService fileStorageService) {
        this.deviceModelRepository = deviceModelRepository;
        this.deviceModelMapper = deviceModelMapper;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public DeviceModelDTO save(DeviceModelDTO deviceModelDTO) {
        LOG.debug("Request to save DeviceModel : {}", deviceModelDTO);

        DeviceModel deviceModel = deviceModelMapper.toEntity(deviceModelDTO);

        uploadConfigTemplate(deviceModelDTO)
            .ifPresent(deviceModel::setConfigTemplatePath);
        uploadFirmwareFile(deviceModelDTO)
            .ifPresent(deviceModel::setFirmwareFilePath);

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
    @Transactional
    public Optional<DeviceModelDTO> partialUpdate(DeviceModelDTO deviceModelDTO) {
        LOG.debug("Request to partially update DeviceModel : {}", deviceModelDTO);

        return deviceModelRepository
            .findById(deviceModelDTO.getId())
            .map(existingDeviceModel -> {
                deviceModelMapper.partialUpdate(existingDeviceModel, deviceModelDTO);

                uploadConfigTemplate(deviceModelDTO)
                    .ifPresent(existingDeviceModel::setConfigTemplatePath);
                uploadFirmwareFile(deviceModelDTO)
                    .ifPresent(existingDeviceModel::setFirmwareFilePath);

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

    @Override
    public Resource getConfigTemplate(Long id) {
        return deviceModelRepository
            .findById(id)
            .map(DeviceModel::getConfigTemplatePath)
            .map(fileStorageService::downloadConfigTemplate)
            .orElseThrow();
    }

    @Override
    public Resource getFirmwareFile(Long id) {
        return deviceModelRepository
            .findById(id)
            .map(DeviceModel::getFirmwareFilePath)
            .map(fileStorageService::downloadFirmwareFile)
            .orElseThrow();
    }

    private Optional<String> uploadConfigTemplate(DeviceModelDTO deviceModelDTO) {
        if (deviceModelDTO.getConfigTemplateFile() == null) {
            return Optional.empty();
        }

        var filepath = String.format(
            "%s/%s/%s",
            deviceModelDTO.getVendor().getName(),
            deviceModelDTO.getName(),
            deviceModelDTO.getConfigTemplateFileName()
        );
        var configTemplateFile = new ByteArrayResource(Base64.from(deviceModelDTO.getConfigTemplateFile()).decode());
        var configTemplatePath = fileStorageService.uploadConfigTemplate(filepath, configTemplateFile);

        return Optional.of(configTemplatePath);
    }

    private Optional<String> uploadFirmwareFile(DeviceModelDTO deviceModelDTO) {
        if (deviceModelDTO.getFirmwareFile() == null) {
            return Optional.empty();
        }

        var filepath = String.format(
            "%s/%s/%s",
            deviceModelDTO.getVendor().getName(),
            deviceModelDTO.getName(),
            deviceModelDTO.getFirmwareFileName()
        );
        var firmwareFile = new ByteArrayResource(Base64.from(deviceModelDTO.getFirmwareFile()).decode());
        var firmwareFilePath = fileStorageService.uploadFirmwareFile(filepath, firmwareFile);

        return Optional.of(firmwareFilePath);
    }
}
