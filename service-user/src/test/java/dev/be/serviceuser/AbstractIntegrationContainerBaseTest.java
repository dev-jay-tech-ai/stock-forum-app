package com.stockforum.project;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public abstract class AbstractIntegrationContainerBaseTest {

    public static final GenericContainer<?> MY_REDIS_CONTAINER;

    static {
        MY_REDIS_CONTAINER = new GenericContainer<>("redis:6")
                .withExposedPorts(6379);

        MY_REDIS_CONTAINER.start();

        System.setProperty("spring.redis.host", MY_REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", String.valueOf(MY_REDIS_CONTAINER.getMappedPort(6379)));
    }
}