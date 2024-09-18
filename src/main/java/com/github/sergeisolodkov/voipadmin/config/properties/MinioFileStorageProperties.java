package com.github.sergeisolodkov.voipadmin.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "file.minio")
public class MinioFileStorageProperties {
    private String url;
    private String accessKey;
    private String secretKey;
    private Map<BucketType, String> buckets;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Map<BucketType, String> getBuckets() {
        return buckets;
    }

    public void setBuckets(Map<BucketType, String> buckets) {
        this.buckets = buckets;
    }

    public enum BucketType {
        CONFIG_TEMPLATE_FILES,
        CONFIG_FILES,
        FIRMWARE_FILES
    }
}
