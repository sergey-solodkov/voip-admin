package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import com.github.sergeisolodkov.voipadmin.processor.config.ConfigurationBuildProcessorFactory;
import com.github.sergeisolodkov.voipadmin.repository.DeviceRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceConfigurationService;
import com.github.sergeisolodkov.voipadmin.integration.FileStorage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog.CONFIG;
import static com.github.sergeisolodkov.voipadmin.integration.domain.StorageType.S3;

@Service
@RequiredArgsConstructor
public class DeviceConfigurationServiceImpl implements DeviceConfigurationService {
    private final DeviceRepository deviceRepository;
    private final FileStorage fileStorage;
    private final ConfigurationBuildProcessorFactory configurationBuildProcessorFactory;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) // TODO temp solution because of db-queue lazy initialization issue
    public String create(Long deviceId) throws IOException {
        var device = deviceRepository.findById(deviceId)
            .orElseThrow();

        if (!device.getModel().getConfigurable()) {
            throw new IllegalArgumentException("Device is not configurable");
        }

        var configBuildProcessor = configurationBuildProcessorFactory.forDevice(device);
        var config = configBuildProcessor.process(device);

        var filepath = Paths.get(
            device.getModel().getVendor().getName(),
            device.getModel().getName(),
            config.getFileName()
        );

        var path = fileStorage.upload(S3, CONFIG, filepath, new ByteArrayResource(config.getFileContent()));

        device.setConfigPath(path);
        deviceRepository.save(device);

        return path;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW) // TODO temp solution because of db-queue lazy initialization issue
    public String store(Long deviceId) throws IOException {
        var device = deviceRepository.findById(deviceId)
            .orElseThrow();

        if (StringUtils.isEmpty(device.getConfigPath())) {
            throw new IllegalStateException("Device has no config yet");
        }

        var path = Path.of(device.getConfigPath());
        var resource = fileStorage.download(S3, CONFIG, path);

        var localPath = Paths.get(
            device.getModel().getVendor().getName(),
            device.getModel().getName(),
            resource.getFilename()
        );

        return fileStorage.upload(StorageType.LOCAL, CONFIG, localPath, resource);
    }
}
