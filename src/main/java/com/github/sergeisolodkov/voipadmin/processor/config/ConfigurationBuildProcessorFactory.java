package com.github.sergeisolodkov.voipadmin.processor.config;

import com.github.sergeisolodkov.voipadmin.domain.Device;
import com.github.sergeisolodkov.voipadmin.domain.DeviceModel;
import com.github.sergeisolodkov.voipadmin.domain.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConfigurationBuildProcessorFactory {
    private final Map<String, ConfigurationBuildProcessor> processors;

    public ConfigurationBuildProcessor forDevice(Device device) {
        return Optional.ofNullable(device.getModel())
            .map(DeviceModel::getVendor)
            .map(Vendor::getName)
            .map(String::toLowerCase)
            .map(processors::get)
            .orElseThrow(() -> new IllegalArgumentException("Configuration builder for device model not exists"));
    }
}
