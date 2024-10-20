package com.github.sergeisolodkov.voipadmin.integration.impl;

import com.github.sergeisolodkov.voipadmin.integration.FileProvider;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
// TODO
public class FTPFileProviderImpl implements FileProvider {

//    private final FTPFileStorageProperties properties;
//    private final FTPClient client;

    @Override
    public StorageType getType() {
        return StorageType.FTP;
    }

    @Override
    public String put(StorageCatalog catalog, Path path, Resource data) throws IOException {
        return null;
    }

    @Override
    public Resource get(StorageCatalog catalog, Path path) throws IOException {
        return null;
    }

    @Override
    public void delete(StorageCatalog catalog, Path path) throws IOException {

    }
}
