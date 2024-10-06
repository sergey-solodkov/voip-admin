package com.github.sergeisolodkov.voipadmin.service;

import java.io.IOException;

/**
 * Service for dealing with device configuration files.
 */
public interface DeviceConfigurationService {

    /**
     * Build config and store it in S3 storage.
     * @param deviceId device ID.
     */
    String create(Long deviceId) throws IOException;
}
