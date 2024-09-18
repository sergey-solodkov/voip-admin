package com.github.sergeisolodkov.voipadmin.service;

import org.springframework.core.io.Resource;

/**
 * Service to deal with file storage.
 */
public interface FileStorageService {

    /**
     * Upload device's config file.
     * @return path to file.
     */
    String uploadConfig();

    /**
     * Upload device model's config template file.
     * @return path to file.
     */
    String uploadConfigTemplate();

    /**
     * Upload device model's firmware file.
     * @return path to file.
     */
    String uploadFirmwareFile();

    /**
     * Download device's config file.
     * @param path path to file.
     * @return {@link Resource}
     */
    Resource downloadConfig(String path);

    /**
     * Download device model's config template file.
     * @param path path to file.
     * @return {@link Resource}
     */
    Resource downloadConfigTemplate(String path);

    /**
     * Download device model's firmware file.
     * @param path path to file.
     * @return {@link Resource}
     */
    Resource downloadFirmwareFile(String path);
}
