package com.github.sergeisolodkov.voipadmin.service;

import org.springframework.core.io.Resource;

/**
 * Service to deal with file storage.
 */
public interface FileStorageService {

    /**
     * Upload device's config resource.
     * @param path path to file within bucket.
     * @param resource {@link Resource}
     * @return path to resource.
     */
    String uploadConfig(String path, Resource resource);

    /**
     * Upload device model's config template resource.
     * @param path path to file within bucket.
     * @param resource {@link Resource}
     * @return path to resource.
     */
    String uploadConfigTemplate(String path, Resource resource);

    /**
     * Upload device model's firmware resource.
     * @param path path to file within bucket.
     * @param resource {@link Resource}
     * @return path to resource.
     */
    String uploadFirmwareFile(String path, Resource resource);

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
