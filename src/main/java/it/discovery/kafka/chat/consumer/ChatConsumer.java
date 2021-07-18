package it.discovery.kafka.chat.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

//TODO refactor this class to make it easier testable
public class ChatConsumer {
    private final List<String> chats;

    private final Properties properties;

    public ChatConsumer(String bootstrapServers, List<String> chats) {
        this.chats = chats;

        properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, getClass().getSimpleName());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //TODO make it configurable
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    public void run() {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(chats);
            //TODO make it configurable
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                records.forEach(record -> {
                    System.out.println("New message: " + record.value() + " in the chat: " + record.topic());
                });
            }
        }
    }

    public static void main(String[] args) {
        ChatConsumer consumer = new ChatConsumer("localhost:9092", List.of("kafka-training"));
        consumer.run();
    }
}
