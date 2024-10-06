package com.github.sergeisolodkov.voipadmin.queue.producer;

import ru.yoomoney.tech.dbqueue.api.impl.ShardingQueueProducer;
import ru.yoomoney.tech.dbqueue.settings.QueueId;
import ru.yoomoney.tech.dbqueue.spring.dao.SpringDatabaseAccessLayer;

import java.util.HashMap;
import java.util.Map;

public class QueueProducerFactory {

    private final static Map<QueueId, ShardingQueueProducer<String, SpringDatabaseAccessLayer>> shardingQueueProducers = new HashMap<>();

    public void addShardingQueueProducer(QueueId queueId, ShardingQueueProducer<String, SpringDatabaseAccessLayer> producer) {
        shardingQueueProducers.put(queueId, producer);
    }

    public ShardingQueueProducer<String, SpringDatabaseAccessLayer> getBuildDeviceConfigQueueProducer() {
        return shardingQueueProducers.get(new QueueId("device-config-creation"));
    }
}
