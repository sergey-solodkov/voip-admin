package com.github.sergeisolodkov.voipadmin.integration.impl;

import com.github.sergeisolodkov.voipadmin.integration.FileProvider;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import com.github.sergeisolodkov.voipadmin.integration.FileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FileStorageImpl implements FileStorage {
    private final Map<StorageType, FileProvider> fileProviders;

    public FileStorageImpl(List<FileProvider> fileProviders) {
        this.fileProviders = fileProviders.stream()
            .collect(Collectors.toMap(FileProvider::getType, provider -> provider));
    }

    @Override
    public String upload(StorageType type, StorageCatalog catalog, Path path, Resource resource) throws IOException {
        return get(type)
            .put(catalog, path, resource);
    }

    @Override
    public Resource download(StorageType type, StorageCatalog catalog, Path path) throws IOException {
        return get(type)
            .get(catalog, path);
    }

    @Override
    public void delete(StorageType type, StorageCatalog catalog, Path path) throws IOException {
        get(type)
            .delete(catalog, path);
    }

    private FileProvider get(StorageType type) {
        return fileProviders.get(type);
    }
}
