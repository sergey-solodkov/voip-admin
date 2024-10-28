package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.integration.FileStorage;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import com.github.sergeisolodkov.voipadmin.repository.DeviceRepository;
import com.github.sergeisolodkov.voipadmin.service.ProvisioningService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

import static com.github.sergeisolodkov.voipadmin.integration.domain.StorageType.S3;

@Service
@RequiredArgsConstructor
public class ProvisioningServiceImpl implements ProvisioningService {

    private final DeviceRepository deviceRepository;
    private final FileStorage fileStorage;

    @Override
    public Resource configForMac(String mac) throws IOException {
        var example = new Device();
        example.setMac(mac);
        var device = deviceRepository.findOne(Example.of(example))
            .orElseThrow();

        return fileStorage.download(S3, StorageCatalog.CONFIG, Path.of(device.getConfigPath()));
    }
}
