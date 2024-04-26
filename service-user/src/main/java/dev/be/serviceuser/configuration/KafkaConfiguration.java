package dev.be.serviceuser.configuration;

import org.springframework.beans.factory.annotation.Value;

//@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

}
