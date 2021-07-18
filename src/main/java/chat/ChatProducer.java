package chat;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class ChatProducer {

	public static void main(String[] args) throws Exception {
		Properties config = new Properties();
		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		final String topic = "uuu";

		System.out.println("Enter your name: ");
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String name = reader.readLine();

		System.out.println("Enter messages: ");

		String text;
		while (!(text = reader.readLine()).isBlank()) {

			try (KafkaProducer<String, String> kafkaProducer =
					new KafkaProducer<String, String>(config)) {

				ProducerRecord<String, String> record =
						new ProducerRecord<String, String>(topic, name, text);

				kafkaProducer.send(record);
			}
		}
	}
}
