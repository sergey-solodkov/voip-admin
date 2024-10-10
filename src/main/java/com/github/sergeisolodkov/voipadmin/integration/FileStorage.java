package com.github.sergeisolodkov.voipadmin.integration;

import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Service to deal with file storage.
 */
public interface FileStorage {

    /**
     * Upload in storage.
     * @param type storage type.
     * @param catalog catalog type.
     * @param path path to file within catalog.
     * @param resource file resource.
     * @return full path to file.
     * @throws IOException in case of I/O problems.
     */
    String upload(StorageType type, StorageCatalog catalog, Path path, Resource resource) throws IOException;

    /**
     * Download file from storage.
     * @param type storage type.
     * @param catalog catalog type.
     * @param path path to file within catalog.
     * @return file resource.
     * @throws IOException in case of I/O problems.
     */
    Resource download(StorageType type, StorageCatalog catalog, Path path) throws IOException;

    /**
     * Remove file from storage.
     * @param type storage type.
     * @param catalog catalog type.
     * @param path path to file within catalog.
     * @throws IOException in case of I/O problems.
     */
    void delete(StorageType type, StorageCatalog catalog, Path path) throws IOException;
}
