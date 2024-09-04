package com.github.sergeisolodkov.voipadmin;

import com.github.sergeisolodkov.voipadmin.config.AsyncSyncConfiguration;
import com.github.sergeisolodkov.voipadmin.config.EmbeddedSQL;
import com.github.sergeisolodkov.voipadmin.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { VoipadminApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
