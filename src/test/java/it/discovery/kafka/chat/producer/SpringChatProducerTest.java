package it.discovery.kafka.chat.producer;

import it.discovery.kafka.chat.ChatApplication;
import it.discovery.kafka.chat.model.Chat;
import it.discovery.kafka.chat.model.ChatMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringJUnitWebConfig(classes = ChatApplication.class)
public class SpringChatProducerTest {

    @Container
    static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

    @Autowired
    ChatProducer producer;

    @DynamicPropertySource
    static void springKafkaProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.producer.bootstrap-servers", () -> kafka.getBootstrapServers());
    }

    @Test
    void send_messageValid_sentCorrectly() {
        Chat chat = new Chat("kafka-training");
        ChatMessage message = new ChatMessage("John", "Hello from John", chat);
        producer.send(message);

        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, getClass().getSimpleName());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(List.of(message.chat().name()));
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
            assertTrue(iterator.hasNext());
            ConsumerRecord<String, String> record = iterator.next();
            assertEquals(message.text(), record.value());
            assertEquals(message.sender(), record.key());
            assertEquals(message.chat().name(), record.topic());
        }
    }
}
