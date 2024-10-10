package com.github.sergeisolodkov.voipadmin.integration;

import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import com.github.sergeisolodkov.voipadmin.integration.domain.StorageType;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;

/**
 * File provider.
 */
public interface FileProvider {

    /**
     * Define storage type to provide files from.
     * @return type.
     */
    StorageType getType();

    /**
     * Put file in storage.
     * @param catalog logical catalog dedicated to some kind of entity.
     * @param path path to file.
     * @param data file.
     * @return path to file.
     */
    String put(StorageCatalog catalog, Path path, Resource data) throws IOException;

    /**
     * Get file from storage.
     * @param catalog logical catalog dedicated to some kind of entity.
     * @param path path to file.
     * @return {@link Resource}
     */
    Resource get(StorageCatalog catalog, Path path) throws IOException;

    /**
     * Delete file.
     * @param catalog logical catalog dedicated to some kind of entity.
     * @param path path to file.
     */
    void delete(StorageCatalog catalog, Path path) throws IOException;
}
