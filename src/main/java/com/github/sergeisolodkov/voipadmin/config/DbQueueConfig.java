package com.github.sergeisolodkov.voipadmin.config;

import com.github.sergeisolodkov.voipadmin.config.properties.DbQueueProperties;
import com.github.sergeisolodkov.voipadmin.queue.consumer.DeviceConfigCreationTaskConsumer;
import com.github.sergeisolodkov.voipadmin.queue.consumer.DeviceConfigStoringTaskConsumer;
import com.github.sergeisolodkov.voipadmin.queue.producer.QueueProducerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;
import ru.yoomoney.tech.dbqueue.api.QueueConsumer;
import ru.yoomoney.tech.dbqueue.api.impl.NoopPayloadTransformer;
import ru.yoomoney.tech.dbqueue.api.impl.ShardingQueueProducer;
import ru.yoomoney.tech.dbqueue.api.impl.SingleQueueShardRouter;
import ru.yoomoney.tech.dbqueue.config.DatabaseDialect;
import ru.yoomoney.tech.dbqueue.config.QueueService;
import ru.yoomoney.tech.dbqueue.config.QueueShard;
import ru.yoomoney.tech.dbqueue.config.QueueShardId;
import ru.yoomoney.tech.dbqueue.config.QueueTableSchema;
import ru.yoomoney.tech.dbqueue.config.impl.LoggingTaskLifecycleListener;
import ru.yoomoney.tech.dbqueue.config.impl.LoggingThreadLifecycleListener;
import ru.yoomoney.tech.dbqueue.settings.ExtSettings;
import ru.yoomoney.tech.dbqueue.settings.FailRetryType;
import ru.yoomoney.tech.dbqueue.settings.FailureSettings;
import ru.yoomoney.tech.dbqueue.settings.PollSettings;
import ru.yoomoney.tech.dbqueue.settings.ProcessingMode;
import ru.yoomoney.tech.dbqueue.settings.ProcessingSettings;
import ru.yoomoney.tech.dbqueue.settings.QueueConfig;
import ru.yoomoney.tech.dbqueue.settings.QueueId;
import ru.yoomoney.tech.dbqueue.settings.QueueLocation;
import ru.yoomoney.tech.dbqueue.settings.QueueSettings;
import ru.yoomoney.tech.dbqueue.settings.ReenqueueRetryType;
import ru.yoomoney.tech.dbqueue.settings.ReenqueueSettings;
import ru.yoomoney.tech.dbqueue.spring.dao.SpringDatabaseAccessLayer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DbQueueConfig {
    private final DbQueueProperties dbQueueProps;
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final DefaultListableBeanFactory listableBeanFactory;


    @Bean
    SpringDatabaseAccessLayer databaseAccessLayer() {
        return new SpringDatabaseAccessLayer(
            DatabaseDialect.POSTGRESQL,
            QueueTableSchema.builder().build(),
            jdbcTemplate,
            transactionTemplate
        );
    }

    @Bean
    QueueShard<SpringDatabaseAccessLayer> queueShard(SpringDatabaseAccessLayer databaseAccessLayer) {
        return new QueueShard<>(new QueueShardId("main"), databaseAccessLayer);
    }

    @Bean
    public Map<QueueId, QueueConfig> queueIdQueueConfigs() {
        Map<QueueId, QueueConfig> configs = new HashMap<>(dbQueueProps.getQueues().size());

        dbQueueProps.getQueues()
            .forEach(queueProperties -> {
                var settings = queueSettings(queueProperties.getThreadCount());
                var queueId = queueId(queueProperties.getQueueId());
                var config = queueConfig(settings, queueId);

                configs.put(queueId, config);
            });

        return configs;
    }

    @Bean
    public QueueProducerFactory queueProducerFactory(Map<QueueId, QueueConfig> configs,
                                                     QueueShard<SpringDatabaseAccessLayer> queueShard) {
        var factory = new QueueProducerFactory();
        configs.forEach((id, config) -> {
            var shardingQueueProducer = new ShardingQueueProducer<>(
                config,
                NoopPayloadTransformer.getInstance(),
                new SingleQueueShardRouter<>(queueShard)
            );

            factory.addShardingQueueProducer(id, shardingQueueProducer);

        });
        return factory;
    }

    @Bean
    public List<QueueConsumer<?>> queueConsumers(Map<QueueId, QueueConfig> configs) {
        List<QueueConsumer<?>> consumers = new ArrayList<>(configs.size());
        configs.forEach((id, config) -> {
            Class<?> consumerClass;
            var values = new MutablePropertyValues();
            values.add("queueConfig", config);

            consumerClass = switch (id.asString()) {
                case "device-config-creation":
                    yield DeviceConfigCreationTaskConsumer.class;
                case "device-config-storing":
                    yield DeviceConfigStoringTaskConsumer.class;
                default:
                    throw new IllegalArgumentException("Queue consumer not found");
            };

            var definition = new GenericBeanDefinition();
            definition.setBeanClass(consumerClass);
            definition.setPropertyValues(values);

            listableBeanFactory.registerBeanDefinition(id.asString(), definition);
            var consumer = (QueueConsumer<?>) listableBeanFactory.getBean(consumerClass);
            consumers.add(consumer);
        });

        return consumers;
    }

    @Bean(initMethod = "start", destroyMethod = "shutdown")
    QueueService queueService(QueueShard<SpringDatabaseAccessLayer> queueShard,
                              List<QueueConsumer<?>> queueConsumers) {
        QueueService queueService = new QueueService(List.of(queueShard),
            new LoggingThreadLifecycleListener(),
            new LoggingTaskLifecycleListener()
        );
        queueConsumers.forEach(queueService::registerQueue);
        return queueService;
    }

    private QueueSettings queueSettings(int threadCount) {
        return QueueSettings.builder()
            .withProcessingSettings(
                ProcessingSettings.builder()
                    .withProcessingMode(ProcessingMode.SEPARATE_TRANSACTIONS)
                    .withThreadCount(threadCount)
                    .build()
            )
            .withPollSettings(
                PollSettings.builder()
                    .withBetweenTaskTimeout(Duration.ofMillis(100))
                    .withNoTaskTimeout(Duration.ofMillis(100))
                    .withFatalCrashTimeout(Duration.ofSeconds(1))
                    .build()
            )
            .withFailureSettings(
                FailureSettings.builder()
                    .withRetryType(FailRetryType.GEOMETRIC_BACKOFF)
                    .withRetryInterval(Duration.ofMinutes(dbQueueProps.getRetryInterval()))
                    .build()
            )
            .withReenqueueSettings(
                ReenqueueSettings.builder()
                    .withRetryType(ReenqueueRetryType.MANUAL)
                    .build()
            )
            .withExtSettings(
                ExtSettings.builder()
                    .withSettings(new LinkedHashMap<>())
                    .build()
            )
            .build();
    }

    private QueueId queueId(String id) {
        return new QueueId(id);
    }

    private QueueConfig queueConfig(QueueSettings queueSettings, QueueId queueId) {
        return new QueueConfig(
            QueueLocation.builder()
                .withTableName(dbQueueProps.getTableName())
                .withQueueId(queueId)
                .build(),
            queueSettings
        );
    }
}
