package com.github.sergeisolodkov.voipadmin.service;

import java.io.IOException;

/**
 * Service for dealing with device configuration files.
 */
public interface DeviceConfigurationService {

    /**
     * Build config and store it in S3 storage.
     * @param deviceId device ID.
     * @throws IOException in case of storage I/O problems.
     */
    String create(Long deviceId) throws IOException;

    /**
     * Store config file in case of FTP/TFTP scenario.
     * @param deviceId device ID.
     * @return file path.
     * @throws IOException in case of storage I/O problems.
     */
    String store(Long deviceId) throws IOException;
}
