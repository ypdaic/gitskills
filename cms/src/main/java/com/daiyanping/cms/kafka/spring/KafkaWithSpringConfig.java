package com.daiyanping.cms.kafka.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath*:kafka/kafka-application-context.properties")
public class KafkaWithSpringConfig {
}
