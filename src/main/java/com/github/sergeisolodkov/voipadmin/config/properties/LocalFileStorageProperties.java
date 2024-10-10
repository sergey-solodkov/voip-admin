package com.github.sergeisolodkov.voipadmin.config.properties;

import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties(prefix = "file.local")
@Configuration
@Getter
@Setter
public class LocalFileStorageProperties {
    private String location;
    private Map<StorageCatalog, String> dirs;

}
