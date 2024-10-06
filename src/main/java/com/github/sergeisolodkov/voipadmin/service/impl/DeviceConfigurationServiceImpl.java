package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.processor.config.ConfigurationBuildProcessorFactory;
import com.github.sergeisolodkov.voipadmin.repository.DeviceRepository;
import com.github.sergeisolodkov.voipadmin.service.DeviceConfigurationService;
import com.github.sergeisolodkov.voipadmin.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DeviceConfigurationServiceImpl implements DeviceConfigurationService {
    private final DeviceRepository deviceRepository;
    private final FileStorageService fileStorageService;
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

        var path = fileStorageService.uploadConfig(
            String.format(
                "%s/%s/%s",
                device.getModel().getVendor().getName(),
                device.getModel().getName(),
                config.getFileName()
            ),
            new ByteArrayResource(config.getFileContent())
        );

        device.setConfigPath(path);
        deviceRepository.save(device);

        return path;
    }
}
