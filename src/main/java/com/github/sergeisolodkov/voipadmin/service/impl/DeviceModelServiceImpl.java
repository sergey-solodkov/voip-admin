package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.repository.DeviceModelRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceModelService;
import com.github.sergeisolodkov.voipadmin.integration.FileStorage;
import com.github.sergeisolodkov.voipadmin.service.dto.DeviceModelDTO;
import com.github.sergeisolodkov.voipadmin.service.mapper.DeviceModelMapper;
import com.nimbusds.jose.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog.CONFIG_TEMPLATE;
import static com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog.FIRMWARE;
import static com.github.sergeisolodkov.voipadmin.integration.domain.StorageType.S3;

/**
 * Service Implementation for managing {@link com.github.sergeisolodkov.voipadmin.domain.DeviceModel}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DeviceModelServiceImpl implements DeviceModelService {

    private final DeviceModelRepository deviceModelRepository;
    private final DeviceModelMapper deviceModelMapper;
    private final FileStorage fileStorage;

    @Override
    @Transactional
    public DeviceModelDTO save(DeviceModelDTO deviceModelDTO) {
        log.debug("Request to save DeviceModel : {}", deviceModelDTO);

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
        log.debug("Request to update DeviceModel : {}", deviceModelDTO);
        DeviceModel deviceModel = deviceModelMapper.toEntity(deviceModelDTO);
        deviceModel = deviceModelRepository.save(deviceModel);
        return deviceModelMapper.toDto(deviceModel);
    }

    @Override
    @Transactional
    public Optional<DeviceModelDTO> partialUpdate(DeviceModelDTO deviceModelDTO) {
        log.debug("Request to partially update DeviceModel : {}", deviceModelDTO);

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
        log.debug("Request to get all DeviceModels");
        return deviceModelRepository.findAll(pageable).map(deviceModelMapper::toDto);
    }

    public Page<DeviceModelDTO> findAllWithEagerRelationships(Pageable pageable) {
        return deviceModelRepository.findAllWithEagerRelationships(pageable).map(deviceModelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DeviceModelDTO> findOne(Long id) {
        log.debug("Request to get DeviceModel : {}", id);
        return deviceModelRepository.findOneWithEagerRelationships(id).map(deviceModelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DeviceModel : {}", id);
        deviceModelRepository.deleteById(id);
    }

    @Override
    public Resource getConfigTemplate(Long id) {
        var path = deviceModelRepository
            .findById(id)
            .map(DeviceModel::getConfigTemplatePath)
            .map(Path::of)
            .orElseThrow();

        try {
            return fileStorage.download(S3, CONFIG_TEMPLATE, path);
        } catch (IOException ex) {
            var message = String.format("Error while trying to download config template via path %s", path);
            log.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    @Override
    public Resource getFirmwareFile(Long id) {
        var path = deviceModelRepository
            .findById(id)
            .map(DeviceModel::getFirmwareFilePath)
            .map(Path::of)
            .orElseThrow();

        try {
            return fileStorage.download(S3, FIRMWARE, path);
        } catch (IOException ex) {
            var message = String.format("Error while trying to download config template via path %s", path);
            log.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    private Optional<String> uploadConfigTemplate(DeviceModelDTO deviceModelDTO) {
        if (deviceModelDTO.getConfigTemplateFile() == null) {
            return Optional.empty();
        }

        var filepath = Paths.get(
            deviceModelDTO.getVendor().getName(),
            deviceModelDTO.getName(),
            deviceModelDTO.getConfigTemplateFileName()
        );
        var configTemplateFile = new ByteArrayResource(Base64.from(deviceModelDTO.getConfigTemplateFile()).decode());
        try {
            var configTemplatePath = fileStorage.upload(S3, CONFIG_TEMPLATE, filepath, configTemplateFile);
            return Optional.of(configTemplatePath);
        } catch (IOException ex) {
            var message = String.format("Error while trying to upload config template file, error: %s", ex.getMessage());
            log.error(message);
            throw new RuntimeException(message, ex);
        }
    }

    private Optional<String> uploadFirmwareFile(DeviceModelDTO deviceModelDTO) {
        if (deviceModelDTO.getFirmwareFile() == null) {
            return Optional.empty();
        }

        var filepath = Paths.get(
            deviceModelDTO.getVendor().getName(),
            deviceModelDTO.getName(),
            deviceModelDTO.getFirmwareFileName()
        );
        var firmwareFile = new ByteArrayResource(Base64.from(deviceModelDTO.getFirmwareFile()).decode());

        try {
            var firmwareFilePath = fileStorage.upload(S3, FIRMWARE, filepath, firmwareFile);
            return Optional.of(firmwareFilePath);
        } catch (IOException ex) {
            var message = String.format("Error while trying to upload config template file, error: %s", ex.getMessage());
            log.error(message);
            throw new RuntimeException(message, ex);
        }
    }
}
