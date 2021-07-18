package it.discovery.kafka.chat.producer;

import it.discovery.kafka.chat.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class SpringChatProducer implements ChatProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Future<?> send(ChatMessage message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(message.chat().name(),
                message.sender(), message.text());
        return kafkaTemplate.send(record);
    }
}
