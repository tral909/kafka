package chat;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class ChatClient {

	public static void main(String[] args) {
		Properties config = new Properties();
		config.put(ConsumerConfig.GROUP_ID_CONFIG, "ChatClient");
		config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		// По умолчанию offset партиции хранится в кафке в системном топике connect_offsets
		// Откуда читать сообщения ("latest" по умолчанию)
		// Отрабатывает только при первом подключении нового консьюмера (с новым group id)
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		final String topic = "uuu";
		try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(config)) {

			kafkaConsumer.subscribe(List.of(topic));
			while (true) {
				ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMillis(100));
				records.forEach(r -> System.out.println(r.key() + " " + r.value()));
			}
		}
	}
}
