package com.github.sergeisolodkov.voipadmin.integration.impl;

import com.github.sergeisolodkov.voipadmin.config.properties.LocalFileStorageProperties;
import com.github.sergeisolodkov.voipadmin.integration.FileProvider;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@RequiredArgsConstructor
public class LocalFileProvider implements FileProvider {

    private final LocalFileStorageProperties localFileStorageProperties;

    @Override
    public StorageType getType() {
        return StorageType.LOCAL;
    }

    @Override
    public String put(StorageCatalog catalog, Path path, Resource data) throws IOException {
        var fullPath = Path.of(localFileStorageProperties.getLocation())
            .resolve(localFileStorageProperties.getDirs().get(catalog))
            .resolve(path)
            .toAbsolutePath()
            .normalize();
        Files.createDirectories(fullPath.getParent());
        Files.copy(data.getInputStream(), fullPath, StandardCopyOption.REPLACE_EXISTING);
        return fullPath.toString();
    }

    @Override
    public Resource get(StorageCatalog catalog, Path path) throws IOException {
        return null;
    }

    @Override
    public void delete(StorageCatalog catalog, Path path) throws IOException {

    }
}
