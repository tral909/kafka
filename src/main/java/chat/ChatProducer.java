package chat;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.Future;

public class ChatProducer {

	private final Properties config;

	public ChatProducer(String server) {
		config = new Properties();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
	}

	public Future<?> send(String topic, String key, String value) {

		try (KafkaProducer<String, String> kafkaProducer =
					 new KafkaProducer<String, String>(config)) {

			ProducerRecord<String, String> record =
					new ProducerRecord<String, String>(topic, key, value);

			return kafkaProducer.send(record);
		}
	}

	public static void main(String[] args) throws Exception {
		final String topic = "uuu";

		System.out.println("Enter your name: ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name = reader.readLine();

		System.out.println("Enter messages: ");

		String text;

		ChatProducer chatProducer = new ChatProducer("localhost:9092");
		while (!(text = reader.readLine()).isBlank()) {
			chatProducer.send(topic, name, text);
		}
	}
}
