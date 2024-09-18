package com.github.sergeisolodkov.voipadmin.service.impl;

import com.github.sergeisolodkov.voipadmin.config.properties.MinioFileStorageProperties;
import com.github.sergeisolodkov.voipadmin.integration.FileStorage;
import com.github.sergeisolodkov.voipadmin.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static com.github.sergeisolodkov.voipadmin.config.properties.MinioFileStorageProperties.BucketType.CONFIG_FILES;
import static com.github.sergeisolodkov.voipadmin.config.properties.MinioFileStorageProperties.BucketType.CONFIG_TEMPLATE_FILES;
import static com.github.sergeisolodkov.voipadmin.config.properties.MinioFileStorageProperties.BucketType.FIRMWARE_FILES;

@Service
public class MinioFileStorageServiceImpl implements FileStorageService {

    private final MinioFileStorageProperties minioFileStorageProperties;
    private final FileStorage fileStorage;

    public MinioFileStorageServiceImpl(MinioFileStorageProperties minioFileStorageProperties, FileStorage fileStorage) {
        this.minioFileStorageProperties = minioFileStorageProperties;
        this.fileStorage = fileStorage;
    }

    @PostConstruct
    void init() {
        minioFileStorageProperties.getBuckets().forEach((key, value) -> {
            fileStorage.makeBucket(value);
        });
    }

    @Override
    public String uploadConfig() {
        return null;
    }

    @Override
    public String uploadConfigTemplate() {
        return null;
    }

    @Override
    public String uploadFirmwareFile() {
        return null;
    }

    @Override
    public Resource downloadConfig(String path) {
        return fileStorage.download(
            minioFileStorageProperties.getBuckets().get(CONFIG_FILES),
            path
        );
    }

    @Override
    public Resource downloadConfigTemplate(String path) {
        return fileStorage.download(
            minioFileStorageProperties.getBuckets().get(CONFIG_TEMPLATE_FILES),
            path
        );
    }

    @Override
    public Resource downloadFirmwareFile(String path) {
        return fileStorage.download(
            minioFileStorageProperties.getBuckets().get(FIRMWARE_FILES),
            path
        );
    }
}
