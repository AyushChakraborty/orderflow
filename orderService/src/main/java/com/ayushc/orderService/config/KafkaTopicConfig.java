package com.ayushc.orderService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

//setting up the kafka topic pertaining to order creation, updation, deletion
//all will go to only one topic

@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic ordersTopic() {
        return TopicBuilder.name("orders")
                .partitions(3)
                .replicas(1)
                .build();
    }

}
