package com.github.sergeisolodkov.voipadmin.config;

import com.github.sergeisolodkov.voipadmin.config.properties.MinioFileStorageProperties;
import io.minio.MinioClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ MinioFileStorageProperties.class })
public class FileStorageConfiguration {

    @Bean
    public MinioClient minioClient(MinioFileStorageProperties properties) {
        return MinioClient
            .builder()
            .credentials(properties.getAccessKey(), properties.getSecretKey())
            .endpoint(properties.getUrl())
            .build();
    }
}
