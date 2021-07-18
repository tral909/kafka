package it.discovery.kafka.chat.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SpringChatConsumer {

    @KafkaListener(groupId = "spring-chat", topics = "kafka-training")
    public void readChatMessages(ConsumerRecord<String, String> record) {
        System.out.println("New message in chat: " + record.value());
    }
}
