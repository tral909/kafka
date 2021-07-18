package chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

@Component
public class SpringChatProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	public Future<?> send(String topic, String key, String value) {
		return kafkaTemplate.send(topic, key, value);
	}
}
