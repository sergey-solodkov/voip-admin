package com.github.sergeisolodkov.voipadmin.integration.impl;

import com.github.sergeisolodkov.voipadmin.config.properties.TFTPFileStorageProperties;
import com.github.sergeisolodkov.voipadmin.integration.FileProvider;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.tftp.TFTP;
import org.apache.commons.net.tftp.TFTPClient;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;

@Component
@RequiredArgsConstructor
public class TFTPFileProviderImpl implements FileProvider {

    private final TFTPFileStorageProperties properties;
    private final TFTPClient tftpClient = new TFTPClient();

    @Override
    public StorageType getType() {
        return StorageType.TFTP;
    }

    @Override
    public String put(StorageCatalog catalog, Path path, Resource data) throws IOException {
        var filename = FilenameUtils.getName(path.toString());
        tftpClient.open();
        tftpClient.sendFile(filename, TFTP.BINARY_MODE, data.getInputStream(), properties.getUrl(), properties.getPort());
        tftpClient.close();
        return String.format("%s://%s", properties.getUrl(), path);
    }

    @Override
    public Resource get(StorageCatalog catalog, Path path) throws IOException {
        return null;
    }

    @Override
    public void delete(StorageCatalog catalog, Path path) throws IOException {

    }
}
