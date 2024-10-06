package com.github.sergeisolodkov.voipadmin.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "db-queue")
public class DbQueueProperties {
    private String tableName;
    private List<QueueProperties> queues;
    private Long retryInterval;

    @Data
    public static class QueueProperties {
        private String queueId;
        private Integer threadCount;
    }
}
