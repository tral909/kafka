package it.discovery.kafka.chat.producer;

import it.discovery.kafka.chat.model.Chat;
import it.discovery.kafka.chat.model.ChatMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

public class JavaClientChatProducer implements ChatProducer {
    private final Properties properties;

    public JavaClientChatProducer(String bootstrapServers) {
        properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    }

    @Override
    public Future<?> send(ChatMessage message) {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(message.chat().name(),
                    message.sender(), message.text());
            return producer.send(record);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Chat chat = new Chat("kafka-training");
        ChatMessage message = new ChatMessage("John", "Hello from John", chat);
        ChatProducer producer = new JavaClientChatProducer("localhost:9092");
        for(int i = 0 ; i < 20; i ++) {
            producer.send(message);
            System.out.println("Send message: " + message);
            Thread.sleep(2000);
        }
    }
}
