package com.example.clickhouseDemo.service;

import com.example.clickhouseDemo.entity.Weblog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@EnableKafka
public class Producer {
    @Value("${kafka.topic.name}")
    private String topic;

    private final KafkaTemplate<String, Weblog> kafkaTemplate;

    @Autowired
    public Producer(KafkaTemplate<String, Weblog> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(Weblog weblog) throws IOException {
        try {
            kafkaTemplate.send(topic, weblog);
            log.info("send msg : " + weblog);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
