package com.github.sergeisolodkov.voipadmin.queue.consumer;

import com.github.sergeisolodkov.voipadmin.config.properties.DbQueueProperties;
import com.github.sergeisolodkov.voipadmin.queue.payload.LongPayloadTransformer;
import com.github.sergeisolodkov.voipadmin.service.DeviceConfigurationService;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import ru.yoomoney.tech.dbqueue.api.QueueConsumer;
import ru.yoomoney.tech.dbqueue.api.Task;
import ru.yoomoney.tech.dbqueue.api.TaskExecutionResult;
import ru.yoomoney.tech.dbqueue.api.TaskPayloadTransformer;
import ru.yoomoney.tech.dbqueue.settings.QueueConfig;

import java.io.IOException;
import java.time.Duration;

@Slf4j
public class DeviceConfigStoringTaskConsumer implements QueueConsumer<Long>, ApplicationContextAware {

    @Setter
    private QueueConfig queueConfig;
    private DbQueueProperties dbQueueProps;
    private DeviceConfigurationService deviceConfigurationService;

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        dbQueueProps = ctx.getBean(DbQueueProperties.class);
        deviceConfigurationService = ctx.getBean(DeviceConfigurationService.class);
    }

    @NonNull
    @Override
    public TaskExecutionResult execute(@NonNull Task<Long> task) {
        var deviceId = task.getPayloadOrThrow();

        try {
            var path = deviceConfigurationService.store(deviceId);
            log.info("Configuration file for device {} stored in path {}", deviceId, path);
            return TaskExecutionResult.finish();
        } catch (IOException | IllegalStateException ex) {
            log.warn("Error while storing configuration for deviceId {}. Error: {}. Task reenqueued for {} seconds.",
                deviceId, ex.getMessage(), dbQueueProps.getRetryInterval());
            return TaskExecutionResult.reenqueue(Duration.ofSeconds(dbQueueProps.getRetryInterval()));
        } catch (Exception ex) {
            log.warn("Error while storing configuration for deviceId {}. Error: {}", deviceId, ex.getMessage());
            return TaskExecutionResult.fail();
        }
    }

    @NonNull
    @Override
    public QueueConfig getQueueConfig() {
        return queueConfig;
    }

    @NonNull
    @Override
    public TaskPayloadTransformer<Long> getPayloadTransformer() {
        return new LongPayloadTransformer();
    }
}
