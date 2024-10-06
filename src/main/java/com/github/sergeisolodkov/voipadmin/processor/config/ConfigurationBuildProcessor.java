package com.github.sergeisolodkov.voipadmin.processor.config;

import com.github.sergeisolodkov.voipadmin.autoprovisioning.domain.ConfigurationFile;
import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.processor.domain.FileExtension;

import java.io.IOException;

public interface ConfigurationBuildProcessor {

    ConfigurationFile process(Device device) throws IOException;

    default String buildFileName(Device device, FileExtension fileExtension) {
        return String.format("%s.%s", device.getMac(), fileExtension.name().toLowerCase());
    }
}
