package chat;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
public class ChatProducerTest {

	//automatically start/stop container, runs zookeeper as well
	@Container
	private KafkaContainer kafka = new KafkaContainer
			(DockerImageName.parse("confluentinc/cp-kafka:5.4.3"));

	private ChatProducer chatProducer;

	@BeforeEach
	void setup() {
		// will map to random local port
		chatProducer = new ChatProducer(kafka.getBootstrapServers());
	}

	@Test
	void testProducer() {
		chatProducer.send("uuu", "Tony", "Hello from testcontainer!");

		Properties config = new Properties();
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "ChatClient");
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka.getBootstrapServers());
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		// По умолчанию offset партиции хранится в кафке в системном топике connect_offsets
		// Откуда читать сообщения ("latest" по умолчанию)
		// Отрабатывает только при первом подключении нового консьюмера (с новым group id)
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(config)) {

			kafkaConsumer.subscribe(List.of("uuu"));
			ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(1000));
			Iterator<ConsumerRecord<String, String>> iterator = records.iterator();
			assertTrue(iterator.hasNext());
			ConsumerRecord<String, String> record = iterator.next();
			assertEquals("Tony", record.key());
			assertEquals("Hello from testcontainer!", record.value());
			records.forEach(r -> System.out.println(r.key() + " " + r.value()));
		}
	}
}
