package com.github.sergeisolodkov.voipadmin.config.properties;

import com.github.sergeisolodkov.voipadmin.integration.domain.StorageCatalog;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@ConfigurationProperties(prefix = "file.tftp")
@Configuration
@Getter
@Setter
public class TFTPFileStorageProperties {
    private boolean enabled;
    private String url;
    private int port;
    private Map<StorageCatalog, String> dirs;
}
