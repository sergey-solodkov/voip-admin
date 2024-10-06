package com.github.sergeisolodkov.voipadmin.queue.payload;

import com.nimbusds.jose.Payload;
import org.jetbrains.annotations.Nullable;
import ru.yoomoney.tech.dbqueue.api.TaskPayloadTransformer;

public class LongPayloadTransformer implements TaskPayloadTransformer<Long> {

    @Nullable
    @Override
    public Long toObject(@Nullable String payload) {
        return payload != null ? Long.parseLong(payload) : null;
    }

    @Nullable
    @Override
    public String fromObject(@Nullable Long payload) {
        return payload != null ? payload.toString() : null;
    }
}
